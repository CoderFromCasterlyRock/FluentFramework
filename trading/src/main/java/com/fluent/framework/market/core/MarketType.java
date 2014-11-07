package com.fluent.framework.market.core;

import static com.fluent.framework.market.core.MarketTransport.*;


public enum MarketType{

    BTEC                ( false,    ION ),
    ESPEED              ( false,    ION ),
    VMO                 ( true,     ION  , BTEC, ESPEED ),
    CASH_SMART_ROUTER   ( false,    ION  , BTEC, ESPEED),

    CME                 ( false,    ION ),
    CBOT                ( false,    ION ),
    LIFFE               ( false,    ION ),

    ISWAP               ( false,    ION ),
    DWEB                ( false,    ION ),
    TRADITION           ( false,    ION ),
    SWAP_SMART_ROUTER   ( false,    ION, ISWAP, DWEB, TRADITION ),
    
    UNSUPPORTED         ( false,    INVALID );

    private final boolean isAggregate;
    private final MarketTransport transport;
    private final MarketType[] underlying;


    MarketType( boolean isAggregate, MarketTransport transport, MarketType ... underlying ){
        this.isAggregate    = isAggregate;
        this.transport      = transport;
        this.underlying     = underlying;
    }


    public final boolean isAggregate( ){
        return isAggregate;
    }


    public final MarketTransport getTransport( ){
        return transport;
    }


    public final MarketType[] getUnderlying( ){
        return underlying;
    }


}
