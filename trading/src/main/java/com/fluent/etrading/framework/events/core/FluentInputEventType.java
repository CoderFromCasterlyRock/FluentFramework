package com.fluent.etrading.framework.events.core;

import java.util.*;

import static com.fluent.etrading.framework.events.core.FluentEventCategory.*;


public enum FluentInputEventType implements FluentEventType{

    ADMIN_EVENT                         ( ADMIN_CATEGORY,    	"Internal Event."),

    //In certain cases, container will transform an outgoing event to an input event
    LOOPBACK_EVENT                      ( INTERNAL_CATEGORY,    "Send an event to the input dispatcher."),

    //Trader Generated Events
    CREATE_STRATEGY                     ( TRADER_CATEGORY,      "Submit and start a Strategy." ),
    AMEND_STRATEGY                      ( TRADER_CATEGORY,      "Amend an existing Strategy." ),
    CANCEL_STRATEGY                     ( TRADER_CATEGORY,      "Cancel an existing Strategy." ),
    CANCEL_ALL_STRATEGY                 ( TRADER_CATEGORY,      "Cancel all strategies owned by this trader." ),

    //Market Data Generated Events
    TREASURY_MD                         ( MARKET_CATEGORY,      "Treasury market data." ),
    FUTURES_MD                 		    ( MARKET_CATEGORY,      "Futures market data." ),
    SWAPS_MD                 		    ( MARKET_CATEGORY,      "IR Swaps market data." ),

    //Execution Generated Events
    EXECUTION_REPORT_UPDATE             ( EXECUTION_CATEGORY,   "Execution report outgoing" ),

    //Reference Data Generated Events
    REFERENCE_DATA_UPDATE               ( REFERENCE_CATEGORY,   "Reference data outgoing." );


    private final FluentEventCategory category;
    private final String description;

    private final static Set<FluentInputEventType> ALL_TYPES;
    
    static{
    	ALL_TYPES = new HashSet<FluentInputEventType>(Arrays.asList(FluentInputEventType.values()));
    }
    

    FluentInputEventType( FluentEventCategory category, String description ){
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


    
    public final static Set<FluentInputEventType> allTypes( ){
        return ALL_TYPES;
    }
    

}
