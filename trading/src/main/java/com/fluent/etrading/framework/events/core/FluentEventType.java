package com.fluent.etrading.framework.events.core;


public interface FluentEventType{

    public String getName( );
    public String getDescription( );
    public FluentEventCategory getCategory( );

}