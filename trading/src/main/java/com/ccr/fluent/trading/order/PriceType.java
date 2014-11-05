package com.ccr.fluent.trading.order;


public enum PriceType{

    PASSIVE,
    AGGRESSIVE,
    UNDETERMINED;


    public static final PriceType get( Side side, double givenPrice, double bestPrice ){

        switch( side ){

            case BUY:
                return ( givenPrice >= bestPrice ) ? AGGRESSIVE : PASSIVE;

            case SELL:
                return ( givenPrice >= bestPrice ) ? PASSIVE    : AGGRESSIVE;
                
            case UNKNOWN:
            	return UNDETERMINED;
            	
        }

        return UNDETERMINED;

    }


}
