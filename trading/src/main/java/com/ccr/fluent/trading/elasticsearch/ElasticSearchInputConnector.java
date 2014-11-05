package com.ccr.fluent.trading.elasticsearch;

import org.slf4j.*;

import com.ccr.fluent.trading.core.FluentService;
import com.ccr.fluent.trading.dispatcher.in.InputEventDispatcher;
import com.ccr.fluent.trading.events.core.FluentInputEvent;
import com.ccr.fluent.trading.events.core.FluentInputEventListener;
import com.ccr.fluent.trading.events.core.FluentInputEventType;

import static com.ccr.fluent.trading.utility.ContainerUtil.*;


public class ElasticSearchInputConnector implements FluentService, FluentInputEventListener{
	
	private final AbstractESClient esClient;
    private final static String NAME        = ElasticSearchInputConnector.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );


    public ElasticSearchInputConnector( AbstractESClient esClient ){
        this.esClient = esClient;
    }

	@Override
	public final String name( ){
		return NAME;
	}


    @Override
    public final void init( ){
    	InputEventDispatcher.add( this );
    }


    @Override
    public final boolean isSupported( final FluentInputEventType type ){
        return true;
    }


    @Override
    public final void update( final FluentInputEvent event ){

        //Send the event to trader via TIBCO?
        //Send the event to the LOGGER, perhaps we can split it inot two classes??
        indexInput( event );

    }


	public final void indexInput( final FluentInputEvent ... events ){
		
		for( FluentInputEvent event : events ){
            indexInput( event );
		}
	
	}

	
	
	public final void indexInput( FluentInputEvent event ){
		
		try{
			
			if( event == null ) return;
			
			//TEST TODO
			//if( event.getCategory() == SmartEventCategory.MARKET_CATEGORY ) return;
						
			String eventId			= String.valueOf( event.getEventId() );
			String message			= event.toJSON();
			if( isBlank(message) ) return;

            boolean indexed         = esClient.indexMessage( eventId, message );
            if( indexed ) LOGGER.debug("Successfully indexed {}", message );
			
		}catch( Exception e ){
			LOGGER.warn("Failed to index data {}", event );
			LOGGER.warn("Exception:", e);
		}
		
	}

	
	@Override
	public void stop(){
		
	}


}
