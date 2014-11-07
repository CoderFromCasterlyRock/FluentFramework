package com.fluent.etrading.framework.collections;

import java.text.*;
import java.util.*;

import static com.fluent.etrading.framework.utility.ContainerUtil.*;


public final class FluentWatch{
	
	public static final String DATE_FORMAT  		= "yyyy-MM-dd";
	public static final String TODAY_FORMATTED		=  FluentWatch.getFormattedDate( DATE_FORMAT );
	public static final long TIME_OFFSET_MS			= TimeZone.getDefault().getOffset( nowMillis() );
	
	
	public final static long nowMillis(){
		return System.currentTimeMillis();
	}

	
	public final static long nowNanos(){
		return System.nanoTime();
	}
	
	
	public final static String getFormattedDate( String dateFormat ){
    	SimpleDateFormat format = new SimpleDateFormat( dateFormat );
    	return format.format( new Date() );
    }
    
    
    public final static String formatTime( long timestampInMillis ){
    	
    	StringBuilder timestamp = new StringBuilder( TWENTY_FOUR );
    	timestampInMillis	= timestampInMillis + TIME_OFFSET_MS;
    	
    	int ms = (int) ( timestampInMillis % SIXTY );
    	int ss = (int) ( timestampInMillis/THOUSAND) % SIXTY;
    	int mi = (int) ((timestampInMillis/(THOUSAND*SIXTY)) % SIXTY);
    	int hr = (int) ((timestampInMillis/(THOUSAND*SIXTY*SIXTY))% TWENTY_FOUR);
    	
    	timestamp.append(hr).append(COLON).append(mi).append(COLON).append(ss).append(DOT).append(ms);
    	
    	return timestamp.toString();
    	
    }
	

}
