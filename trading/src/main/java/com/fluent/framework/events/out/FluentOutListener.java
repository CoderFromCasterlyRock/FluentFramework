package com.fluent.framework.events.out;



public interface FluentOutListener{

    public String name( );

    public boolean isSupported( FluentOutType type );

    public boolean outUpdate( FluentOutEvent event );

}
