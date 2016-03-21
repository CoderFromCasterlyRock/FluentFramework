package com.fluent.framework.transport.jeromq.core;

import org.zeromq.*;

import java.util.*;

import static com.fluent.framework.util.FluentUtil.*;


public enum ZMessageType {

    INVALID( ZERO, new byte[ ]{ZERO} ),
    HEARTBEAT( ONE, new byte[ ]{ONE} ),
    CONNECT( TWO, new byte[ ]{TWO} ),
    PAYLOAD( THREE, new byte[ ]{THREE} ),
    DISCONNECT( FOUR, new byte[ ]{FOUR} );

    private final int                               code;
    private final byte[ ]                           data;

    private final static Map<Integer, ZMessageType> _M_;

    static{
        _M_ = new HashMap<Integer, ZMessageType>( );

        for( ZMessageType type : ZMessageType.values( ) ){
            _M_.put( type.code, type );
        }

    }


    private ZMessageType( int code, byte[ ] data ){
        this.code = code;
        this.data = data;
    }


    public final int getCode( ) {
        return code;
    }


    public final byte[ ] getData( ) {
        return data;
    }


    public final static ZMessageType getType( ZFrame frame ) {
        if( frame == null )
            return INVALID;

        int codeType = frame.getData( )[ ZERO ];
        ZMessageType type = _M_.get( codeType );

        return (type == null) ? INVALID : type;
    }

}
