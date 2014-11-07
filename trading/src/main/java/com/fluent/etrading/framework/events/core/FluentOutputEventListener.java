package com.fluent.etrading.framework.events.core;


public interface FluentOutputEventListener{

    public String name( );
    public boolean isSupported( FluentOutputEventType type );
    public void update( FluentOutputEvent event );

}
