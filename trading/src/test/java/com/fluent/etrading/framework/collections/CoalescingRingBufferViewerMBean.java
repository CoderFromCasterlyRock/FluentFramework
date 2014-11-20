package com.fluent.etrading.framework.collections;


public interface CoalescingRingBufferViewerMBean {

    int getSize();

    int getCapacity();

    int getRemainingCapacity();

    long getRejectionCount();

    long getProducerIndex();

    long getConsumerIndex();

}