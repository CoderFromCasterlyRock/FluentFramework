package com.fluent.framework.transport.file;

import java.io.*;
import org.slf4j.*;
import java.util.*;
import java.util.concurrent.*;

import com.fluent.framework.transport.core.*;
import com.fluent.framework.util.FluentThreadFactory;

import static com.fluent.framework.util.FluentUtil.*;


public final class FileMarketDataTransport extends AbstractTransport implements Runnable{

	private volatile int index;
	
	private final String fileLocation;
	private final int frequency;
	private final TimeUnit timeUnit;
	private final List<String> dataList;
	private final ScheduledExecutorService executor;
	
	private final static String NAME		= FileMarketDataTransport.class.getSimpleName();
	private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );

	
	public FileMarketDataTransport( String fileLocation, int frequency, TimeUnit timeUnit ){
		super( TransportType.FILE );
		
		this.fileLocation 	= fileLocation;
		this.frequency		= frequency;
		this.timeUnit		= timeUnit;
		this.dataList		= loadData( );
	    this.executor       = Executors.newSingleThreadScheduledExecutor( new FluentThreadFactory(NAME) );
				
	}
	
	
	@Override
	public final String name( ){
		return NAME;
	}

	
	@Override
	public final void init( ){
		
		if( dataList.isEmpty() ){
			LOGGER.warn( "Shutting down publisher type [{}] as we didn't load any data.{}", getType(), NEWLINE );
			stop();
			return;
		}
		
		executor.scheduleAtFixedRate( this, frequency, frequency, timeUnit );
		LOGGER.info( "Publisher type [{}] started, will publish prices every {} {}.", getType(), frequency, timeUnit );
	
	}
	
	
	@Override
	public final void run( ){
		
		String message = dataList.get( index );
		
		distribute( message );
		++index;
	
	}
	
	
	
	protected final List<String> loadData( ){
		
		FileReader reader		= null;
		BufferedReader buff		= null;
		List<String> dataList 	= new LinkedList<>( );
		
		try{
		
			reader	= new FileReader( fileLocation );
			buff 	= new BufferedReader( reader );
			
		    String str;
		    while( (str = buff.readLine()) != null ){
		        dataList.add( str );
		    }
		    
		}catch( Exception e ){
			LOGGER.warn("FAILED to load data from [{}].", fileLocation, e );
		
		}finally{
			if( buff != null ){
				try{
					buff.close();
				}catch( IOException e ){}
			}
		
		}
	
		return dataList;
		
	}
	

	
	@Override
	public final boolean isConnected( ){
		return true;
	}

	
	@Override
	public final void stop( ){
		 executor.shutdown();
	}

	
}
