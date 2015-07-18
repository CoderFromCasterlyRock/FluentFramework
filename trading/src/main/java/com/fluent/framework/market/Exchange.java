package com.fluent.framework.market;

import static com.fluent.framework.util.FluentToolkit.*;



public enum Exchange{

    BTEC                ( false ),
    ESPEED              ( false	),
    CASH_SMART_ROUTER   ( true, 	BTEC, ESPEED),

    CME                 ( false	),
    
    ISWAP               ( false	),
    DWEB                ( false	),
    TRADITION           ( false	),
    SWAP_SMART_ROUTER   ( true, ISWAP, DWEB, TRADITION ),
    
    UNSUPPORTED         ( false );

    private final boolean isAggregate;
    private final Exchange[] underlying;


    Exchange( boolean isAggregate, Exchange ... underlying ){
        this.isAggregate    = isAggregate;
        this.underlying     = underlying;
    }


    public final boolean isAggregate( ){
        return isAggregate;
    }


    public final Exchange[] getUnderlying( ){
        return underlying;
    }


    public final static Exchange valueFrom( String name ){
        if( isBlank(name) ) return UNSUPPORTED;
        return Exchange.valueOf( name.trim().toUpperCase() );
    }
    
    
}
