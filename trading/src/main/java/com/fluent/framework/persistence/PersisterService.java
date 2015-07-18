package com.fluent.framework.persistence;

import java.util.*;

import com.fluent.framework.core.*;
import com.fluent.framework.events.core.FluentEvent;


public interface PersisterService<E extends FluentEvent> extends FluentService{

	public boolean persistEvent( E event );
    public List<E> retrieveAll( );

    
}
