package com.fluent.framework.market.parameters;

import java.util.*;

import com.fluent.framework.events.out.order.OrderEvent;
import com.fluent.framework.market.core.Exchange;


public class TIBCOBtecMarketParameter extends TIBCOMarketParameter{


    public TIBCOBtecMarketParameter( ){
        super( Exchange.BTEC );
    }


    @Override
    public Map<String, String> newOrderParams( OrderEvent event ){
        return null;
    }


    @Override
    public Map<String, String> amendOrderParams( OrderEvent event ){
        return null;
    }


    @Override
    public Map<String, String> cancelOrderParams( OrderEvent event ){
        return null;
    }

}
