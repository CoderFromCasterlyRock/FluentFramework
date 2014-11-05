package com.ccr.fluent.trading.market.parameters;

import java.util.*;

import com.ccr.fluent.trading.market.core.MarketType;
import com.ccr.fluent.trading.events.out.order.OrderEvent;


public class TIBCOBtecMarketParameter extends TIBCOMarketParameter{


    public TIBCOBtecMarketParameter( ){
        super( MarketType.BTEC );
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
