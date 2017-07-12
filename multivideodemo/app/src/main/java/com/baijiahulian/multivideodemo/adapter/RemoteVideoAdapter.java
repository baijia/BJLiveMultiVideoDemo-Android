package com.baijiahulian.multivideodemo.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.baijiahulian.avsdk.liveplayer.ViESurfaceViewRenderer;
import com.baijiahulian.livecore.models.imodels.IMediaModel;
import com.baijiahulian.livecore.wrapper.LPPlayer;
import com.baijiahulian.multivideodemo.R;
import com.baijiahulian.multivideodemo.multi.MultiVideoRouterListener;

import java.util.List;

/**
 * Created by wangkangfei on 17/7/11.
 */

public class RemoteVideoAdapter extends RecyclerView.Adapter<RemoteVideoAdapter.RemoteViewHolder> {
    private Context context;
    private List<IMediaModel> dataList;
    private MultiVideoRouterListener routerListener;

    public RemoteVideoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RemoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout llItem = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_remote_video, parent, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(5, 5, 5, 5);
        View videoView = ViESurfaceViewRenderer.CreateRenderer(context, true);
        ((SurfaceView) videoView).setZOrderMediaOverlay(true);
        videoView.setLayoutParams(lp);
        llItem.addView(videoView);
        return new RemoteViewHolder(llItem);
    }

    @Override
    public void onBindViewHolder(RemoteViewHolder holder, int position) {
        IMediaModel mediaModel = dataList.get(position);
        getRouterListener().getLiveRoom().getPlayer().playVideo(mediaModel.getUser().getUserId(), holder.llItem.getChildAt(0));
    }

    @Override
    public int getItemCount() {
        return getDataList().size();
    }

    public List<IMediaModel> getDataList() {
        return dataList;
    }

    public void setDataList(List<IMediaModel> dataList) {
        this.dataList = dataList;
    }

    public MultiVideoRouterListener getRouterListener() {
        return routerListener;
    }

    public void setRouterListener(MultiVideoRouterListener routerListener) {
        this.routerListener = routerListener;
    }


    class RemoteViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llItem;

        public RemoteViewHolder(View itemView) {
            super(itemView);
            llItem = (LinearLayout) itemView;
        }
    }
}
