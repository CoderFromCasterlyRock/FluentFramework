package com.fluent.framework.market;

import java.util.ArrayList;
import java.util.List;

import com.typesafe.config.Config;
import com.fluent.framework.transport.core.*;



public final class ExchangeInfoFactory{
	
	
	public final static List<ExchangeInfo> create( List<? extends Config> configs ){
		
		List<ExchangeInfo> infoList = new ArrayList<>();
		
		for( Config config : configs ){
			ExchangeInfo info	= create(config);
			if( info == null ) continue;
			
			infoList.add( info );
		}
		
		return infoList;
	}
	
	
	
	public final static ExchangeInfo create( Config config ){
		
		try{
		
			String name			= config.getString("name");
			Exchange exchange 	= Exchange.valueOf(name);
			String openTime		= config.getString("openTime");
			String closeTime	= config.getString("closeTime");
			String timeZone		= config.getString("timeZone");
			TransportType tType	= TransportType.valueOf(config.getString("transportType"));
		
			switch( tType ){
				case FILE:
					return createFile( config, exchange, openTime, closeTime, timeZone );
					
				default:
					throw new IllegalStateException("No Exchange info defined for TransportType: " + tType );
					
			}
			
		}catch( Exception e ){
			throw new IllegalStateException("FAILED to correctly parse Exchange info from Config: " + config );
		}
		
	}
	
	
	
	protected final static ExchangeInfo createFile( Config config, Exchange exchange, String open, String close, String timeZone ){
		ExchangeInfo fileExchange	= null;
		
		try{
			String fileLocation		= config.getString("fileLocation");
			fileExchange			= new FileExchangeInfo( exchange, open, close, timeZone, fileLocation );
			
		}catch( Exception e ){
			throw new IllegalStateException("FAILED to correctly parse FILE Exchange info from Config: " + config );
		}
		
		return fileExchange;
	}
	
	
	
	protected final static ExchangeInfo createTibrv( Config config, Exchange exchange, String open, String close, String timeZone ){
		
		ExchangeInfo rvExchange		= null;
		
		try{
			
			String daemon			= config.getString("daemon");
			String network			= config.getString("network");
			String service			= config.getString("service");
			String topic			= config.getString("topic");
			rvExchange				= new TibcoExchangeInfo( exchange, open, close, timeZone, daemon, network, service, topic );
			
		}catch( Exception e ){
			throw new IllegalStateException("FAILED to correctly parse TIBRV Exchange info from Config: " + config );
		}
		
		return rvExchange;
	}
	

}
