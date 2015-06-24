package com.fluent.framework.market;

import org.slf4j.*;

import java.util.concurrent.*;

import com.fluent.framework.core.FluentStartable;
import com.fluent.framework.events.in.*;
import com.fluent.framework.market.core.Exchange;
import com.fluent.framework.market.core.MarketDataEvent;
import com.fluent.framework.util.TimeUtil;
import com.google.common.util.concurrent.RateLimiter;

import static com.fluent.framework.market.core.Exchange.*;
import static com.fluent.framework.util.FluentUtil.*;



public final class DummyThrottledMDProducer implements Runnable, FluentStartable{

	private final long timeToRun;
	private final long productionRate;
	private final RateLimiter rateLimiter;
	private final Exchange marketType;
    private final InboundEventDispatcher dispatcher;
    
    private final static String NAME    = DummyThrottledMDProducer.class.getSimpleName();
    private final static Logger LOGGER  = LoggerFactory.getLogger( NAME );
    private final static String[] INS   = { "2_YEAR", "3_YEAR", "5_YEAR", "7_YEAR", "10_YEAR", "30_YEAR" };


    public DummyThrottledMDProducer( long timeToRun, long productionRate, InboundEventDispatcher dispatcher ){
    	this( BTEC, timeToRun, TimeUnit.SECONDS, productionRate, TimeUnit.SECONDS, dispatcher );
    }
    
    
    public DummyThrottledMDProducer( Exchange marketType,
    										long timeToRun,
                                        	TimeUnit runUnit,
                                        	long productionRate,
                                        	TimeUnit rateUnit,
                                        	InboundEventDispatcher dispatcher ){

        this.marketType     = marketType;
        this.dispatcher     = dispatcher;
        this.timeToRun		= TimeUnit.SECONDS.convert( timeToRun, runUnit );
        this.productionRate	= TimeUnit.SECONDS.convert( productionRate, rateUnit );
                
        this.rateLimiter	= RateLimiter.create(productionRate);
    }
    

    @Override
    public final String name( ){
        return NAME;
    }

    
    @Override
    public final void init( ){
        LOGGER.info( "Configured {}, will publish FAKE prices for {} seconds at the rate of {} events per second.{}", NAME, timeToRun, productionRate, NEWLINE );
    }


    @Override
    public final void run( ){
    	
		long eventsProducedCount 	= ZERO;
    	long runTimeAsMilliseonds	= timeToRun * THOUSAND;
    	long durationToRunAsMillis	= (TimeUtil.currentMillis() + runTimeAsMilliseonds);

		while( TimeUtil.currentMillis() < durationToRunAsMillis ){
    	
			rateLimiter.acquire();

	    	String instrument		= INS[0];
			double bid0             = 100.0;
			double bidTick          = Math.random();
			double ask0             = 101.0;
			double askTick          = Math.random();

            
            double[] bid            = { bid0, (bid0 - bidTick), (bid0 - 2*bidTick), (bid0 - 3*bidTick), (bid0 - 2*bidTick)};
            int[] bidSize           = { 55, 65, 75, 85, 95 };
            double[] ask            = { ask0, (ask0 + askTick), (ask0 + 2*askTick), (ask0 + 3*askTick), (ask0 + 2*askTick)};
            int[] askSize           = { 50, 60, 70, 80, 90 };

            MarketDataEvent event   = new MarketDataEvent(  marketType, instrument, bid[0], bidSize[0], ask[0], askSize[0] );
            dispatcher.enqueue( event );
   		    ++ eventsProducedCount;
   		    
    	}
		
		long eventProducedRate		= eventsProducedCount/timeToRun;
	    LOGGER.info( "Produced {} market data events in {} seconds.", eventsProducedCount, timeToRun );
	    LOGGER.info( "ProducedRate {} per second, ConfiguredRate {} per second.", eventProducedRate, productionRate );
	    		
    }


    @Override
    public final void stop( ){
       
    }


}
