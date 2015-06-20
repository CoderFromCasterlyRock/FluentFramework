package com.fluent.framework.events.core;

import java.io.Serializable;

import com.eclipsesource.json.*;
import com.fluent.framework.collection.*;

import static com.fluent.framework.events.core.FluentJsonTags.*;
import static com.fluent.framework.util.TimeUtil.*;


public abstract class FluentEvent implements Serializable{

	private final long sequenceId;
    private final long timeCreated;

	private final static long serialVersionUID 			= 1l;
    private final static FluentAtomicLong SEQUENCE_GEN 	= new FluentAtomicLong(); 
    
    
    protected FluentEvent( ){
        this.timeCreated	= currentNanos();
        this.sequenceId		= nextSequenceId();
    }


    public abstract String getEventId( );
    public abstract FluentEventType getType();
    protected abstract void toJSON( JsonObject object );


    public final long getSequenceId( ){
        return sequenceId;
    }

    
    public final long getCreationTime( ){
        return timeCreated;
    }

    
    public final boolean isIncoming( ){
        return getCategory().isEventInput();
    }
    
    
    public final FluentEventCategory getCategory( ){
        return getType().getCategory();
    }
    
    
    protected final static long nextSequenceId( ){
    	//check the state, if we are in warmup then dont increment the sequenceId
        return SEQUENCE_GEN.getAndIncrement();
    }
    
    
    public final String toJSON( ){
    	
    	JsonObject object = new JsonObject( );
    	
        //Header
        object.add( EVENT_ID.field(),       	getEventId() );
        object.add( EVENT_TYPE.field(),     	getType().getName() );
        object.add( TIMESTAMP.field(),          timeCreated );
        object.add( SEQUENCE.field(),       	sequenceId );
        
        toJSON( object );

        //Footer
        object.add( EVENT_CATEGORY.field(), 	getCategory().name() );
        

        return object.toString();
        
    }
    
    
    @Override
    public String toString(){
    	return toJSON();
    }
    

}
