package com.fluent.etrading.framework.market.core;

import static com.fluent.etrading.framework.market.core.MarketTransport.*;


public enum Marketplace{

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
    private final Marketplace[] underlying;


    Marketplace( boolean isAggregate, MarketTransport transport, Marketplace ... underlying ){
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


    public final Marketplace[] getUnderlying( ){
        return underlying;
    }


}
