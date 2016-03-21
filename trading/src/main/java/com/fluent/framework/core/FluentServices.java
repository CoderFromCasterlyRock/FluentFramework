package com.fluent.framework.core;

import com.fluent.framework.admin.core.*;
import com.fluent.framework.events.in.*;
import com.fluent.framework.events.out.*;
import com.fluent.framework.market.adaptor.*;
import com.fluent.framework.persistence.*;


public final class FluentServices{

    private final FluentConfigManager          cfgManager;
    private final StateManager                 stateManager;
    private final FluentInEventDispatcher      inDispatcher;
    private final InChroniclePersisterService  inPersister;
    private final FluentOutEventDispatcher     outDispatcher;
    private final OutChroniclePersisterService outPersister;
    private final MarketDataManager            mdManager;


    public FluentServices( String configFileName ) throws FluentException{
        this.cfgManager = new FluentConfigManager( configFileName );

        this.inPersister = new InChroniclePersisterService( cfgManager );
        this.inDispatcher = new FluentInEventDispatcher( cfgManager );
        this.outPersister = new OutChroniclePersisterService( cfgManager );
        this.outDispatcher = new FluentOutEventDispatcher( cfgManager );

        this.stateManager = new StateManager( cfgManager, inDispatcher );
        this.mdManager = new MarketDataManager( cfgManager, inDispatcher );
    }


    public final FluentConfigManager getCfgManager( ) {
        return cfgManager;
    }


    public final StateManager getStateManager( ) {
        return stateManager;
    }

    public final FluentInEventDispatcher getInDispatcher( ) {
        return inDispatcher;
    }

    public final InChroniclePersisterService getInPersister( ) {
        return inPersister;
    }

    public final FluentOutEventDispatcher getOutDispatcher( ) {
        return outDispatcher;
    }

    public final OutChroniclePersisterService getOutPersister( ) {
        return outPersister;
    }

    public final MarketDataManager getMdManager( ) {
        return mdManager;
    }

    // private final OrderManager orderManager;
    // private final ReferenceDataManager refManager;


}
