package com.ccr.fluent.trading.events.in;

import com.eclipsesource.json.JsonObject;
import com.ccr.fluent.trading.events.core.*;

import static com.ccr.fluent.trading.utility.JSONUtil.*;
import static com.ccr.fluent.trading.events.core.FluentJsonTags.*;



public final class ReferenceDataEvent extends FluentInputEvent{

	private final String data;
    
    public ReferenceDataEvent( long eventId, FluentInputEventType type, String data ){

        super( eventId, type );

        this.data	= data;
    }


    public final String getData( ){
        return data;
    }

    
    @Override
    protected final String toJSON( final JsonObject object ){

        object.add( REFERENCE_DATA.field(), getData() );

        return object.toString();
    }


    public final static ReferenceDataEvent convert( final String jsonString, final JsonObject object ){

        return new ReferenceDataEvent( 	valueAsLong(EVENT_ID, object),
                                        valueAsInputType( object ),
                                        valueAsString(REFERENCE_DATA, object) );
    
    }
}
