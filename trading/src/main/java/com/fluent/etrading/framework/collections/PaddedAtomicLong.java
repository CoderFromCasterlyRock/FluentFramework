package com.fluent.etrading.framework.collections;

import java.util.concurrent.atomic.*;

import static com.fluent.etrading.framework.utility.ContainerUtil.*;


public class PaddedAtomicLong extends AtomicLong{
	
	public volatile long p1, p2, p3, p4, p5, p6 = 7L;

	private static final long serialVersionUID = ONE;
	
	
    public PaddedAtomicLong( ){
    	this( ZERO );
    }
    
    public PaddedAtomicLong( final long initialValue ){
        super(initialValue);
    }

    
    public final long foilJVMOptimization(){
        return p1 + p2 + p3 + p4 + p5 + p6;
    }
    
    
}