package com.ccr.fluent.trading.market.parameters;

import com.ccr.fluent.trading.market.core.MarketType;
import com.ccr.fluent.trading.events.out.order.OrderEvent;


public abstract class FIXMarketParameter extends MarketParameter<String, OrderEvent>{

    public FIXMarketParameter( MarketType type ){
        super( type );
    }

}
