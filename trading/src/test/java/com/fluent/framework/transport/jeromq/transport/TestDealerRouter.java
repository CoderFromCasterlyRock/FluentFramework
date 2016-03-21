package com.fluent.framework.transport.jeromq.transport;

import org.zeromq.*;

import com.fluent.framework.core.*;


public class TestDealerRouter{


    private class TestRouter implements ZListener<ZMsg>, FluentLifecycle{

        private final ReliableRouterTransport router;

        public TestRouter( ZContext context ){
            this.router = new ReliableRouterTransport( false, 100000, 10, "tcp://localhost:9070", context );
        }

        public void start( ) {
            router.subscribe( this );
            router.start( );
        }

        @Override
        public String name( ) {
            return TestRouter.class.getSimpleName( );
        }

        @Override
        public void update( ZMsg data ) {
            System.err.println( "[Router received] > " + data );
        }

        @Override
        public void stop( ) {
            router.stop( );
        }

    }


    private class TestDealer implements ZListener<String>, FluentLifecycle{

        private final ReliableDealerTransport dealer;

        public TestDealer( ZContext context ){
            this.dealer = new ReliableDealerTransport( true, 100000, 10, "TestDealer", "tcp://localhost:9070", context );
        }

        public void start( ) {
            dealer.start( );
        }

        @Override
        public String name( ) {
            return TestDealer.class.getSimpleName( );
        }

        @Override
        public void update( String data ) {
            System.err.println( "[Dealer received] > " + data );
        }

        public void send( String data ) {
            dealer.send( data );
        }

        @Override
        public void stop( ) {
            dealer.stop( );
        }

    }


    public static void main( String args[] ) throws Exception {

        ZContext context = new ZContext( 1 );
        TestDealerRouter te = new TestDealerRouter( );
        TestRouter router = te.new TestRouter( context );
        TestDealer dealer = te.new TestDealer( context );

        router.start( );
        Thread.sleep( 1000 );

        dealer.start( );

        for( int i = 0; i < 100; i++ ){
            dealer.send( String.valueOf( System.currentTimeMillis( ) ) );
        }

        Thread.sleep( 1000 );
        dealer.stop( );
        router.stop( );
    }

}
