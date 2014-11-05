package com.ccr.fluent.trading.messaging.jeromq.protocol;

import org.zeromq.ZMsg;

import com.ccr.fluent.trading.messaging.jeromq.core.*;


public interface RouterProtocol extends Protocol{

    ZMsg heartbeat( String clientId );
    ZMsg marshall( String clientId, byte[] data );
    MajordomoMessage unmarshall( ZMsg message );
    ZMsg disconnect( String clientId );

}
