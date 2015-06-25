package com.fluent.framework.admin;

import com.eclipsesource.json.JsonObject;
import com.fluent.framework.events.in.FluentInboundEvent;

import static com.fluent.framework.events.in.FluentInboundType.*;



public final class MetronomeEvent extends FluentInboundEvent{
	
	private final String eventId;
	private final long secondsToClose;
	
	private final static long serialVersionUID = 1L;
	private final static String PREFIX			= "Metronome_"; 
	

	public MetronomeEvent( long secondsToClose ){
		super( METRONOME_EVENT );
		
		this.eventId	  	= PREFIX + getSequenceId(); 
		this.secondsToClose = secondsToClose;
	}
	
	
	@Override
	public final String getEventId( ){
		return eventId;
	}

	
	public final long getSecondsToClose( ){
		return secondsToClose;
	}

	
	@Override
	public void toJSON( JsonObject object ){}
	

}
