package com.fluent.framework.strategy;

import static com.fluent.framework.util.FluentToolkit.*;


public enum StrategyType {

    SLS( "Single Leg Execution" ),
    VLS( "Variable Leg Spreader" ),
    OCO( "OCO Strategy" ),
    UNSUPPORTED( "OCO Strategy" );

    private final String description;


    private StrategyType( String description ){
        this.description = description;
    }

    public final String getDescription( ) {
        return description;
    }


    public final static StrategyType getType( String name ) {
        if( isBlank( name ) )
            return UNSUPPORTED;

        for( StrategyType type : StrategyType.values( ) ){
            if( type.name( ).equalsIgnoreCase( name ) ){
                return type;
            }
        }

        return UNSUPPORTED;
    }

}
