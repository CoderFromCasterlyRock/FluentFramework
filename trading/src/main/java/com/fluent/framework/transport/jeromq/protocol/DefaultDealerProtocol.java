package com.fluent.framework.transport.jeromq.protocol;

import org.slf4j.*;
import org.zeromq.*;

import com.fluent.framework.transport.jeromq.core.*;

import static com.fluent.framework.transport.jeromq.core.ZMessageType.*;
import static com.fluent.framework.util.FluentUtil.*;


public class DefaultDealerProtocol implements DealerProtocol{

    private final String        version;

    private final static String NAME   = DefaultDealerProtocol.class.getSimpleName( );
    private final static Logger LOGGER = LoggerFactory.getLogger( NAME );

    public DefaultDealerProtocol( String version ){
        this.version = version;
    }

    @Override
    public final String getVersion( ) {
        return version;
    }


    /**
     * A connect command contains 3 frames Frame 0 : Empty Frame 1 : Protocol Version String Frame 2
     * : Byte value representing CONNECT
     */
    @Override
    public final ZMsg connect( ) {
        ZMsg message = new ZMsg( );

        message.add( EMPTY_BYTE );
        message.add( version );
        message.add( CONNECT.getData( ) );

        return message;
    }


    @Override
    public final ZMsg heartbeat( ) {
        ZMsg message = new ZMsg( );

        message.add( EMPTY_BYTE );
        message.add( version );
        message.add( HEARTBEAT.getData( ) );

        return message;
    }


    @Override
    public final ZMsg marshall( final byte[ ] data ) {
        ZMsg message = new ZMsg( );

        message.add( EMPTY_BYTE );
        message.add( version );
        message.add( PAYLOAD.getData( ) );
        message.add( EMPTY_BYTE );
        message.add( data );

        return message;
    }


    @Override
    public final MajordomoMessage unmarshall( final String identity, final ZMsg message ) {

        String version = null;
        MajordomoMessage msg = null;
        ZMessageType type = ZMessageType.INVALID;

        try{

            @SuppressWarnings( "unused" )
            ZFrame emnptyFrame = message.pop( );
            ZFrame versionFrame = message.pop( );
            ZFrame typeFrame = message.pop( );

            version = new String( versionFrame.getData( ) );
            type = ZMessageType.getType( typeFrame );
            byte[ ] dataBytes = null;

            if( PAYLOAD == type ){
                @SuppressWarnings( "unused" )
                ZFrame envelope = message.pop( );
                dataBytes = message.pop( ).getData( );
            }

            msg = new MajordomoMessage( identity, version, type, dataBytes );

        }catch( Exception e ){
            LOGGER.warn( "Exception while parsing message from [{}]", identity );
            LOGGER.warn( "Exception:", e );
        }

        return msg;

    }

    @Override
    public final ZMsg disconnect( ) {
        ZMsg message = new ZMsg( );

        message.add( EMPTY_BYTE );
        message.add( version );
        message.add( DISCONNECT.getData( ) );

        return message;
    }

}
