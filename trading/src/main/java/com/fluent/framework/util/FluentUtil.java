package com.fluent.framework.util;

import java.io.File;
import java.lang.management.*;
import java.net.InetAddress;


public final class FluentUtil{

    public static final int ZERO                = 0;
    public static final int ONE                 = 1;
    public static final int TWO                 = 2;
    public static final int THREE               = 3;
    public static final int FOUR                = 4;
    public static final int FIVE                = 5;
    public static final int EIGHT               = 8;
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
    public static final String TAB              = "\t";
    public static final String PIPE             = "|";
    public static final String PLUS             = "+";
    public static final String EMPTY            = "";
    public static final String SPACE            = " ";
    public static final String COMMA            = ",";
    public static final String COLON            = ":";
    public static final String QUOTE            = "\"";
    public static final String NEWLINE          = "\n";
    public static final String X_SPACE          = " x ";
    public static final String L_CURL           = "{";
    public static final String R_CURL           = "}";
    public static final String L_BRACKET        = "[";
    public static final String R_BRACKET        = "]";
    public static final String SEMICOLON        = ".";
    public static final String UNDERSCORE       = "_";
    
    public static final String SLASH 			= File.separator;
    public static final String HOSTNAME			= getHostName( );
    public static final String OS_TYPE 			= System.getProperty("os.name").toLowerCase();
    public static final boolean IS_WINDOWS		= OS_TYPE.indexOf("win") >= ZERO;
    public static final boolean IS_LINUX		= OS_TYPE.indexOf("nix") >= ZERO || OS_TYPE.indexOf("nux") >= ZERO || OS_TYPE.indexOf("aix") > ZERO;

    
    protected FluentUtil(){}


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
    
    
    public final static int parseInteger( String value ){
		return isBlank(value) ? 0 : Integer.parseInt(value);
    }
	
    
    public final static double parseDouble( String value ){
		return isBlank(value) ? 0.0 : Double.parseDouble(value);
    }
    
    
    public final static String getFullProcessName(){
    	return ManagementFactory.getRuntimeMXBean().getName();
    }
    
    
    public final static double rndNumberBetween( final double upper, final double lower ){
        return (Math.random() * (upper - lower)) + lower;
    }
    
    
    public final static String padRight( final double number, final int space ){
        return String.format("%1$-" + space + "s", String.valueOf(number) );
    }
   
    
    public final static String padRight( final String number, final int space ){
        return String.format("%1$-" + space + "s", String.valueOf(number) );
    }
    

    public final static String padLeft( final double number, final int space ){
        return String.format("%1$" + space + "s", String.valueOf(number) );
    }
   
    
    public final static String padLeft( final String number, final int space ){
        return String.format("%1$" + space + "s", String.valueOf(number) );
    }
    
    
    public final static String getHostName( ){
    	String hostName = EMPTY;
    	try{
    		hostName = InetAddress.getLocalHost().getHostName();
    	}catch( Exception e ){}
    	
    	return hostName;
    
    }
    
}

