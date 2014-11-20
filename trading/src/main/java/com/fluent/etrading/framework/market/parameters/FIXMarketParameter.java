package com.fluent.etrading.framework.market.parameters;

import com.fluent.etrading.framework.events.out.order.OrderEvent;
import com.fluent.etrading.framework.market.core.Marketplace;


public abstract class FIXMarketParameter extends MarketParameter<String, OrderEvent>{

    public FIXMarketParameter( Marketplace type ){
        super( type );
    }

}
