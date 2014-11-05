package com.ccr.fluent.trading.messaging.jeromq.transport;


public interface ZListener<DATA>{

    public String name();
    public void update( DATA data );

}
