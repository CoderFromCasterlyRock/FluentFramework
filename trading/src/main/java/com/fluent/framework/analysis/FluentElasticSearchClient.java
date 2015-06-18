package com.fluent.framework.analysis;

import org.slf4j.*;
import java.util.*;
import java.util.concurrent.*;

import com.fluent.framework.core.*;
import com.fluent.framework.events.core.*;
import com.fluent.framework.collection.*;
import com.fluent.framework.events.in.*;
import com.fluent.framework.events.out.*;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;

import static com.fluent.framework.util.TimeUtil.*;
import static com.fluent.framework.util.FluentUtil.*;


public final class FluentElasticSearchClient implements Runnable, FluentInboundListener, FluentOutboundListener, FluentService{

	private volatile boolean isRunning;
	
	private final String indexName;
    private final String clusterName;
    private final String hostName;
    private final int portNumber;
    
    private final Settings settings;
    private final TransportClient client;
    
    private final ExecutorService eService;
    private final BlockingQueue<FluentEvent> queue;
    
    private final static String DEFAULT_INDEX_PREFIX    = "Fluent-";
    private final static String DEFAULT_INDEX_SUFFIX	= "-" + TODAY;
    private final static String NAME    				= FluentElasticSearchClient.class.getSimpleName();
    private final static Logger LOGGER  				= LoggerFactory.getLogger( NAME );



    public FluentElasticSearchClient( String indexName, String clusterName, String hostName, int portNumber ){
		
    	this.indexName      = indexName;
        this.clusterName	= clusterName;
        this.hostName		= hostName;
        this.portNumber		= portNumber;
        
        this.settings 		= ImmutableSettings.settingsBuilder()
        						.put("cluster.name", clusterName )
        						.put("node.client", false )
        						.put("client.transport.sniff", false ).build();
        						
		this.client 		= new TransportClient( settings, false);
        this.queue			= new ArrayBlockingQueue<>( SIXTY_FOUR * SIXTY_FOUR );
		this.eService		= Executors.newSingleThreadExecutor( new FluentThreadFactory("Elasticsearch"));
	}

    
    @Override
	public final String name(){
		return NAME;
	}
    
    
    @Override
 	public final void init(){
    	eService.submit( this );
    	InboundEventDispatcher.register( this );
    	OutboundEventDispatcher.register( this );
 		
 	}
    
    
    @Override
    public final boolean isSupported( FluentInboundType type ){
		return true;
	}
    
    
    @Override
    public final boolean isSupported( FluentOutboundType type ){
		return true;
	}
    
    
    @Override
	public final boolean update( FluentInboundEvent event ){
		if( !isRunning ) return false;
		
		return queue.offer( event );
	}
	
	@Override
	public final boolean update( FluentOutboundEvent event ){
		if( !isRunning ) return false;
		
		return queue.offer( event );
	}
	
	

    @Override
    public final void run( ){
    	
    	List<FluentEvent> bucket = new ArrayList<>( SIXTY_FOUR );
    	
    	while( isRunning ){
    	
    		int itemCount = queue.drainTo( bucket );
    		if( itemCount == ZERO ){
    			FluentBackoffStrategy.apply( TEN );
    			continue;
    		}
    		
    		indexEvents( bucket );
    		bucket.clear();
    		
    	}
    	
    }
    

	
	protected final void indexEvents( final List<FluentEvent> bucket ){
	
		boolean hasFailures		= true;
		
		for( FluentEvent event : bucket ){
		
			try{
			
				if( event == null ) continue;
				if( !isRunning ) continue;
			
						
				String eventId			= event.getEventId();
				String message			= event.toJSON();
				String eventType		= event.getType().getName();
				
				BulkRequestBuilder bulk	= client.prepareBulk();
				bulk.add( client.prepareIndex(indexName, eventType, eventId)
						.setSource(message) 
						);

				BulkResponse response	= bulk.execute().actionGet();
				hasFailures				= response.hasFailures( );
				
			}catch( NoNodeAvailableException nnae ){
				LOGGER.warn("Failed to index data, elasticsearch server at [{}] is DOWN!", toString(), nnae );
				
			}catch( Exception e ){
				LOGGER.warn("Failed to index data at [{}]!", toString(), e );
			
			}finally{
		
				if( hasFailures ){
					isRunning = false;
					
					LOGGER.warn("Failed to index one or more messages." );
					LOGGER.warn("Suspending indexing more data till the issue is manually resolved!" );
					LOGGER.warn("------------------------------------------------------------------");
				}
			}
	
		}
	
	}
    
    
    @Override
	public final void stop( ){
        try{
		    
        	isRunning = false;
            client.close();
		    LOGGER.debug( "Stopped client: {}", client.toString() );

        }catch( Exception e ){
            LOGGER.warn("Exception while closing client", e );
        }
	}
    
    
    @Override
	public final String toString( ){
    
    	StringBuilder builder = new StringBuilder( SIXTY_FOUR );
    	builder.append("Cluster: ").append( clusterName ).append( COMMA );
    	builder.append(" Index: ").append( indexName ).append( COMMA );
    	builder.append(" Address: ").append( hostName).append( COLON ).append( portNumber );
    	
    	return builder.toString( );
    
    }

}
