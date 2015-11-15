package com.fluent.framework.market.adaptor;

import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.*;

import com.fluent.framework.core.*;
import com.fluent.framework.events.core.*;
import com.fluent.framework.market.core.*;
import com.fluent.framework.reference.ReferenceDataEvent;


public abstract class MarketDataAdapter implements FluentDataListener, FluentService{
	
	private final Exchange[] exchanges;
	private final MarketDataProvider provider;
	private final AtomicReference<MarketDataManager> managerRef;

		
	private final static String NAME		= MarketDataAdapter.class.getSimpleName();
	private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );

	
	public MarketDataAdapter( MarketDataProvider provider, Exchange ... exchanges ){
		this.provider		= provider;
		this.exchanges		= exchanges;
		this.managerRef		= new AtomicReference<>();
						
	}
	
	
	@Override
	public final String name( ){
		return NAME;
	}
	
	
	public final void register( MarketDataManager manager ){
		managerRef.set(manager);
		LOGGER.debug("Registered [{}] as listener for MarketDataAdapter.", manager.name() );
	}
	
	
	public final void unregister( MarketDataManager manager ){
		managerRef.set(null);
		LOGGER.debug("UnRegistered [{}] as listener for MarketDataAdapter.", manager.name() );
	}

	
	public abstract boolean subscribe( ReferenceDataEvent event );
	public abstract boolean unsubscribe( ReferenceDataEvent event );

	

	public final Exchange[] getExchanges() {
		return exchanges;
	}


	public final MarketDataProvider getProvider() {
		return provider;
	}

	
}


