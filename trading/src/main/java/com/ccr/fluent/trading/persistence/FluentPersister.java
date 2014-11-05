package com.ccr.fluent.trading.persistence;

import java.util.List;

import com.ccr.fluent.trading.core.FluentService;
import com.ccr.fluent.trading.events.core.FluentEvent;


public interface FluentPersister<_E_ extends FluentEvent> extends FluentService{

    public boolean persist( _E_ event );
    public void persistAll( _E_ ... events );
    public List<_E_> retrieveAll( );


}
