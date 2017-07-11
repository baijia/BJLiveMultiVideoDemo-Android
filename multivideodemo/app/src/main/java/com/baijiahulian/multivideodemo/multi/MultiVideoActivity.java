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
import com.baijiahulian.livecore.launch.LPLaunchListener;
import com.baijiahulian.multivideodemo.R;

public class MultiVideoActivity extends AppCompatActivity implements MultiVideoContract.View, MultiVideoRouterListener {
    //view
//    private CameraGLSurfaceView glCamera;
    private FrameLayout flMySelf;
    private RecyclerView rvRemoteVideo;
    private MaterialDialog loadingDlg;

    //logic
    private MultiVideoContract.Presenter mPresenter;
    private LiveRoom liveRoom;

    //listeners
    private LPLaunchListener launchListener;

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

    }

    //进入房间成功后的处理
    private void launchSuccess() {
        loadingDlg.dismiss();
        CameraGLSurfaceView glCamera = new CameraGLSurfaceView(this);
        glCamera.setZOrderMediaOverlay(true);
        flMySelf.addView(glCamera);
        liveRoom.getRecorder().setPreview(glCamera);

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
    public void setLiveRoom(LiveRoom liveRoom) {
        this.liveRoom = liveRoom;
    }

    @Override
    public LiveRoom getLiveRoom() {
        return liveRoom;
    }
}
