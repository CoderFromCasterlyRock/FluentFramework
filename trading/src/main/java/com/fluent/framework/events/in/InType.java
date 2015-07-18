package com.fluent.framework.events.in;

import java.util.*;

import com.fluent.framework.events.core.FluentEventCategory;
import com.fluent.framework.events.core.FluentEventType;

import static com.fluent.framework.events.core.FluentEventCategory.*;


public enum InType implements FluentEventType{

   
	NEW_STRATEGY				( FROM_TRADER_CATEGORY ),
	MODIFY_STRATEGY				( FROM_TRADER_CATEGORY ),
	CANCEL_STRATEGY				( FROM_TRADER_CATEGORY ),
        
	MARKET_DATA					( FROM_MARKET_CATEGORY ),
	
	EXECUTION_REPORT		    ( FROM_EXECUTION_CATEGORY ),
	REFERENCE_DATA			    ( FROM_REFERENCE_CATEGORY ),
	
	METRONOME_EVENT				( FROM_INTERNAL_CATEGORY ),
	WARM_UP_EVENT				( FROM_INTERNAL_CATEGORY, 	false ),
	START_UP_EVENT				( FROM_INTERNAL_CATEGORY ),
	CLOSING_DOWN_EVENT			( FROM_INTERNAL_CATEGORY ),
	LOOPBACK_EVENT				( FROM_INTERNAL_CATEGORY ),

	EXCHANGE_UP					( FROM_ADMIN_CATEGORY ),
	EXCHANGE_DOWN				( FROM_ADMIN_CATEGORY ),
	CANCEL_ALL_STRATEGY         ( FROM_ADMIN_CATEGORY );
	


    private final FluentEventCategory category;
    private final boolean isPersistable;
    
    private final static Set<InType> ALL_TYPES;
    
    static{
    	ALL_TYPES = new HashSet<InType>(Arrays.asList(InType.values()));
    }
    

    InType( FluentEventCategory category ){
        this( category, true );
    }
    
    InType( FluentEventCategory category, boolean isPersistable ){
        this.category       = category;
        this.isPersistable	= isPersistable;
    }


    @Override
    public final String getName( ){
        return name();
    }

    @Override
    public final FluentEventCategory getCategory( ){
        return category;
    }


    public final boolean isPersistable( ){
        return isPersistable;
    }

    
    public final static Set<InType> allTypes( ){
        return ALL_TYPES;
    }
    

}
