package com.fluent.framework.market.adaptor;

import com.fluent.framework.config.ConfigManager;
import com.fluent.framework.market.event.MarketDataEvent;


public final class MarketDataFilter{
	
	private final ConfigManager cfgManager;
	
	public MarketDataFilter( ConfigManager cfgManager ){
		this.cfgManager = cfgManager;
	}

	
	public final boolean toFilter( MarketDataEvent prevMDEvent, MarketDataEvent currMDEvent ){
		return false;
	}
	
	
}
