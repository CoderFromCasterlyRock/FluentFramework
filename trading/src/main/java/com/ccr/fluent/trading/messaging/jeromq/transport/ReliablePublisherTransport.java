package com.ccr.fluent.trading.messaging.jeromq.transport;

import org.slf4j.*;
import org.zeromq.*;
import org.zeromq.ZMQ.*;

//TODO: Publisher should only start publishing when it explicitly gets a READY message via a dealer socket.
public class ReliablePublisherTransport extends AbstractTransport{

    private volatile boolean isAlive;

    private final Socket publisher;
    private final String address;
    
    private final static String NAME        = AbstractTransport.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );

    
    public ReliablePublisherTransport( boolean toLog, int hwm, int linger, String address, ZContext context ){
        super( toLog, hwm, linger, context );

        this.address    = address;
        this.publisher  = context.createSocket( ZMQ.PUB );
    }


    @Override
    public final String name( ){
        return NAME;
    }


    @Override
    public final void init(){
      
    	try{
        	publisher.setHWM( getHighWaterMark() );
            publisher.setLinger( getTimeToLinger() );
            publisher.bind( address );

            isAlive = true;
            LOGGER.info("Successfully started PUBLISHER transport [{}].", toString() );
            
        }catch( Exception e ){
            LOGGER.error("FAILED to start reliable transport service [{}].", toString() );
            LOGGER.error("Exception:", e);
        }

    }

    @Override
    public final void send( byte[] data ){
    	publisher.send(data);
    }
    
    @Override
    public final void send( String data ){
    	publisher.send(data);
    }
    

    @Override
    public final void stop(){
    	
        try{

        	if( isAlive ){
                isAlive = false;
                destroyContext();
                LOGGER.info("Successfully stopped PUBLISHER transport [{}].", toString() );
            }

        }catch( Exception e ){
            LOGGER.error("Exception while stopping PUBLISHER transport.", e);
        }

    }


}
