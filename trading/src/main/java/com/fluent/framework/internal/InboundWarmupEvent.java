package com.fluent.framework.internal;

import static com.fluent.framework.events.in.FluentInboundType.*;


public final class InboundWarmupEvent extends InternalEvent{
	
	private final String eventId;
		
	private final static long serialVersionUID = 1L;
	private final static String PREFIX			= "Warmup_"; 
	

	public InboundWarmupEvent( ){
		super( WARM_UP_EVENT );
		
		this.eventId	  	= PREFIX + getSequenceId(); 
	}
	
	@Override
	public final String getEventId( ){
		return eventId;
	}
	

}
