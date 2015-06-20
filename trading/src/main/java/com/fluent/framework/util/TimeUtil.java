package com.fluent.framework.util;

import java.text.*;
import java.util.*;

import static com.fluent.framework.util.FluentUtil.*;


public final class TimeUtil{

    protected TimeUtil(){}

    public final static String TODAY = new SimpleDateFormat("yyyy.MM.dd").format( new Date() );

   
    public final static long currentNanos( ){
        return System.nanoTime( );
    }

    
    public final static long currentMillis( ){
        return System.currentTimeMillis();
    }
    
    
    public final static long parseTime( String name, String timeAsString ){
    	
    	String[] timeTokens 	= timeAsString.trim().split( COLON );
    	if( timeTokens.length < THREE ){
    		throw new RuntimeException("Invalid time format of " + name + " = " + timeAsString + " (must be in HH:mm:ss)" );	
    	}
    	
    	long timeInMillis 		= ZERO;
    	
    	try{
    		Calendar calendar	= Calendar.getInstance( TimeZone.getDefault() );
    		calendar.set( Calendar.HOUR_OF_DAY, Integer.parseInt(timeTokens[ZERO]));
    		calendar.set( Calendar.MINUTE, 		Integer.parseInt(timeTokens[ONE]));
    		calendar.set( Calendar.SECOND, 		Integer.parseInt(timeTokens[TWO]));
    		calendar.set( Calendar.MILLISECOND,	ZERO);
    
    		timeInMillis		= calendar.getTimeInMillis();
    		
    	}catch( Exception e ){
    		throw new RuntimeException("Failed to parse time format of name: " + name  + ", " + timeAsString, e);
    	}
    	
        return timeInMillis;
    
    }
    
    

}
