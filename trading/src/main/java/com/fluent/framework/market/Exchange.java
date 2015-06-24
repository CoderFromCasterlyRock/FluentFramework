package com.fluent.framework.market;


public enum Exchange{

    BTEC                ( false ),
    ESPEED              ( false	),
    VMO                 ( true, 	BTEC, ESPEED ),
    CASH_SMART_ROUTER   ( false, 	BTEC, ESPEED),

    CME                 ( false	),
    CBOT                ( false	),
    LIFFE               ( false	),

    ISWAP               ( false	),
    DWEB                ( false	),
    TRADITION           ( false	),
    SWAP_SMART_ROUTER   ( false, ISWAP, DWEB, TRADITION ),
    
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


}
