package com.fluent.etrading.framework.messaging.jeromq.protocol;

import org.zeromq.ZMsg;

import com.fluent.etrading.framework.messaging.jeromq.core.*;


public interface RouterProtocol extends Protocol{

    ZMsg heartbeat( String clientId );
    ZMsg marshall( String clientId, byte[] data );
    MajordomoMessage unmarshall( ZMsg message );
    ZMsg disconnect( String clientId );

}
