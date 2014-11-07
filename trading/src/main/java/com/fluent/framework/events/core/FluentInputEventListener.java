package com.fluent.framework.events.core;

import com.fluent.framework.events.core.FluentInputEvent;


public interface FluentInputEventListener{

    public String name( );
    public boolean isSupported( FluentInputEventType type );
    public void update( FluentInputEvent event );

}
