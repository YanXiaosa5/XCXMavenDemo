package com.fanhua.uiadapter.enums;

/**
 * Created by Misl on 2016/2/22.
 */

public enum ViewScaleType {

    /**
     * View不执行缩放
     */
    NO_SCALE(0),

    /**
     * View按照最大边缩放
     */
    AS_MAX_EDGES_SCALE(0),

    /**
     * View按照最小边缩放
     */
    AS_MIN_EDGES_SCALE(1),

    /**
     * View按照各自边缩放
     */
    AS_WIDTH_EDGES_SCALE(2),

    /**
     * View按照各自边缩放
     */
    AS_HEIGHT_EDGES_SCALE(3),

    /**
     * View按照各自边缩放
     */
    AS_TWO_EDGES_SCALE(4);

    ViewScaleType(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }

    int tag;
}
