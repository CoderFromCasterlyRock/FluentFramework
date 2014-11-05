package com.ccr.fluent.trading.events.in;

import com.eclipsesource.json.*;

import com.ccr.fluent.trading.events.core.*;

import static com.ccr.fluent.trading.events.core.FluentJsonTags.*;
import static com.ccr.fluent.trading.events.core.FluentInputEventType.*;



public final class LoopbackEvent extends FluentInputEvent{

    private final String reason;
    private final FluentInputEvent event;

    public LoopbackEvent( long eventId, String reason, FluentInputEvent event ){
        this( false, eventId, reason, event );
    }

    public LoopbackEvent( boolean isValid, long eventId, String reason, FluentInputEvent event ){
        super( isValid, eventId, LOOPBACK_EVENT );

        this.event  	= event;
        this.reason	= reason;
    }

    public final String getReason( ){
        return reason;
    }

    public final FluentInputEvent getLoopbackEvent( ){
        return event;
    }

    public final FluentInputEventType getLoopbackEventType( ){
        return event.getType();
    }
    
   
    @Override
    protected final String toJSON( final JsonObject object ){
        object.add( REASON.field(),   			getReason() );
        object.add( INPUT_EVENT_ID.field(), 	getLoopbackEvent().getEventId());
        object.add( INPUT_EVENT_TYPE.field(), 	getLoopbackEvent().getType().name());
                
        return object.toString();
        		
       
    }
    
    
    public final static LoopbackEvent convert( final String jsonString, final JsonObject object ){
             /*
        String originalString   = valueAsString( LOOPBACK_MESSAGE, object );



        return new LoopbackEvent(   valueAsBoolean( EVENT_VALID, object ),
                                    valueAsLong( EVENT_ID, object ),
                                    valueAsString( LOOPBACK_MESSAGE, object );
        */
        return null;
    }

}
