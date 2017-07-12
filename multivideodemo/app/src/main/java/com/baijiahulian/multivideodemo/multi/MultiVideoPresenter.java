package com.baijiahulian.multivideodemo.multi;

import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.context.LiveRoom;
import com.baijiahulian.livecore.models.imodels.IMediaModel;
import com.baijiahulian.multivideodemo.base.PrintErrorSubscriber;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wangkangfei on 17/7/11.
 */

public class MultiVideoPresenter implements MultiVideoContract.Presenter {
    private MultiVideoContract.View view;
    private MultiVideoRouterListener listener;
    private LiveRoom liveRoom;

    private Subscription subscriptionOfActiveUser, subscriptionOfMediaNew, subscriptionOfMediaChange, subscriptionOfMediaClose;


    public MultiVideoPresenter(MultiVideoContract.View view) {
        this.view = view;
    }

    @Override
    public void setRouterListener(MultiVideoRouterListener listener) {
        this.listener = listener;
    }

    @Override
    public void destroy() {
        unSubscribeSubscription(subscriptionOfActiveUser);
        unSubscribeSubscription(subscriptionOfMediaNew);
        unSubscribeSubscription(subscriptionOfMediaChange);
        unSubscribeSubscription(subscriptionOfMediaClose);
        liveRoom.quitRoom();
        view = null;
    }

    private void unSubscribeSubscription(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        subscription = null;
    }

    @Override
    public void onLaunchSuccess() {
        this.liveRoom = listener.getLiveRoom();
        //打开本地音视频
        liveRoom.getRecorder().setCaptureVideoDefinition(LPConstants.LPResolutionType.LOW);
        liveRoom.getRecorder().publish();
        if (!liveRoom.getRecorder().isVideoAttached()) {
            liveRoom.getRecorder().attachVideo();
        }
        if (!liveRoom.getRecorder().isAudioAttached()) {
            liveRoom.getRecorder().attachAudio();
        }

        init();
    }

    private void init() {
        //初始化拉取活跃用户
        subscriptionOfActiveUser = liveRoom.getSpeakQueueVM().getObservableOfActiveUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PrintErrorSubscriber<List<IMediaModel>>() {
                    @Override
                    public void call(List<IMediaModel> iMediaModels) {
                        view.mediaInfoInit(iMediaModels);
                    }
                });
        liveRoom.getSpeakQueueVM().requestActiveUsers();

        //新用户
        subscriptionOfMediaNew = liveRoom.getSpeakQueueVM().getObservableOfMediaNew()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PrintErrorSubscriber<IMediaModel>() {
                    @Override
                    public void call(IMediaModel iMediaModel) {
                        view.mediaInfoNew(iMediaModel);
                    }
                });
        //改变状态
        subscriptionOfMediaChange = liveRoom.getSpeakQueueVM().getObservableOfMediaChange()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PrintErrorSubscriber<IMediaModel>() {
                    @Override
                    public void call(IMediaModel iMediaModel) {
                        view.mediaInfoChange(iMediaModel);
                    }
                });
        //关闭
        subscriptionOfMediaClose = liveRoom.getSpeakQueueVM().getObservableOfMediaClose()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PrintErrorSubscriber<IMediaModel>() {
                    @Override
                    public void call(IMediaModel iMediaModel) {
                        view.mediaInfoClose(iMediaModel);
                    }
                });

    }
}
