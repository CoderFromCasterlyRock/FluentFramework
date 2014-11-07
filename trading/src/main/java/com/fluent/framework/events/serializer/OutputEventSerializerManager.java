package com.fluent.framework.events.serializer;

import org.slf4j.*;

import com.eclipsesource.json.*;
import com.fluent.framework.events.core.*;
import com.fluent.framework.events.out.order.*;
import com.fluent.framework.events.out.response.*;

import static com.fluent.framework.utility.JSONUtil.*;


public class OutputEventSerializerManager{

    private final static String NAME    = OutputEventSerializerManager.class.getSimpleName();
    private final static Logger LOGGER  = LoggerFactory.getLogger( NAME );
    
    public final static FluentOutputEvent convert( String jsonString ){

    	FluentOutputEvent outputEvent        = null;

        try{
            JsonObject object	            = JsonObject.readFrom( jsonString );
            FluentOutputEventType type        = valueAsOutputType( object );
            
            switch( type ){
            	
            	case ORDER_TO_MARKET:
            		outputEvent = OutrightOrderEvent.convert(jsonString, object);
            		
            	case EVENT_TO_TRADER:
            		outputEvent = ResponseEvent.convert(jsonString, object);
            	         		
            	default:
            		LOGGER.warn("NO serializer defined for [{}]", type );
            
            }
            

        }catch(Exception e ){
            LOGGER.warn( "Exception while de-serializing Smart Input Event.", e );
        }

        return outputEvent;

    }
    
    
    /*
    private final static Map<SmartOutputEventType, AbstractOutputEventSerializer<? extends SmartOutputEvent>> MAP;

   
    static{
        MAP = new HashMap<SmartOutputEventType, AbstractOutputEventSerializer<? extends SmartOutputEvent>>( THIRTY_TWO );
        MAP.put( ORDER_TO_MARKET,    new OutrightEventSerializer() );
        MAP.put( EVENT_TO_TRADER,    new ResponseEventSerializer() );
    }
    

    public final static AbstractOutputEventSerializer<? extends SmartOutputEvent> retrieve( SmartOutputEventType type ){
        return MAP.get( type );
    }

    
    public final static void register( SmartOutputEventType type, AbstractOutputEventSerializer<? extends SmartOutputEvent> serializer ){
        MAP.put( type, serializer );
    }
	

    public final static String toJSON( SmartOutputEvent event ){

        String jsonString   = EMPTY;

        try{
            SmartOutputEventType eventType      = event.getType();
            AbstractOutputEventSerializer ser   = OutputEventSerializerManager.retrieve( eventType );
            if( ser == null ){
            	LOGGER.warn( "AbstractOutputEventSerializer is empty for {}.", eventType );
            	return EMPTY;
            }

            jsonString       	                = ser.toJson( event );

        }catch( Exception e ){
            LOGGER.warn( "Exception while serializing Smart Output Event.", e );
        }

        return jsonString;
    }

*/

    

    
    
}
