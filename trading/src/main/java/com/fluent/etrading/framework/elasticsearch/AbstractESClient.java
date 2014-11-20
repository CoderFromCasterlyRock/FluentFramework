package com.fluent.etrading.framework.elasticsearch;

import org.slf4j.*;

import com.fluent.etrading.framework.core.*;

import org.elasticsearch.action.admin.indices.create.*;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import static com.fluent.etrading.framework.utility.ContainerUtil.*;



//TODO
//1. Should we create the index when start() is called (then accept indexName and typeName in const)?
//2. Am I correctly creating the index first in createIndex()
//3. When should we delete an Index 
// 	a. Delete every time the app shuts down and then recreate when it starts.
//	b. But what is we shut it down during the day, in that case it shouldn't delete the index.
//4. Set up Kibana correctly.
//5. Attach a jeromq logger, so application can just publish the data.

public class AbstractESClient implements FluentService{

	private final String indexName;
	private final String typeName;
    private final String clusterName;
    private final Settings settings;
    private final Object atomicityLock;
	private final TransportClient client;
	private final IndicesAdminClient adminClient;
    
	private final static String DEFAULT_TYPE_NAME		= "LOG_DATA";
	private final static String DEFAULT_INDEX_PREFIX    = "simulator_";
    private final static String NAME    				= AbstractESClient.class.getSimpleName();
    private final static Logger LOGGER  				= LoggerFactory.getLogger( NAME );

    public AbstractESClient( String clusterName, String[] servers, int esPort ){
    	this( getDefaultIndexName(), DEFAULT_TYPE_NAME, clusterName, servers, esPort );
    }
    
    public AbstractESClient( String indexName, String clusterName, String[] servers, int esPort){
    	this( indexName, DEFAULT_TYPE_NAME, clusterName, servers, esPort );
    }
    
    public AbstractESClient( String indexName, String typeName, String clusterName, String[] servers, int esPort){
    	this.indexName 		= indexName;
    	this.typeName 		= typeName;
    	this.clusterName 	= clusterName;
    	this.atomicityLock	= new Object();
    	this.settings 		= ImmutableSettings.settingsBuilder()
								.put("cluster.name", clusterName)
								.put("node.client", false)
								.put("client.transport.sniff", "false")
								.build();
		
    	this.client 		= new TransportClient( settings, false );
    	this.adminClient    = client.admin().indices();
    	
    	for (String server: servers){
			client.addTransportAddress(new InetSocketTransportAddress(server, esPort));
		}
    	
    }
    

	@Override
	public String name() {
		return NAME;
	}


    protected final String getIndexName( ){
        return indexName;
    }
    
    protected final String getClusterName( ){
        return clusterName;
    }
   
    
	@Override
	public void init(){
		
		synchronized( atomicityLock ){
		
			boolean exists = indexExists();
			if( exists ){
				LOGGER.warn("Index [{}] already exists, won't re-create it.", indexName );
				return;
			}
		
			createIndex();
		}
		
	}
	
	
    protected final boolean indexExists( ){
        IndicesExistsResponse exists    = adminClient.exists( new IndicesExistsRequest( indexName ) ).actionGet();
        return exists.isExists();
    }

    protected final boolean createIndex(){

        boolean indexedCreated          = false;

        try{

        	CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    		CreateIndexResponse resposne = createIndexRequestBuilder.execute().actionGet();
    		indexedCreated = resposne.isAcknowledged();

            if( indexedCreated ) LOGGER.info( "Successfully created Index: [{}] with Type: [{}]!", indexName, typeName );
                                    
        }catch( Exception e ){
            LOGGER.warn( "FAILED to create index with Type: [{}], IndexName: [{}].", typeName, indexName );
            LOGGER.warn( "Exception", e );
        }

        return indexedCreated;

    }

    /*
    protected final boolean deleteIndex( ){

        boolean deletedSuccessfully     = false;

        try{

            DeleteIndexResponse delete  = adminClient.delete( new DeleteIndexRequest( indexName ) ).actionGet();
            if( !delete.isAcknowledged() ){
                LOGGER.warn("FAILED to delete Index [{}].",  indexName );
                return false;
            }

            LOGGER.debug( "Successfully deleted Index [{}].", indexName );
            deletedSuccessfully         = true;

        }catch(Exception e ){
            LOGGER.warn( "Exception while attempting to delete Index [{}].", indexName, e );
        }

        return deletedSuccessfully;

    }
    */
    
    protected final boolean indexMessage( String eventId, String message ){

        boolean indexedSuccessfully     = false;

        try{

            IndexRequestBuilder builder = client.prepareIndex( indexName, typeName, eventId );
            IndexResponse response 	    = builder.setSource( message ).execute().actionGet();
            indexedSuccessfully         = response.isCreated();
            
            if( !indexedSuccessfully ) LOGGER.error( "Failed to successfully index EventId [{}] and [{}].", eventId, message );

        }catch( NoNodeAvailableException noe ){
        	LOGGER.warn( "FAILED to insert message at Index [{}], most likely ElasticSearch is down!", indexName, noe);
        	
        }catch( Exception e ){
            LOGGER.warn( "Exception while attempting to insert [{}] at Index [{}].", message, indexName, e );
        }

        return indexedSuccessfully;

    }
    
    
    protected final static String getDefaultIndexName(){
    	return DEFAULT_INDEX_PREFIX + TODAY_FORMATTED;
    }
    
    
    @Override
	public final void stop( ){
    	
    	synchronized( atomicityLock ){
    		try{
    			
    			client.close();
    			LOGGER.debug( "Stopped client: {}", client.toString() );

    		}catch( Exception e ){
    			LOGGER.warn("Exception while closing client", e );
    		}
    		
    	}
	}


}
