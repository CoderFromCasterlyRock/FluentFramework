package com.fluent.framework.market.event;

import com.fluent.framework.events.in.*;
import com.fluent.framework.market.core.Exchange;
import com.fluent.framework.market.core.InstrumentSubType;
import com.fluent.framework.market.core.InstrumentType;

import static com.fluent.framework.util.FluentUtil.*;
import static com.fluent.framework.events.in.InType.*;


public final class MarketDataEvent extends InEvent{

	private final Exchange exchange;
    private final String symbol;
    private final InstrumentSubType subType;
        
    private final double bid;
    private final int bidSize;
    private final double ask;
    private final int askSize;

    private final static long serialVersionUID = 1l;
    
    
    public MarketDataEvent( Exchange exchange, InstrumentSubType subType, String symbol, double bid, int bidSize, double ask, int askSize ){
    	
    	super( MARKET_DATA );

        this.exchange	= exchange;
        this.subType	= subType;
        this.symbol   	= symbol;
        
        this.bid      	= bid;
        this.bidSize   	= bidSize;
        this.ask       	= ask;
        this.askSize   	= askSize;
        
    }


    public final String getSymbol( ){
        return symbol;
    }
    
    
    public final Exchange getExchange( ){
        return exchange;
    }
    
    
    public final InstrumentType getInstrumentType( ){
        return subType.getType();
    }
    
    
    public final InstrumentSubType getInstrumentSubType( ){
        return subType;
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
  
    
    public final String toCompactString( ){

    	StringBuilder builder = new StringBuilder( );
    	builder.append( symbol ).append( COLON );
    	builder.append( bidSize ).append( X_SPACE ).append( bid );
    	builder.append( TAB );
    	builder.append( askSize ).append( X_SPACE ).append( ask );
    	
        return builder.toString();
    }
    
    
    @Override
    public final void toEventString( StringBuilder builder ){
    	builder.append( symbol ).append( COMMA );
    	builder.append( bidSize ).append( X_SPACE ).append( bid );
    	builder.append( TAB );
    	builder.append( askSize ).append( X_SPACE ).append( ask );
    	builder.append( TAB );
    	builder.append( subType );
    	
    }
   

}