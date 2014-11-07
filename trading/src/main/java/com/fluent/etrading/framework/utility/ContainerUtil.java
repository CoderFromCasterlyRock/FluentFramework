package com.fluent.etrading.framework.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public final class ContainerUtil{

    public static final int ZERO                = 0;
    public static final int ONE                 = 1;
    public static final int TWO                 = 2;
    public static final int THREE               = 3;
    public static final int FOUR                = 4;
    public static final int FIVE                = 5;
    public static final int TEN                 = 10;
    public static final int SIXTEEN             = 16;
    public static final int TWENTY_FOUR         = 24;
    public static final int THIRTY_TWO          = 32;
    public static final int SIXTY		        = 60;
    public static final int SIXTY_FOUR          = 64;
    public static final int HUNDRED             = 100;
    public static final int THOUSAND            = 1000;
    public static final int NEGATIVE_ONE        = -1;

    public static final byte[] EMPTY_BYTE       = {};

    public static final double ZERO_DOUBLE      = 0.0d;
    public static final double PRICE_TOLERANCE  = 1.0e-8;


    public static final String DOT              = ".";
    public static final String DASH             = "-";
    public static final String PIPE             = "|";
    public static final String PLUS             = "+";
    public static final String EMPTY            = "";
    public static final String SPACE            = " ";
    public static final String COMMA            = ",";
    public static final String COLON            = ":";
    public static final String QUOTE            = "\"";
    public static final String NEWLINE          = "\n";
    public static final String L_CURL           = "{";
    public static final String R_CURL           = "}";
    public static final String L_BRACKET        = "[";
    public static final String R_BRACKET        = "]";
    public static final String SEMICOLON        = ".";
    public static final String UNDERSCORE       = "_";
    public static final String DATE_FORMAT      = "yyyy-MM-dd";
    public static final String ES_DATE_FORMAT   = "hour_minute_second_millis";
        
    public static final String TODAY_FORMATTED	=  getFormattedDate( DATE_FORMAT );
    
    public static final long TIME_OFFSET_MS		= TimeZone.getDefault().getOffset(System.currentTimeMillis());

    
    protected ContainerUtil(){}


    public final static boolean isBlank( String data ){
        return ( data == null || data.isEmpty() ) ? true : false;
    }

    public final static int findNextPositivePowerOfTwo( final int value ){
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }


    public final static int getRoundedTickDifference( double pivotPrice, double newPrice, double minPriceTick ){

        double priceDifference      = ( newPrice - pivotPrice );
        double tickDifference       = ( priceDifference / minPriceTick );
        double tickDifferenceAdj    = ( tickDifference >= ZERO ) ? (tickDifference + PRICE_TOLERANCE) : (tickDifference - PRICE_TOLERANCE);
        int roundedTickDifference   = (int) tickDifferenceAdj;

        return roundedTickDifference;

    }
    
    
    protected final static String getFormattedDate( String dateFormat ){
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
    
    
    public final static int parseInteger( String value ){
		return isBlank(value) ? 0 : Integer.parseInt(value);
    }
	
    public final static double parseDouble( String value ){
		return isBlank(value) ? 0.0 : Double.parseDouble(value);
    }
    
	
}

