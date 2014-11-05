package com.ccr.fluent.trading.events.core;

import java.util.*;


public enum FluentEventCategory{

	ADMIN_CATEGORY   	( true ),
    INTERNAL_CATEGORY   ( true ),
    TRADER_CATEGORY     ( true ),
    MARKET_CATEGORY     ( true ),
    EXECUTION_CATEGORY  ( true ),
    REFERENCE_CATEGORY  ( true ),

    TO_MARKET_CATEGORY  ( false ),
    TO_LOGGER_CATEGORY  ( false ),
    TO_TRADER_CATEGORY  ( false );


    private final boolean isEventInput;

    private final static EnumSet<FluentEventCategory> OUTPUT  = EnumSet.of          ( TO_MARKET_CATEGORY, TO_TRADER_CATEGORY );
    private final static EnumSet<FluentEventCategory> INPUT   = EnumSet.complementOf( OUTPUT );


    FluentEventCategory( boolean isEventInput ){
        this.isEventInput = isEventInput;
    }

    
    public final boolean isEventInput( ){
        return isEventInput;
    }

    
    public final boolean isEventOutput( ){
        return !isEventInput;
    }


    public final static EnumSet<FluentEventCategory> allInput(){
        return INPUT;
    }

    public final static EnumSet<FluentEventCategory> allOutput(){
        return OUTPUT;
    }


}
