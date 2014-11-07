package com.fluent.etrading.framework.persistence;

import java.io.*;
import java.util.*;

import org.slf4j.*;

import net.openhft.chronicle.*;

import com.fluent.etrading.framework.events.core.*;

import static com.fluent.etrading.framework.events.serializer.OutputEventSerializerManager.*;


/**
 * Writes are not thread safe!
 * Must be written from a single thread.
 */

public final class FluentOutputEventPersister extends NonConcurrentPersister<FluentOutputEvent>{

    
    private final static String DEFAULT_FILE_NAME   = "logs/OutputEventJournal";
    private final static String NAME                = FluentOutputEventPersister.class.getSimpleName();
    private final static Logger LOGGER              = LoggerFactory.getLogger( NAME );


    public FluentOutputEventPersister( ) throws IOException{
        this( DEFAULT_FILE_NAME );
    }



    public FluentOutputEventPersister( String fileName ) throws IOException{
        this( fileName, ChronicleConfig.SMALL.clone() ); 
    }
    

    public FluentOutputEventPersister( String fileName, ChronicleConfig config ) throws IOException{
        super( fileName, config );

    }


    @Override
    public final String name(){
        return getFileName();
    }


    @Override
    public final void init(){
    	LOGGER.info("[{}] initialized, will Persist and recover all Output events.", NAME );
    }

    
    @Override
    public final void persistAll( final FluentOutputEvent ... events ){
    	for( FluentOutputEvent event : events ){
    	    persist( event );
    	}
    }


    @Override
    public final boolean persist( final FluentOutputEvent event ){
        
    	if( event == null ) return false;
        
        String message = event.toJSON();
        return persistString( message );

    }


    @Override
    public final List<FluentOutputEvent> retrieveAll(  ){

        List<String> allMessages            = retrieveAllString( );
        List<FluentOutputEvent> eventList    = new LinkedList<FluentOutputEvent>( );

        for( String message : allMessages ){

            try{
                FluentOutputEvent outputEvent = convert( message );
                if( outputEvent == null ) continue;

                eventList.add( outputEvent );
                LOGGER.debug("OUT REC [{}]", message );
                
            }catch( Exception e ){
                LOGGER.warn( "Failed to convert [{}] to Output event.", message );
                LOGGER.warn( "Exception", e );
            }

        }

        return eventList;

    }


}
