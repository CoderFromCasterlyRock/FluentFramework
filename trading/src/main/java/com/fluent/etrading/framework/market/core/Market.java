package com.fluent.etrading.framework.market.core;


public abstract class Market{

    public final MarketType type;
    public final String currency;
    public final String executionSource;
    public final String marketDataSource;


    protected Market( MarketType type, String currency, String executionSource, String marketDataSource ){
        this.type               = type;
        this.currency           = currency;
        this.executionSource    = executionSource;
        this.marketDataSource   = marketDataSource;
    }

    public final MarketType getType( ){
        return type;
    }

    public final String getCurrency( ){
        return currency;
    }

    public final String getExecutionSource( ){
        return executionSource;
    }

    public final String getMarketDataSource( ){
        return marketDataSource;
    }

}