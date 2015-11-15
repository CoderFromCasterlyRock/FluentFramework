package com.fluent.framework.transport.jeromq.transport;

import org.slf4j.*;
import org.zeromq.*;
import org.zeromq.ZMQ.*;

import java.util.concurrent.*;

import com.fluent.framework.collection.FluentThreadFactory;

import static com.fluent.framework.util.FluentUtil.*;


//TODO: Subscriber should explicitly send publisher a READY message via a dealer socket.

public class ReliableSubscriberTransport extends AbstractJMQTransport implements Runnable{

    private volatile boolean isAlive;

    private final Socket subscriber;
    private final String address;
    private final String[] topics;
    private final ExecutorService service;

    private final static String NAME        = AbstractJMQTransport.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );

    public ReliableSubscriberTransport( boolean toLog, int hwm, int linger, String address, ZContext context ){
    	this( toLog, hwm, linger, new String[]{EMPTY}, address, context );
    }
    
    public ReliableSubscriberTransport( boolean toLog, int hwm, int linger, String[] topics, String address, ZContext context ){
        super( toLog, hwm, linger, context );

        this.address    = address;
        this.topics		= topics;
        this.subscriber = context.createSocket( ZMQ.SUB );
        this.service    = Executors.newSingleThreadExecutor( new FluentThreadFactory( NAME ) );

    }

    @Override
    public final String name( ){
        return NAME;
    }


    @Override
    public final void start(){
        try{
        	
        	for( String topic : topics ){
        		subscriber.subscribe(topic.getBytes());
        	}
        	subscriber.setRcvHWM( getHighWaterMark() );
        	subscriber.connect( address );

            isAlive = true;
            service.execute( this );
            
            LOGGER.info("Successfully started SUBSCRIBE transport [{}].", toString() );
            
        }catch( Exception e ){
            LOGGER.error("FAILED to start reliable transport service [{}].", toString() );
            LOGGER.error("Exception:", e);
        }

    }

    @Override
    public final void send( byte[] data ){
    	throw new UnsupportedOperationException();
    }
    
    @Override
    public final void send( String data ){
    	throw new UnsupportedOperationException();
    }
    

    @Override
    public final void run( ){

        while( isAlive && !Thread.currentThread().isInterrupted() ){

            try{

            	ZMsg message = ZMsg.recvMsg( subscriber );
                //ZFrame message will be converted to majordomo message
                //using the SUBSCRIBE protocol and then
                //distributed among the interested listeners.
                logFrames( message );
                update( message );
                
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
                LOGGER.info("Successfully stopped SUBSCRIBE transport [{}].", toString() );
            }

        }catch( Exception e ){
            LOGGER.error("Exception while stopping SUBSCRIBE transport.", e);
        }

    }


}
