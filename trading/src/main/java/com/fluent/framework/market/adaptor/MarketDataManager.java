package com.fluent.framework.market.adaptor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.cliffc.high_scale_lib.*;

import com.fluent.framework.config.ConfigManager;
import com.fluent.framework.core.FluentService;
import com.fluent.framework.events.in.InEvent;
import com.fluent.framework.events.in.InEventDispatcher;
import com.fluent.framework.events.in.InType;
import com.fluent.framework.events.out.OutEvent;
import com.fluent.framework.events.out.OutListener;
import com.fluent.framework.events.out.OutType;
import com.fluent.framework.market.core.Exchange;
import com.fluent.framework.market.event.MarketDataEvent;
import com.fluent.framework.market.event.MarketDataSubscribeEvent;
import com.fluent.framework.market.event.MarketDataUnsubscribeEvent;
import com.fluent.framework.reference.ReferenceDataEvent;

import static com.fluent.framework.events.out.OutType.*;
import static com.fluent.framework.util.FluentUtil.*;
import static com.fluent.framework.util.FluentToolkit.*;


public final class MarketDataManager implements FluentService, OutListener, MarketDataListener{
	
	private final ConfigManager cfgManager;
	private final MarketDataFilter mdFilter;
	private final InEventDispatcher inDispatcher;

	private final Map<String, Integer> subsCounter;
	private final Map<String, MarketDataEvent> mdCache;
	private final Map<Exchange, MarketDataAdapter> adaptorMap;
	
    private final static String NAME	= MarketDataManager.class.getSimpleName();
    private final static Logger LOGGER  = LoggerFactory.getLogger( NAME );

    
	public MarketDataManager( ConfigManager cfgManager, InEventDispatcher inDispatcher ) throws Exception{
		
		this.cfgManager		= notNull( cfgManager, "Algo config can't be null.");
		this.inDispatcher	= inDispatcher;
		this.mdCache		= new NonBlockingHashMap<>();
		this.subsCounter	= new NonBlockingHashMap<>();
		this.mdFilter		= new MarketDataFilter(cfgManager );
		this.adaptorMap		= MarketDataAdaptorFactory.createAdaptorMap( cfgManager );
		
	}
	
	
	@Override
	public final String name( ){
		return NAME;
	}
	
	
	@Override
	public final boolean isSupported( OutType type ){
		return ( MD_SUBSCRIBE_EVENT == type || MD_UNSUBSCRIBE_EVENT == type );
	}
	
	
	@Override
	public final void start( ){
		
		for( MarketDataAdapter adaptor : adaptorMap.values() ){
			adaptor.register( this );
			adaptor.start();
		}
	}
	

	@Override
	public final void mdUpdate( InEvent event ){
		
		InType eventType = event.getType( );
				
		switch( eventType ){
		
		case MARKET_DATA:
			processMarketData( event );
			break;
			
		case MARKET_STATUS:
			processMarketStatus( event );
			break;
			
		default:
			LOGGER.warn("Event InType:{} is unsupported.", eventType );
		}
		
	}

	
	protected final void processMarketData( InEvent event ){
		
		MarketDataEvent mdEvent = (MarketDataEvent) event;
		String mdSymbol			= mdEvent.getSymbol();
		
		MarketDataEvent prevMd 	= mdCache.get(mdSymbol);
		boolean toFilterOut		= mdFilter.toFilter( prevMd, mdEvent );
		if( toFilterOut ) return;
		
		inDispatcher.enqueue( mdEvent );
		mdCache.put(mdSymbol, mdEvent );
		
	}

	
	protected final void processMarketStatus( InEvent statusEvent ){
		inDispatcher.enqueue( statusEvent );
	}


	
	

	@Override
	public final boolean outUpdate( OutEvent event ){
		
		OutType outType = event.getType();
		
		switch( outType ){
			
			case MD_SUBSCRIBE_EVENT:
				handleSubscribe( event );
				break;
				
			case MD_UNSUBSCRIBE_EVENT:
				handleUnsubscribe( event );
				break;
				
			default:
				LOGGER.warn("Event OutType:{} is unsupported.", outType );
				
		}
		
		return true;
		
	}


	protected final void handleSubscribe( OutEvent event ){
		
		try{
			
			MarketDataSubscribeEvent sEvent	= (MarketDataSubscribeEvent) event;
			ReferenceDataEvent[] subEvents	= sEvent.getReferenceEvents();
			
			for( ReferenceDataEvent subEvent : subEvents ){
				
				LOGGER.debug("Attempting to subscribe for [{}]", sEvent );
				
				Exchange exchange			= subEvent.getExchange();
				MarketDataAdapter adaptor 	= adaptorMap.get( exchange );
				
				if( adaptor == null ){
					LOGGER.warn("FAILED to subscribe for [{}] as Adaptor is unconfigured.", exchange );
					continue;
				}
				
				String mdSymbol				= subEvent.getSymbol();
				int subscriptionCounter		= subsCounter.get( mdSymbol );
				if( subscriptionCounter > ZERO ){
					++subscriptionCounter;
					sendLocalSnapshot( mdSymbol );
				
					LOGGER.debug("Subscription already exists, count incremented [{}].", subscriptionCounter );
					subsCounter.put(mdSymbol, subscriptionCounter );
					continue;
				}
				
				//Subscription doesn't exist, really subscribe
				boolean subscribed			= adaptor.subscribe( subEvent );
				if( subscribed ){
					subscriptionCounter		= ONE;
					subsCounter.put(mdSymbol, subscriptionCounter );
					LOGGER.debug("NEW Subscription created, count [{}].", subscriptionCounter );
				}
				
			}
			
		}catch( Exception e ){
			LOGGER.warn("FAILED to subscribe for [{}]", event, e );
		}
	
	}
	

	protected final void sendLocalSnapshot( String mdSymbol ){
		
		MarketDataEvent mdEvent = mdCache.get(mdSymbol);
		if( mdEvent == null ){
			LOGGER.warn("Can't send local snapshot for [{}] as MS isn't  in cache.", mdSymbol );
			return;
		}
		
		inDispatcher.enqueue(mdEvent);
		LOGGER.debug("Sent MD update from local cache.", mdEvent );
		
	}


	protected final void handleUnsubscribe( OutEvent event ){
		
		try{
			
			MarketDataUnsubscribeEvent sEvent	= (MarketDataUnsubscribeEvent) event;
			ReferenceDataEvent[] subEvents		= sEvent.getReferenceEvents();
			
			for( ReferenceDataEvent subEvent : subEvents ){
				
				LOGGER.debug("Attempting to unsubscribe for [{}]", sEvent );
				
				Exchange exchange				= subEvent.getExchange();
				MarketDataAdapter adaptor 		= adaptorMap.get( exchange );
				
				if( adaptor == null ){
					LOGGER.warn("FAILED to unsubscribe for [{}] as Adaptor is unconfigured.", exchange );
					continue;
				}
				
				String mdSymbol					= subEvent.getSymbol();
				int unsubscriptionCount			= subsCounter.get( mdSymbol );
				if( unsubscriptionCount == ZERO ){
					LOGGER.warn("FAILED to unsubscribe as we werent subscribed for [{}] on [{}].", mdSymbol, exchange );
					continue;
				}
				
				//Subscription Count more than 1, reduce the count
				if( unsubscriptionCount > ZERO ){
					--unsubscriptionCount;
					subsCounter.put(mdSymbol, unsubscriptionCount);
					LOGGER.debug("Other subscription exists, count reduced [{}].", unsubscriptionCount );
					continue;
				}
				
				//Subscription Count exactly 1, unsubscribe
				boolean unsubscribed		= adaptor.unsubscribe( subEvent );
				if( unsubscribed ){
					unsubscriptionCount		= ZERO;
					subsCounter.put(mdSymbol, unsubscriptionCount);
					LOGGER.debug("Successfully unsubscribed for [{}] on [{}].", mdSymbol, exchange );
				}
				
			}
			
		}catch( Exception e ){
			LOGGER.warn("FAILED to unsubscribe for [{}]", event, e );
		}
			
	}
	

	@Override
	public final void stop() {
		for( MarketDataAdapter adaptor : adaptorMap.values() ){
			adaptor.stop();
		}
	}



	
}
