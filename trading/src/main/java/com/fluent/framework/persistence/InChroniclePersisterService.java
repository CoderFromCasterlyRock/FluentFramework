package com.fluent.framework.persistence;

import org.slf4j.*;

import java.io.*;
import java.util.*;

import org.jctools.queues.*;

import net.openhft.chronicle.Chronicle;
import net.openhft.chronicle.ChronicleQueueBuilder.*;
import net.openhft.chronicle.tools.ChronicleTools;
import net.openhft.chronicle.ExcerptAppender;
import net.openhft.chronicle.ExcerptTailer;

import com.fluent.framework.collection.*;
import com.fluent.framework.config.ConfigManager;
import com.fluent.framework.events.in.InEvent;


import com.fluent.framework.events.in.InListener;
import com.fluent.framework.events.in.InType;

import static com.fluent.framework.util.FluentUtil.*;
import static com.fluent.framework.util.FluentToolkit.*;


public final class InChroniclePersisterService implements Runnable, InListener, PersisterService<InEvent>{

	private volatile boolean keepDispatching;
	
	private final int eventCount;
	private final int queueSize;
	private final String basePath;
	private final Chronicle chronicle;
	private final ExcerptAppender excerpt;
	private final SpscArrayQueue<InEvent> eventQueue;
	private final FluentSingleThreadExecutor service;
	
	private final static int DEFAULT_SIZE	= MILLION;
	private final static String NAME		= InChroniclePersisterService.class.getSimpleName();
    private final static Logger LOGGER     	= LoggerFactory.getLogger( NAME );

    //AlgoConfigManager cfgManager
    public InChroniclePersisterService( ConfigManager cfgManager) throws IOException{
    	this( DEFAULT_SIZE, "" );
    }
    
    public InChroniclePersisterService( int eventCount, String basePath ) throws IOException{
    	this( eventCount, eventCount, basePath );
    }

    public InChroniclePersisterService( int eventCount, int queueSize, String basePath ) throws IOException{
    	
    	this.eventCount 	= eventCount;
    	this.queueSize 		= nextPowerOfTwo(queueSize);
    	this.basePath		= basePath;
    	this.chronicle		= createChronicle( eventCount, basePath );
    	this.excerpt		= chronicle.createAppender();
    	this.eventQueue		= new SpscArrayQueue<>( eventCount );
    	this.service		= new FluentSingleThreadExecutor( new FluentThreadFactory("Persister"));
        
    }


	@Override
    public final String name( ){
        return NAME;
    }
	
	
	@Override
	public final boolean isSupported( InType type ){
		return ( InType.WARM_UP_EVENT != type );
	}

	
	public final int getEventCount( ){
		return eventCount;
	}
	 
	public final int getQueueSize( ){
	    return queueSize;
	}
	 
		
	private final void prime( ){
	    	
		for( int i =ZERO; i <( eventCount); i++ ){
    		eventQueue.offer( IN_WARMUP_EVENT );
    		eventQueue.poll( );
    	}

		ChronicleTools.warmup();
    	eventQueue.clear();
    	
    }

    

    @Override
    public final void start( ){
    	
        keepDispatching = true;
        
        prime();
        
        service.start();
        service.execute( this );
                
        LOGGER.info("Successfully primed and initialized InboundPersister for [{}] events.", eventCount );
    }

    
    @Override
	public final boolean inUpdate( InEvent event ){
    	return eventQueue.offer( event );
	}

    
    
    protected static Chronicle createChronicle( int eventCount, String basePath ){
    	
    	Chronicle chronicle = null;
    	try{
    		chronicle = IndexedChronicleQueueBuilder
    				.indexed(basePath)
    				//.dataBlockSize(128)
    				//.indexBlockSize(128)
    				.build();
    		
    	}catch( Exception e ){
    		throw new IllegalStateException("Failed to create Chronicle at " + basePath, e );
    	}
    	
    	return chronicle;
    }
    
 

    
    @Override
    public final void run( ){

        while( keepDispatching ){
        	process( );
        }
    
    }
    
    
    protected final void process( ){

    	try{
            	
           	InEvent event  = eventQueue.poll();
            if( event == null ){
              	FluentBackoffStrategy.apply( ONE );
                return;
            }

            persist( event );
                
        }catch( Exception e ){
         	LOGGER.warn("Failed to persist event.", e );                  
        }
            
    }

    //TODO: Replace it with the upper bound of object size we expect to save in bytes
    protected final boolean persist( InEvent event ){
    	boolean successful = false;
    	
    	try{
    		
    		excerpt.startExcerpt( 500 );
    		excerpt.writeObject( event );
    		excerpt.finish();
    		
    		successful = true;
    	
    	}catch( Exception e ){
    		LOGGER.warn("FAILED to journal event [{}].", event, e);
    	}
    	
    	return successful;
    }
    
    
    @Override
    public final List<InEvent> retrieveAll(  ){
    	
    	ExcerptTailer reader	= null;
    	List<InEvent> list		= new ArrayList<>( eventCount );
    	
    	try{
    		
    		reader = chronicle.createTailer();
    		
    		while( reader.nextIndex() ){
    			InEvent event= (InEvent) reader.readObject();
    			list.add( event );
    			reader.finish();
    			
    		}
    		
    		LOGGER.info("Retrieved [{}] events from the Journal [{}].", list.size(), basePath );
    	
    	}catch( Exception e ){
    		LOGGER.warn("FAILED to retrieve events from the Journal [{}].", basePath, e );
    	
    	}finally{
    		if( reader != null ) reader.close();
    	}
    	
    	return list;
    }
    
    
    @Override
    public final void stop( ){
    	
    	try{
    	     
    		keepDispatching = false;
    		
    		excerpt.close();
    		chronicle.close();
    	    service.shutdown();
    	
    	    LOGGER.info("Successfully stopped chronicle." );
    	    
    	}catch( Exception e ){
    		LOGGER.warn("FAILED to stop chroncile.", e );
    	}
        
    }


}
