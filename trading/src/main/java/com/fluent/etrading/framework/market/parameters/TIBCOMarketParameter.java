package com.fluent.etrading.framework.market.parameters;

import java.util.*;

import com.fluent.etrading.framework.events.out.order.OrderEvent;
import com.fluent.etrading.framework.market.core.MarketType;


public abstract class TIBCOMarketParameter extends MarketParameter<Map<String, String>, OrderEvent>{

    public TIBCOMarketParameter( MarketType type ){
        super( type );
    }

}
