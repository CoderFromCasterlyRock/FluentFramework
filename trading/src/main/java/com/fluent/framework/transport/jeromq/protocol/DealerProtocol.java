package com.fluent.framework.transport.jeromq.protocol;

import org.zeromq.ZMsg;

import com.fluent.framework.transport.jeromq.core.*;



public interface DealerProtocol extends Protocol{

    ZMsg connect();
    ZMsg heartbeat();
    ZMsg marshall( byte[] data );
    MajordomoMessage unmarshall( String identity, ZMsg message );
    ZMsg disconnect();

}
