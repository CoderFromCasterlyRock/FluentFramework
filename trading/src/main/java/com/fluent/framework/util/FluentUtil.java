package com.fluent.framework.util;

import java.io.*;

import com.eclipsesource.json.JsonObject;
import com.fluent.framework.events.in.InEvent;
import com.fluent.framework.events.in.InType;
import com.fluent.framework.events.out.OutEvent;
import com.fluent.framework.events.out.OutType;

import static com.fluent.framework.util.FluentToolkit.*;


public final class FluentUtil{

    public static final int ZERO                	= 0;
    public static final int ONE                 	= 1;
    public static final int TWO                 	= 2;
    public static final int THREE               	= 3;
    public static final int FOUR                	= 4;
    public static final int FIVE                	= 5;
    public static final int EIGHT               	= 8;
    public static final int TEN                 	= 10;
    public static final int SIXTEEN             	= 16;
    public static final int TWENTY_FOUR        		= 24;
    public static final int THIRTY_TWO          	= 32;
    public static final int SIXTY		        	= 60;
    public static final int SIXTY_FOUR          	= 64;
    public static final int HUNDRED             	= 100;
    public static final int THOUSAND            	= 1000;
    public static final int MILLION	            	= 1000 * 1000;
    public static final int NEGATIVE_ONE        	= -1;

    public static final char COLON_CHAR         	= ':';
    public static final byte[] EMPTY_BYTE       	= {};

    public static final double ZERO_DOUBLE      	= 0.0d;
    public static final double PRICE_TOLERANCE  	= 1.0e-8;


    public static final String DOT              	= ".";
    public static final String DASH             	= "-";
    public static final String TAB              	= "\t";
    public static final String PIPE             	= "|";
    public static final String PLUS             	= "+";
    public static final String EMPTY            	= "";
    public static final String SPACE            	= " ";
    public static final String COMMA            	= ",";
    public static final String COLON            	= ":";
    public static final String QUOTE            	= "\"";
    public static final String NEWLINE          	= "\n";
    public static final String X_SPACE          	= " x ";
    public static final String L_CURL           	= "{";
    public static final String R_CURL          	 	= "}";
    public static final String L_BRACKET       		= "[";
    public static final String R_BRACKET        	= "]";
    public static final String SEMICOLON        	= ".";
    public static final String UNDERSCORE       	= "_";
    
    public static final String SLASH 				= File.separator;
    public static final boolean IS_WINDOWS			= isWindows();
    public static final boolean IS_LINUX			= isLinux();
    public static final String HOSTNAME				= getHostName();

    public static final InEvent IN_WARMUP_EVENT 	= new InWarmupEvent();
    public static final OutEvent OUT_WARMUP_EVENT	= new OutWarmupEvent();
    
    protected FluentUtil(){}
 
    
    
    private static final class InWarmupEvent extends InEvent{
    	
    	private final String eventId;
    		
    	private final static long serialVersionUID = 1L;
    	private final static String PREFIX			= "Warmup_"; 
    	
    	
    	public InWarmupEvent( ){
    		super( InType.WARM_UP_EVENT );
    		
    		this.eventId	= PREFIX + getSequenceId(); 
    	}
    	
    	
    	@Override
    	public final String getEventId( ){
    		return eventId;
    	}

    	
    	@Override
    	protected final void toJSON( JsonObject object ){}

    }
    
    
    private static final class OutWarmupEvent extends OutEvent{
    	
    	private final String eventId;
    		
    	private final static long serialVersionUID = 1L;
    	private final static String PREFIX			= "WarmupOut_"; 
    	

    	public OutWarmupEvent( ){
    		super( OutType.WARM_UP_EVENT );
    		
    		this.eventId	  	= PREFIX + getSequenceId(); 
    	}
    	
    	
    	@Override
    	public final String getEventId( ){
    		return eventId;
    	}

    	
    	@Override
    	protected final void toJSON( JsonObject object ) {}
    	
    	

    }
}

