package com.fluent.framework.market;

import static com.fluent.framework.util.FluentUtil.*;


public final class IONMarket extends Market{

    public IONMarket( Exchange type, String currency, String executionSource, String marketDataSource ){
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
