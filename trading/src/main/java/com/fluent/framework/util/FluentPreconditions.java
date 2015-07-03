package com.fluent.framework.util;


public final class FluentPreconditions{
	
	
	public static <T> T notNull( T reference, Object message ){
		if( reference == null ){
			throw new NullPointerException( String.valueOf(message) );
		 }
		 
		return reference;
	}
	

	public static String notBlank( String reference, Object message ){
		if( reference == null || reference.trim().isEmpty() ){
			throw new IllegalStateException( String.valueOf(message) );
		 }
		 
		return reference;
	}
	
	
	public static int notNegative( int value, Object message ){
		if( value <= 0 ){
			throw new IllegalStateException( String.valueOf(message) );
		 }
		 
		return value;
	}
	

}
