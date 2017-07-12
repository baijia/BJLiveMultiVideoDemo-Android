package com.baijiahulian.multivideodemo.base;

import rx.Subscriber;

/**
 * Created by wangkangfei on 17/7/11.
 */

public abstract class PrintErrorSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(T t) {
        call(t);
    }

    public abstract void call(T t);
}
