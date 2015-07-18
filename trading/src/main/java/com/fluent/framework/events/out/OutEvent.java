package com.fluent.framework.events.out;

import com.fluent.framework.events.core.FluentEvent;



public abstract class OutEvent extends FluentEvent{

    private final OutType type;

    private final static long serialVersionUID = 1l;
     
    public OutEvent( OutType type ){
    	this.type   = type;    	
    }
    
    
    @Override
    public final OutType getType( ){
        return type;
    }


}
