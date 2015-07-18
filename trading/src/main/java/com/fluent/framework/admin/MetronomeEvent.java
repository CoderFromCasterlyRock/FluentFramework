package com.fluent.framework.admin;

import com.eclipsesource.json.JsonObject;
import com.fluent.framework.events.in.InEvent;

import static com.fluent.framework.events.in.InType.*;



public final class MetronomeEvent extends InEvent{
	
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
