package com.fluent.framework.performance;

import com.fluent.framework.collection.*;
import com.fluent.framework.events.core.*;
import com.fluent.framework.events.in.*;
import com.fluent.framework.events.persister.*;
import com.fluent.framework.test.*;


public class DispatcherPerformanceComparison{
	
		
	public static class MDEventsProcessor implements FluentInboundListener{

		private static final FluentAtomicLong CTR	= new FluentAtomicLong();
		private static final FluentInboundType TYPE  	= FluentInboundType.MARKET_DATA;
		private static final String MD_PROCESSOR_NAME		= MDEventsProcessor.class.getSimpleName();
		
		@Override
		public final String name(){
			return MD_PROCESSOR_NAME;
		}

		@Override
		public final boolean isSupported( FluentInboundType type ){
			return ( TYPE == type );
		}

		
		@Override
		public final boolean update( FluentInboundEvent event ){
			CTR.getAndIncrement();
			return true;
		}
		
		
		public final void printReport( ){
			System.err.println( MD_PROCESSOR_NAME + " received [" +  CTR.get() + "] events." );
		}
		
		
	}
		
	
	public static void main( final String[] args ) throws Exception{
		
		long howLongToRunInSeconds			= 1;
		long productionRatePerSeconds		= 3 * 1000 * 1000;
		
		MDEventsProcessor eventProcessor	= new MDEventsProcessor();
		InboundEventDispatcher.register( eventProcessor );
		InboundEventDispatcher dispatcher 	= new InboundEventDispatcher( 50000, new FluentEventFakePersister() );
		dispatcher.init();
		Thread.sleep( 1000 );
		
		DummyThrottledMDProducer producer	= new DummyThrottledMDProducer( howLongToRunInSeconds, productionRatePerSeconds, dispatcher );
		producer.init();
		Thread producerThread				= new Thread( producer );
		producerThread.start();
		
		producerThread.join();
		
		Thread.sleep( 1000 );
		dispatcher.stop();
		
		eventProcessor.printReport();
		
	}

	
}