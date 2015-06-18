package com.fluent.framework.transport.jeromq.transport;


public interface ZListener<DATA>{

    public String name();
    public void update( DATA data );

}
