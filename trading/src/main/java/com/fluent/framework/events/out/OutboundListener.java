package com.fluent.framework.events.out;



public interface OutboundListener{

	public String name( );
    public boolean isSupported( OutboundType type );
    public boolean update( OutboundEvent event );

}
