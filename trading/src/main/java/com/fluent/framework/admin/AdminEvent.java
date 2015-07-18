package com.fluent.framework.admin;

import com.fluent.framework.events.in.InEvent;
import com.fluent.framework.events.in.InType;


public abstract class AdminEvent extends InEvent{

	private final String message;
	private final String eventId;
	
	private final static String PREFIX = "Admin_";
	private final static long serialVersionUID = 1l;
	
	public AdminEvent( InType type, String message ){
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