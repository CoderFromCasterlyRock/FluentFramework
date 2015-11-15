package com.fluent.framework.market.core;

import java.util.TimeZone;

import com.fluent.framework.util.TimeUtil;

import static com.fluent.framework.util.TimeUtil.*;
import static com.fluent.framework.util.FluentUtil.*;
import static com.fluent.framework.util.FluentToolkit.*;


public final class ExchangeDetails{
	
	private final Exchange exchange;
	private final String openTime;
	private final long openTimeMillis;
	private final String closeTime;
	private final long closeTimeMillis;
	private final TimeZone timeZone;
	private final int speedLimit;
		
	
	public ExchangeDetails( String exchangeKey, String open, String close, String timeZoneStr, String speedLimit ) throws Exception{
		
		verify( exchangeKey, open, close, timeZoneStr, speedLimit );
		
		this.exchange 		= Exchange.fromCode( exchangeKey);
		this.openTime 		= open;
		this.closeTime 		= close;
		this.timeZone 		= TimeZone.getTimeZone(timeZoneStr);
		this.speedLimit		= Integer.parseInt(speedLimit);		
		this.openTimeMillis = getAdjustedOpen(open, close, timeZone, System.currentTimeMillis());
		this.closeTimeMillis= getAdjustedClose(open, close, timeZone, System.currentTimeMillis());
		
	}
	
	
	private final void verify( String exchangeKey, String openTime, String closeTime, String timeZone, String speedimit ) throws Exception{
		
		String prefixMessage	= "Exchange details is invalid as";
		
		if( Exchange.UNSUPPORTED == Exchange.fromCode(exchangeKey) ){
			throw new Exception( prefixMessage + " Exchange [" + exchangeKey + "] is invalid.");
		}
		
		if( isBlank(openTime) ){
			throw new Exception( prefixMessage + " OpenTime [" + openTime + "] is invalid.");
		}
		
		if( isBlank(closeTime) ){
			throw new Exception( prefixMessage + " CloseTime [" + closeTime + "] is invalid.");
		}
		
		TimeUtil.parseTimeZone(timeZone);
				
		if( !isInteger(speedimit) ){
			throw new Exception( prefixMessage + " Speedimit [" + speedimit + "] is invalid.");
		}
		
	}
	
	public final Exchange getExchange( ){
		return exchange;
	}


	public final String getOpenTime( ){
		return openTime;
	}

	
	public final long getOpenMillis( ){
		return openTimeMillis;
	}
	

	public final String getCloseTime( ){
		return closeTime;
	}

	
	public final long getCloseMillis( ){
		return closeTimeMillis;
	}
	

	public final TimeZone getTimeZone( ){
		return timeZone;
	}


	public final int getSpeedLimit( ){
		return speedLimit;
	}
	
	public final boolean isExchangeOpen( ){
		return isExchangeOpen( System.currentTimeMillis() );
	}
	
	
	protected final boolean isExchangeOpen( long nowMillis ){
		return isOpen(nowMillis, openTimeMillis, closeTimeMillis);
	}
	
	
	@Override
	public String toString(){
		
		StringBuilder builder = new StringBuilder( TWO * SIXTY_FOUR );
		
		builder.append("[Exchange=").append(exchange);
		builder.append(", Open=").append(openTime);
		builder.append(", Close=").append(closeTime);
		builder.append(", TimeZone=").append(timeZone.getID());
		builder.append(", SpeedLimit/sec=").append(speedLimit);
		builder.append("]");
		
		return builder.toString();
	}
	
	
}
