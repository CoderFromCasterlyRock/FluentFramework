package com.fluent.framework.market.core;

import java.util.*;

import static com.fluent.framework.util.FluentUtil.*;


public enum Exchange {

    CME( "CME", false ),
    BTEC( "BTEC", false ),
    ESPEED( "ESPEED", false ),
    CASH_SMART_ROUTER( "CASH_SMART", true, BTEC, ESPEED ),

    ISWAP( "ISWAP", false ),
    DWEB( "DWEB", false ),
    TRADITION( "TRADITION", false ),
    SWAP_SMART_ROUTER( "SWAP_SMART", true, ISWAP, DWEB, TRADITION ),

    UNSUPPORTED( EMPTY, false );


    private final String                       code;
    private final boolean                      isAggregate;
    private final Exchange[ ]                  underlying;

    private final static Map<String, Exchange> MAP;

    static{
        MAP = new HashMap<>( );
        for( Exchange exchange : Exchange.values( ) ){
            MAP.put( exchange.code, exchange );
            MAP.put( exchange.name( ), exchange );
        }
    }


    private Exchange( String code, boolean isAggregate, Exchange ... underlying ){
        this.code = code;
        this.isAggregate = isAggregate;
        this.underlying = underlying;
    }


    public final String getCode( ) {
        return code;
    }


    public final boolean isAggregate( ) {
        return isAggregate;
    }


    public final Exchange[ ] getUnderlying( ) {
        return underlying;
    }


    public final static Exchange fromCode( String codeName ) {
        Exchange exchange = MAP.get( codeName );
        return (exchange == null) ? UNSUPPORTED : exchange;
    }


}
