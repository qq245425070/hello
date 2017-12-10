package cn.gietv.mlive.constants;

import android.support.v4.util.ArrayMap;

/**
 * author：steven
 * datetime：15/9/16 21:22
 */
public interface CommConstants {
    String API_VERSION = "V1";
    String API_VERSION_MVR="V2";
    int FOLLOW_ACTION_ON = 1;
    int FOLLOW_ACTION_OFF = 2;

    int FOLLOW_TYPE_LIVE = 1;
    int FOLLOW_TYPE_VIDEO = 2;
    int FOLLOW_TYPE_GAME = 3;
    int FOLLOW_TYPE_AREA = 5;

    int COMMON_PAGE_COUNT = 20;
    int COMMON_PAGE_COUNT2 = 30;
    int DEFAULT_SORT_ONLINE_PERSON = 1;
    String GAME_TYPE_ALL = "all";

    int TRUE = 1;
    int FALSE = 0;

    int FOLLOW_TRUE = 1;
    int FOLLOW_FALSE =2;


    int CAROUSEL_TYPE_LIVE = 1;
    int CAROUSEL_TYPE_VIDEO = 2;
    int CAROUSEL_TYPE_GAME = 3;
    int CAROUSEL_TYPE_COMPERE = 4;
    int CAROUSEL_TYPE_AREA = 5;//专区
    int CAROUSEL_TYPE_360 = 6;//360视频
    int CAROUSEL_TYPE_ALBUM = 7;//专辑
    int CAROUSEL_TYPE_ANCHOR = 8;//作者
    int CAROUSEL_TYPE_3D = 9;//3d视频
    int CAROUSEL_TYPE_WCA = 80;//WCA报名
    int TYPE_COMMENT = 10;//评论type
    int TYPE_TAG = 11;

    int THEME_GREEN = 1;
    int THEME_BLUE = 2;
    int THEME_ORANGE = 3;
    int THEME_YELLOW = 4;
    int THEME_RED = 5;
    int THEME_PURPLE = 6;
//    String NULL_USER_ID = "visitorid";
//    String NULL_TOKEN = "visitortoken";

    String FRAGMENT_TAG_GAME_INFO = "tag_game_info";
    String FRAGMENT_TAG_COMPERE_INFO = "tag_compere_info";


    ArrayMap<Integer, Integer> THEME_MAP = new ArrayMap<Integer, Integer>() {
        {
            put(THEME_GREEN, cn.gietv.mlive.R.style.theme_green);
            put(THEME_BLUE, cn.gietv.mlive.R.style.theme_blue);
            put(THEME_ORANGE, cn.gietv.mlive.R.style.theme_orange);
            put(THEME_PURPLE, cn.gietv.mlive.R.style.theme_purple);
            put(THEME_RED, cn.gietv.mlive.R.style.theme_red);
            put(THEME_YELLOW, cn.gietv.mlive.R.style.theme_yellow);
        }
    };
    ArrayMap<Integer, Integer> COLOR_BACKGROUND = new ArrayMap<Integer, Integer>() {
        {
            put(THEME_GREEN, cn.gietv.mlive.R.drawable.shape_bg_green);
            put(THEME_BLUE, cn.gietv.mlive.R.drawable.shape_bg_blue);
            put(THEME_ORANGE, cn.gietv.mlive.R.drawable.shape_bg_orange);
            put(THEME_PURPLE, cn.gietv.mlive.R.drawable.shape_bg_purple);
            put(THEME_RED, cn.gietv.mlive.R.drawable.shape_bg_red);
            put(THEME_YELLOW, cn.gietv.mlive.R.drawable.shape_bg_yellow);
        }
    };
    ArrayMap<Integer, Integer> COLOR_TEXT = new ArrayMap<Integer, Integer>() {
        {
            put(THEME_GREEN, cn.gietv.mlive.R.color.color_theme_green);
            put(THEME_BLUE, cn.gietv.mlive.R.color.color_theme_blue);
            put(THEME_ORANGE, cn.gietv.mlive.R.color.color_theme_orange);
            put(THEME_PURPLE, cn.gietv.mlive.R.color.color_theme_purple);
            put(THEME_RED, cn.gietv.mlive.R.color.color_theme_red);
            put(THEME_YELLOW, cn.gietv.mlive.R.color.color_theme_yellow);
        }
    };
    ArrayMap<Integer, Integer> COLOR_TEXT_SELECT = new ArrayMap<Integer, Integer>() {
        {
            put(THEME_GREEN, cn.gietv.mlive.R.color.color_theme_green_selected);
            put(THEME_BLUE, cn.gietv.mlive.R.color.color_theme_blue_selected);
            put(THEME_ORANGE, cn.gietv.mlive.R.color.color_theme_orange_selected);
            put(THEME_PURPLE, cn.gietv.mlive.R.color.color_theme_purple_selected);
            put(THEME_RED, cn.gietv.mlive.R.color.color_theme_red_selected);
            put(THEME_YELLOW, cn.gietv.mlive.R.color.color_theme_yellow_selected);
        }
    };
}
