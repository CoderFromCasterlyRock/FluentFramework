package com.fluent.etrading.framework.market.parameters;

import com.fluent.etrading.framework.events.out.order.OrderEvent;
import com.fluent.etrading.framework.market.core.Marketplace;
import com.fluent.etrading.framework.order.OrderType;


public abstract class MarketParameter<R, O extends OrderEvent>{

    private final Marketplace type;

    public MarketParameter( Marketplace type ){
        this.type   = type;
    }

    protected abstract R newOrderParams   ( O event );
    protected abstract R amendOrderParams ( O event );
    protected abstract R cancelOrderParams( O event );


    public final Marketplace getMarketType( ){
        return type;
    }


    public final R getParams( O event ){

        OrderType orderType = event.getOrderType();

        switch( orderType ){

            case NEW:
                return newOrderParams( event );

            case AMEND:
                return amendOrderParams( event );

            case CANCEL:
                return cancelOrderParams( event );

            default:
                throw new UnsupportedOperationException( );
        }

    }

}
