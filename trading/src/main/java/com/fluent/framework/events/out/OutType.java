package com.fluent.framework.events.out;

import java.util.*;

import com.fluent.framework.events.core.FluentEventType;


public enum OutType implements FluentEventType{

	WARM_UP_EVENT,
	MD_SUBSCRIBE_EVENT,
	MD_UNSUBSCRIBE_EVENT,
	ORDER_TO_MARKET,
    EVENT_TO_LOGGER,
    EVENT_TO_TRADER;

    private final static Set<OutType> ALL_TYPES;
   

    static{
    	ALL_TYPES = new HashSet<OutType>(Arrays.asList(OutType.values()));
    }


    @Override
    public final boolean isIncoming( ){
        return false;
    }
    
    
    public final static Set<OutType> allTypes( ){
        return ALL_TYPES;
    }

}
