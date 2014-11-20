package com.fluent.etrading.framework.collections;

import java.io.*;
import java.util.concurrent.*;

import com.fluent.etrading.framework.core.*;
import com.fluent.etrading.framework.events.in.*;
import com.fluent.etrading.framework.events.core.*;

import static com.fluent.etrading.framework.utility.ContainerUtil.*;
import static com.fluent.etrading.framework.market.core.Marketplace.*;
import static com.fluent.etrading.framework.events.core.FluentInputEventType.*;


public class FluentSPSCMergingQueueTest implements MarketDataEventProvider{

	private final FluentSPSCMergingQueue<String, MarketDataEvent> mergingQueue;
	
	public FluentSPSCMergingQueueTest(){
		mergingQueue = new FluentSPSCMergingQueue<String, MarketDataEvent>(10);
	}

	
	@Override
	public boolean addMarketDataEvent( final MarketDataEvent event ){
		boolean value1 = mergingQueue.offer( event.getSymbol(), event );
		System.err.println( Thread.currentThread().getName() + " : Added MD :: " + event.getSymbol() + ", Size: " + mergingQueue.size() );
		return value1;
	}
	
	
	/*
	 * TODO
	 * 1. Reuters thread sends Marketdata at its own frequency, including bursts of data for the same instrument.
	 * 
	 * Option 1::
	 * 1. Insert the MD into a FluentSPSCMergingQueue queue.
	 * 2. Have a thread that throttles MD at the frequency at which the application can handle (Only beneficial if Throttle Reuters > Throttle App so the merging queue can merge ).
	 * 3. This thread then reads data from the queue and then distributes it.
	 * 
	 * Option 2::
	 * 1. Insert the MD into a FluentSPSCMergingQueue queue.
	 * 2. Have a thread be notified that MD has arrived, it then generates an MD event (but doesnt send the data) to the listeners.
	 * 3. Listeners then get the data.
	 *  PROBLEM: Listeners may be other threads, therefore the poll() will have to be Single producer, Multiple consumer safe!!
	 * 
	 * http://www.infoq.com/articles/Java-Thread-Pool-Performance-Tuning
	 * 
	 */
	
	
	private final static class DummyMarketDataAdaptorTest implements Runnable{

	    private final MarketDataEventProvider provider;
	    private final ScheduledExecutorService executor;
	    private final String[] INS 	= { "2_YEAR", "5_YEAR" };

	    public DummyMarketDataAdaptorTest( MarketDataEventProvider provider ) throws IOException{
	        this.provider       = provider;
	        this.executor       = Executors.newSingleThreadScheduledExecutor( new FluentThreadFactory("DummyMarketDataAdaptor") );
	    }
	    
	    public final void init( ){
	        executor.scheduleAtFixedRate( this, ONE, FIVE, TimeUnit.SECONDS );
	    }


	    @Override
	    public final void run( ){

	    	for( String instrument : INS ){

	            double bid0     = rndNumberBetween(101.90, 98.0);
	            int bidSize0	= 5000;
	            double ask0     = bid0 + rndNumberBetween(0.50, 0.10);
	            int askSize0	= 7500;

	            MarketDataEvent event   = new MarketDataEvent(  FluentInputEventId.nextId(), TREASURY_MD, BTEC, 
	            												instrument, bid0, bidSize0, ask0, askSize0 );

	            provider.addMarketDataEvent( event );

	        }

	    }


	}


	
	
	public static void main( String[] args ) throws Exception{
		
		FluentSPSCMergingQueueTest perf 		= new FluentSPSCMergingQueueTest();
		DummyMarketDataAdaptorTest mdAdaptor	= new DummyMarketDataAdaptorTest( perf );
		mdAdaptor.init();
				
	}
	
	
	
}
