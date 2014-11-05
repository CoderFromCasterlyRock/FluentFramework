package com.ccr.fluent.trading.events.out.order;

import com.ccr.fluent.trading.events.core.FluentOutputEvent;
import com.ccr.fluent.trading.events.core.FluentOutputEventType;
import com.ccr.fluent.trading.market.core.MarketType;
import com.ccr.fluent.trading.order.OrderType;
import com.ccr.fluent.trading.order.Side;


public abstract class OrderEvent extends FluentOutputEvent{

    private final long strategyId;
    private final long mdUpdateId;
    private final String instrumentId;
    private final OrderType orderType;
    private final MarketType marketType;

    private final Side side;
    private final double price;
    private final int sendQuantity;
    private final String traderId;


    public OrderEvent( long strategyId, long orderId, long mdUpdateId, FluentOutputEventType type,
                       MarketType marketType, OrderType orderType,
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

    public final MarketType getMarketType( ){
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
