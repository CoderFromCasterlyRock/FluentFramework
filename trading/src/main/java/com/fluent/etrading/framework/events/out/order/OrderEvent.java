package com.fluent.etrading.framework.events.out.order;

import com.fluent.etrading.framework.events.core.FluentOutputEvent;
import com.fluent.etrading.framework.events.core.FluentOutputEventType;
import com.fluent.etrading.framework.market.core.Marketplace;
import com.fluent.etrading.framework.order.OrderType;
import com.fluent.etrading.framework.order.Side;


public abstract class OrderEvent extends FluentOutputEvent{

    private final long strategyId;
    private final long mdUpdateId;
    private final String instrumentId;
    private final OrderType orderType;
    private final Marketplace marketType;

    private final Side side;
    private final double price;
    private final int sendQuantity;
    private final String traderId;


    public OrderEvent( long strategyId, long orderId, long mdUpdateId, FluentOutputEventType type,
                       Marketplace marketType, OrderType orderType,
                       Side side, String instrumentId, double price, int sendQuantity, String traderId ){

        super( orderId, type );

        this.strategyId     = strategyId;
        this.mdUpdateId     = mdUpdateId;
        this.marketType     = marketType;
        this.orderType      = orderType;
        this.side           = side;
        this.instrumentId   = instrumentId;
        this.price          = price;
        this.sendQuantity   = sendQuantity;
        this.traderId       = traderId;

    }


    public final long getStrategyId( ){
        return strategyId;
    }

    public final long getOrderId( ){
        return getEventId();
    }

    public final long getMDUpdateId(){
        return mdUpdateId;
    }

    public final String getInstrumentId( ){
        return instrumentId;
    }

    public final OrderType getOrderType( ){
        return orderType;
    }

    public final Marketplace getMarketType( ){
        return marketType;
    }

    public final Side getSide(){
        return side;
    }

    public final double getPrice( ){
        return price;
    }

    public final int getSendQuantity( ){
        return sendQuantity;
    }

    public final String getTraderId( ){
        return traderId;
    }

    public abstract int getShowQuantity( );
    public abstract String getTraderName( );
    public abstract String getPortfolio( );


}
