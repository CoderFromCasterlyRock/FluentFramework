package com.fluent.etrading.framework.events.in;

import com.eclipsesource.json.*;
import com.fluent.etrading.framework.events.core.FluentInputEvent;
import com.fluent.etrading.framework.events.core.FluentInputEventType;

import static com.fluent.etrading.framework.events.core.FluentJsonTags.*;
import static com.fluent.etrading.framework.utility.JSONUtil.*;


public final class AdminEvent extends FluentInputEvent{

	private final String reason;


	public AdminEvent( long eventId, FluentInputEventType type, String reason ){
	    super( eventId, type );

        this.reason		= reason;
    }
    

    public final String getReason( ){
        return reason;
    }

    
    @Override
    protected final String toJSON( final JsonObject object ){
        object.add( REASON.field(), reason );
        
        return object.toString();
    }
    

    
    public final static AdminEvent convert( final String jsonString, final JsonObject object ){

        return new AdminEvent( 	valueAsLong( EVENT_ID, object ),
                				valueAsInputType( object ),
                				valueAsString(REASON, object) );
    }
    
}