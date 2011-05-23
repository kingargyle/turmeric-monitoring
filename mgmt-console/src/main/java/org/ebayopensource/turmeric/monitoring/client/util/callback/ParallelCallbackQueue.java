/*******************************************************************************
 *   Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"); 
 *   you may not use this file except in compliance with the License. 
 *   You may obtain a copy of the License at 
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.util.callback;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Class ParallelCallbackQueue.
 *
 * @param <T> the generic type
 */
public abstract class ParallelCallbackQueue<T> implements AsyncCallback<T>{
    
    
    private int finishedCallbacks = 0;
    
    /** The callbacks. */
    protected List<ParallelCallback<T>> callbacks; 

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
     */
    @Override
    public void onFailure(Throwable excp) {
        GWT.log("onFailure", excp);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
     */
    @Override
    public void onSuccess(T receivedData) {
        GWT.log("onSuccess = "+receivedData);
    }

    /**
     * Process next in queue.
     */
    public synchronized void processNextInQueue() {
        finishedCallbacks++;
        GWT.log("calling processNextInQueue. finishedCallbacks = "+finishedCallbacks);
        //now, let's see if I must call the queue callback
        if(finishedCallbacks == callbacks.size()){
            GWT.log("calling sucess in queue. All callbacks finished");
            success();
        }
    }
    
    /**
     * Called only when all callbacks have completed.
     */
    protected abstract void success();

    /**
     * Stop on error.
     *
     * @param exp the exp
     * @param callbackInQueue the callback in queue
     */
    protected abstract void stopOnError(Throwable exp, ParallelCallback<T> callbackInQueue);
    
    /**
     * Adds the.
     *
     * @param callback the callback
     */
    public void add(ParallelCallback<T> callback){
        if(callbacks == null){
            callbacks = new ArrayList<ParallelCallback<T>>();
        }
        callbacks.add(callback);
        callback.setQueue(this);
    }
    
    /**
     * Gets the callback data.
     *
     * @param <T> the generic type
     * @param index the index
     * @return the callback data
     */
    protected <T extends Object> T getCallbackData(int index) {
        if (index < 0 || index >= callbacks.size()) {
            throw new RuntimeException("Invalid callback index");
        }

        return (T) callbacks.get(index).getData();
    }

}
