package com.ccr.fluent.trading.events.in;

import com.ccr.fluent.trading.events.core.FluentInputEvent;
import com.ccr.fluent.trading.events.core.FluentInputEventType;


public abstract class TraderDataEvent extends FluentInputEvent{

    private final String owner;


    public TraderDataEvent( long eventId, FluentInputEventType type, String owner ){
        super( eventId, type );

        this.owner   = owner;

    }


    public final String getStrategyOwner( ){
        return owner;
    }


}
