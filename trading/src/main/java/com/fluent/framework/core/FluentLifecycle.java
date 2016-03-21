package com.fluent.framework.core;


public interface FluentLifecycle{

    public String name( );

    public void start( ) throws FluentException;

    public void stop( ) throws FluentException;

}
