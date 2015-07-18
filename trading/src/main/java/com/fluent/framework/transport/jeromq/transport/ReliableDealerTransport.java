package com.fluent.framework.transport.jeromq.transport;

import org.slf4j.*;

import zmq.ZError;

import org.zeromq.*;
import org.zeromq.ZMQ.*;

import java.util.concurrent.*;

import com.fluent.framework.collection.FluentThreadFactory;


public class ReliableDealerTransport extends AbstractTransport implements Runnable{

    private volatile boolean isAlive;

    private final String identity;
    private final Socket dealer;
    private final String address;

    private final ExecutorService service;

    private final static String NAME    = AbstractTransport.class.getSimpleName();
    private final static Logger LOGGER  = LoggerFactory.getLogger( NAME );


    public ReliableDealerTransport( boolean toLog, int hwm, int linger, String identity, String address, ZContext context ){
        super( toLog, hwm, linger, context );

        this.identity   = identity;
        this.address    = address;
        this.dealer     = context.createSocket( ZMQ.DEALER );
        this.service    = Executors.newSingleThreadExecutor( new FluentThreadFactory( NAME ) );

    }


    @Override
    public final String name( ){
        return NAME;
    }


    @Override
    public final void start(){
        try{

            dealer.setHWM( getHighWaterMark() );
            dealer.setLinger( getTimeToLinger() );
            dealer.setIdentity(identity.getBytes());
            dealer.connect( address );

            isAlive = true;
            service.execute( this );
            
            LOGGER.info("Successfully started DEALER transport [{}].", toString() );
            
        }catch( Exception e ){
            LOGGER.error("FAILED to start reliable DEALER transport service [{}].", toString() );
            LOGGER.error("Exception:", e);
        }

    }

    @Override
    public final void send( byte[] data ){
    	dealer.send(data);
    }
    
    
    @Override
    public final void send( String data ){
    	dealer.send(data);
    }
    
    
    @Override
    public final void run( ){

    	try{
    		
    		while( isAlive && !Thread.currentThread().isInterrupted() ){
    			
    			ZMsg message = ZMsg.recvMsg( dealer );
    			//ZFrame message will be converted to majordomo message
    			//using the dealer protocol and then
    			//distributed among the interested listeners.
    			logFrames( message );
    			update( message );
    		}
    	
    	}catch( ZMQException zmqe ){
    		int errorCode = zmqe.getErrorCode();

    		if( ZError.EHOSTUNREACH == errorCode ){
    			LOGGER.warn("Message dropped! Is the router alive?");
    		}

    	}catch( Exception e ){
    		LOGGER.error("Exception while receiving messages.:", e);
    	}
    
    }


    @Override
    public final void stop(){
        
    	try{

    		if( isAlive ){
                isAlive = false;
                destroyContext();
                LOGGER.info("Successfully stopped DEALER transport [{}].", toString() );
            }

        }catch( Exception e ){
            LOGGER.error("Exception while stopping DEALER transport.", e);
        }

    }


}
