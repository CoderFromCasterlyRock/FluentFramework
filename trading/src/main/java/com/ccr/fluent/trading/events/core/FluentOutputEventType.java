package com.ccr.fluent.trading.events.core;

import java.util.*;

import static com.ccr.fluent.trading.events.core.FluentEventCategory.*;


public enum FluentOutputEventType implements FluentEventType{

    ORDER_TO_MARKET     ( TO_MARKET_CATEGORY,   "Send Outright order to the market."),
    EVENT_TO_LOGGER     ( TO_LOGGER_CATEGORY,   "Send event to logger."),
    EVENT_TO_TRADER     ( TO_TRADER_CATEGORY,   "Send event to trader.");

    private final String description;
    private final FluentEventCategory category;

    private final static Set<FluentOutputEventType> ALL_TYPES;
   

    static{
    	ALL_TYPES = new HashSet<FluentOutputEventType>(Arrays.asList(FluentOutputEventType.values()));
    }


    FluentOutputEventType( FluentEventCategory category, String description ){
        this.category       = category;
        this.description    = description;
    }


    @Override
    public final String getName( ){
        return name();
    }


    @Override
    public final FluentEventCategory getCategory( ){
        return category;
    }


    @Override
    public final String getDescription( ){
        return description;
    }

    
    
    public final static Set<FluentOutputEventType> allTypes( ){
        return ALL_TYPES;
    }

}
