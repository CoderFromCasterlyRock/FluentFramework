package com.fluent.framework.transport.jeromq.core;

import static com.fluent.framework.util.FluentUtil.*;


public final class MajordomoMessage{

    private final boolean hasData;
    private final String clientId;
    private final String version;
    private final ZMessageType type;
    private final byte[] data;


    public MajordomoMessage( String version, ZMessageType type, byte[] data ){
        this( EMPTY, version, type, data );
    }


    public MajordomoMessage( String clientId, String version, ZMessageType type, byte[] data ){

        this.clientId   = clientId;
        this.version    = version;
        this.type       = type;
        this.data       = data;
        this.hasData    = (ZMessageType.PAYLOAD == type && data != null );
    }


    public final boolean hasData(){
        return hasData;
    }


    public final String getClientId(){
        return clientId;
    }


    public final String getVersion(){
        return version;
    }


    public final ZMessageType getType(){
        return type;
    }


    public final byte[] getData(){
        return data;
    }

    public final int getDataSize(){
        return ( data == null ) ? ZERO : data.length;
    }


    @Override
    public final String toString(){
        StringBuilder builder = new StringBuilder( SIXTY_FOUR );

        builder.append( L_BRACKET );
        builder.append( clientId ).append( PIPE );
        builder.append( version ).append( PIPE );
        builder.append( type ).append( PIPE );
        builder.append( getDataSize() ).append( PIPE );
        builder.append( R_BRACKET );

        return builder.toString();

    }

}
