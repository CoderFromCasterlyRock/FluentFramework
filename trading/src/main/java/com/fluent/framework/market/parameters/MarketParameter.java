package com.fluent.framework.market.parameters;

import com.fluent.framework.events.out.order.OrderEvent;
import com.fluent.framework.market.core.MarketType;
import com.fluent.framework.order.OrderType;


public abstract class MarketParameter<R, O extends OrderEvent>{

    private final MarketType type;

    public MarketParameter( MarketType type ){
        this.type   = type;
    }

    protected abstract R newOrderParams   ( O event );
    protected abstract R amendOrderParams ( O event );
    protected abstract R cancelOrderParams( O event );


    public final MarketType getMarketType( ){
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
