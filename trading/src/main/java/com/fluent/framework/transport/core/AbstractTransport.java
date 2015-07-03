package com.fluent.framework.transport.core;

import java.util.*;
import java.util.concurrent.*;

import com.fluent.framework.events.core.FluentDataListener;


public abstract class AbstractTransport implements Transport{

	private final TransportType type;
	private final AbstractSet<FluentDataListener> listeners;
	
	
	public AbstractTransport( TransportType type ){
		this.type 		= type;
		this.listeners	= new CopyOnWriteArraySet<>(); 
	}
	
	
	@Override
	public final TransportType getType( ){
		return type;
	}

	
	@Override
	public final boolean register( FluentDataListener listener ){
		if( listener == null ) return false;
		
		return listeners.add( listener );
	}

		
	@Override
	public final boolean deregister( FluentDataListener listener ){
		if( listener == null ) return false;
		
		return listeners.remove( listener );
	}
	
	
	protected final void distribute( String message ){
		
		for( FluentDataListener listener : listeners ){
			listener.onMessage( message );
		}
	}
	
	

}
