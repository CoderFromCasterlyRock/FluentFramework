package com.ccr.fluent.trading.events.out.execution;

import org.slf4j.*;

import com.ccr.fluent.trading.events.in.LoopbackEvent;
import com.ccr.fluent.trading.events.in.LoopbackEventProvider;
import com.ccr.fluent.trading.events.out.order.OrderEvent;
import com.ccr.fluent.trading.market.parameters.MarketParameter;


public final class ZMQOrderExecutor extends OrderExecutor{

    private final static String NAME    = ZMQOrderExecutor.class.getSimpleName();
    private final static Logger LOGGER  = LoggerFactory.getLogger( NAME );


    public ZMQOrderExecutor( MarketParameter parameter ){
        super( parameter );
    }


    @Override
    public void init(){
        LOGGER.debug( "Successfully initialized order executor for MarketType [{}].", getMarketType() );
    }


    @Override
    public ExecutionResult execute( final LoopbackEventProvider provider, final OrderEvent orderEvent ){

        String reason           = "ZMQParameters missing for Market: " + orderEvent.getMarketType();
        LoopbackEvent invalid   = createInvalidReport( reason, orderEvent );
        provider.addLoopbackEvent( invalid );

        return null;

    }


    @Override
    public void stop(){

    }


}
