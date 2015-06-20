package com.fluent.framework.persistence;

import java.io.File;
import org.HdrHistogram.*;

import com.fluent.framework.market.core.Exchange;
import com.fluent.framework.market.core.MarketDataEvent;

import static java.util.concurrent.TimeUnit.*;


public class FluentPersisterPerformance{

	
	public static void main( String[] args ){

		FluentPersister persister = null;
		
		try{
			
			int eventCount 			= 50000;
			int size1ObjBytes		= 1000;
			long totalSizeBytes 	= eventCount * size1ObjBytes;
			String fileLocation		= "C:\\Temp";
			Histogram histogram		= new Histogram( NANOSECONDS.convert(1, SECONDS), 2);
			persister				= new FluentEventFstPersister(fileLocation, "", "Test", totalSizeBytes, eventCount );
			persister.init();
						
			for( int i=0; i< eventCount; i++ ){
				long startTimeNanos		= System.nanoTime();
				MarketDataEvent event 	= new MarketDataEvent( Exchange.CME, "EDM6", 99, 1000+i, 100, 1100+i );
				persister.persist( event );
				long timeTakenNanos		= System.nanoTime() - startTimeNanos;
				histogram.recordValue( timeTakenNanos );
			}
			
			
			int retrievedCount		= persister.retrieveAll().size();
			if( retrievedCount != eventCount ){
				throw new RuntimeException("FAILED as we stored " + eventCount + " but retrieved " + retrievedCount );
			}
		
			System.out.println("Result of storing " + eventCount + " MarketDataEvents.");
			System.out.println("Time taken (in micros) of 99.99th percentile " +  histogram.getValueAtPercentile(99.99d) );
			System.out.println("---------------------------------------------------" );
			histogram.outputPercentileDistribution( System.out,  1000.0 );
			
		}catch( Exception e ){
			e.printStackTrace();
		
		}finally{
		
			if( persister != null ){
				persister.stop();
				boolean deleted = new File( persister.getFileName() ).delete();
				
				if( deleted ){
					System.out.println("Successfully deleted " + persister.getFileName());		
				}else{
					throw new RuntimeException("FAILED to delete file " + persister.getFileName() );
				}
			}
		}
	}

}
