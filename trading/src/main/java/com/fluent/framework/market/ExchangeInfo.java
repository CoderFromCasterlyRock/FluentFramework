package com.fluent.framework.market;

import com.fluent.framework.transport.core.*;

import static com.fluent.framework.util.FluentUtil.*;


public abstract class ExchangeInfo{
	
	private final Exchange exchange;
	private final String openTime;
	private final String closeTime;
	private final String timeZone;
	private final TransportType tType;
	
	
	public ExchangeInfo( Exchange exchange, String openTime, String closeTime, String timeZone, TransportType tType ){
		
		this.exchange 		= exchange;
		this.openTime 		= openTime;
		this.closeTime 		= closeTime;
		this.timeZone 		= timeZone;
		this.tType 			= tType;
		
	}

	
	protected abstract String getTransportInfo();
	
	
	public final Exchange getExchange( ){
		return exchange;
	}


	public final String getOpenTime( ){
		return openTime;
	}


	public final String getCloseTime( ){
		return closeTime;
	}


	public final String getTimeZone( ){
		return timeZone;
	}


	public final TransportType getType( ){
		return tType;
	}


	@Override
	public String toString(){
		
		StringBuilder builder = new StringBuilder( TWO * SIXTY_FOUR );
		
		builder.append("[Exchange=").append(exchange);
		builder.append(", Open=").append(openTime);
		builder.append(", Close=").append(closeTime);
		builder.append(", TimeZone=").append(timeZone);
		builder.append(", Type=").append(tType);
		builder.append( getTransportInfo() );
		builder.append("]");
		
		return builder.toString();
	}
	
	
}
