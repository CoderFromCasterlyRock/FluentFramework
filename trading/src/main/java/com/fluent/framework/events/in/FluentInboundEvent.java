package com.fluent.framework.events.in;

import com.fluent.framework.events.core.FluentEvent;


public abstract class FluentInboundEvent extends FluentEvent{

    private final FluentInboundType type;

    private final static long serialVersionUID = 1l;
    
    
    public FluentInboundEvent( FluentInboundType type ){
        this.type   = type;    	
    }   

   
    @Override
    public final FluentInboundType getType( ){
        return type;
    }


}
