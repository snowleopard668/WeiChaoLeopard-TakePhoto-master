package com.liji.as.pictureselect.utils;

/**
 * Created by liji on 2016/5/13.
 */
public abstract class XCallbackListener {
    
    protected abstract void callback(Object... obj);
    
    public void call(Object... obj) {
        try {
            callback(obj);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}