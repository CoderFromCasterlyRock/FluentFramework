package com.fluent.framework.events.in;

import com.eclipsesource.json.JsonObject;
import com.fluent.framework.events.core.*;

import static com.fluent.framework.events.core.FluentInboundType.*;
import static com.fluent.framework.events.core.FluentJsonTags.*;
import static com.fluent.framework.util.FluentUtil.*;


public final class ReferenceDataEvent extends FluentInboundEvent{

	private final String eventId;
	private final String message;
	
	private final static String PREFIX = "REFERENCE_";
	private final static long serialVersionUID = 1l;
	
	public ReferenceDataEvent( String message ){

        super( REFERENCE_DATA );

        this.message	= message;
        this.eventId	= PREFIX + UNDERSCORE + getSequenceId();
    }


	@Override
	public final String getEventId( ){
        return eventId;
    }
	
	
    public final String getData( ){
        return message;
    }

    
    @Override
    protected final String toJSON( final JsonObject object ){
        object.add( REF_DATA_TAG.field(), getData() );
        return object.toString();
    }


}

