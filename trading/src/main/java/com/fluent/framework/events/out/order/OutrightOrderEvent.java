package com.fluent.framework.events.out.order;

import com.eclipsesource.json.*;
import com.fluent.framework.events.core.*;
import com.fluent.framework.market.core.*;
import com.fluent.framework.order.*;

import static com.fluent.framework.events.core.FluentJsonTags.*;
import static com.fluent.framework.utility.JSONUtil.*;


public final class OutrightOrderEvent extends OrderEvent{

    private final int showQuantity;
    private final String traderName;
    private final String portfolio;


    public OutrightOrderEvent( long strategyId, long orderId, long mdEventId, FluentOutputEventType type,
                               MarketType marketType, OrderType orderType,
                               String instrumentId, Side side, double price, int sendQuantity, int showQuantity,
                               String traderId, String traderName, String portfolio ){

        super( strategyId, orderId, mdEventId, type, marketType, orderType, side, instrumentId, price, sendQuantity, traderId );

        this.showQuantity   = showQuantity;
        this.traderName     = traderName;
        this.portfolio      = portfolio;

    }



    @Override
    public final int getShowQuantity(){
        return showQuantity;
    }


    @Override
    public final String getTraderName(){
        return traderName;
    }


    @Override
    public final String getPortfolio(){
        return portfolio;
    }

    
    @Override
    protected final String toJSON( final JsonObject object ){

        object.add( STRATEGY_ID.field(),        getStrategyId() );
        object.add( ORDER_ID.field(),           getOrderId() );
        object.add( MARKET_DATA_ID.field(),     getMDUpdateId() );
        object.add( MARKET.field(),             getMarketType().name() );
        object.add( ORDER_TYPE.field(),         getOrderType().name() );
        object.add( SIDE.field(),               getSide().name() );
        object.add( INSTRUMENT_NAME.field(),    getInstrumentId() );
        object.add( PRICE.field(),              getPrice() );
        object.add( SEND_QUANTITY.field(),      getSendQuantity() );
        object.add( SHOW_QUANTITY.field(),      getShowQuantity() );
        object.add( TRADER_ID.field(),          getTraderId() );
        object.add( TRADER_NAME.field(),        getTraderName() );
        object.add( PORTFOLIO.field(),          getPortfolio());

        return object.toString();
    }


    
    public final static OutrightOrderEvent convert( final String jsonString, final JsonObject object ){

        return new OutrightOrderEvent(
                valueAsLong(STRATEGY_ID, object),
                valueAsLong(ORDER_ID, object),
                valueAsLong( MARKET_DATA_ID, object),
                FluentOutputEventType.valueOf( valueAsString( EVENT_TYPE, object )),
                MarketType.valueOf( valueAsString( MARKET, object ) ),
                OrderType.valueOf( valueAsString( ORDER_TYPE, object ) ),
                valueAsString( INSTRUMENT_NAME, object ),
                Side.valueOf( valueAsString( SIDE, object ) ),
                valueAsDouble( PRICE, object ),
                valueAsInt( SEND_QUANTITY, object ),
                valueAsInt( SHOW_QUANTITY, object ),
                valueAsString( TRADER_ID, object ),
                valueAsString( TRADER_NAME, object ),
                valueAsString( PORTFOLIO, object )
        );

    }


}
