package com.fluent.framework.persistence;

import java.io.File;

import org.HdrHistogram.*;

import com.fluent.framework.util.FluentUtil;

import static java.util.concurrent.TimeUnit.*;


public class FASTEventPersisterLatency{

	
	public static void main( String[] args ){

		EventFstPersister persister = null;
		
		try{
			
			int eventCount 			= 1 * 1000 * 1000;
			int size1ObjBytes		= 1000;
			long totalSizeBytes 	= eventCount * size1ObjBytes;
			String fileLocation		= "C:\\Temp";
			Histogram histogram		= new Histogram( NANOSECONDS.convert(1, SECONDS), 2);
			persister				= new EventFstPersister(fileLocation, "", "Test", totalSizeBytes, eventCount );
			persister.start();
						
			for( int i=0; i< eventCount; i++ ){
				long startTimeNanos		= System.nanoTime();
				persister.persist( FluentUtil.IN_WARMUP_EVENT );
				long timeTakenNanos		= System.nanoTime() - startTimeNanos;
				histogram.recordValue( timeTakenNanos );
			}
			
			
			int retrievedCount		= persister.retrieveAll().size();
			if( retrievedCount != eventCount ){
				throw new RuntimeException("FAILED as we stored " + eventCount + " but retrieved " + retrievedCount );
			}
		
			System.out.println("Result of storing " + eventCount + " MarketDataEvents.");
			System.out.println("Time taken (in micros) of 99.99th percentile " +  (histogram.getValueAtPercentile(99d)/1000.0) );
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