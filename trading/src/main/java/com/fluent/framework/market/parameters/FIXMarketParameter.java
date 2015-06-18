package com.fluent.framework.market.parameters;

import com.fluent.framework.events.out.order.OrderEvent;
import com.fluent.framework.market.core.Exchange;


public abstract class FIXMarketParameter extends MarketParameter<String, OrderEvent>{

    public FIXMarketParameter( Exchange type ){
        super( type );
    }

}
