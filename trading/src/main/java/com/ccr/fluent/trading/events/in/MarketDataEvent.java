package com.ccr.fluent.trading.events.in;

import com.eclipsesource.json.JsonObject;

import com.ccr.fluent.trading.market.core.MarketType;
import com.ccr.fluent.trading.events.core.FluentInputEvent;
import com.ccr.fluent.trading.events.core.FluentInputEventType;

import static com.ccr.fluent.trading.utility.JSONUtil.*;
import static com.ccr.fluent.trading.utility.ContainerUtil.*;
import static com.ccr.fluent.trading.events.core.FluentJsonTags.*;


public final class MarketDataEvent extends FluentInputEvent{

    private final MarketType market;
    private final String instrumentId;
    private final boolean isCompact;
    
    private final double bid0;
    private final double bid1;
    private final double bid2;
    private final double bid3;
    private final double bid4;

    private final int bid0Size;
    private final int bid1Size;
    private final int bid2Size;
    private final int bid3Size;
    private final int bid4Size;

    private final double ask0;
    private final double ask1;
    private final double ask2;
    private final double ask3;
    private final double ask4;

    private final int ask0Size;
    private final int ask1Size;
    private final int ask2Size;
    private final int ask3Size;
    private final int ask4Size;

    
    public MarketDataEvent( long eventId, FluentInputEventType type, MarketType market, String instrumentId,
    						double bid, int bidSize, double ask, int askSize ){
    	
    	this( 	true, eventId, type, market, instrumentId, 
    			bid, ZERO_DOUBLE, ZERO_DOUBLE, ZERO_DOUBLE, ZERO_DOUBLE,
    			bidSize, ZERO, ZERO, ZERO, ZERO,
    			ask, ZERO_DOUBLE, ZERO_DOUBLE, ZERO_DOUBLE, ZERO_DOUBLE,
    			askSize, ZERO, ZERO, ZERO, ZERO	);
    	
    }

    
    public MarketDataEvent( long eventId, FluentInputEventType type, MarketType market, String instrumentId,
    						double bid0, double bid1, double bid2, double bid3, double bid4,
    						int bid0Size, int bid1Size, int bid2Size, int bid3Size, int bid4Size,
    						double ask0, double ask1, double ask2, double ask3, double ask4,
    						int ask0Size, int ask1Size, int ask2Size, int ask3Size, int ask4Size ){
    	
    	this( 	false, eventId, type, market, instrumentId, 
    			bid0, bid1, bid2, bid3, bid4,
    			bid0Size, bid1Size, bid2Size, bid3Size, bid4Size,
    			ask0, ask1, ask2, ask3, ask4,
    			ask0Size, ask1Size, ask2Size, ask3Size, ask4Size );
    	
    }


    protected MarketDataEvent( boolean isCompact, long eventId, FluentInputEventType type, MarketType market, String instrumentId,
                            	double bid0, double bid1, double bid2, double bid3, double bid4,
                            	int bid0Size, int bid1Size, int bid2Size, int bid3Size, int bid4Size,
                            	double ask0, double ask1, double ask2, double ask3, double ask4,
                            	int ask0Size, int ask1Size, int ask2Size, int ask3Size, int ask4Size ){

        super( eventId, type );

        this.market         = market;
        this.instrumentId   = instrumentId;
        this.isCompact		= isCompact;
        
        this.bid0           = bid0;
        this.bid1           = bid1;
        this.bid2           = bid2;
        this.bid3           = bid3;
        this.bid4           = bid4;

        this.bid0Size       = bid0Size;
        this.bid1Size       = bid1Size;
        this.bid2Size       = bid2Size;
        this.bid3Size       = bid3Size;
        this.bid4Size       = bid4Size;

        this.ask0           = ask0;
        this.ask1           = ask1;
        this.ask2           = ask2;
        this.ask3           = ask3;
        this.ask4           = ask4;

        this.ask0Size       = ask0Size;
        this.ask1Size       = ask1Size;
        this.ask2Size       = ask2Size;
        this.ask3Size       = ask3Size;
        this.ask4Size       = ask4Size;

    }


    public final long getMarketDataId( ){
        return getEventId();
    }

    public final String getInstrumentId( ){
        return instrumentId;
    }
    
    public final boolean isCompact( ){
        return isCompact;
    }


    public final MarketType getMarket( ){
        return market;
    }

    public final double getBid0( ){
        return bid0;
    }

    public final double getBid1( ){
        return bid1;
    }

    public final double getBid2( ){
        return bid2;
    }

    public final double getBid3( ){
        return bid3;
    }

    public final double getBid4( ){
        return bid4;
    }

    public final int getBid0Size( ){
        return bid0Size;
    }

    public final int getBid1Size( ){
        return bid1Size;
    }

    public final int getBid2Size( ){
        return bid2Size;
    }

    public final int getBid3Size( ){
        return bid3Size;
    }

    public final int getBid4Size( ){
        return bid4Size;
    }

    public final double getAsk0( ){
        return ask0;
    }

    public final double getAsk1( ){
        return ask1;
    }

    public final double getAsk2( ){
        return ask2;
    }

    public final double getAsk3( ){
        return ask3;
    }

    public final double getAsk4( ){
        return ask4;
    }

    public final int getAsk0Size( ){
        return ask0Size;
    }

    public final int getAsk1Size( ){
        return ask1Size;
    }

    public final int getAsk2Size( ){
        return ask2Size;
    }

    public final int getAsk3Size( ){
        return ask3Size;
    }

    public final int getAsk4Size( ){
        return ask4Size;
    }

    
    @Override
    protected final String toJSON( JsonObject object ){

        object.add( MARKET_DATA_ID.field(), getMarketDataId() );
        object.add( MARKET.field(),         getMarket().name() );
        object.add( INSTRUMENT_NAME.field(),getInstrumentId() );
        object.add( BID0.field(),           getBid0() );
        object.add( BID1.field(),           getBid1() );
        object.add( BID2.field(),           getBid2() );
        object.add( BID3.field(),           getBid3() );
        object.add( BID4.field(),           getBid4() );

        object.add( BID0SIZE.field(),       getBid0Size() );
        object.add( BID1SIZE.field(),       getBid1Size() );
        object.add( BID2SIZE.field(),       getBid2Size() );
        object.add( BID3SIZE.field(),       getBid3Size() );
        object.add( BID4SIZE.field(),       getBid4Size() );

        object.add( ASK0.field(),           getAsk0() );
        object.add( ASK1.field(),           getAsk1() );
        object.add( ASK2.field(),           getAsk2() );
        object.add( ASK3.field(),           getAsk3() );
        object.add( ASK4.field(),           getAsk4() );

        object.add( ASK0SIZE.field(),       getAsk0Size() );
        object.add( ASK1SIZE.field(),       getAsk1Size() );
        object.add( ASK2SIZE.field(),       getAsk2Size() );
        object.add( ASK3SIZE.field(),       getAsk3Size() );
        object.add( ASK4SIZE.field(),       getAsk4Size() );
        
        return object.toString();

    }
    

    public final static MarketDataEvent convert( final String jsonString, final JsonObject object ){

        return new MarketDataEvent(
                valueAsLong(EVENT_ID, object),
                valueAsInputType( object ),
                MarketType.valueOf( valueAsString( MARKET, object ) ),
                valueAsString( INSTRUMENT_NAME, object ),
                valueAsDouble( BID0, object ),
                valueAsDouble( BID1, object ),
                valueAsDouble( BID2, object ),
                valueAsDouble( BID3, object ),
                valueAsDouble( BID4, object ),
                valueAsInt( BID0SIZE, object ),
                valueAsInt( BID1SIZE, object ),
                valueAsInt( BID2SIZE, object ),
                valueAsInt( BID3SIZE, object ),
                valueAsInt( BID4SIZE, object ),
                valueAsDouble(ASK0, object ),
                valueAsDouble(ASK1, object ),
                valueAsDouble(ASK2, object ),
                valueAsDouble(ASK3, object ),
                valueAsDouble(ASK4, object ),
                valueAsInt( ASK0SIZE, object ),
                valueAsInt( ASK1SIZE, object ),
                valueAsInt( ASK2SIZE, object ),
                valueAsInt( ASK3SIZE, object ),
                valueAsInt( ASK4SIZE, object ) );

    }

}