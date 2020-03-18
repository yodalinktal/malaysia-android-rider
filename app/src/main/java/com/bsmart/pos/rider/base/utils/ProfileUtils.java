package com.bsmart.pos.rider.base.utils;

import com.blankj.utilcode.util.SPUtils;
import com.bsmart.pos.rider.base.App;
import com.bsmart.pos.rider.base.api.bean.ProfileBean;
import com.bsmart.pos.rider.base.api.bean.RiderBean;


public class ProfileUtils {

    public final static String PROFILE = "profile";
    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";

    private ProfileUtils() {
    }


    public static void saveProfile(RiderBean bean) {
        SPUtils.getInstance().put(PROFILE, App.gson.toJson(bean));
    }

    /**
     * 获取用户Token
     * @return
     */
    public static String getToken(){
        RiderBean riderBean = getRider();
        if (null != riderBean){
            return riderBean.getToken();
        }else{
            return null;
        }
    }

    /**
     * todo: update
     */
    public static void removeSharePreference() {
        SPUtils.getInstance().clear(true);
//        SPUtils.getInstance().remove(PROFILE);
//        SPUtils.getInstance().remove(USERNAME);
//        SPUtils.getInstance().remove(PASSWORD);
    }

    public static RiderBean getRider() {
        String profileString = SPUtils.getInstance().getString(PROFILE);
        return App.gson.fromJson(profileString, RiderBean.class);
    }

    public static void saveIdAndPassword(String username, String password) {
        SPUtils.getInstance().put(USERNAME, username);
        SPUtils.getInstance().put(PASSWORD, password);
    }



    public static String getOfficerId() {
        return SPUtils.getInstance().getString(USERNAME);
    }

    public static String getUsername() {
        return SPUtils.getInstance().getString(USERNAME);
    }

}
