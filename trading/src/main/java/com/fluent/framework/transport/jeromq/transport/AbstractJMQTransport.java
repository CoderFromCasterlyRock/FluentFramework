package com.fluent.framework.transport.jeromq.transport;

import org.cliffc.high_scale_lib.*;
import org.slf4j.*;
import org.zeromq.*;

import java.util.*;

import com.fluent.framework.core.*;

import static com.fluent.framework.util.FluentUtil.*;


public abstract class AbstractJMQTransport implements FluentLifecycle{

    private volatile boolean           toLog;
    private final Set<ZListener<ZMsg>> listeners;

    private final ZContext             context;
    private final int                  timeToLinger;
    private final int                  highWaterMark;

    private final static String        NAME   = AbstractJMQTransport.class.getSimpleName( );
    private final static Logger        LOGGER = LoggerFactory.getLogger( NAME );


    public AbstractJMQTransport( boolean toLog, int timeToLinger, int highWaterMark, ZContext context ){
        this.toLog = toLog;
        this.timeToLinger = timeToLinger;
        this.highWaterMark = highWaterMark;
        this.context = context;
        this.listeners = new NonBlockingHashSet<ZListener<ZMsg>>( );
    }


    public abstract void send( byte[ ] data );

    public abstract void send( String data );



    public final void flipLogging( ) {
        boolean before = toLog;
        toLog = !before;

        LOGGER.debug( "Toggled protocol log from [{}] to [{}].", before, toLog );
    }


    public final int getHighWaterMark( ) {
        return highWaterMark;
    }


    public final int getTimeToLinger( ) {
        return timeToLinger;
    }

    protected final void update( ZMsg message ) {
        for( ZListener<ZMsg> listener : listeners ){
            listener.update( message );
        }
    }

    protected final boolean subscribe( ZListener<ZMsg> listener ) {
        if( listener == null )
            return false;

        listeners.add( listener );
        LOGGER.debug( "Added [{}] as a listener.", listener.name( ) );

        return true;
    }


    public final void destroyContext( ) {
        context.destroy( );
    }


    protected final void logFrames( ZMsg zFrames ) {
        if( !toLog )
            return;

        StringBuilder builder = new StringBuilder( zFrames.size( ) );

        builder.append( L_BRACKET );
        for( ZFrame frame : zFrames ){
            builder.append( frame ).append( PIPE );
        }
        builder.append( R_BRACKET );

        LOGGER.debug( "{}", builder.toString( ) );

    }


}
