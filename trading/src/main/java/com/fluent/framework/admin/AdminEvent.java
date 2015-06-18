package com.fluent.framework.admin;

import com.fluent.framework.events.core.FluentInboundEvent;
import com.fluent.framework.events.core.FluentInboundType;


public abstract class AdminEvent extends FluentInboundEvent{

	private final String message;
	private final String eventId;
	
	private final static String PREFIX = "Admin_";

	
	public AdminEvent( FluentInboundType type, String message ){
	    super( type );

	    this.message	= message;
        this.eventId	= PREFIX + type + "_" + getSequenceId();
    }
    
	
	@Override
	public final String getEventId( ){
        return eventId;
    }
	

    public final String getMessage( ){
        return message;
    }

    
}