package com.fluent.framework.events.core;

import java.util.*;


public enum FluentEventCategory{

	FROM_TRADER_CATEGORY    ( true ),
    FROM_INTERNAL_CATEGORY  ( true ),
    FROM_ADMIN_CATEGORY   	( true ),
    FROM_MARKET_CATEGORY    ( true ),
    FROM_EXECUTION_CATEGORY ( true ),
    FROM_REFERENCE_CATEGORY ( true ),

    TO_MARKET_CATEGORY  	( false ),
    TO_ANALYSIS_CATEGORY  	( false ),
    TO_TRADER_CATEGORY  	( false );
	

    private final boolean isIncoming;

    public final static EnumSet<FluentEventCategory> OUTPUT  = EnumSet.of          ( TO_MARKET_CATEGORY, TO_ANALYSIS_CATEGORY, TO_TRADER_CATEGORY );
    public final static EnumSet<FluentEventCategory> INPUT   = EnumSet.complementOf( OUTPUT );


    FluentEventCategory( boolean isIncoming ){
        this.isIncoming = isIncoming;
    }

    
    public final boolean isEventInput( ){
        return isIncoming;
    }

    
    public final boolean isEventOutput( ){
        return !isEventInput();
    }

    
    public final static Set<FluentEventCategory> allInputs(){
    	return INPUT;
    }
    
    
    public final static Set<FluentEventCategory> allOutputs(){
    	return OUTPUT;
    }
    
    

}
