package com.fluent.etrading.framework.events.core;


public abstract class FluentOutputEvent extends FluentEvent{

    private final FluentOutputEventType type;

    public FluentOutputEvent( long eventId, FluentOutputEventType type ){
        this( true, eventId, type );
    }
    
    public FluentOutputEvent( boolean isValid, long eventId, FluentOutputEventType type ){
        super( isValid, eventId );

        this.type   = type;    	
    }

    @Override
    public final FluentOutputEventType getType( ){
        return type;
    }


}
