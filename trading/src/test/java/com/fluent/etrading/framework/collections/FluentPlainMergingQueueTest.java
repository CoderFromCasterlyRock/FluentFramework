package com.fluent.etrading.framework.collections;

import org.junit.*;

import com.fluent.etrading.framework.events.in.*;
import com.fluent.etrading.framework.utility.ContainerUtil;

import static org.assertj.core.api.Assertions.*;
import static com.fluent.etrading.framework.market.core.Marketplace.*;
import static com.fluent.etrading.framework.events.core.FluentInputEventType.*;

public class FluentPlainMergingQueueTest{

	private FluentPlainMergingQueue<String, MarketDataEvent> mergingQueue;
	
	@Before
	public void init(){
		mergingQueue = new FluentPlainMergingQueue<String, MarketDataEvent>(10);
	}
	
	
	@Test
	public void testIsEmpty( ){
		assertThat( mergingQueue != null ).isTrue();
		assertThat( mergingQueue.isEmpty() ).isTrue();
	}

	
	@Test
	public void testOfferCheckReturnValue( ){
		boolean value1 = mergingQueue.offer( "AAPL", new MarketDataEvent( 1, FUTURES_MD, UNSUPPORTED, "AAPL", 99.0, 1500, 99.9, 2000 ) );
		assertThat( mergingQueue.size() ).isEqualTo( 1 );
		assertThat( value1 ).isTrue();
	
		boolean value2 = mergingQueue.offer( "AAPL", new MarketDataEvent( 2, FUTURES_MD, UNSUPPORTED, "AAPL", 99.9, 2500, 100.1, 7000 ) );
		assertThat( mergingQueue.size() ).isEqualTo( 1 );
		assertThat( value2 ).isFalse();
		
	}
	
	
	@Test
	public void testOffer( ){
		int eventId		= 0;
		String[] datas 	= { "AAPL", "GOOG", "AAPL", "IBM", "IBM" };
		
		for( String data : datas ){
			double bid	= ContainerUtil.rndNumberBetween( 550, 50 );
			double ask	= bid + ContainerUtil.rndNumberBetween( 10, 1 );
			
			MarketDataEvent event = new MarketDataEvent( ++eventId, FUTURES_MD, UNSUPPORTED, data, bid, 1500, ask, 2000 );
			mergingQueue.offer( data, event );
		}
		
		assertThat( mergingQueue.size() ).isEqualTo( 3 );
	
	}
	
	
	@Test
	public void testPoll( ){
		
		int eventId		= 0;
		String[] datas 	= { "AAPL", "GOOG", "AAPL", "IBM", "IBM" };
		
		for( String data : datas ){
			double bid	= ContainerUtil.rndNumberBetween( 550, 50 );
			double ask	= bid + ContainerUtil.rndNumberBetween( 10, 1 );
			
			MarketDataEvent event = new MarketDataEvent( ++eventId, FUTURES_MD, UNSUPPORTED, data, bid, 1500, ask, 2000 );
			mergingQueue.offer( data, event );
		}
		
		//3, 2, 5
		MarketDataEvent event3 = mergingQueue.poll();
		assertThat( event3.getEventId() ).isEqualTo( 3L );
		assertThat( event3.getSymbol() ).isEqualToIgnoringCase( "AAPL");
		System.err.println( event3 );
		
		MarketDataEvent event2 = mergingQueue.poll();
		assertThat( event2.getEventId() ).isEqualTo( 2L );
		assertThat( event2.getSymbol() ).isEqualToIgnoringCase( "GOOG");
		System.err.println( event2 );
		
		MarketDataEvent event5 = mergingQueue.poll();
		assertThat( event5.getEventId() ).isEqualTo( 5L );
		assertThat( event5.getSymbol() ).isEqualToIgnoringCase( "IBM");
		System.err.println( event5 );
	}

	
	@Test
	public void testClear( ){
		
		assertThat( mergingQueue.isEmpty() ).isTrue();
		
		int eventId		= 0;
		String[] datas 	= { "AAPL", "GOOG", "AAPL", "IBM", "IBM" };
		
		for( String data : datas ){
			double bid	= ContainerUtil.rndNumberBetween( 550, 50 );
			double ask	= bid + ContainerUtil.rndNumberBetween( 10, 1 );
			
			MarketDataEvent event = new MarketDataEvent( ++eventId, FUTURES_MD, UNSUPPORTED, data, bid, 1500, ask, 2000 );
			mergingQueue.offer( data, event );
		}
		
		assertThat( mergingQueue.isEmpty() ).isFalse();
		
		mergingQueue.clear();
		assertThat( mergingQueue.isEmpty() ).isTrue();
		
	}
	
	
	@After
	public void tear(){
		mergingQueue = null;
	}

}
