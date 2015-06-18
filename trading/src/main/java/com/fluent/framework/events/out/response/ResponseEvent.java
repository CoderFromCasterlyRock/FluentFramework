package com.fluent.framework.events.out.response;

import com.eclipsesource.json.*;
import com.fluent.framework.events.core.*;

import static com.fluent.framework.events.core.FluentJsonTags.*;
import static com.fluent.framework.events.core.FluentOutboundType.*;


public final class ResponseEvent extends FluentOutboundEvent{

	private final String eventId;
	private final String strategyId;
    private final String orderId;
    private final String message;

    private final static String PREFIX = "RESPONSE_";
    
    public ResponseEvent( String strategyId, String orderId, String message ){
        super( EVENT_TO_TRADER );

        this.eventId     	= PREFIX + getSequenceId();
        this.strategyId     = strategyId;
        this.orderId    	= orderId;
        this.message    	= message;
        
    }


    @Override
    public final String getEventId( ){
        return eventId;
    }

    
    public final String getStrategyId( ){
        return strategyId;
    }
    
    
    public final String getOrderId( ){
        return orderId;
    }
    

    public final String getMessage( ){
        return message;
    }

    
    @Override
    protected final String toJSON( final JsonObject object ){
        
    	object.add( STRATEGY_ID.field(),     strategyId );
    	object.add( ORDER_ID.field(),  		 orderId );
    	object.add( UPDATE_MESSAGE.field(),  message );
        
        return object.toString();

    }

}
