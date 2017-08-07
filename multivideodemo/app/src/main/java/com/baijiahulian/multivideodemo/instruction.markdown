#多视频直播集成文档

github链接:[https://github.com/baijia/BJLiveMultiVideoDemo-Android](https://github.com/baijia/BJLiveMultiVideoDemo-Android)

多视频直播适合多人同时视频通话场景

###进入多视频房间
```
LiveSDK.enterRoom(this, code, name, launchListener);
```
方法定义如下：
```
/**
 * @param context
 * @param joinCode 参加吗
 * @param userName 昵称
 * @param listener 进房间回调
 */
public static LiveRoom enterRoom(Context context, String joinCode, String userName, final LPLaunchListener listener)


/**
 * @param context
 * @param roomId     房间号
 * @param userNumber 用户 ID
 * @param userName   用户名
 * @param userType   用户类型 {@link com.baijiahulian.livecore.context.LPConstants.LPUserType}
 * @param userAvatar 用户头像
 * @param sign       请求接口参数签名, 签名由 (roomId, userNumber, userName, userType, userAvatar) 5 个参数生成
 *                   refer : http://www.baijiacloud.com/default/home/showDoc#signRule
 * @param listener   如果进入房间失败在 listener 中抛出失败信息 {@link com.baijiahulian.livecore.launch.LPLaunchListener}
 */
public static LiveRoom enterRoom(Context context, final long roomId, String userNumber, String userName,
                                 LPConstants.LPUserType userType, String userAvatar, String sign, final LPLaunchListener listener)

/**
 * @param context
 * @param joinCode   参加吗
 * @param userName   昵称
 * @param userType   角色
 * @param userAvatar 头像
 * @param listener   进房间回调
 */
public static LiveRoom enterRoom(final Context context, String joinCode, String userName, LPConstants.LPUserType userType, String userAvatar, final LPLaunchListener listener)

```
回调接口如下：
```
public interface LPLaunchListener {

    void onLaunchSteps(int step, int totalStep);

    void onLaunchError(LPError error);

    void onLaunchSuccess(LiveRoom liveRoom);
}
```
###初始化视频采集视图（自己的视频）
```
CameraGLSurfaceView glCamera = new CameraGLSurfaceView(this);
glCamera.setZOrderMediaOverlay(true);
liveRoom.getRecorder().setPreview(glCamera);
```
此视图需根据需要添加在ViewGroup中，详情请参考demo

###初始化远程视频视图（他人的视频）
```
View videoView = ViESurfaceViewRenderer.CreateRenderer(context, true);
((SurfaceView) videoView).setZOrderMediaOverlay(true);
```
同样根据需要添加在ViewGroup中，详情请参考demo

###打开本地音视频（自己的音视频）
```
//清晰度
liveRoom.getRecorder().setCaptureVideoDefinition(LPConstants.LPResolutionType.LOW);
liveRoom.getRecorder().publish();
//打开视频
if (!liveRoom.getRecorder().isVideoAttached()) {
    liveRoom.getRecorder().attachVideo();
}
//打开音频
if (!liveRoom.getRecorder().isAudioAttached()) {
    liveRoom.getRecorder().attachAudio();
}
```
底层已处理权限问题，集成方不需再处理动态权限

###主动获取所有远程视频
```
//回调
subscriptionOfActiveUser = liveRoom.getSpeakQueueVM().getObservableOfActiveUsers()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new PrintErrorSubscriber<List<IMediaModel>>() {
            @Override
            public void call(List<IMediaModel> iMediaModels) {
                view.mediaInfoInit(iMediaModels);
            }
        });
//拉取远程视频信息
liveRoom.getSpeakQueueVM().requestActiveUsers();
```

###远程视频状态回调
```
//新用户进入房间
subscriptionOfMediaNew = liveRoom.getSpeakQueueVM().getObservableOfMediaNew()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new PrintErrorSubscriber<IMediaModel>() {
            @Override
            public void call(IMediaModel iMediaModel) {
                view.mediaInfoNew(iMediaModel);
            }
        });
//用户音视频改变状态
subscriptionOfMediaChange = liveRoom.getSpeakQueueVM().getObservableOfMediaChange()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new PrintErrorSubscriber<IMediaModel>() {
            @Override
            public void call(IMediaModel iMediaModel) {
                view.mediaInfoChange(iMediaModel);
            }
        });
//用户关闭音视频
subscriptionOfMediaClose = liveRoom.getSpeakQueueVM().getObservableOfMediaClose()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new PrintErrorSubscriber<IMediaModel>() {
            @Override
            public void call(IMediaModel iMediaModel) {
                view.mediaInfoClose(iMediaModel);
            }
        });
```



