package com.fluent.framework.market;


public abstract class Market{

    public final Exchange type;
    public final String currency;
    public final String executionSource;
    public final String marketDataSource;


    protected Market( Exchange type, String currency, String executionSource, String marketDataSource ){
        this.type               = type;
        this.currency           = currency;
        this.executionSource    = executionSource;
        this.marketDataSource   = marketDataSource;
    }

    public final Exchange getType( ){
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
