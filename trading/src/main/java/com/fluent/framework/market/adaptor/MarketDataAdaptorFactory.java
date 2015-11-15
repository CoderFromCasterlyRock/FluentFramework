package com.fluent.framework.market.adaptor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fluent.framework.config.ConfigManager;
import com.fluent.framework.market.core.Exchange;
import com.typesafe.config.Config;


public final class MarketDataAdaptorFactory{
	
	
	  private final static String NAME		= MarketDataAdaptorFactory.class.getSimpleName();
	  private final static Logger LOGGER  	= LoggerFactory.getLogger( NAME );
	    
	
	  protected final static Map<Exchange, MarketDataAdapter> createAdaptorMap( ConfigManager cfgManager ) throws Exception{
		
		Map<Exchange, MarketDataAdapter> map	= new HashMap<>( );
		
		for( Config adaptorConfig : cfgManager.getMarketDataAdaptorConfigs() ){
		
			MarketDataAdapter adaptor 	= null;
			Exchange[] exchanges		= {};
			String mdProviderName		= adaptorConfig.getString("mdProvider");
			MarketDataProvider provider	= MarketDataProvider.getProvider( mdProviderName );
		
			switch( provider ){
			
				case REUTERS:
					adaptor		= createReutersMDAdaptor( cfgManager );
					break;
					
				case TIBCO_SERVER:
					adaptor		= createTibcoMDAdaptor( cfgManager );
					break;
					
				default:
					throw new Exception("MarketDataAdapter unimplemented for " + provider );
					
			}
			
			if( adaptor == null ) continue;
			
			for( Exchange exchange : exchanges ){
				
				boolean alreadyExists = map.containsKey(exchange);
				if( alreadyExists ){
					LOGGER.error("Market data adaptor for Exchange: " + exchange + " was already created." );
					throw new RuntimeException();
				}
			
				map.put( exchange, adaptor );
				LOGGER.info("Market data adaptor created for Exchange: " + exchange );
			
			}
			
		}
				
		return map;
		
	}


	
	protected final static MarketDataAdapter createReutersMDAdaptor( ConfigManager cfgManager2 ){
		return null;
	}


	protected final static  MarketDataAdapter createTibcoMDAdaptor(ConfigManager cfgManager2) {
		return null;
	}
	

}
