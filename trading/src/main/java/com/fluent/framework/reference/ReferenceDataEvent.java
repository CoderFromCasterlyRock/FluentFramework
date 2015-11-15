package com.fluent.framework.reference;

import com.fluent.framework.events.in.InEvent;
import com.fluent.framework.events.in.InType;
import com.fluent.framework.market.core.Exchange;


public final class ReferenceDataEvent extends InEvent{

	private final String symbol;
	private final Exchange exchange;

	private static final long serialVersionUID = 1L;
	
	
	public ReferenceDataEvent( String symbol, Exchange exchange ){
		super( InType.REFERENCE_DATA );
		
		this.symbol		= symbol;
		this.exchange 	= exchange;
	}

	
	public final String getSymbol( ){
		return symbol;
	}
	
	public final Exchange getExchange( ){
		return exchange;
	}

	
	@Override
	public final void toEventString( StringBuilder builder ){
		
	}

	
	
}
