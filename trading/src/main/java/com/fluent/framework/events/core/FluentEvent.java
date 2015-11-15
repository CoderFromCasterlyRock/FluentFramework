package com.fluent.framework.events.core;

import java.io.Serializable;
import com.fluent.framework.collection.*;

import static com.fluent.framework.util.TimeUtil.*;
import static com.fluent.framework.util.FluentUtil.*;
import static com.fluent.framework.events.core.FluentJsonTags.*;


public abstract class FluentEvent implements Serializable{

	private final long sequenceId;
            
    private volatile long[] timestamps;

	private final static long serialVersionUID 			= 1l;
	private final static FluentAtomicLong SEQUENCE_GEN 	= new FluentAtomicLong( ZERO );

    protected FluentEvent( ){
    	this.sequenceId		= SEQUENCE_GEN.getAndIncrement();
    	this.timestamps		= new long[]{ currentNanos(), ZERO, ZERO, ZERO };
    }
    
    
    public abstract FluentEventType getType();
    public abstract void toEventString( StringBuilder builder );
    
    
    public final long getSequenceId( ){
        return sequenceId;
    }

    
    public final long getCreationTime( ){
        return timestamps[ZERO];
    }
    
    
    public final void setDispatchTime( ){
    	timestamps[ONE] = currentNanos();
    }
    
    
    public final void setConsumedTime( ){
    	timestamps[TWO] = currentNanos();
    }
    
    
    public final void setAdaptorTime( ){
        timestamps[THREE] = currentNanos();
    }
    

    
    public final String getEventId( ){
    	return getType() + UNDERSCORE + sequenceId;
    }
    
    
    public final boolean isIncoming( ){
        return getType().isIncoming();
    }
        
    
    /*
    protected final long getNextId( ){
    	boolean isWarmingUp = ( WARMING_UP == FluentStateManager.getState() );
    	return ( isWarmingUp ) ? SEQUENCE_GEN.get() : SEQUENCE_GEN.getAndIncrement(); 
    }
    */
        
    
    @Override
    public final String toString( ){
    	
    	StringBuilder builder = new StringBuilder( EIGHT * SIXTY_FOUR );
    	
        //Header
    	toEventString( builder );

        //Footer
    	builder.append( SEQUENCE ).append( COLON).append( sequenceId ).append( COMMASP );
    	builder.append( EVENT_TYPE ).append( COLON).append( getType() );
    	
    	
        return builder.toString();
        
    }
        

}
