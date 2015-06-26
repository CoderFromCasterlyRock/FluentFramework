package com.fluent.framework.persistence;

import java.util.*;

import com.fluent.framework.core.*;
import com.fluent.framework.events.core.*;


public interface Persister extends FluentStartable{

	public String getFileName();
	public void persist( FluentEvent event );
    public void persistAll( FluentEvent ... events );
    
    public Collection<FluentEvent> retrieveAllEvents( );
    public Map<Long, FluentEvent> retrieveAll( );

    
}
