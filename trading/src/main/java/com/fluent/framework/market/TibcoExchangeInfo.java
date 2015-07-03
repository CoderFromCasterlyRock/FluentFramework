package com.fluent.framework.market;

import com.fluent.framework.transport.core.*;

import static com.fluent.framework.util.FluentUtil.*;


public final class TibcoExchangeInfo extends ExchangeInfo{
	
	private final String daemon;
	private final String network;
	private final String service;
	private final String topic;
	
	private final static String PREFIX = "TibcoRV: ";
	
	
	public TibcoExchangeInfo( Exchange exchange, String open, String close,	String timeZone,
							  String daemon, String network, String service, String topic ){
		
		super( exchange, open, close, timeZone, TransportType.TIBCO ); 
			
		this.daemon 	= daemon;
		this.network 	= network;
		this.service 	= service;
		this.topic 		= topic;
		
	}


	public final String getDaemon( ){
		return daemon;
	}



	public final String getNetwork( ){
		return network;
	}



	public final String getService( ){
		return service;
	}



	public final String getTopic( ){
		return topic;
	}
	

	
	@Override
	public final String getTransportInfo( ){
		
		StringBuilder builder = new StringBuilder( SIXTY_FOUR );
		
		builder.append( PREFIX );
		builder.append( "Daemon: ").append( daemon );
		builder.append( "Network: ").append( network );
		builder.append( "Service: ").append( service );
		builder.append( "Topic: ").append( topic );
		
		return builder.toString();
	}
	
}
