package com.fluent.framework.messaging.jeromq.protocol;

import org.zeromq.ZMsg;

import com.fluent.framework.messaging.jeromq.core.*;



public interface DealerProtocol extends Protocol{

    ZMsg connect();
    ZMsg heartbeat();
    ZMsg marshall( byte[] data );
    MajordomoMessage unmarshall( String identity, ZMsg message );
    ZMsg disconnect();

}
