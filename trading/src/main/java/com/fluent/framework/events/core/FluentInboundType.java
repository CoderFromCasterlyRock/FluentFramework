package com.fluent.framework.events.core;

import java.util.*;

import static com.fluent.framework.events.core.FluentEventCategory.*;


public enum FluentInboundType implements FluentEventType{

   
	NEW_STRATEGY				( FROM_TRADER_CATEGORY ),
	MODIFY_STRATEGY				( FROM_TRADER_CATEGORY ),
	CANCEL_STRATEGY				( FROM_TRADER_CATEGORY ),
        
	MARKET_DATA					( FROM_MARKET_CATEGORY ),
	
	EXECUTION_REPORT		    ( FROM_EXECUTION_CATEGORY ),
	REFERENCE_DATA			    ( FROM_REFERENCE_CATEGORY ),
	
	METRONOME_EVENT				( FROM_INTERNAL_CATEGORY ),
	START_UP_EVENT				( FROM_INTERNAL_CATEGORY ),
	CLOSING_DOWN_EVENT			( FROM_INTERNAL_CATEGORY ),
	LOOPBACK_EVENT				( FROM_INTERNAL_CATEGORY ),

	EXCHANGE_UP					( FROM_ADMIN_CATEGORY ),
	EXCHANGE_DOWN				( FROM_ADMIN_CATEGORY ),
	CANCEL_ALL_STRATEGY         ( FROM_ADMIN_CATEGORY );
	


    private final FluentEventCategory category;
   
    private final static Set<FluentInboundType> ALL_TYPES;
    
    static{
    	ALL_TYPES = new HashSet<FluentInboundType>(Arrays.asList(FluentInboundType.values()));
    }
    

    FluentInboundType( FluentEventCategory category ){
        this.category       = category;
    }


    @Override
    public final String getName( ){
        return name();
    }

    @Override
    public final FluentEventCategory getCategory( ){
        return category;
    }

    
    public final static Set<FluentInboundType> allTypes( ){
        return ALL_TYPES;
    }
    

}
