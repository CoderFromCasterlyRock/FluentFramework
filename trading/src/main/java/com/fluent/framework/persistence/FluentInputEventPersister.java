package com.fluent.framework.persistence;

import java.io.*;
import java.util.*;

import org.slf4j.*;

import net.openhft.chronicle.*;

import com.fluent.framework.events.core.*;

import static com.fluent.framework.events.serializer.InputEventSerializerManager.*;


/**
 * Writes are not thread safe!
 * Must be written from a single thread.
 * 
 * TODO: Must run in persist Mode
 */

public final class FluentInputEventPersister extends NonConcurrentPersister<FluentInputEvent>{

    private final static String DEFAULT_FILE_NAME   	= "logs/InputEventJournal";
    private final static String NAME                	= FluentInputEventPersister.class.getSimpleName();
    private final static Logger LOGGER              	= LoggerFactory.getLogger( NAME );


    public FluentInputEventPersister( ) throws IOException{
        this( DEFAULT_FILE_NAME );
    }


    public FluentInputEventPersister( String fileName ) throws IOException{
        this( fileName, ChronicleConfig.SMALL.clone() );
    }

    

    public FluentInputEventPersister( String fileName, ChronicleConfig config ) throws IOException{
        super( fileName, config );
    }


    @Override
    public final String name(){
        return getFileName();
    }


    @Override
    public final void init(){
        LOGGER.info("[{}] initialized, will Persist and recover all Input events.", NAME );
    }


    @Override
    public final void persistAll( final FluentInputEvent ... events ){
    	for( FluentInputEvent event : events ){
    	    persist( event );
    	}
    }
    

    @Override
    public final boolean persist( final FluentInputEvent event ){

    	if( event == null ) return false;
                
        String message  = event.toJSON();
        return persistString( message );

    }


    @Override
    public final List<FluentInputEvent> retrieveAll(  ){

        List<String> allMessages        = retrieveAllString( );
        List<FluentInputEvent> eventList = new LinkedList<FluentInputEvent>( );

        for( String message : allMessages ){

            try{

                FluentInputEvent inputEvent  = convert( message );
                if( inputEvent == null ) continue;

                eventList.add( inputEvent );
                LOGGER.debug("IN REC [{}]", message );

            }catch( Exception e ){
                LOGGER.warn( "Failed to convert [{}] to Input event.", message );
                LOGGER.warn( "Exception", e );
            }

        }

        return eventList;

    }


}
