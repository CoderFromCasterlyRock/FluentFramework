package com.fluent.etrading.framework.events.out.executor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fluent.etrading.framework.events.in.LoopbackEvent;
import com.fluent.etrading.framework.events.in.LoopbackEventProvider;
import com.fluent.etrading.framework.events.out.order.OrderEvent;
import com.fluent.etrading.framework.market.parameters.MarketParameter;
import com.fluent.etrading.framework.market.parameters.TIBCOMarketParameter;


/**
 * TODO:
 *
 * What if we have an admin error than prevents us from sending an order.
 * How do we notify the Strategy?
 *
 */

public final class TIBCOOrderExecutor extends OrderExecutor{

    private final static String NAME    = TIBCOOrderExecutor.class.getSimpleName();
    private final static Logger LOGGER  = LoggerFactory.getLogger( NAME );


    public TIBCOOrderExecutor( MarketParameter parameter ){
        super( parameter );
    }


    @Override
    public void init( ){
        LOGGER.debug( "Successfully initialized order executor for MarketType [{}].", getMarketType() );
    }


    @Override
    public final ExecutionResult execute( final LoopbackEventProvider provider, final OrderEvent orderEvent ){

        boolean executed                = false;
        StringBuilder reason            = new StringBuilder(  );

        try{

            TIBCOMarketParameter param  = (TIBCOMarketParameter) getAdaptor( );
            Map<String, String> paramMap= param.getParams( orderEvent );

            //execute the Order
            //Here we would send the order to market but atm we reject the Order.
            reason.append( "Connectivity unavailable to ").append( getMarketType().name() );
            executed                    = false;

        }catch( Exception e ){
            reason.append( "FAILED to execute Order as " ).append(e.getMessage());
            LOGGER.warn("Exception", e );
        }

        if( !executed ){
            LoopbackEvent invalid       = createInvalidReport( reason.toString(), orderEvent );
            provider.addLoopbackEvent( invalid );
        }

        return null;

    }


    @Override
    public void stop(){
        //Drop connection to TIBCO if needed.
    }


}
