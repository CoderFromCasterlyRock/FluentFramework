package com.fluent.framework.events.core;

import java.io.*;

import com.fluent.framework.collection.*;

import static com.fluent.framework.events.core.FluentJsonTags.*;
import static com.fluent.framework.util.FluentUtil.*;
import static com.fluent.framework.util.TimeUtil.*;


public abstract class FluentEvent implements Serializable{

    private volatile long                 dispatchTime;
    private volatile long                 consumedTime;

    private final long                    sequenceId;
    private final String                  eventId;
    private final long                    creationTime;

    private final static long             serialVersionUID = 1l;
    private final static FluentAtomicLong SEQUENCE_GEN     = new FluentAtomicLong( ZERO );


    protected FluentEvent( ){
        this( SEQUENCE_GEN.getAndIncrement( ) );
    }


    protected FluentEvent( long sequenceId ){
        this.sequenceId = sequenceId;
        this.creationTime = currentNanos( );
        this.eventId = getType( ) + UNDERSCORE + sequenceId;
    }


    public abstract FluentEventType getType( );

    public abstract void toEventString( StringBuilder builder );


    public final long getSequenceId( ) {
        return sequenceId;
    }


    public final String getEventId( ) {
        return eventId;
    }


    public final long getCreationTime( ) {
        return creationTime;
    }


    public final void setDispatchTime( ) {
        dispatchTime = currentNanos( );
    }


    public final long getDispatchedTime( ) {
        return dispatchTime;
    }


    public final void setConsumedTime( ) {
        consumedTime = currentNanos( );
    }


    public final long getConsumedTime( ) {
        return consumedTime;
    }


    public final boolean isIncoming( ) {
        return getType( ).isIncoming( );
    }


    /*
     * protected final long getNextId( ){ boolean isWarmingUp = ( WARMING_UP ==
     * FluentStateManager.getState() ); return ( isWarmingUp ) ? SEQUENCE_GEN.get() :
     * SEQUENCE_GEN.getAndIncrement(); }
     */


    @Override
    public final String toString( ) {

        StringBuilder builder = new StringBuilder( EIGHT * SIXTY_FOUR );

        // Header
        builder.append( eventId ).append( COMMASP );
        toEventString( builder );

        // Footer
        builder.append( COMMASP );
        builder.append( SEQUENCE.field( ) ).append( COLON ).append( sequenceId );
        builder.append( COMMASP );
        builder.append( EVENT_TYPE.field( ) ).append( COLON ).append( getType( ) );

        return builder.toString( );

    }


}
