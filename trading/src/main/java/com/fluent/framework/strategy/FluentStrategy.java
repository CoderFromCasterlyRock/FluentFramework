package com.fluent.framework.strategy;

import com.fluent.framework.core.*;
import com.fluent.framework.events.in.*;


public abstract class FluentStrategy implements FluentLifecycle{

    private final String       strategyId;
    private final StrategyType strategyType;

    public FluentStrategy( String strategyId, StrategyType strategyType ){
        this.strategyId = strategyId;
        this.strategyType = strategyType;

    }


    public abstract void update( FluentInEvent event );


    public final String getStrategyId( ) {
        return strategyId;
    }


    public final StrategyType getStrategyType( ) {
        return strategyType;
    }



    protected final boolean marketUpdateRequired( String instrument, String[ ] legInstruments ) {
        for( String legInstrument : legInstruments ){
            if( legInstrument.equalsIgnoreCase( instrument ) ){
                return true;
            }
        }

        return false;
    }


}
