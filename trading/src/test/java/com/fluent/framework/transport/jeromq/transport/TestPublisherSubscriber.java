package com.fluent.framework.transport.jeromq.transport;

import org.zeromq.*;

import com.fluent.framework.core.*;


public class TestPublisherSubscriber{


    private class TestSubscriber implements ZListener<ZMsg>, FluentLifecycle{

        private final ReliableSubscriberTransport subscriber;

        public TestSubscriber( ZContext context ){
            this.subscriber = new ReliableSubscriberTransport( false, 100000, 10, "tcp://localhost:7090", context );
        }

        public void start( ) {
            subscriber.subscribe( this );
            subscriber.start( );
        }

        @Override
        public String name( ) {
            return TestSubscriber.class.getSimpleName( );
        }

        @Override
        public void update( ZMsg data ) {
            System.err.println( "[Subscriber received] > " + data );
        }

        @Override
        public void stop( ) {
            subscriber.stop( );
        }

    }


    private class TestPublisher implements FluentLifecycle{

        private final ReliablePublisherTransport publisher;

        public TestPublisher( ZContext context ){
            this.publisher = new ReliablePublisherTransport( true, 100000, 10, "tcp://*:7090", context );
        }

        public void start( ) {
            publisher.start( );
        }

        @Override
        public String name( ) {
            return TestPublisher.class.getSimpleName( );
        }

        public void send( String data ) {
            publisher.send( data );
        }

        @Override
        public void stop( ) {
            publisher.stop( );
        }

    }


    public static void main( String args[] ) throws Exception {

        ZContext context = new ZContext( 1 );
        TestPublisherSubscriber te = new TestPublisherSubscriber( );
        TestSubscriber subscriber = te.new TestSubscriber( context );
        TestPublisher publisher = te.new TestPublisher( context );

        subscriber.start( );
        Thread.sleep( 1000 );

        publisher.start( );
        Thread.sleep( 3000 );

        for( int i = 0; i < 1000; i++ ){
            publisher.send( String.valueOf( i ) );
        }

        publisher.stop( );
        subscriber.stop( );

    }

}
