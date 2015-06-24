package com.fluent.framework.events.in;



public interface FluentInboundListener{

	public String name();
    public boolean isSupported( FluentInboundType type );
    public boolean update( FluentInboundEvent event );

}
