package com.fluent.framework.events.dispatch;

import org.HdrHistogram.*;

import java.util.concurrent.*;

import com.fluent.framework.market.core.Exchange;
import com.fluent.framework.market.core.MarketDataEvent;
import com.fluent.framework.persistence.FluentEventFstPersister;
import com.fluent.framework.persistence.InboundEventPersisterService;
import com.fluent.framework.events.core.FluentInboundEvent;
import com.fluent.framework.events.core.FluentInboundListener;
import com.fluent.framework.events.core.FluentInboundType;
import com.fluent.framework.events.dispatch.InboundEventDispatcher;


public final class InboundDispatcherPerformance{

	private static volatile boolean warmingUp;


	public static void testDispatcher( ) throws Exception{

        int eventCount                  = 50000;
        int memorySizeOf1Object 		= 1000;
        long expectedMemory     		= memorySizeOf1Object * eventCount;
        String fileLocation     		= "C:\\Temp";
        String journalName      		= "Test";
        
        InboundEventDispatcher dispatch	= new InboundEventDispatcher( );
        
        InboundEventPersisterService l1 = new InboundEventPersisterService( eventCount, new FluentEventFstPersister( fileLocation, "", journalName, expectedMemory, eventCount ) );
        InboundEventDispatcher.register( l1 );
        
        DummyListener l2		        = new DummyListener( );
        InboundEventDispatcher.register( l2 );
        
        warmingUp = true;
        //l1.warmUp( new MarketDataEvent( Exchange.CME, "EDM6", 99.0, 100, 99.50, 200) );
        //dispatch.warmUp( new MarketDataEvent( Exchange.CME, "EDM6", 99.0, 100, 99.50, 200) );
        warmingUp = false;
        System.err.println("InboundDispatcherPerformance:: Finished warm up : " );
        
        System.gc();
        Thread.sleep( 1000 );
        
        l1.init();
        dispatch.init();
        
        Thread.sleep( 2000 );

        for( int i = 0; i< eventCount; i++ ){
        	MarketDataEvent event = new MarketDataEvent( Exchange.CME, "EDM6", 99.0, (100 + i), 99.50, (200 + i) );
        	InboundEventDispatcher.enqueue( event );
        	
        	if( System.nanoTime() - event.getCreationTime() < 5000 ){
        		Thread.yield();
        	}
        }
        
        System.err.println("InboundDispatcherPerformance:: Producer finished producing " + eventCount + " events.");
        		
        //Let the listener get all the elements
        while( (dispatch.getQueueSize() != 0) ){
            Thread.yield();
        }
        System.err.println("InboundDispatcherPerformance:: Consumers finished producing events.");
        
        Thread.sleep( 2000 );
        
        l1.generateStats();
        l2.generateStats();
        
        dispatch.stop();
        l1.stop();
        
    }


    
    public static class DummyListener implements FluentInboundListener{

    	private final Histogram histogram;
    	
        public DummyListener( ){
        	this.histogram	= new Histogram( TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS), 2);
        }   

        
        @Override
		public final String name( ){
			return "DummyListener";
		}


		@Override
		public final boolean isSupported(FluentInboundType type) {
			return true;
		}
        
		
		@Override
		public final boolean update( FluentInboundEvent event ){
			if( warmingUp ) return false;
			
        	histogram.recordValue( (System.nanoTime() - event.getCreationTime()) );
            return true;
		}
		
        
        public final void generateStats( ){
        	
            histogram.outputPercentileDistribution( System.out, 1000.0 );
            double valueAt99Percentile  = histogram.getValueAtPercentile( 99.99d );
            System.out.println( "\nValue at 99.99th percentile (micros) >> " + valueAt99Percentile/1000.0 );

        }
        
    }

    

    public static void main( String ... args ) throws Exception{
    	testDispatcher( );
    }


}