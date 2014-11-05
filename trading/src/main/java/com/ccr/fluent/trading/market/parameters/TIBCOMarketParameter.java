package com.ccr.fluent.trading.market.parameters;

import java.util.*;

import com.ccr.fluent.trading.market.core.MarketType;
import com.ccr.fluent.trading.events.out.order.OrderEvent;


public abstract class TIBCOMarketParameter extends MarketParameter<Map<String, String>, OrderEvent>{

    public TIBCOMarketParameter( MarketType type ){
        super( type );
    }

}
