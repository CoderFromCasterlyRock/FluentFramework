package com.fluent.framework.admin;

import com.fluent.framework.events.in.InboundEvent;
import com.fluent.framework.events.in.InboundType;


public abstract class AdminEvent extends InboundEvent{

	private final String message;
	private final String eventId;
	
	private final static String PREFIX = "Admin_";
	private final static long serialVersionUID = 1l;
	
	public AdminEvent( InboundType type, String message ){
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