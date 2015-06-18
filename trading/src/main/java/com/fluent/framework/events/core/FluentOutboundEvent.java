package com.fluent.framework.events.core;



public abstract class FluentOutboundEvent extends FluentEvent{

    private final FluentOutboundType type;

     
    public FluentOutboundEvent( FluentOutboundType type ){
    	this.type   = type;    	
    }

    @Override
    public final FluentOutboundType getType( ){
        return type;
    }


}
