package com.fluent.framework.events.core;


public interface FluentOutboundListener{

	public String name( );
    public boolean isSupported( FluentOutboundType type );
    public boolean update( FluentOutboundEvent event );

}
