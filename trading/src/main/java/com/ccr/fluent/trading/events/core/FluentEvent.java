package com.ccr.fluent.trading.events.core;

import com.eclipsesource.json.*;

import static com.ccr.fluent.trading.utility.ContainerUtil.*;
import static com.ccr.fluent.trading.events.core.FluentJsonTags.*;


public abstract class FluentEvent{

    private final long eventId;
    private final boolean isValid;
    private final long timeCreated;
    
    protected FluentEvent( boolean isValid, long eventId ){
        this.eventId		= eventId;
        this.isValid    	= isValid;
        this.timeCreated	= System.currentTimeMillis();
    }

    public abstract FluentEventType getType();
    protected abstract String toJSON( JsonObject object );

    public final long getEventId( ){
        return eventId;
    }

    public final boolean isValid( ){
        return isValid;
    }
    
    public final long getCreationTime( ){
        return timeCreated;
    }

    public final FluentEventCategory getCategory( ){
        return getType().getCategory();
    }
    
    
    public final String toJSON( ){
    	
    	JsonObject object = new JsonObject( );
    	
        //Header
        object.add( EVENT_ID.field(),       	getEventId() );
        object.add( EVENT_TYPE.field(),     	getType().getName() );
        object.add( TIMESTAMP.field(),          formatTime( getCreationTime()) );
        
        toJSON( object );

        //Footer
        object.add( EVENT_VALID.field(),    	isValid() );
        object.add( EVENT_CATEGORY.field(), 	getCategory().name() );
        

        return toJSON( object );
        
    }
    

}
