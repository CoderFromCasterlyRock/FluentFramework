package com.fluent.framework.market.parameters;

import java.util.*;

import com.fluent.framework.events.out.order.OrderEvent;
import com.fluent.framework.market.core.Exchange;


public abstract class TIBCOMarketParameter extends MarketParameter<Map<String, String>, OrderEvent>{

    public TIBCOMarketParameter( Exchange type ){
        super( type );
    }

}
