package com.fluent.framework.events.core;


public interface FluentInboundListener{

	public String name();
    public boolean isSupported( FluentInboundType type );
    public boolean update( FluentInboundEvent event );

}
