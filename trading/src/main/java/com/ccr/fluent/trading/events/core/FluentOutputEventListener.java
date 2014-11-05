package com.ccr.fluent.trading.events.core;


public interface FluentOutputEventListener{

    public String name( );
    public boolean isSupported( FluentOutputEventType type );
    public void update( FluentOutputEvent event );

}
