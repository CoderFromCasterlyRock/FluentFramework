package com.fluent.etrading.framework.events.in;

import com.eclipsesource.json.*;

import com.fluent.etrading.framework.order.*;
import com.fluent.etrading.framework.events.core.*;
import com.fluent.etrading.framework.market.core.*;

import static com.fluent.etrading.framework.utility.JSONUtil.*;
import static com.fluent.etrading.framework.utility.ContainerUtil.*;
import static com.fluent.etrading.framework.events.core.FluentJsonTags.*;
import static com.fluent.etrading.framework.events.core.FluentInputEventType.*;


public final class ExecutionReportEvent extends FluentInputEvent{

    private final long strategyId;
    private final long externalId;

    private final boolean isRejected;
    private final String rejectionReason;
    private final OrderFillStatus fillStatus;

    private final Side side;
    private final Marketplace marketType;
    private final OrderType orderType;
    private final String instrumentId;
    private final double executionPrice;
    private final double executionQuantity;
    private final long executionTime;
    

    public ExecutionReportEvent( long strategyId, long orderId, OrderFillStatus oStatus, String reason,
                                 OrderType oType, Side side, Marketplace mType, String instrumentId ){

        this( false, strategyId, orderId, NEGATIVE_ONE, nowMillis(), oStatus, true, reason, oType, side, mType, instrumentId, ZERO_DOUBLE, ZERO_DOUBLE );

    }


    public ExecutionReportEvent( boolean isValid, long strategyId, long orderId, long externalId,
                                 long executionTime, OrderFillStatus fillStatus, boolean isRejected, String rejectionReason,
                                 OrderType orderType, Side side, Marketplace marketType,
                                 String instrumentId, double executionPrice, double executionQuantity ){

        super( isValid, orderId, EXECUTION_REPORT_UPDATE );

        this.strategyId         = strategyId;
        this.externalId         = externalId;
        this.fillStatus         = fillStatus;
        this.isRejected         = isRejected;
        this.rejectionReason    = rejectionReason;
        this.orderType          = orderType;
        this.side               = side;
        this.marketType         = marketType;
        this.instrumentId       = instrumentId;
        this.executionTime		= executionTime;
        this.executionPrice     = executionPrice;
        this.executionQuantity  = executionQuantity;

    }


    public final long getOrderId( ){
        return getEventId();
    }

    public final long getStrategyId( ){
        return strategyId;
    }

    public final long getOrderExternalId( ){
        return externalId;
    }

    public final OrderFillStatus getFillStatus( ){
        return fillStatus;
    }

    public final boolean isRejected( ){
        return isRejected;
    }

    public final String getRejectionReason( ){
        return rejectionReason;
    }

    public final OrderType getOrderType( ){
        return orderType;
    }

    public final Side getSide( ){
        return side;
    }

    public final Marketplace getMarketType( ){
        return marketType;
    }
    
    public final long getExecutionTime( ){
        return executionTime;
    }

    public final String getInstrumentId( ){
        return instrumentId;
    }

    public final double getExecutionPrice( ){
        return executionPrice;
    }

    public final double getExecutionQuantity( ){
        return executionQuantity;
    }


    @Override
    protected final String toJSON( final JsonObject object ){

        object.add( STRATEGY_ID.field(),        getStrategyId() );
        object.add( ORDER_ID.field(),           getOrderId() );
        object.add( ORDER_EXTERNAL_ID.field(),  getOrderExternalId() );
        object.add( FILL_STATUS.field(),        getFillStatus().name() );
        object.add( INSTRUMENT_NAME.field(),    getInstrumentId() );
        object.add( EXECUTION_PRICE.field(),    getExecutionPrice() );
        object.add( EXECUTION_QUANTITY.field(), getExecutionQuantity() );
        object.add( SIDE.field(),               getSide().name() );
        object.add( ORDER_TYPE.field(),         getOrderType().name() );
        object.add( MARKET.field(),             getMarketType().name() );
        object.add( IS_REJECTED.field(),        isRejected() );
        object.add( REJECTED_REASON.field(),    getRejectionReason() );
        
        return object.toString();

    }
    
    
    public final static ExecutionReportEvent convert( final String jsonString, final JsonObject object ){

        return new ExecutionReportEvent(valueAsBoolean(EVENT_VALID, object),
                                        valueAsLong(STRATEGY_ID, object),
                                        valueAsLong(ORDER_ID, object),
                                        valueAsLong(ORDER_EXTERNAL_ID, object),
                                        valueAsLong( TIMESTAMP, object),
                                        OrderFillStatus.valueOf( valueAsString( FILL_STATUS, object ) ),
                                        valueAsBoolean(IS_REJECTED, object),
                                        valueAsString( REJECTED_REASON, object ),
                                        OrderType.valueOf( valueAsString( ORDER_TYPE, object ) ),
                                        Side.valueOf( valueAsString( SIDE, object ) ),
                                        Marketplace.valueOf( valueAsString( MARKET, object ) ),
                                        valueAsString( INSTRUMENT_NAME, object ),
                                        valueAsDouble( EXECUTION_PRICE, object ),
                                        valueAsDouble( EXECUTION_QUANTITY, object ) );


    }
    
}
