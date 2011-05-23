/*******************************************************************************
 *   Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"); 
 *   you may not use this file except in compliance with the License. 
 *   You may obtain a copy of the License at 
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.util.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Class ParallelCallback.
 *
 * @param <T> the generic type
 */
public class ParallelCallback<T> implements AsyncCallback<T> {
    private String id;

    private T data;
    private ParallelCallbackQueue<T> queue;

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
     */
    @Override
    public void onFailure(Throwable excp) {
        queue.stopOnError(excp, this);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
     */
    @Override
    public void onSuccess(T receivedData) {
        this.data = receivedData;
        queue.processNextInQueue();
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * Sets the queue.
     *
     * @param queue the new queue
     */
    void setQueue(ParallelCallbackQueue<T> queue) {
        this.queue = queue;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

}
