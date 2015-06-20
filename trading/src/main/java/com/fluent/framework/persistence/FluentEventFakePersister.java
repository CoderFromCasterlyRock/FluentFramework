package com.fluent.framework.persistence;

import java.util.*;
import org.slf4j.*;

import com.fluent.framework.events.core.*;

import static com.fluent.framework.util.FluentUtil.*;


public final class FluentEventFakePersister implements FluentPersister{

	private final static String NAME		= FluentEventFakePersister.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );

    
	@Override
    public final String name( ){
        return NAME;
    }


    @Override
    public final void init( ){
        LOGGER.info("Fake persister, will simply discard calls to persist.");
    }
    
    
    @Override
	public final String getFileName(){
		return EMPTY;
	}
    
    @Override
    public final void persistAll( FluentEvent ... events ){}
    

    @Override
    public final void persist( final FluentEvent event ){}

    
    @Override
    public final Collection<FluentEvent> retrieveAllEvents( ){
    	return retrieveAll().values();
    }
    
    
    @Override
    public final Map<Long, FluentEvent> retrieveAll( ){
    	return Collections.emptyMap();
    }
    
    
    @Override
    public final void stop( ){}

}
