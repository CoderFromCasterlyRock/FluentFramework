package com.fluent.framework.events.core;

import java.util.*;

import static com.fluent.framework.events.core.FluentEventCategory.*;


public enum FluentOutboundType implements FluentEventType{

    ORDER_TO_MARKET     ( TO_MARKET_CATEGORY	),
    EVENT_TO_LOGGER     ( TO_ANALYSIS_CATEGORY 	),
    EVENT_TO_TRADER     ( TO_TRADER_CATEGORY	);

    private final FluentEventCategory category;

    private final static Set<FluentOutboundType> ALL_TYPES;
   

    static{
    	ALL_TYPES = new HashSet<FluentOutboundType>(Arrays.asList(FluentOutboundType.values()));
    }


    FluentOutboundType( FluentEventCategory category ){
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

    
    public final static Set<FluentOutboundType> allTypes( ){
        return ALL_TYPES;
    }

}
