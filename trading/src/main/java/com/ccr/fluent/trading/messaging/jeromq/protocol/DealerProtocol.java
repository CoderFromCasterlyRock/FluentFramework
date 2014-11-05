package com.ccr.fluent.trading.messaging.jeromq.protocol;

import org.zeromq.ZMsg;

import com.ccr.fluent.trading.messaging.jeromq.core.*;



public interface DealerProtocol extends Protocol{

    ZMsg connect();
    ZMsg heartbeat();
    ZMsg marshall( byte[] data );
    MajordomoMessage unmarshall( String identity, ZMsg message );
    ZMsg disconnect();

}
