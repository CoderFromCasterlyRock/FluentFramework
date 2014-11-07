package com.fluent.etrading.framework.events.out.response;

import com.fluent.etrading.framework.events.core.FluentEvent;


public interface ResponseEventProvider{
	
	public boolean addResponseEvent( ResponseEvent event );
    public boolean addResponseEvent( long eventId, FluentEvent event, String outputMessage );

}
