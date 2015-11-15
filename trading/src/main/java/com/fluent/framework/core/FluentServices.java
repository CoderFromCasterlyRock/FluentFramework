package com.fluent.framework.core;

import com.fluent.framework.config.*;
import com.fluent.framework.events.in.*;
import com.fluent.framework.events.out.*;
import com.fluent.framework.market.adaptor.MarketDataManager;


public final class FluentServices{

	private final ConfigManager cfgManager;
	private final InEventDispatcher inDispatcher;
	private final OutEventDispatcher outDispatcher;
	private final MarketDataManager mdManager;
	
	
	public FluentServices(ConfigManager cfgManager, InEventDispatcher inDispatcher, OutEventDispatcher outDispatcher, MarketDataManager mdManager) {
		
		this.cfgManager 	= cfgManager;
		this.inDispatcher 	= inDispatcher;
		this.outDispatcher 	= outDispatcher;
		this.mdManager 		= mdManager;
	
	}


	public final ConfigManager getCfgManager() {
		return cfgManager;
	}


	public final InEventDispatcher getInDispatcher() {
		return inDispatcher;
	}


	public final OutEventDispatcher getOutDispatcher() {
		return outDispatcher;
	}


	public final MarketDataManager getMdManager() {
		return mdManager;
	}
	
	//private final OrderManager orderManager;
	//private final ReferenceDataManager refManager;
	

}
