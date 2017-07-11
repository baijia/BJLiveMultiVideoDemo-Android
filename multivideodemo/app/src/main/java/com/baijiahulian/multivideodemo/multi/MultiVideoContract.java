package com.baijiahulian.multivideodemo.multi;

import com.baijiahulian.livecore.context.LPError;
import com.baijiahulian.livecore.context.LiveRoom;
import com.baijiahulian.multivideodemo.base.MultiBasePresenter;

/**
 * Created by wangkangfei on 17/7/11.
 */

public interface MultiVideoContract {
    public interface View {

    }

    public interface Presenter extends MultiBasePresenter{
        void onLaunchSuccess();
    }
}
