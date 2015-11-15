package com.fluent.framework.collection;

import java.util.*;
import org.slf4j.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import com.fluent.framework.core.*;


public final class FluentSingleThreadExecutor implements FluentLifecycle, ExecutorService{

	private volatile boolean running;
    private volatile boolean stopped;
    
    private final Thread thread;
    private final AtomicReference<Runnable> reference;
    
    private final static String NAME    = FluentSingleThreadExecutor.class.getSimpleName();
    private final static Logger LOGGER  = LoggerFactory.getLogger( NAME );
    
    
    public FluentSingleThreadExecutor( ThreadFactory factory ){
    	
    	this.reference 	= new AtomicReference<>();
        this.thread		= factory.newThread( new Worker() );
    }

    
    @Override
    public final String name( ){
    	return NAME; 
    }
    
    
    @Override
    public final void start( ){
    	running 	= true;
        stopped		= false;
    }
    
    
    @Override
    public final void execute( Runnable runnable ){
    	
    	if( stopped ) return;
    	if( runnable == null ) return;
    	
    	reference.set(runnable);
    	thread.start();
        
    }

   
   @Override
   public final void shutdown(){
	   running = false;
	   thread.interrupt();
       stopped = true;
   }
   
   
   @Override
   public final boolean isShutdown( ){
	   return running;
   }
   
   
   @Override
   public final boolean isTerminated( ){
	   return stopped;
   }
   
   
   @Override
   public final boolean awaitTermination( long timeout, TimeUnit unit ){
	   throw new UnsupportedOperationException();
   }
   
   
   @Override
   public final <T> List<Future<T>> invokeAll( Collection<? extends Callable<T>> tasks ){
	   throw new UnsupportedOperationException();
   }
   
   
   @Override
   public final <T> List<Future<T>> invokeAll( Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit ){
	   throw new UnsupportedOperationException();
   }
   
   
   @Override
   
   public final <T> T invokeAny( Collection<? extends Callable<T>> tasks ){
	   throw new UnsupportedOperationException();
   }
   
   
   @Override
   
   public final <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit ){
	   throw new UnsupportedOperationException();
   }
   
   
   @Override
   public final List<Runnable> shutdownNow( ){
	   throw new UnsupportedOperationException();
   }
   
   
   @Override
   
   public final <T> Future<T> submit( Callable<T> task ){
	   throw new UnsupportedOperationException();
   }
   
   
   @Override
   public final Future<?> submit(Runnable task){
	   throw new UnsupportedOperationException();
   }
   
   
   @Override
   
   public final <T> Future<T> submit( Runnable task, T result ){
	   throw new UnsupportedOperationException();
   }
   
   
   @Override
   public final String toString(){
	   throw new UnsupportedOperationException();
   }
    
   
   @Override
   public final void stop( ){
	   shutdown();
   }
   
   
   
  private final class Worker implements Runnable{
   	
   	
	   @Override
	   public void run( ){
             
		   while( running ){
			   
			   Runnable runnable = null;

			   //Can we simplify this logic to use only one loop??
			   
			   while( true ){
				   
				   if( Thread.interrupted() ) return;
				   
				   runnable = reference.get();
				   if( runnable != null ) 
					   break;
				   
				   FluentBackoffStrategy.apply();
			   }
				
			   try{
						   
				   if( runnable != null ){ 
					   runnable.run();
				   }
				   				   
			   }catch( Exception e ){
				   LOGGER.warn( "Exception while running thread.", e );
			   }

		   }
      
	   }
      
  
  }
   
   
}