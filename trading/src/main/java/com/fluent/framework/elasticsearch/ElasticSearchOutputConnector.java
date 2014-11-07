package com.fluent.framework.elasticsearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fluent.framework.core.FluentService;
import com.fluent.framework.dispatcher.out.OutputEventDispatcher;
import com.fluent.framework.events.core.FluentOutputEvent;
import com.fluent.framework.events.core.FluentOutputEventListener;
import com.fluent.framework.events.core.FluentOutputEventType;


public class ElasticSearchOutputConnector implements FluentService, FluentOutputEventListener{

	private final AbstractESClient esClient;
    private final static String NAME        = ElasticSearchOutputConnector.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );

    public ElasticSearchOutputConnector( AbstractESClient esClient ){
        this.esClient = esClient;
    }

    @Override
	public final String name( ){
		return NAME;
	}


    @Override
    public final void init( ){
        OutputEventDispatcher.add( this );
    }


    @Override
    public final boolean isSupported( final FluentOutputEventType type ){
        return ( FluentOutputEventType.EVENT_TO_TRADER == type );
    }


    @Override
    public final void update( final FluentOutputEvent event ){

        FluentOutputEventType type = event.getType();

        switch( type ){

            case EVENT_TO_TRADER:
                indexOutput( event );
                break;

            default:
                LOGGER.warn( "Discarding output event as type[{}] is unsupported.", type );

        }

    }


    public final void indexOutput( final FluentOutputEvent ... events ){
		for( FluentOutputEvent event : events ){
            indexOutput( event );
		}
	}


	public final void indexOutput( FluentOutputEvent event ){
		
		try{
			
			if( event == null ) return;
			
			String eventId		= String.valueOf( event.getEventId() );
			String message		= event.toJSON( );
            boolean indexed     = esClient.indexMessage( eventId, message );

            if( indexed )  LOGGER.debug( "INDEXED >> {}", message );

		}catch( Exception e ){
			LOGGER.warn("Failed to index data {}", event );
			LOGGER.warn("Exception:", e);
		}
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}


}
