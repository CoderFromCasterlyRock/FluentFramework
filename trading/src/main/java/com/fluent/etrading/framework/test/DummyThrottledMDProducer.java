package com.fluent.etrading.framework.test;

import org.slf4j.*;
import java.util.concurrent.*;

import com.fluent.etrading.framework.core.FluentService;
import com.fluent.etrading.framework.events.core.FluentInputEventId;
import com.fluent.etrading.framework.events.core.FluentInputEventType;
import com.fluent.etrading.framework.events.in.MarketDataEvent;
import com.fluent.etrading.framework.events.in.MarketDataEventProvider;
import com.fluent.etrading.framework.market.core.Marketplace;
import com.google.common.util.concurrent.RateLimiter;

import static com.fluent.etrading.framework.utility.ContainerUtil.*;
import static com.fluent.etrading.framework.market.core.Marketplace.*;
import static com.fluent.etrading.framework.events.core.FluentInputEventType.*;


public final class DummyThrottledMDProducer implements Runnable, FluentService{

	private final long timeToRun;
	private final long productionRate;
	private final RateLimiter rateLimiter;
	private final Marketplace marketType;
    private final FluentInputEventType ieType;
    private final MarketDataEventProvider provider;
    
    private final static String NAME    = DummyThrottledMDProducer.class.getSimpleName();
    private final static Logger LOGGER  = LoggerFactory.getLogger( NAME );
    private final static String[] INS   = { "2_YEAR", "3_YEAR", "5_YEAR", "7_YEAR", "10_YEAR", "30_YEAR" };


    public DummyThrottledMDProducer( long timeToRun, long productionRate, MarketDataEventProvider provider ){
    	this( TREASURY_MD, BTEC,timeToRun, TimeUnit.SECONDS, productionRate, TimeUnit.SECONDS, provider );
    }
    
    
    public DummyThrottledMDProducer( FluentInputEventType ieType,
                                        	Marketplace marketType,
                                        	long timeToRun,
                                        	TimeUnit runUnit,
                                        	long productionRate,
                                        	TimeUnit rateUnit,
                                        	MarketDataEventProvider provider ){

        this.ieType         = ieType;
        this.marketType     = marketType;
        this.provider       = provider;
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
    	long durationToRunAsMillis	= (nowMillis() + runTimeAsMilliseonds);

		while( nowMillis() < durationToRunAsMillis ){
    	
			rateLimiter.acquire();

	    	String instrument			= INS[0];
			double bid0             	= 100.0;
			double bidTick          	= Math.random();
			double ask0             	= 101.0;
			double askTick          	= Math.random();

			double[] bid            	= { bid0, (bid0 - bidTick), (bid0 - 2*bidTick), (bid0 - 3*bidTick), (bid0 - 2*bidTick)};
			int[] bidSize           	= { 55, 65, 75, 85, 95 };
			double[] ask            	= { ask0, (ask0 + askTick), (ask0 + 2*askTick), (ask0 + 3*askTick), (ask0 + 2*askTick)};
			int[] askSize           	= { 50, 60, 70, 80, 90 };
			long eventId            	= FluentInputEventId.nextId();
			
   			MarketDataEvent event   	= new MarketDataEvent(  eventId, ieType, marketType, instrument,
   																bid[0],     bid[1],     bid[2],     bid[3],     bid[4],
   																bidSize[0], bidSize[1], bidSize[2], bidSize[3], bidSize[4],
   																ask[0],     ask[1],     ask[2],     ask[3],     ask[4],
   																askSize[0], askSize[1], askSize[2], askSize[3], askSize[4]
                                                          		);
    	
   		    provider.addMarketDataEvent( event );
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
