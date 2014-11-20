package com.fluent.etrading.framework.collections;

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.*;
import java.lang.management.*;

import static java.util.Arrays.asList;


public class CoalesingBufferExample{

	
	private final FluentSPSCMergingQueue<String, StockPrice> buffer;
	
	public CoalesingBufferExample(FluentSPSCMergingQueue<String, StockPrice> buffer) {
        this.buffer = buffer;
    }

    
	
	public void run() throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        consumer.join();
    }
	
	

	public class Producer extends Thread {

		private final List<String> stockSymbols = asList("FB", "RHT", "AAPL");
        private final Random random = new Random();

        public Producer() {
            super("producer");
        }

        @Override
        public void run() {
            while (true) {
                for (String symbol : stockSymbols) {
                    StockPrice price = new StockPrice(symbol, 100 * random.nextDouble());
                    boolean success = buffer.offer(symbol, price);

                    if (!success) {
                        throw new AssertionError("offer of " + symbol + " failed");
                    }
                }
            }
        }
    }

    
	
	public class Consumer extends Thread {

        public Consumer() {
            super("consumer");
        }

        @Override
        public void run() {
            List<StockPrice> prices = new ArrayList<StockPrice>(3);

            while (true) {
                buffer.poll(prices);
                for (StockPrice price : prices) {
                    System.out.println(price);
                }
                prices.clear();
            }
        }
    }

    

    public static class StockPrice{
    	
        public final String symbol;
        public final double price;

        public StockPrice(String symbol, double price) {
            this.symbol = symbol;
            this.price = price;
        }

        @Override
        public String toString() {
            return String.format("%s =\t$%.2f", symbol, price);
        }
    }
    

    public static void main(String[] args) throws Exception {
    	FluentSPSCMergingQueue<String, StockPrice> buffer = new FluentSPSCMergingQueue<String, StockPrice>(8);

        // register an mbean to be able to view the state of the coalescing ring buffer
        CoalescingRingBufferViewer.register("Example", buffer, ManagementFactory.getPlatformMBeanServer());

        CoalesingBufferExample example = new CoalesingBufferExample(buffer);
        example.run();
    }

}