package com.fluent.framework.events.in;

import java.util.*;

import com.fluent.framework.events.core.*;


public enum FluentInType implements FluentEventType {

    WARM_UP_EVENT,
    APP_STATE_EVENT,
    EXCHANGE_CLOSING,
    METRONOME_EVENT,

    NEW_STRATEGY,
    MODIFY_STRATEGY,
    CANCEL_STRATEGY,
    CANCEL_ALL_STRATEGY,
    PAUSE_STRATEGY,
    STOP_STRATEGY,

    MARKET_DATA,
    MARKET_STATUS,

    EXECUTION_REPORT,

    REFERENCE_DATA;


    private final static Set<FluentInType> ALL_TYPES;

    static{
        ALL_TYPES = new HashSet<FluentInType>( Arrays.asList( FluentInType.values( ) ) );
    }


    @Override
    public final boolean isIncoming( ) {
        return true;
    }


    public final static Set<FluentInType> allTypes( ) {
        return ALL_TYPES;
    }


}
