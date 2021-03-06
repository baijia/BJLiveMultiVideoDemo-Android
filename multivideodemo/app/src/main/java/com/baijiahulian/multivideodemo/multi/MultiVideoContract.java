package com.baijiahulian.multivideodemo.multi;

import com.baijiahulian.livecore.context.LPError;
import com.baijiahulian.livecore.context.LiveRoom;
import com.baijiahulian.livecore.models.imodels.IMediaModel;
import com.baijiahulian.multivideodemo.base.MultiBasePresenter;

import java.util.List;

/**
 * Created by wangkangfei on 17/7/11.
 */

public interface MultiVideoContract {
    public interface View {
        void mediaInfoInit(List<IMediaModel> mediaModelList);

        void mediaInfoNew(IMediaModel mediaModel);

        void mediaInfoChange(IMediaModel mediaModel);

        void mediaInfoClose(IMediaModel mediaModel);
    }

    public interface Presenter extends MultiBasePresenter {
        void onLaunchSuccess();
    }
}
