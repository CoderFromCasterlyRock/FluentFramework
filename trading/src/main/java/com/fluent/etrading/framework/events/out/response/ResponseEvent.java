package com.fluent.etrading.framework.events.out.response;

import com.eclipsesource.json.*;
import com.fluent.etrading.framework.events.core.*;

import static com.fluent.etrading.framework.events.core.FluentJsonTags.*;


public final class ResponseEvent extends FluentOutputEvent{

    private final FluentEvent event;
    private final String outputMessage;


    public ResponseEvent( long eventId, FluentEvent event, String outputMessage ){
        this( true, eventId, event, outputMessage );
    }

    public ResponseEvent( boolean isValid, long eventId, FluentEvent event, String outputMessage ){
        super( isValid, eventId, FluentOutputEventType.EVENT_TO_TRADER );

        this.event       	= event;
        this.outputMessage    = outputMessage;
    }


    public final FluentEvent getInputEvent( ){
        return event;
    }


    public final String getOutputMessage( ){
        return outputMessage;
    }

    
    @Override
    protected final String toJSON( final JsonObject object ){
        object.add( UPDATE_MESSAGE.field(),     getOutputMessage() );
        
        if( event != null ){
        	object.add( INPUT_EVENT_ID.field(), 	event.getEventId());
        	object.add( INPUT_EVENT_TYPE.field(), 	event.getType().toString());
        }
        
        return object.toString();

    }


    public final static ResponseEvent convert( final String jsonString, final JsonObject object ){
    	return null;
    	/*
        return new ResponseEvent(
                valueAsBoolean( EVENT_VALID, object ),
                valueAsLong( EVENT_ID, object ),
                valueAsString( INPUT_JSON_MESSAGE, object ),
                valueAsString( UPDATE_MESSAGE, object )
        );
        */

    }


}
