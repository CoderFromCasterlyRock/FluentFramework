package com.ccr.fluent.trading.market.core;

import static com.ccr.fluent.trading.utility.ContainerUtil.*;


public final class IONMarket extends Market{

    public IONMarket( MarketType type, String currency, String executionSource, String marketDataSource ){
        super( type, currency, executionSource, marketDataSource );
    }


    public final String getMarketDataChain( ){
        return EMPTY;
    }


    public final String getOrderChain( ){
        return EMPTY;
    }


    public final String getTradeChain( ){
        return EMPTY;
    }


    public final String getLoginChain( ){
        return EMPTY;
    }


    public final String getStatusChain( ){
        return EMPTY;
    }


}
