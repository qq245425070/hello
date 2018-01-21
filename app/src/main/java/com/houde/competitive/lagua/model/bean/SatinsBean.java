package com.houde.competitive.lagua.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author : houde
 *         Date : 18-1-21
 *         Desc :
 */

public class SatinsBean implements MultiItemEntity {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT = 1;
    public String content;
    public long upvoteCount;
    public long caiCount;
    public long shareCount;
    public int id;
    public int type;
    @Override
    public int getItemType() {
        return type;
    }
}
