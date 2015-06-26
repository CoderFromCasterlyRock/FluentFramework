package com.fluent.framework.events.out;

import com.fluent.framework.events.core.FluentEvent;



public abstract class OutboundEvent extends FluentEvent{

    private final OutboundType type;

    private final static long serialVersionUID = 1l;
     
    public OutboundEvent( OutboundType type ){
    	this.type   = type;    	
    }
    
    
    @Override
    public final OutboundType getType( ){
        return type;
    }


}
