package com.fluent.framework.market.core;

import java.util.Map;
import java.util.HashMap;


import static com.fluent.framework.util.FluentUtil.*;


public enum Exchange{

    BTEC                ( "Btec", 		false ),
    ESPEED              ( "Espeed", 	false	),
    CASH_SMART_ROUTER   ( "CashSmart", 	true, 	BTEC, ESPEED),

    CME                 ( "Cme", 		false	),
    
    ISWAP               ( "ISwap", 		false	),
    DWEB                ( "DWeb", 		false	),
    TRADITION           ( "Tradition", 	false	),
    SWAP_SMART_ROUTER   ( "SwapSmart", 	true, ISWAP, DWEB, TRADITION ),
    
    ALL         		( EMPTY, 		false ),
    UNSUPPORTED         ( EMPTY, 		false );

    
    private final String code;
    private final boolean isAggregate;
    private final Exchange[] underlying;
    
    private final static Map<String, Exchange> MAP;
    
    static{
    	MAP = new HashMap<>();
    	for( Exchange exchange : Exchange.values() ){
    		MAP.put( exchange.code, exchange );
    		MAP.put( exchange.name(), exchange );
    	}
    }


    Exchange( String code, boolean isAggregate, Exchange ... underlying ){
        this.code			= code;
    	this.isAggregate    = isAggregate;
        this.underlying     = underlying;
    }

    
    public final String getCode( ){
        return code;
    }
    

    public final boolean isAggregate( ){
        return isAggregate;
    }


    public final Exchange[] getUnderlying( ){
        return underlying;
    }


    public final static Exchange fromCode( String codeName ){
    	Exchange exchange = MAP.get(codeName);
        return ( exchange == null ) ? UNSUPPORTED : exchange;
    }
    
    
}
