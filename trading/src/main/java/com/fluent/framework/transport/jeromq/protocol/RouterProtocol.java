package com.fluent.framework.transport.jeromq.protocol;

import org.zeromq.*;

import com.fluent.framework.transport.jeromq.core.*;


public interface RouterProtocol extends Protocol{

    ZMsg heartbeat( String clientId );

    ZMsg marshall( String clientId, byte[ ] data );

    MajordomoMessage unmarshall( ZMsg message );

    ZMsg disconnect( String clientId );

}
