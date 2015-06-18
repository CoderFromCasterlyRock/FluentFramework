package com.fluent.framework.util;

import java.text.*;
import java.util.*;


public final class TimeUtil{

    protected TimeUtil(){}

    public final static String TODAY = new SimpleDateFormat("yyyy.MM.dd").format( new Date() );

   
    public final static long currentNanos(  ){
        return System.nanoTime( );
    }

    
    public final static long currentMillis(  ){
        return System.currentTimeMillis();
    }
    

}
