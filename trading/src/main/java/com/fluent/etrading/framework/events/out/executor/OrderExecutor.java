package com.fluent.etrading.framework.events.out.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fluent.etrading.framework.core.FluentService;
import com.fluent.etrading.framework.events.core.FluentInputEvent;
import com.fluent.etrading.framework.events.in.ExecutionReportEvent;
import com.fluent.etrading.framework.events.in.LoopbackEvent;
import com.fluent.etrading.framework.events.in.LoopbackEventProvider;
import com.fluent.etrading.framework.events.out.order.OrderEvent;
import com.fluent.etrading.framework.market.core.Marketplace;
import com.fluent.etrading.framework.market.parameters.MarketParameter;
import com.fluent.etrading.framework.order.OrderFillStatus;



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


    public final Marketplace getMarketType( ){
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
