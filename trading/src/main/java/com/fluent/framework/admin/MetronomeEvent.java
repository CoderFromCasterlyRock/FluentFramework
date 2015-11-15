package com.fluent.framework.admin;

import com.fluent.framework.events.in.InEvent;

import static com.fluent.framework.events.in.InType.*;


public final class MetronomeEvent extends InEvent{
	
	private final static long serialVersionUID = 1L;
	

	public MetronomeEvent(){
		super( METRONOME_EVENT );
	}
	
	@Override
	public final void toEventString( StringBuilder builder ){}
	
	
}

