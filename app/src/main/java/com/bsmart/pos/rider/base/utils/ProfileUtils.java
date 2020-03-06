package com.bsmart.pos.rider.base.utils;

import com.blankj.utilcode.util.SPUtils;
import com.bsmart.pos.rider.base.App;
import com.bsmart.pos.rider.base.api.bean.ProfileBean;


public class ProfileUtils {
    private ProfileUtils() {
    }


    public static void saveProfile(ProfileBean bean) {
        SPUtils.getInstance().put("profile", App.gson.toJson(bean));
    }

    /**
     * todo: update
     */
    public static void removeSharePreference() {
        SPUtils.getInstance().remove("profile");
        SPUtils.getInstance().remove("officer_id");
        SPUtils.getInstance().remove("officer_password");
        SPUtils.getInstance().remove("cmc_to_call");
    }

    public static ProfileBean getProfile() {
        String profileString = SPUtils.getInstance().getString("profile");
        return App.gson.fromJson(profileString, ProfileBean.class);
    }

    public static void saveIdAndPassword(String id, String password) {
        SPUtils.getInstance().put("officer_id", id);
        SPUtils.getInstance().put("officer_password", password);
    }

    public static String getOfficerId() {
        return SPUtils.getInstance().getString("officer_id");
    }

    public static String getOfficerPassword() {
        return SPUtils.getInstance().getString("officer_password");
    }

    public static int getCmcToCall() {
        return SPUtils.getInstance().getInt("cmc_to_call");
    }

    public static void saveCmcToCall(int i) {
        SPUtils.getInstance().put("cmc_to_call", i);
    }
}
