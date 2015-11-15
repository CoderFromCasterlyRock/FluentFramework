package com.fluent.framework.persistence;

import java.util.*;

import org.slf4j.*;
import org.nustaq.offheap.FSTLongOffheapMap;
import org.nustaq.serialization.simpleapi.DefaultCoder;
import org.nustaq.serialization.simpleapi.FSTCoder;

import com.fluent.framework.core.FluentLifecycle;
import com.fluent.framework.events.core.*;
import com.fluent.framework.events.in.InEvent;
import com.fluent.framework.events.out.OutEvent;

import static com.fluent.framework.util.FluentUtil.*;


public final class EventFstPersister implements FluentLifecycle{

	private final String fileName;
	private final int eventCount;
	private final FSTCoder fstCoder;
	private final FSTLongOffheapMap<FluentEvent> offHeapMap;
	
	private final static String JOURNAL_SUFFIX	= ".mmf";
    private final static String NAME			= InChroniclePersisterService.class.getSimpleName();
    private final static Logger LOGGER      	= LoggerFactory.getLogger( NAME );


    public EventFstPersister( String location, String instanceName, String journal, long totalSizeBytes, int eventCount ){
    	
    	this.fileName 		= createMMFile( location, instanceName, journal );
    	this.eventCount 	= eventCount;
    	this.fstCoder		= new DefaultCoder( );
    	this.offHeapMap		= getOffHeapMap( fileName, totalSizeBytes, eventCount, fstCoder );
    }


	@Override
    public final String name( ){
        return NAME;
    }

	
	public final String getFileName(){
		return fileName;
	}
	
	
	public final void register( Class<?> ... clazzes ){
		
    	for( Class<?> clazz : clazzes ){
    		fstCoder.getConf().registerClass( clazz ); 
    	}
    
    	LOGGER.info("Initialized by registering [{}] custom classes, will persist and recover all Input events.", Arrays.deepToString(clazzes) );
    	
    }
	

    @Override
    public final void start( ){
    	fstCoder.getConf().registerClass( FluentEvent.class, InEvent.class, OutEvent.class );
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

    
    
    public final void persist( final FluentEvent event ){
    	offHeapMap.put( event.getSequenceId(), event );  
    }


    public final List<FluentEvent> retrieveAll( ){

    	List<FluentEvent> list = new ArrayList<>( eventCount );
        
        try{

        	for( Iterator<FluentEvent> iterator = offHeapMap.values(); iterator.hasNext(); ){
        		FluentEvent event = (FluentEvent) iterator.next( );
        		list.add( event );
        	}
        	
        }catch( Exception e ){
        	LOGGER.warn( "Failed to retrieve events from [{}].", fileName, e );
        }

        return list;

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
