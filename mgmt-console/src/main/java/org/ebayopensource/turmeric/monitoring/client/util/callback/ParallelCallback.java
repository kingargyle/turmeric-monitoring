package org.ebayopensource.turmeric.monitoring.client.util.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ParallelCallback<T> implements AsyncCallback<T> {
    private String id;

    private T data;
    private ParallelCallbackQueue<T> queue;

    @Override
    public void onFailure(Throwable excp) {
        queue.stopOnError(excp, this);
    }

    @Override
    public void onSuccess(T receivedData) {
        this.data = receivedData;
        queue.processNextInQueue();
    }

    public T getData() {
        return data;
    }

    void setQueue(ParallelCallbackQueue<T> queue) {
        this.queue = queue;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
