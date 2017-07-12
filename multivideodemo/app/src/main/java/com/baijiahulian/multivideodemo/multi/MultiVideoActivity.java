package com.baijiahulian.multivideodemo.multi;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.baijiahulian.avsdk.liveplayer.CameraGLSurfaceView;
import com.baijiahulian.livecore.LiveSDK;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.context.LPError;
import com.baijiahulian.livecore.context.LiveRoom;
import com.baijiahulian.livecore.context.OnLiveRoomListener;
import com.baijiahulian.livecore.launch.LPLaunchListener;
import com.baijiahulian.livecore.models.imodels.IMediaModel;
import com.baijiahulian.multivideodemo.R;
import com.baijiahulian.multivideodemo.adapter.RemoteVideoAdapter;

import java.util.List;

public class MultiVideoActivity extends AppCompatActivity implements MultiVideoContract.View, MultiVideoRouterListener {
    //view
//    private CameraGLSurfaceView glCamera;
    private FrameLayout flMySelf;
    private RecyclerView rvRemoteVideo;
    private MaterialDialog loadingDlg;

    //logic
    private MultiVideoContract.Presenter mPresenter;
    private LiveRoom liveRoom;
    private List<IMediaModel> remoteList;

    //listeners
    private LPLaunchListener launchListener;
    private OnLiveRoomListener onLiveRoomListener;

    //adapter
    private RemoteVideoAdapter remoteVideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_video);
        initViews();
        initAdapters();
        initListeners();
        initData();
    }

    private void initViews() {
        flMySelf = (FrameLayout) findViewById(R.id.fl_myself);
        rvRemoteVideo = (RecyclerView) findViewById(R.id.rv_remote_video);
        rvRemoteVideo.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void initAdapters() {
        remoteVideoAdapter = new RemoteVideoAdapter(this);
    }

    private void initListeners() {
        //进入房间进度
        launchListener = new LPLaunchListener() {
            @Override
            public void onLaunchSteps(int step, int totalSteps) {
                loadingDlg.incrementProgress(step);
            }

            @Override
            public void onLaunchError(LPError lpError) {
                Toast.makeText(MultiVideoActivity.this, lpError.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLaunchSuccess(LiveRoom liveRoom) {
                setLiveRoom(liveRoom);
                launchSuccess();
            }
        };
        //房间出错监听
        onLiveRoomListener = new OnLiveRoomListener() {
            @Override
            public void onError(LPError lpError) {
                //断开连接了
                if (lpError.getCode() == LPError.CODE_ERROR_NETWORK_FAILURE
                        || lpError.getCode() == LPError.CODE_ERROR_ROOMSERVER_LOSE_CONNECTION
                        || lpError.getCode() == LPError.CODE_ERROR_MEDIA_SERVER_CONNECT_FAILED) {
                    Toast.makeText(MultiVideoActivity.this, "已断开连接，即将退出", Toast.LENGTH_SHORT).show();
                    flMySelf.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000);
                }
            }
        };

    }

    //进入房间成功后的处理
    private void launchSuccess() {
        loadingDlg.dismiss();
        CameraGLSurfaceView glCamera = new CameraGLSurfaceView(this);
        glCamera.setZOrderMediaOverlay(true);
        flMySelf.addView(glCamera);
        liveRoom.getRecorder().setPreview(glCamera);
        liveRoom.setOnLiveRoomListener(onLiveRoomListener);

        mPresenter.onLaunchSuccess();
    }

    private void initData() {
        mPresenter = new MultiVideoPresenter(this);
        mPresenter.setRouterListener(this);
        showLoading();
        String code = getIntent().getStringExtra("enter_code");
        String name = getIntent().getStringExtra("enter_name");
        LiveSDK.enterRoom(this, code, name, launchListener);
    }

    private void showLoading() {
        loadingDlg = new MaterialDialog.Builder(this)
                .title(R.string.multi_loading_title)
                .progress(false, 5, true)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void setLiveRoom(LiveRoom liveRoom) {
        this.liveRoom = liveRoom;
    }

    @Override
    public LiveRoom getLiveRoom() {
        return liveRoom;
    }

    @Override
    public void mediaInfoInit(List<IMediaModel> mediaModelList) {
        this.remoteList = mediaModelList;
        remoteVideoAdapter.setDataList(mediaModelList);
        remoteVideoAdapter.setRouterListener(this);
        rvRemoteVideo.setAdapter(remoteVideoAdapter);
    }

    @Override
    public void mediaInfoNew(IMediaModel mediaModel) {
        remoteList.add(mediaModel);
        remoteVideoAdapter.setDataList(remoteList);
        remoteVideoAdapter.notifyItemInserted(remoteList.size());
    }


    @Override
    public void mediaInfoChange(IMediaModel mediaModel) {

    }

    @Override
    public void mediaInfoClose(IMediaModel mediaModel) {
        remoteVideoAdapter.notifyItemRemoved(getSpecificPosition(mediaModel));
        removeSpecificRemote(mediaModel);
        remoteVideoAdapter.setDataList(remoteList);
    }

    private void removeSpecificRemote(IMediaModel mediaModel) {
        if (remoteList == null || remoteList.size() <= 0) return;
        for (int i = 0; i < remoteList.size(); i++) {
            if (mediaModel.getUser().getUserId().equals(remoteList.get(i).getUser().getUserId())) {
                remoteList.remove(i);
                return;
            }
        }
    }

    private int getSpecificPosition(IMediaModel mediaModel) {
        if (remoteList == null || remoteList.size() <= 0) return 0;
        for (int i = 0; i < remoteList.size(); i++) {
            if (mediaModel.getUser().getUserId().equals(remoteList.get(i).getUser().getUserId())) {
                return i;
            }
        }
        return 0;
    }
}
