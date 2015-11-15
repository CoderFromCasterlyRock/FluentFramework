package com.fluent.framework.market.adaptor;


public enum MarketDataProvider{
	
	REUTERS,
	TIBCO_SERVER,
	ZERO_MQ_SERVER,
	UNKNOWN;

	
	public final static MarketDataProvider getProvider( String name ){
		return MarketDataProvider.valueOf( name );
	}
	
}



