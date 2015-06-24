package com.fluent.framework.persistence;

import java.util.*;

import org.slf4j.*;
import org.nustaq.offheap.FSTLongOffheapMap;
import org.nustaq.serialization.simpleapi.DefaultCoder;
import org.nustaq.serialization.simpleapi.FSTCoder;

import com.fluent.framework.events.core.*;
import com.fluent.framework.events.in.FluentInboundEvent;
import com.fluent.framework.events.out.FluentOutboundEvent;
import com.fluent.framework.market.core.MarketDataEvent;

import static com.fluent.framework.util.FluentUtil.*;


public final class FluentEventFstPersister implements FluentPersister{

	private final String fileName;
	private final int eventCount;
	private final FSTCoder fstCoder;
	private final FSTLongOffheapMap<FluentEvent> offHeapMap;
	
	private final static String JOURNAL_SUFFIX	= ".mmf";
    private final static String NAME			= InboundEventPersisterService.class.getSimpleName();
    private final static Logger LOGGER      	= LoggerFactory.getLogger( NAME );


    public FluentEventFstPersister( String location, String instanceName, String journal, long totalSizeBytes, int eventCount ){
    	
    	this.fileName 		= createMMFile( location, instanceName, journal );
    	this.eventCount 	= eventCount;
    	this.fstCoder		= new DefaultCoder( );
    	this.offHeapMap		= getOffHeapMap( fileName, totalSizeBytes, eventCount, fstCoder );
    }


	@Override
    public final String name( ){
        return NAME;
    }

	
	@Override
	public final String getFileName(){
		return fileName;
	}
	

    @Override
    public final void init( ){
    	fstCoder.getConf().registerClass( FluentEvent.class, FluentInboundEvent.class, FluentOutboundEvent.class, MarketDataEvent.class );
        LOGGER.info("[{}] initialized, will Persist and recover all Input events.", NAME );
    }

    
    protected final static FSTLongOffheapMap<FluentEvent> getOffHeapMap( String fileName, long totalSizeBytes, int eventCount, FSTCoder fstCoder  ){
    	FSTLongOffheapMap<FluentEvent> offHeapMap = null;
    			
    	try{
    		
    		LOGGER.info("Attempting to create journal at [{}] for Count [{}], Size [{}].", fileName, eventCount, totalSizeBytes );
    		offHeapMap = new FSTLongOffheapMap<>( fileName, totalSizeBytes, eventCount, fstCoder );
    	
    	}catch( Exception e ){
    		throw new IllegalStateException("FAILED to create FSTLongOffheapMap", e);
    	}
    	
    	return offHeapMap;
    }

    
    
    @Override
    public final void persistAll( FluentEvent ... events ){
    	for( FluentEvent event : events ){
    	    persist( event );
    	}
    }
    

    @Override
    public final void persist( final FluentEvent event ){
    	offHeapMap.put( event.getSequenceId(), event );      
    }


    @Override
    public final Collection<FluentEvent> retrieveAllEvents( ){
    	return retrieveAll().values();
    }
    
    
    @Override
    public final Map<Long, FluentEvent> retrieveAll( ){

    	Map<Long, FluentEvent> retrievedMap = new HashMap<>( eventCount );
        
        try{

        	for( Iterator<FluentEvent> iterator = offHeapMap.values(); iterator.hasNext(); ){
        		FluentEvent event = (FluentEvent) iterator.next( );
        		retrievedMap.put( event.getSequenceId(), event );
        	}
        	
        }catch( Exception e ){
               LOGGER.warn( "Failed to retrieve events from [{}].", fileName, e );
        }

        return retrievedMap;

    }
    
    
    protected static String createMMFile(String location, String instanceName, String journal ){
    	StringBuilder builder = new StringBuilder( );
    	builder.append( location );
    	builder.append( SLASH );
    	builder.append( instanceName );
    	builder.append( UNDERSCORE );
    	builder.append( journal );
    	builder.append( JOURNAL_SUFFIX );
    	
    	return builder.toString();
	}
    

    
    @Override
    public final void stop( ){
    	
    	try{
    		
    		offHeapMap.free();
    		LOGGER.info("Successfully released memeory from [{}].", fileName, NAME );
    	
    	}catch( Exception e ){
    		LOGGER.warn("FAILED to close [{}] and free memory.", fileName, e );
    	}
        
    }

}
