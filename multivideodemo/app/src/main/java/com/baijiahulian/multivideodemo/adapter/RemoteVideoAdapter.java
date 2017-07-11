package com.baijiahulian.multivideodemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.baijiahulian.avsdk.liveplayer.ViESurfaceViewRenderer;

/**
 * Created by wangkangfei on 17/7/11.
 */

public class RemoteVideoAdapter extends RecyclerView.Adapter<RemoteVideoAdapter.RemoteViewHolder> {
    private Context context;

    public RemoteVideoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RemoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View videoView = ViESurfaceViewRenderer.CreateRenderer(context, true);
        ((SurfaceView) videoView).setZOrderMediaOverlay(true);
        return new RemoteViewHolder(videoView);
    }

    @Override
    public void onBindViewHolder(RemoteViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class RemoteViewHolder extends RecyclerView.ViewHolder {

        public RemoteViewHolder(View itemView) {

            super(itemView);
        }
    }
}
