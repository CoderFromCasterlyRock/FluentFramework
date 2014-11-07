package com.fluent.etrading.framework.events.serializer;

import org.slf4j.*;

import com.eclipsesource.json.*;
import com.fluent.etrading.framework.events.core.*;
import com.fluent.etrading.framework.events.in.*;

import static com.fluent.etrading.framework.utility.JSONUtil.*;


public class InputEventSerializerManager{

    private final static Logger LOGGER = LoggerFactory.getLogger( InputEventSerializerManager.class.getSimpleName() );

    
    public final static FluentInputEvent convert( String jsonString ){

        FluentInputEvent inputEvent           = null;

        try{
            JsonObject object	            = JsonObject.readFrom( jsonString );
            FluentInputEventType type        = valueAsInputType( object );
            
            switch( type ){
            	
            	case ADMIN_EVENT:
            		return AdminEvent.convert(jsonString, object);
            		
            	case LOOPBACK_EVENT:
            		return LoopbackEvent.convert(jsonString, object);
            	
            	case TREASURY_MD:
            	case T_FUTURES_MD:
            	case ED_FUTURES_MD:
            		return MarketDataEvent.convert(jsonString, object);
            		            
            	case EXECUTION_REPORT_UPDATE:
            		return ExecutionReportEvent.convert(jsonString, object);
            		
            	case REFERENCE_DATA_UPDATE:
            		return ReferenceDataEvent.convert(jsonString, object);
            		
            	default:
            		LOGGER.warn("NO serializer defined for [{}]", type );
            
            }
            

        }catch(Exception e ){
            LOGGER.warn( "Exception while de-serializing Smart Input Event.", e );
        }

        return inputEvent;

    }
    
    
    /*
    private final static Map<SmartInputEventType, AbstractInputEventSerializer<? extends SmartInputEvent>> map;

    
    static{
        map = new HashMap<SmartInputEventType, AbstractInputEventSerializer<? extends SmartInputEvent>>( THIRTY_TWO );

        map.put( ADMIN_EVENT,               new AdminEventSerializer() );
        map.put( LOOPBACK_EVENT,            new LoopbackEventSerializer() );
       // map.put( CREATE_STRATEGY,           new CreateStrategyEventSerializer() );
        map.put( AMEND_STRATEGY,            null );
        map.put( CANCEL_STRATEGY,           null );
        map.put( CANCEL_ALL_STRATEGY,       null );

        map.put( TREASURY_MD,               new MarketDataEventSerializer() );
        map.put( T_FUTURES_MD,              new MarketDataEventSerializer() );
        map.put( ED_FUTURES_MD,             new MarketDataEventSerializer() );

        map.put( EXECUTION_REPORT_UPDATE,   new ExecutionEventSerializer() );
        map.put( REFERENCE_DATA_UPDATE,     null );

    }
	

    protected final static AbstractInputEventSerializer<? extends SmartInputEvent> retrieve( SmartInputEventType type ){
        return map.get( type );
    }


    public final static void register( SmartInputEventType type, AbstractInputEventSerializer<? extends SmartInputEvent> serializer ){
        map.put( type, serializer );
    }


    public final static String toJSON( SmartInputEvent event ){

        String jsonString   = EMPTY;

        try{
            SmartInputEventType eventType       = event.getType();
            AbstractInputEventSerializer ser = InputEventSerializerManager.retrieve( eventType );
            if( ser == null ){
            	LOGGER.warn( "AbstractInputEventSerializer is empty for {}.", eventType );
            	return EMPTY;
            }

            jsonString       	                = ser.toJson( event );

        }catch( Exception e ){
            LOGGER.warn( "Exception while serializing Smart Input Event.", e );
        }

        return jsonString;
    }
*/


    



}
