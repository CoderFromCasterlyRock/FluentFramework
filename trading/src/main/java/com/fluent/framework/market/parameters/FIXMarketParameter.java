package com.fluent.framework.market.parameters;

import com.fluent.framework.events.out.order.OrderEvent;
import com.fluent.framework.market.core.MarketType;


public abstract class FIXMarketParameter extends MarketParameter<String, OrderEvent>{

    public FIXMarketParameter( MarketType type ){
        super( type );
    }

}
