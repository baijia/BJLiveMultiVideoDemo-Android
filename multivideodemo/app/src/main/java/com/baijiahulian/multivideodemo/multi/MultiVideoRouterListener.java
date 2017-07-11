package com.baijiahulian.multivideodemo.multi;

import com.baijiahulian.livecore.context.LiveRoom;

/**
 * Created by wangkangfei on 17/7/11.
 */

public interface MultiVideoRouterListener {
    void setLiveRoom(LiveRoom liveRoom);

    LiveRoom getLiveRoom();
}
