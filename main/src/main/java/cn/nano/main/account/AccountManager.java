package cn.nano.main.account;

import android.content.Context;
import android.text.TextUtils;

import java.nio.charset.StandardCharsets;

import cn.nano.common.app.GlobalConfig;
import cn.nano.common.utils.Base64Util;
import cn.nano.common.utils.GsonUtil;
import cn.nano.common.utils.PreferenceUtil;
import cn.nano.main.server.result.MemberInfo;
import cn.nano.main.server.result.MemberInfoResult;

public enum AccountManager {
    INSTANCE;


    private static final String VIDEO_REWARD_ACCOUNT_FILE = "mars_room_account";
    private static final String ACCOUNT_USER = "user";

    //个推需要记录token
    private static final String PUSH_TOKEN = "push_token";

    //用于确定用户是否登陆，及限制多设备登陆
    private static final String USER_TOKEN = "user_token";


    //用于记录用户是否实名认证成功
    private static final String USER_CERT_STATE = "cert_state";

    private MemberInfo mUserInfo;

    public void syncUserToken(String token) {
        PreferenceUtil.putString(GlobalConfig.getInstance().currentApp(), VIDEO_REWARD_ACCOUNT_FILE, USER_TOKEN, token);
    }

    public String getUserToken() {
        return PreferenceUtil.getString(GlobalConfig.getInstance().currentApp(), VIDEO_REWARD_ACCOUNT_FILE, USER_TOKEN, null);
    }

    public void syncUserCertState(boolean certed) {
        PreferenceUtil.putBoolean(GlobalConfig.getInstance().currentApp(), VIDEO_REWARD_ACCOUNT_FILE, USER_CERT_STATE, certed);
    }

    public boolean getUserCertState() {
        return PreferenceUtil.getBoolean(GlobalConfig.getInstance().currentApp(), VIDEO_REWARD_ACCOUNT_FILE, USER_CERT_STATE, false);
    }


    public void syncPushToken(String token) {
        PreferenceUtil.putString(GlobalConfig.getInstance().currentApp(), VIDEO_REWARD_ACCOUNT_FILE, PUSH_TOKEN, token);
    }

    public String getPushToken() {
        return PreferenceUtil.getString(GlobalConfig.getInstance().currentApp(), VIDEO_REWARD_ACCOUNT_FILE, PUSH_TOKEN, null);
    }

    //保存用户信息
    public void syncUserInfo(MemberInfo member) {
        mUserInfo = member;

        //使用base64加密后再存储,防止特殊字符导致crash
        String userJson = GsonUtil.createGson().toJson(mUserInfo);
        String base64 = Base64Util.encode(userJson.getBytes(StandardCharsets.UTF_8));
        PreferenceUtil.putString(GlobalConfig.getInstance().currentApp(), VIDEO_REWARD_ACCOUNT_FILE, ACCOUNT_USER, base64);
    }

    //读取用户信息
    public MemberInfo loadLocalUserInfo() {
        String base64 = PreferenceUtil.getString(GlobalConfig.getInstance().currentApp(),
                VIDEO_REWARD_ACCOUNT_FILE, ACCOUNT_USER, null);

        if (!TextUtils.isEmpty(base64)) {
            byte[] decodeByte = Base64Util.decode(base64);
            String json = new String(decodeByte, StandardCharsets.UTF_8);
            if (!TextUtils.isEmpty(json)) {
                try {
                    mUserInfo = GsonUtil.createGson().fromJson(json, MemberInfo.class);
                } catch (Exception e) {
                }
            }
        }

        return mUserInfo;
    }


    public boolean isLogin() {
        return !TextUtils.isEmpty(getUserToken()) || getUserCertState();
    }

    public void clear(Context context) {
        PreferenceUtil.clear(context, VIDEO_REWARD_ACCOUNT_FILE);
    }
}
