package com.fluent.framework.events.in;

import com.eclipsesource.json.*;
import com.fluent.framework.events.core.*;

import static com.fluent.framework.events.core.FluentInboundType.*;
import static com.fluent.framework.events.core.FluentJsonTags.*;



public final class LoopbackEvent extends FluentInboundEvent{

	private final boolean isValid;
    private final String reason;
    private final String eventId;
    private final FluentInboundEvent event;

    private final static String PREFIX = "LOOPBACK_";
    
    
    public LoopbackEvent( boolean isValid, FluentInboundEvent event, String reason ){
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


    public final FluentInboundEvent getLoopbackEvent( ){
        return event;
    }

    
   
    @Override
    protected final String toJSON( final JsonObject object ){
        object.add( REASON.field(),   			getReason() );
        object.add( INPUT_EVENT_ID.field(), 	getLoopbackEvent().getEventId());
        object.add( INPUT_EVENT_TYPE.field(), 	getLoopbackEvent().getType().getName());
                
        return object.toString();
    }
    

}
