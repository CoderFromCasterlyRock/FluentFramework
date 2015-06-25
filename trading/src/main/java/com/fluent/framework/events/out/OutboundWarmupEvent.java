package com.fluent.framework.events.out;

import com.eclipsesource.json.JsonObject;

import static com.fluent.framework.events.out.FluentOutboundType.*;


public final class OutboundWarmupEvent extends FluentOutboundEvent{
	
	private final String eventId;
		
	private final static long serialVersionUID = 1L;
	private final static String PREFIX			= "WarmupOut_"; 
	

	public OutboundWarmupEvent( ){
		super( WARM_UP_EVENT );
		
		this.eventId	  	= PREFIX + getSequenceId(); 
	}
	
	
	@Override
	public final String getEventId( ){
		return eventId;
	}

	
	@Override
	protected final void toJSON( JsonObject object ) {}
	
	

}
