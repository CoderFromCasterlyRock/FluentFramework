package com.fluent.framework.events.out;

import com.fluent.framework.events.core.*;



public abstract class FluentOutEvent extends FluentEvent{

    private final FluentOutType type;

    private final static long   serialVersionUID = 1l;

    public FluentOutEvent( FluentOutType type ){
        this.type = type;
    }


    @Override
    public final FluentOutType getType( ) {
        return type;
    }


}
