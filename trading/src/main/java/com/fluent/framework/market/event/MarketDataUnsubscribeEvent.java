package com.fluent.framework.market.event;

import java.util.Arrays;

import com.fluent.framework.events.out.OutEvent;
import com.fluent.framework.events.out.OutType;
import com.fluent.framework.reference.ReferenceDataEvent;


public final class MarketDataUnsubscribeEvent extends OutEvent{

	private final ReferenceDataEvent[] refEvents;
	
	private static final long serialVersionUID = 1L;
	
	
	public MarketDataUnsubscribeEvent( ReferenceDataEvent[] refEvents ){
		super( OutType.MD_UNSUBSCRIBE_EVENT );
		
		this.refEvents	= refEvents;
	
	}

	
	public final ReferenceDataEvent[] getReferenceEvents( ){
		return refEvents;
	}
	
	
	@Override
	public final void toEventString( StringBuilder builder ){
		builder.append( Arrays.deepToString(refEvents));
	}
	
}
