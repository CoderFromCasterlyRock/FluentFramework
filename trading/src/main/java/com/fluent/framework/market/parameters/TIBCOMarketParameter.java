package com.fluent.framework.market.parameters;

import java.util.*;

import com.fluent.framework.events.out.order.OrderEvent;
import com.fluent.framework.market.core.MarketType;


public abstract class TIBCOMarketParameter extends MarketParameter<Map<String, String>, OrderEvent>{

    public TIBCOMarketParameter( MarketType type ){
        super( type );
    }

}
