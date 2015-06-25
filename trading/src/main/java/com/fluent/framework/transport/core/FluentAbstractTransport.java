package com.fluent.framework.transport.core;

import java.util.*;
import java.util.concurrent.*;


public abstract class FluentAbstractTransport implements FluentTransport{

	private final FluentTransportType transportType;
	private final AbstractSet<FluentTransportListener> listeners;
	
	
	public FluentAbstractTransport( FluentTransportType transportType ){
		this.transportType 	= transportType;
		this.listeners		= new CopyOnWriteArraySet<>(); 
	}
	
	
	@Override
	public final FluentTransportType getType( ){
		return transportType;
	}

	
	@Override
	public final boolean register( FluentTransportListener listener ){
		if( listener == null ) return false;
		
		return listeners.add( listener );
	}

		
	@Override
	public final boolean deregister( FluentTransportListener listener ){
		if( listener == null ) return false;
		
		return listeners.remove( listener );
	}
	
	
	protected final void distribute( String message ){
		for( FluentTransportListener listener : listeners ){
			listener.onMessage( message );
		}
	}
	
	

}
