package com.fluent.framework.transport.core;

import com.fluent.framework.core.*;

public interface FluentTransport extends FluentStartable{
	
	public boolean isConnected();
	public FluentTransportType getType();
	
	public boolean register( FluentTransportListener listener );
	public boolean deregister( FluentTransportListener listener );
	
}
