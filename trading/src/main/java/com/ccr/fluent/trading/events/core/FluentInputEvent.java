package com.ccr.fluent.trading.events.core;


public abstract class FluentInputEvent extends FluentEvent{

    private final FluentInputEventType type;

    public FluentInputEvent( long eventId, FluentInputEventType type ){
        this( true, eventId, type );
    }

    public FluentInputEvent( boolean isValid, long eventId, FluentInputEventType type ){
        super( isValid, eventId );

        this.type   = type;    	
    }
        

    @Override
    public final FluentInputEventType getType( ){
        return type;
    }


}
