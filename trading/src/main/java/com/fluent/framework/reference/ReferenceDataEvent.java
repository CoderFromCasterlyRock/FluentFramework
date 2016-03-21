package com.fluent.framework.reference;

import com.fluent.framework.events.in.*;
import com.fluent.framework.market.core.*;


public final class ReferenceDataEvent extends FluentInEvent{

    private final String      symbol;
    private final Exchange    exchange;

    private static final long serialVersionUID = 1L;


    public ReferenceDataEvent( String symbol, Exchange exchange ){
        super( FluentInType.REFERENCE_DATA );

        this.symbol = symbol;
        this.exchange = exchange;
    }


    public final String getSymbol( ) {
        return symbol;
    }

    public final Exchange getExchange( ) {
        return exchange;
    }


    @Override
    public final void toEventString( StringBuilder builder ) {

    }



}
