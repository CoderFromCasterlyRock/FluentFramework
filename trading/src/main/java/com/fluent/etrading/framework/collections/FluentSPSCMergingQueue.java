package com.fluent.etrading.framework.collections;

import java.util.*;

/**
 * A merging queue for values with keys. Null key/values are not welcome. Backed by
 * ArrayDeque and HashMap.
 *
 * @author nitsanw
 *
 * @param <KEY>
 * @param <VAL>
 */


public class FluentSPSCMergingQueue<KEY, VAL> implements MergingQueue<KEY, VAL>{

    private final Map<KEY, VAL> lastValMap;
    private final FluentSPSCQueue<KEY> keyQueue;


    public FluentSPSCMergingQueue( int capacity ){
        this.keyQueue   = new FluentSPSCQueue<KEY>( capacity );
        this.lastValMap = new HashMap<KEY, VAL>( capacity );
    }


    @Override
    public VAL poll(){

        // Optimisation: put null to avoid recycling the entry in the map:
        // + no garbage overhead, keep the entries and as the use case is for a limited set
        //   of keys this means you'll hit max capacity required in a short while and be done.
        // - keys are kept for the lifetime of the map, if you were hoping for them to get
        //   picked up by the GC then you'll need to go another way

        KEY key =  keyQueue.poll();
        return lastValMap.put(key, null);

    }


    @Override
    public void offer( KEY key, VAL val ){

        assert key != null && val != null;
        VAL lastVal = lastValMap.put(key, val);
        if (lastVal == null) {
            keyQueue.add(key);
        }

    }

    @Override
    public boolean isEmpty( ){
        return keyQueue.isEmpty();
    }


    @Override
    public void clear( ){
        lastValMap.clear();
        keyQueue.clear();
    }


}