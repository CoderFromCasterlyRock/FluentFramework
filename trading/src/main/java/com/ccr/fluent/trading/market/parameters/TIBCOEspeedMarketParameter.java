package com.ccr.fluent.trading.market.parameters;

import java.util.*;

import com.ccr.fluent.trading.market.core.MarketType;
import com.ccr.fluent.trading.events.out.order.OrderEvent;


public class TIBCOEspeedMarketParameter extends TIBCOMarketParameter{


    public TIBCOEspeedMarketParameter(){
        super( MarketType.ESPEED );
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
