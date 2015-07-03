package com.fluent.framework.market;

import com.fluent.framework.transport.core.*;


public final class FileExchangeInfo extends ExchangeInfo{
	
	private final String fileLocation;

	private final static String PREFIX = "File: ";
	
	
	public FileExchangeInfo( Exchange exchange, String open, String close, 
							String timeZone, String fileLocation ){
		
		super( exchange, open, close, timeZone, TransportType.FILE ); 
			
		this.fileLocation 	= fileLocation;
	}
	
	
	public final String getFileLocation( ){
		return fileLocation;
	}

	
	@Override
	public final String getTransportInfo( ){
		return PREFIX + getFileLocation();
	}
	
	
}
