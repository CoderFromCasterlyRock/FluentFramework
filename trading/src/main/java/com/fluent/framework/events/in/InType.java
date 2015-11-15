package com.fluent.framework.events.in;

import java.util.*;

import com.fluent.framework.events.core.FluentEventType;


public enum InType implements FluentEventType{

	NEW_STRATEGY,
	MODIFY_STRATEGY,
	CANCEL_STRATEGY,
        
	MARKET_DATA,
	MARKET_STATUS,
	
	EXECUTION_REPORT,
	REFERENCE_DATA,
	
	CLOSING_EVENT,
	METRONOME_EVENT,
	WARM_UP_EVENT,
	START_UP_EVENT,
	CLOSING_DOWN_EVENT,
	LOOPBACK_EVENT,

	EXCHANGE_UP,
	EXCHANGE_DOWN,
	CANCEL_ALL_STRATEGY;
	
    private final static Set<InType> ALL_TYPES;
    
    static{
    	ALL_TYPES = new HashSet<InType>(Arrays.asList(InType.values()));
    }
    

    @Override
    public final boolean isIncoming( ){
        return true;
    }

    
    public final static Set<InType> allTypes( ){
        return ALL_TYPES;
    }
    

}
