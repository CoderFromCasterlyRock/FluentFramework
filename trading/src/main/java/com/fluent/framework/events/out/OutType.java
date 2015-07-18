package com.fluent.framework.events.out;

import java.util.*;

import com.fluent.framework.events.core.FluentEventCategory;
import com.fluent.framework.events.core.FluentEventType;

import static com.fluent.framework.events.core.FluentEventCategory.*;


public enum OutType implements FluentEventType{

	WARM_UP_EVENT		( FROM_INTERNAL_CATEGORY, false ),
    ORDER_TO_MARKET     ( TO_MARKET_CATEGORY	),
    EVENT_TO_LOGGER     ( TO_ANALYSIS_CATEGORY 	),
    EVENT_TO_TRADER     ( TO_TRADER_CATEGORY	);

    private final FluentEventCategory category;
    private final boolean isPersistable;
    
    private final static Set<OutType> ALL_TYPES;
   

    static{
    	ALL_TYPES = new HashSet<OutType>(Arrays.asList(OutType.values()));
    }

    OutType( FluentEventCategory category ){
        this( category, true );
    }
    
    OutType( FluentEventCategory category, boolean isPersistable ){
        this.category       = category;
        this.isPersistable	= isPersistable;
    }


    @Override
    public final String getName( ){
        return name();
    }

    
    public final boolean isPersistable( ){
        return isPersistable;
    }

    
    @Override
    public final FluentEventCategory getCategory( ){
        return category;
    }

    
    public final static Set<OutType> allTypes( ){
        return ALL_TYPES;
    }

}
