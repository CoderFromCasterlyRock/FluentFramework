package com.ccr.fluent.trading.events.out.response;

import com.ccr.fluent.trading.events.core.FluentEvent;


public interface ResponseEventProvider{
	
	public boolean addResponseEvent( ResponseEvent event );
    public boolean addResponseEvent( long eventId, FluentEvent event, String outputMessage );

}
