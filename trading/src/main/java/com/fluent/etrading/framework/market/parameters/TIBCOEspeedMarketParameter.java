package com.fluent.etrading.framework.market.parameters;

import java.util.*;

import com.fluent.etrading.framework.events.out.order.OrderEvent;
import com.fluent.etrading.framework.market.core.MarketType;


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
