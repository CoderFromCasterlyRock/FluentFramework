package com.fluent.framework.internal;

import com.eclipsesource.json.*;
import com.fluent.framework.events.in.InboundEvent;

import static com.fluent.framework.events.core.FluentJsonTags.*;
import static com.fluent.framework.events.in.InboundType.*;



public final class LoopbackEvent extends InboundEvent{

	private final boolean isValid;
    private final String reason;
    private final String eventId;
    private final InboundEvent event;

    private final static String PREFIX = "LOOPBACK_";
    private final static long serialVersionUID = 1l;
    
    public LoopbackEvent( boolean isValid, InboundEvent event, String reason ){
        super( LOOPBACK_EVENT );

        this.isValid	= isValid;
        this.event  	= event;
        this.reason		= reason;
        this.eventId	= PREFIX + event.getEventId();
        
    }

    
    @Override
    public final String getEventId( ){
        return eventId;
    }
    
    
    public final boolean isValid( ){
        return isValid;
    }
    
    
    public final String getReason( ){
        return reason;
    }


    public final InboundEvent getLoopbackEvent( ){
        return event;
    }

    
   
    @Override
    protected final void toJSON( final JsonObject object ){
    	
        object.add( REASON.field(),   			getReason() );
        object.add( INPUT_EVENT_ID.field(), 	getLoopbackEvent().getEventId());
        object.add( INPUT_EVENT_TYPE.field(), 	getLoopbackEvent().getType().getName());
                
    }
    

}
