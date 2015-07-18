package com.fluent.framework.market;

import com.eclipsesource.json.*;
import com.fluent.framework.events.in.*;

import static com.fluent.framework.util.FluentUtil.*;
import static com.fluent.framework.events.in.InType.*;
import static com.fluent.framework.events.core.FluentJsonTags.*;


public final class MarketDataEvent extends InEvent{

	private final String eventId;
	private final Exchange exchange;
    private final String symbol;
    
    private final double bid;
    private final int bidSize;
    private final double ask;
    private final int askSize;

    private final static long serialVersionUID = 1l;
    
    
    public MarketDataEvent( Exchange exchange, String symbol, double bid, int bidSize, double ask, int askSize ){
    	
    	super( MARKET_DATA );

    	this.eventId	= exchange + DOT + symbol + DOT + getSequenceId();
        this.exchange	= exchange;
        this.symbol   	= symbol;
        
        this.bid      	= bid;
        this.bidSize   	= bidSize;
        this.ask       	= ask;
        this.askSize   	= askSize;
        
    }


    @Override
    public final String getEventId( ){
        return eventId;
    }

    public final String getSymbol( ){
        return symbol;
    }
    
    
    public final Exchange getExchange( ){
        return exchange;
    }


    public final double getBid( ){
        return bid;
    }

    
    public final int getBidSize( ){
        return bidSize;
    }

    
    public final double getAsk( ){
        return ask;
    }

    
    public final int getAskSize( ){
        return askSize;
    }

        
    @Override
    protected final void toJSON( JsonObject object ){

        object.add( EXCHANGE.field(),	exchange.name() );
        object.add( SYMBOL.field(),		symbol );
        object.add( BID.field(),        bid );
        object.add( BIDSIZE.field(),    bidSize );
        object.add( ASK.field(),        ask );
        object.add( ASKSIZE.field(),    askSize );

    }
   
    
    public final String toCompactString( ){

    	StringBuilder builder = new StringBuilder( );
    	builder.append( bidSize ).append( X_SPACE ).append( bid );
    	builder.append( TAB );
    	builder.append( askSize ).append( X_SPACE ).append( ask );
    	
        return builder.toString();
    }
    
    
    @Override
    public final String toString( ){

    	StringBuilder builder = new StringBuilder( );
    	builder.append( eventId ).append( COMMA );
    	builder.append( getType() ).append( COMMA );
    	builder.append( symbol ).append( COMMA );
    	builder.append( bidSize ).append( X_SPACE ).append( bid );
    	builder.append( TAB );
    	builder.append( askSize ).append( X_SPACE ).append( ask );
    	
        return builder.toString();
    }
   

}