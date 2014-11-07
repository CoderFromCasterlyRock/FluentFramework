package com.fluent.etrading.framework.persistence;

import java.util.List;

import com.fluent.etrading.framework.core.FluentService;
import com.fluent.etrading.framework.events.core.FluentEvent;


public interface FluentPersister<_E_ extends FluentEvent> extends FluentService{

    public boolean persist( _E_ event );
    public void persistAll( _E_ ... events );
    public List<_E_> retrieveAll( );


}
