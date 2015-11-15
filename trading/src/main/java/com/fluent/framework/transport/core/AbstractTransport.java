package com.fluent.framework.transport.core;

import java.util.*;
import java.util.concurrent.*;

import com.fluent.framework.events.core.FluentDataListener;
import com.fluent.framework.transport.core.Transport;
import com.fluent.framework.transport.core.TransportType;


public abstract class AbstractTransport implements Transport<FluentDataListener>{

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
	public final void register( FluentDataListener listener ){
		listeners.add( listener );
	}

		
	@Override
	public final void deregister( FluentDataListener listener ){
		listeners.remove( listener );
	}
	
	
	protected final void distribute( String message ){
		
		for( FluentDataListener listener : listeners ){
			listener.onMessage( message );
		}
	}
	
	

}
