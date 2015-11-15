package com.fluent.framework.admin;

import com.fluent.framework.events.in.InEvent;
import com.fluent.framework.market.core.Exchange;

import static com.fluent.framework.events.in.InType.*;


public final class AdminClosingEvent extends InEvent{
	
	private final Exchange exchange;
	private final TimedTask timeTask;
	
	private final static long serialVersionUID = 1L;
	
	
	public AdminClosingEvent( Exchange exchange, TimedTask timeTask ){
		super( CLOSING_EVENT );
		
		this.exchange	= exchange;
		this.timeTask 	= timeTask;
	}
	
	
	public final boolean appClosing( ){
		return (Exchange.ALL == exchange);
	}
	

	public final Exchange getExchange( ){
		return exchange;
	}
	
	
	public final TimedTask getTimedTask( ){
		return timeTask;
	}
	
	
	public final boolean isClosing( ){
		return timeTask.isClosing();
	}
	

	@Override
	public final void toEventString( StringBuilder builder ){
	
		if( appClosing() ){
			builder.append( "AppClosing:").append( appClosing() );
			builder.append( ", TimedTask:").append( timeTask );
		}else{
			builder.append( ", Exchange:").append( exchange );
			builder.append( ", TimedTask:").append( timeTask );
		}
		
	}

	
}

