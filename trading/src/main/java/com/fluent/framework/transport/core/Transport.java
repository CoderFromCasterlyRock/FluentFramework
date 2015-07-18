package com.fluent.framework.transport.core;

import com.fluent.framework.core.*;
import com.fluent.framework.events.core.FluentDataListener;

public interface Transport extends FluentService{
	
	public boolean isConnected();
	public TransportType getType();
	
	public boolean register( FluentDataListener listener );
	public boolean deregister( FluentDataListener listener );
	
}
