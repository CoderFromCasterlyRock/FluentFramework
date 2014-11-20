package com.fluent.etrading.framework.performance;

import java.util.concurrent.*;

import com.fluent.etrading.framework.test.*;
import com.fluent.etrading.framework.events.core.*;
import com.fluent.etrading.framework.collections.*;
import com.fluent.etrading.framework.dispatcher.in.*;
import com.fluent.etrading.framework.dispatcher.core.*;



public class DispatcherPerformanceComparison{
	
		
	public static class MDEventsProcessor implements FluentInputEventListener{

		private static final FluentAtomicLongCounter CTR	= new FluentAtomicLongCounter();
		private static final FluentInputEventType TYPE  	= FluentInputEventType.TREASURY_MD;
		private static final String MD_PROCESSOR_NAME		= MDEventsProcessor.class.getSimpleName();
		
		@Override
		public final String name(){
			return MD_PROCESSOR_NAME;
		}

		@Override
		public final boolean isSupported( FluentInputEventType type ){
			return TYPE == type;
		}

		
		@Override
		public final void update( FluentInputEvent event ){
			CTR.getAndIncrement();
		}
		
		
		public final void printReport( ){
			System.err.println( MD_PROCESSOR_NAME + " received [" +  CTR.get() + "] events." );
		}
		
		
	}
		
	
	public static void main( final String[] args ) throws Exception{
		
		long howLongToRunInSeconds			= 1;
		long productionRatePerSeconds		= 3 * 1000 * 1000;
		
		MDEventsProcessor eventProcessor	= new MDEventsProcessor();
		BackoffStrategy backOff		 		= new BackoffStrategy(1, TimeUnit.NANOSECONDS);
		
		InputEventDispatcher.add( eventProcessor );
		InputEventDispatcher dispatcher 	= new FluentInputEventDispatcher(backOff);
		dispatcher.startDispatch();
		Thread.sleep( 1000 );
		
		DummyThrottledMDProducer producer	= new DummyThrottledMDProducer( howLongToRunInSeconds, productionRatePerSeconds, dispatcher );
		producer.init();
		Thread producerThread				= new Thread( producer );
		producerThread.start();
		
		producerThread.join();
		
		Thread.sleep( 1000 );
		dispatcher.stopDispatch();
		
		eventProcessor.printReport();
		
	}

	
}