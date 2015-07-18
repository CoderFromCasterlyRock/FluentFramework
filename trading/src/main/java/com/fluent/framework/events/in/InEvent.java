package com.fluent.framework.events.in;

import com.fluent.framework.events.core.FluentEvent;


public abstract class InEvent extends FluentEvent{

    private final InType type;

    private final static long serialVersionUID = 1l;
    
    
    public InEvent( InType type ){
        this.type   = type;    	
    }   

   
    @Override
    public final InType getType( ){
        return type;
    }


}
