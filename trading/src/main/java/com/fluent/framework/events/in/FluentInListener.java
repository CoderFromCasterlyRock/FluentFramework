package com.fluent.framework.events.in;



public interface FluentInListener{

    public String name( );

    public boolean isSupported( FluentInType type );

    public boolean inUpdate( FluentInEvent event );

}
