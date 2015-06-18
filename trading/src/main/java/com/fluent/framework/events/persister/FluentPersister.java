package com.fluent.framework.events.persister;

import java.util.*;

import com.fluent.framework.core.*;
import com.fluent.framework.events.core.*;


public interface FluentPersister extends FluentService{

	public String getFileName();
	public void persist( FluentEvent event );
    public void persistAll( FluentEvent ... events );
    
    public Collection<FluentEvent> retrieveAllEvents( );
    public Map<Long, FluentEvent> retrieveAll( );

    
}
