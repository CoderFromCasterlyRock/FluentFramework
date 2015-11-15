package com.fluent.framework.market.adaptor;

import com.fluent.framework.events.in.InEvent;


public interface MarketDataListener{
	
	public void mdUpdate( InEvent inEvent );

}
