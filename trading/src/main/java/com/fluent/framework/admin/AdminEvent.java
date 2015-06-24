package com.fluent.framework.admin;

import com.fluent.framework.events.in.FluentInboundEvent;
import com.fluent.framework.events.in.FluentInboundType;


public abstract class AdminEvent extends FluentInboundEvent{

	private final String message;
	private final String eventId;
	
	private final static String PREFIX = "Admin_";
	private final static long serialVersionUID = 1l;
	
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