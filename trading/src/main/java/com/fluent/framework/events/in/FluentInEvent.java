package com.fluent.framework.events.in;

import com.fluent.framework.events.core.*;


public abstract class FluentInEvent extends FluentEvent{

    private final FluentInType type;

    private final static long  serialVersionUID = 1l;


    public FluentInEvent( FluentInType type ){
        this.type = type;
    }


    @Override
    public final FluentInType getType( ) {
        return type;
    }


}
