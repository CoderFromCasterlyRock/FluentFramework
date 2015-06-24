package com.fluent.framework.market;

import java.io.*;

import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

import com.fluent.framework.core.FluentStartable;
import com.fluent.framework.market.core.Exchange;
import com.fluent.framework.market.core.MarketDataEvent;
import com.fluent.framework.market.core.MarketDataEventProvider;
import com.fluent.framework.util.FluentThreadFactory;

import static com.fluent.framework.util.FluentUtil.*;


public final class DummyScheduledMDProducer implements Runnable, FluentStartable{

    private final Exchange marketType;
    private final MarketDataEventProvider provider;
    private final ScheduledExecutorService executor;

    private final static String NAME    = DummyScheduledMDProducer.class.getSimpleName();
    private final static Logger LOGGER  = LoggerFactory.getLogger( NAME );
    private final static String[] INS   = { "2_YEAR", "3_YEAR", "5_YEAR", "7_YEAR", "10_YEAR", "30_YEAR" };


    public DummyScheduledMDProducer(Exchange marketType, MarketDataEventProvider provider ) throws IOException{
        this.marketType     = marketType;
        this.provider       = provider;
        this.executor       = Executors.newSingleThreadScheduledExecutor( new FluentThreadFactory(NAME) );
    }
    

    @Override
    public final String name( ){
        return NAME;
    }

    
    @Override
    public final void init( ){
        executor.scheduleAtFixedRate( this, FIVE, FIVE, TimeUnit.SECONDS );
        LOGGER.warn( "Starting {}, will publish FAKE prices for {} every 5 seconds.", NAME, Arrays.deepToString( INS ) );
    }


    @Override
    public final void run( ){

    	
        for( String instrument : INS ){

            double bid0             = 100.0;
            double bidTick          = Math.random();

            double ask0             = 101.0;
            double askTick          = Math.random();

            double[] bid            = { bid0, (bid0 - bidTick), (bid0 - 2*bidTick), (bid0 - 3*bidTick), (bid0 - 2*bidTick)};
            int[] bidSize           = { 55, 65, 75, 85, 95 };
            double[] ask            = { ask0, (ask0 + askTick), (ask0 + 2*askTick), (ask0 + 3*askTick), (ask0 + 2*askTick)};
            int[] askSize           = { 50, 60, 70, 80, 90 };

            MarketDataEvent event   = new MarketDataEvent(  marketType, instrument, bid[0], bidSize[0], ask[0], askSize[0] );

            provider.addMarketDataEvent( event );

        }

    }


    @Override
    public final void stop( ){
        executor.shutdown();
    }


    public final static double between( final double upper, final double lower ){
        return (Math.random() * (upper - lower)) + lower;
    }


}
