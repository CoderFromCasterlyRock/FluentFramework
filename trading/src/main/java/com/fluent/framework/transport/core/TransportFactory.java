package com.fluent.framework.transport.core;

import com.fluent.framework.market.*;
import com.fluent.framework.transport.file.*;


public final class TransportFactory{
	
	
	public final static Transport create( ExchangeInfo eInfo ){
		
		TransportType type	= eInfo.getType();
		
		try{
		
			switch( type ){
		
				case FILE:
					return createFileTransport( eInfo );
				
				default:
					throw new IllegalStateException("Unsupport TransportType: " + type );
			
			}
			
		}catch( Exception e ){
			throw new IllegalStateException("transportType is missing from Config", e );
		}

	}

	
	
	protected final static Transport createFileTransport(  ExchangeInfo eInfo  ){
		FileExchangeInfo fInfo 	= ( FileExchangeInfo ) eInfo;
		String fileLocation		= fInfo.getFileLocation();
		Transport fileTransport	= new FileMarketDataTransport( fileLocation );
		
		return fileTransport;
	}
	
	
}
