package com.baijiahulian.multivideodemo.multi;

import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.context.LiveRoom;

/**
 * Created by wangkangfei on 17/7/11.
 */

public class MultiVideoPresenter implements MultiVideoContract.Presenter {
    private MultiVideoContract.View view;
    private MultiVideoRouterListener listener;
    private LiveRoom liveRoom;


    public MultiVideoPresenter(MultiVideoContract.View view) {
        this.view = view;
    }

    @Override
    public void setRouterListener(MultiVideoRouterListener listener) {
        this.listener = listener;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void onLaunchSuccess() {
        this.liveRoom = listener.getLiveRoom();
        liveRoom.getRecorder().setCaptureVideoDefinition(LPConstants.LPResolutionType.LOW);
        if (!liveRoom.getRecorder().isVideoAttached()) {
            liveRoom.getRecorder().attachVideo();
        }
        if (!liveRoom.getRecorder().isAudioAttached()) {
            liveRoom.getRecorder().attachAudio();
        }
        liveRoom.getRecorder().attachVideo();
    }
}
