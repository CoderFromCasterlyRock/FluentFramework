package com.ccr.fluent.trading.events.out.execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ccr.fluent.trading.core.FluentService;
import com.ccr.fluent.trading.events.core.FluentInputEvent;
import com.ccr.fluent.trading.events.in.ExecutionReportEvent;
import com.ccr.fluent.trading.events.in.LoopbackEvent;
import com.ccr.fluent.trading.events.in.LoopbackEventProvider;
import com.ccr.fluent.trading.events.out.order.OrderEvent;
import com.ccr.fluent.trading.market.core.MarketType;
import com.ccr.fluent.trading.market.parameters.MarketParameter;
import com.ccr.fluent.trading.order.OrderFillStatus;



public abstract class OrderExecutor<R, O extends OrderEvent> implements FluentService{

    private final MarketParameter<R, O> marketAdaptor;

    private final static String NAME        = OrderExecutor.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );


    public OrderExecutor( MarketParameter<R, O> marketAdaptor ){
        this.marketAdaptor  = marketAdaptor;
    }


    public abstract ExecutionResult execute( LoopbackEventProvider provider, OrderEvent outEvent );


    @Override
    public final String name( ){
        return "OrderExector for " + getMarketType();
    }


    public final MarketType getMarketType( ){
        return marketAdaptor.getMarketType();
    }

    protected final MarketParameter<R, O> getAdaptor( ){
        return marketAdaptor;
    }


    public final static LoopbackEvent createInvalidReport( String reason, OrderEvent orderEvent ){
        long strategyId             = orderEvent.getStrategyId();
        FluentInputEvent report      = new ExecutionReportEvent( strategyId, orderEvent.getOrderId(),
                                                            OrderFillStatus.INTERNALLY_REJECTED, reason,
                                                            orderEvent.getOrderType(), orderEvent.getSide(),
                                                            orderEvent.getMarketType(), orderEvent.getInstrumentId() );

        LOGGER.warn( "Generating a fake execution report for StrategyId [{}] as [{}]", strategyId, reason );

        LoopbackEvent loopEvent    = new LoopbackEvent( strategyId, reason, report );
        return loopEvent;
    }



}
