package com.ccr.fluent.trading.events.core;

import com.ccr.fluent.trading.events.core.FluentInputEvent;


public interface FluentInputEventListener{

    public String name( );
    public boolean isSupported( FluentInputEventType type );
    public void update( FluentInputEvent event );

}
