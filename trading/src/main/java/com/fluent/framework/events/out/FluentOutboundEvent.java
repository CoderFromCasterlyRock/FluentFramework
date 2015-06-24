package com.fluent.framework.events.out;

import com.fluent.framework.events.core.FluentEvent;



public abstract class FluentOutboundEvent extends FluentEvent{

    private final FluentOutboundType type;

    private final static long serialVersionUID = 1l;
     
    public FluentOutboundEvent( FluentOutboundType type ){
    	this.type   = type;    	
    }
    
    
    @Override
    public final FluentOutboundType getType( ){
        return type;
    }


}
