package com.houde.competitive.lagua.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import java.util.List;

/**
 * @author : houde
 *         Date : 18-1-20
 *         Desc :
 */

public class RecommendBean implements MultiItemEntity {
    public final static int TYPE_IMAGE = 0;
    public final static int TYPE_ARTICLE = 1;
    public final static int TYPE_SATINS = 2;
    public final static int TYPE_HEADER = 3;
    public int id;
    public int type;
    public String title;
    public int readCount;
    public String imagePath;
    public List<String> imagePaths;
    @Override
    public int getItemType() {
        return type;
    }
}
