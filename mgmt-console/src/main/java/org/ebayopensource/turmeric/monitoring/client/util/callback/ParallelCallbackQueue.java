package org.ebayopensource.turmeric.monitoring.client.util.callback;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class ParallelCallbackQueue<T> implements AsyncCallback<T>{
    
    
    private int finishedCallbacks = 0;
    
    protected List<ParallelCallback<T>> callbacks; 

    @Override
    public void onFailure(Throwable excp) {
        GWT.log("onFailure", excp);
    }

    @Override
    public void onSuccess(T receivedData) {
        GWT.log("onSuccess = "+receivedData);
    }

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

    protected abstract void stopOnError(Throwable exp, ParallelCallback<T> callbackInQueue);
    
    public void add(ParallelCallback<T> callback){
        if(callbacks == null){
            callbacks = new ArrayList<ParallelCallback<T>>();
        }
        callbacks.add(callback);
        callback.setQueue(this);
    }
    
    protected <T extends Object> T getCallbackData(int index) {
        if (index < 0 || index >= callbacks.size()) {
            throw new RuntimeException("Invalid callback index");
        }

        return (T) callbacks.get(index).getData();
    }

}
