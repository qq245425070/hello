package cn.gietv.mlive.modules.login.bean;

import cn.gietv.mlive.modules.usercenter.bean.UserCenterBean;

/**
 * author：steven
 * datetime：15/9/15 15:35
 *
 */
public class UserInfo {

    /**
     * userinfo : {"game":5,"myfollow":4,"gender":0,"nickname":"测试用户1","follows":1,"photo":3,"id":"55f26d85aa45eb86c364d191","avatar":"12","video":1,"email":"asdfadf@163.com"}
     * token : fasdfasdfasdfasdfasdfasfdasdfasfsa
     */
    public UserCenterBean.UserinfoEntity userinfo;
    public String token;
}
