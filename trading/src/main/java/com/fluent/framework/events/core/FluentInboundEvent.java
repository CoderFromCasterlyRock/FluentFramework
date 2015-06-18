package com.fluent.framework.events.core;


public abstract class FluentInboundEvent extends FluentEvent{

    private final FluentInboundType type;

    public FluentInboundEvent( FluentInboundType type ){
        this.type   = type;    	
    }
        

    @Override
    public final FluentInboundType getType( ){
        return type;
    }


}
