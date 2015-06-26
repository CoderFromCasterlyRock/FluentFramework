package com.fluent.framework.events.in;

import com.fluent.framework.events.core.FluentEvent;


public abstract class InboundEvent extends FluentEvent{

    private final InboundType type;

    private final static long serialVersionUID = 1l;
    
    
    public InboundEvent( InboundType type ){
        this.type   = type;    	
    }   

   
    @Override
    public final InboundType getType( ){
        return type;
    }


}
