package com.fluent.framework.events.in;

import com.eclipsesource.json.JsonObject;

import static com.fluent.framework.events.in.InboundType.*;


public final class InboundWarmupEvent extends InboundEvent{
	
	private final String eventId;
		
	private final static long serialVersionUID = 1L;
	private final static String PREFIX			= "Warmup_"; 
	
	
	public InboundWarmupEvent( ){
		super( WARM_UP_EVENT );
		
		this.eventId	= PREFIX + getSequenceId(); 
	}
	
	
	@Override
	public final String getEventId( ){
		return eventId;
	}

	
	@Override
	protected final void toJSON( JsonObject object ){}
	

}
