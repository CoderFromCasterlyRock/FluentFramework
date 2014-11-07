package com.fluent.etrading.framework.persistence;

import java.io.*;
import java.text.*;
import java.util.*;

import org.slf4j.*;

import net.openhft.chronicle.*;

import com.fluent.etrading.framework.events.core.*;

import static com.fluent.etrading.framework.utility.ContainerUtil.*;

/**
 * TODO: 
 * 
 * After every restart of the app, we create a new Chronicle file.
 * When we start, scan the location and find the last file, then the new file will have a number to indicate that it can next
 * 
 */

public abstract class NonConcurrentPersister<EVENT extends FluentEvent> implements FluentPersister<EVENT>{

    private final String location;
    private final Chronicle chronicle;
    private final ExcerptAppender writer;
    private final ExcerptTailer reader;

    private final static int STRING_SIZE_OVERHEAD   = 1000;
    private final static String FILE_DATE_FORMAT    = "MM-dd-yyyy";
    private final static String NAME                = NonConcurrentPersister.class.getSimpleName();
    private final static Logger LOGGER              = LoggerFactory.getLogger( NAME );


    public NonConcurrentPersister( String fileName ) throws IOException{
        this( fileName, ChronicleConfig.MEDIUM.clone() );
    }

    
    public NonConcurrentPersister( String fileName, ChronicleConfig config ) throws IOException{
        this.location       = fileName + DASH + new SimpleDateFormat( FILE_DATE_FORMAT ).format(new Date( ));
        this.chronicle      = new IndexedChronicle( location, config );
        this.writer         = chronicle.createAppender();
        this.reader         = chronicle.createTailer();

    }

    protected final String getFileName(  ){
        return location;
    }

    protected final boolean persistString( final String message ){

        boolean wrote   = false;

        try{

            if( isBlank(message)) return false;

            int length  =  STRING_SIZE_OVERHEAD + message.length();

            writer.startExcerpt( length );
            writer.append( message );
            writer.append( NEWLINE );
            writer.finish();

            wrote       = true;

        }catch( Exception e ){
            LOGGER.warn("Failed to persist Message [{}]", message );
            LOGGER.warn("Exception: ", e );
        }

        return wrote;

    }


    protected final List<String> retrieveAllString(  ){

        List<String> list          = new LinkedList<String>( );

        while( reader.nextIndex() ){

            try{

                String message      = reader.readLine( );
                if( message == null ) continue;

                list.add( message );
                reader.finish();

            }catch( Exception e ){
                LOGGER.warn("Exception while retrieving all input events." );
                LOGGER.warn("Exception:", e );
            }

        }

        return list;

    }


    @Override
    public final void stop(){
        try{
            chronicle.close();

        }catch( Exception e ){
            LOGGER.warn("Exception while closing chronicle.", e );
        }

    }

}
