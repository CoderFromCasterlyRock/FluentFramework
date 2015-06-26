package com.fluent.framework.events.in;



public interface InboundListener{

	public String name();
    public boolean isSupported( InboundType type );
    public boolean update( InboundEvent event );

}
