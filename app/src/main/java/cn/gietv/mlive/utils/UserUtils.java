package cn.gietv.mlive.utils;

import cn.gietv.mlive.constants.CacheConstants;
import cn.gietv.mlive.constants.HttpConstants;

/**
 * author：steven
 * datetime：15/10/13 17:40
 *
 */
public class UserUtils {
    public static boolean isNotLogin() {
        if(CacheUtils.getCache() == null){
            CacheUtils.initCache();
        }
        return HttpConstants.USER_ID_NONE.equals(CacheUtils.getCache().getAsString(CacheConstants.CACHE_USERID));
    }
}
