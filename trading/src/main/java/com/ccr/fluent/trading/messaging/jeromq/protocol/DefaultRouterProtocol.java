package com.ccr.fluent.trading.messaging.jeromq.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZFrame;
import org.zeromq.ZMsg;

import com.ccr.fluent.trading.messaging.jeromq.core.*;

import static com.ccr.fluent.trading.utility.ContainerUtil.*;
import static com.ccr.fluent.trading.messaging.jeromq.core.ZMessageType.*;


public class DefaultRouterProtocol implements RouterProtocol{

    private final String version;

    private final static String NAME        = DefaultRouterProtocol.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );

    public DefaultRouterProtocol( String version ){
        this.version = version;
    }


    @Override
    public final String getVersion(){
        return version;
    }


    /**
     * A connect command contains 4 frames
     * Frame 0 : ClientId
     * Frame 1 : Empty
     * Frame 2 : Protocol Version String
     * Frame 3 : Byte value representing HEARTBEAT
     */
    @Override
    public final ZMsg heartbeat( final String clientId ){
        ZMsg message = new ZMsg();

        message.add( clientId );
        message.add( EMPTY_BYTE );
        message.add( version );
        message.add( HEARTBEAT.getData() );

        return message;
    }


    @Override
    public final ZMsg marshall( final String clientId, final byte[] data ){
        ZMsg message = new ZMsg();

        message.add( clientId );
        message.add( EMPTY_BYTE );
        message.add( version );
        message.add( PAYLOAD.getData() );
        message.add( EMPTY_BYTE );
        message.add( data );

        return message;
    }


    @Override
    public final MajordomoMessage unmarshall( final ZMsg message ){

        String version          = null;
        String clientId         = null;
        MajordomoMessage msg    = null;
        ZMessageType type       = ZMessageType.INVALID;

        try{

            ZFrame idFrame      = message.pop();
            @SuppressWarnings("unused")
            ZFrame emnptyFrame  = message.pop();
            ZFrame versionFrame = message.pop();
            ZFrame typeFrame    = message.pop();


            version             = new String( versionFrame.getData() );
            clientId            = new String( idFrame.getData() );
            type                = ZMessageType.getType( typeFrame );
            byte[] dataBytes    = null;

            if( PAYLOAD == type ){
                @SuppressWarnings("unused")
                ZFrame envelope = message.pop();
                dataBytes       = message.pop().getData();
            }

            msg                 = new MajordomoMessage( clientId, version, type, dataBytes );
            
        }catch( Exception e ){
            LOGGER.warn( "Exception while parsing message from [{}]", clientId );
            LOGGER.warn( "Exception:", e);
        }

        return msg;
        
    }


    @Override
    public final ZMsg disconnect( final String clientId ){
        ZMsg message = new ZMsg();

        message.add( clientId );
        message.add( EMPTY_BYTE );
        message.add( version );
        message.add( DISCONNECT.getData() );

        return message;

    }


}
