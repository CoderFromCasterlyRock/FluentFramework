package com.fluent.framework.transport.jeromq.transport;

import org.slf4j.*;
import zmq.ZError;
import org.zeromq.*;
import org.zeromq.ZMQ.*;
import java.util.concurrent.*;

import com.fluent.framework.util.FluentThreadFactory;

import static com.fluent.framework.util.FluentUtil.*;


public class ReliableRouterTransport extends AbstractTransport implements Runnable{

    private volatile boolean isAlive;

    private final Socket router;
    private final String address;
    private final ExecutorService service;

    private final static String NAME        = AbstractTransport.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );

    public ReliableRouterTransport( boolean toLog, int hwm, int linger, String address, ZContext context ){
        super( toLog, hwm, linger, context );

        this.address    = address;
        this.router     = context.createSocket( ZMQ.ROUTER );
        this.service    = Executors.newSingleThreadExecutor( new FluentThreadFactory( NAME ) );

    }


    @Override
    public final String name( ){
        return NAME;
    }


    @Override
    public final void init(){
        try{
        	
        	router.setHWM( getHighWaterMark() );
            router.setLinger( getTimeToLinger() );
            router.setRouterMandatory( true );
            router.bind( address );

            isAlive = true;
            service.execute( this );
            
            LOGGER.info("Successfully started ROUTER transport [{}].", toString() );
            
        }catch( Exception e ){
            LOGGER.error("FAILED to start reliable transport service [{}].", toString() );
            LOGGER.error("Exception:", e);
        }

    }

    @Override
    public final void send( byte[] data ){
    	router.send(data);
    }
    
    @Override
    public final void send( String data ){
    	router.send(data);
    }
    

    @Override
    public final void run( ){

        Poller poller = new Poller( ONE );
        poller.register( router, Poller.POLLIN );
        
        while( isAlive && !Thread.currentThread().isInterrupted() ){

            try{

                int pollCount	= poller.poll();
                if( pollCount == NEGATIVE_ONE ){
                    LOGGER.warn("Poll count is -1, most likely we are being shut down!");
                    stop();
                }

                if( poller.pollin(ZERO) ){
                    ZMsg message = ZMsg.recvMsg( router );
                    //ZFrame message will be converted to majordomo message
                    //using the router protocol and then
                    //distributed among the interested listeners.
                    logFrames( message );
                    update( message );
                }

            }catch( ZMQException zmqe ){
                int errorCode = zmqe.getErrorCode();

                if( ZError.EHOSTUNREACH == errorCode ){
                    LOGGER.warn("Message dropped! Is the dealer/s alive?");
                }

            }catch( Exception e ){
                LOGGER.error("Exception while receiving messages.:", e);
            }
        }
        
    }


    @Override
    public final void stop(){
        try{

            if( isAlive ){
                isAlive = false;
                destroyContext();
                LOGGER.info("Successfully stopped ROUTER transport [{}].", toString() );
            }

        }catch( Exception e ){
            LOGGER.error("Exception while stopping ROUTER transport.", e);
        }

    }


}
