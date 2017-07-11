package com.baijiahulian.multivideodemo.base;

import com.baijiahulian.multivideodemo.multi.MultiVideoRouterListener;

/**
 * Created by wangkangfei on 17/7/11.
 */

public interface MultiBasePresenter {
    void setRouterListener(MultiVideoRouterListener listener);

    void destroy();
}
