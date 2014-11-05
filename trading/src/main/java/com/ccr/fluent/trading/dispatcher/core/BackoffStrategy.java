package com.ccr.fluent.trading.dispatcher.core;


public interface BackoffStrategy{

    public long getBackoffTime();
    public String description( );
    public void apply( ) throws InterruptedException;

}
