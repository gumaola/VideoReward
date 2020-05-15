package cn.nano.main.server;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.lcodecore.tkrefreshlayout.footer.BallPulseView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.nano.common.app.GlobalConfig;
import cn.nano.common.okhttp.OkHttpUtils;
import cn.nano.common.okhttp.builder.PostFormBuilder;
import cn.nano.common.utils.PreferenceUtil;
import cn.nano.main.constant.AppPrefs;
import cn.nano.main.server.result.BaseResult;
import cn.nano.main.server.result.ConfigResult;
import okhttp3.MediaType;


public class ServerApi {

    private static final String URL_HOST = "http://47.57.22.49/api/";

    /**
     * 注册接口
     *
     * @param callback
     */
    public static void register(String phoneNum, String psw, String confirmPsw, String inviteCode, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("uPhone", phoneNum);
        params.put("uPwd", psw);
        params.put("uConfirmPwd", confirmPsw);
        params.put("uCode", inviteCode);

        post(ServerApiEntity.REGISTER, params, null, callback);
    }

    /**
     * 登陆接口
     *
     * @param phoneNum
     * @param psw
     * @param callback
     */
    public static void login(String phoneNum, String psw, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("uPhone", phoneNum);
        params.put("uPwd", psw);

        post(ServerApiEntity.LOGIN, params, null, callback);
    }


    /**
     * 获取视频列表
     *
     * @param usertoken
     * @param callback
     */
    public static void getVideoList(String usertoken, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", usertoken);

        post(ServerApiEntity.VIDEOLIST, params, null, callback);
    }


    /**
     * 关注视频或者作者[0视频1会员]
     *
     * @param usertoken
     * @param type
     * @param id
     */
    public static void follow(String usertoken, int type, int id, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", usertoken);
        params.put("fType", String.valueOf(type));
        params.put("followID", String.valueOf(id));

        post(ServerApiEntity.FOLLOW, params, null, callback);
    }

    /**
     * 获取用户信息
     *
     * @param usertoken
     * @param callback
     */
    public static void getSelfInfo(String usertoken, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", usertoken);

        post(ServerApiEntity.SELFINFO, params, null, callback);
    }

    /**
     * 获取他人信息
     *
     * @param usertoken
     * @param otherId
     * @param callback
     */
    public static void getOtherUserInfo(String usertoken, int otherId, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", usertoken);
        params.put("memberID", String.valueOf(otherId));


        post(ServerApiEntity.OTHERINFO, params, null, callback);
    }


    /**
     * 更新个人信息
     *
     * @param token
     * @param name
     * @param birthday
     * @param desc
     * @param gender   【0保密1男2女】
     * @param avatar
     * @param city
     * @param callback
     */
    public static void modifyUserInfo(String token, String name, String birthday, String desc, String gender, File avatar, String city, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", token);

        if (!TextUtils.isEmpty(name)) {
            params.put("nickName", name);
        }

        if (!TextUtils.isEmpty(birthday)) {
            params.put("mBirthday", birthday);
        }

        if (!TextUtils.isEmpty(desc)) {
            params.put("mAutograph", desc);
        }

        if (!TextUtils.isEmpty(gender)) {
            params.put("mSex", gender);
        }

        List<PostFormBuilder.FileInput> inputs = null;
        if (avatar != null) {
            inputs = new ArrayList<>();
            PostFormBuilder.FileInput input = new PostFormBuilder.FileInput("mAvatar", "avatar.jpg", avatar);
            inputs.add(input);
        }

        if (!TextUtils.isEmpty(city)) {
            params.put("mCity", city);
        }

        post(ServerApiEntity.MODIFYINFO, params, inputs, callback);
    }


    /**
     * 获取基本config信息
     *
     * @param userToken
     */
    public static void getConfigInfo(String userToken, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", userToken);

        post(ServerApiEntity.CONFIGINFO, params, null, callback);
    }


    /**
     * 兑换金币和上报视频看完接口
     *
     * @param token
     * @param videoId
     * @param finish      是否看完【1看完0未看完默认0】
     * @param needGetCoin 是否获取金币【1获取0为获取默认0】
     * @param coinId      【1获取金币时必填参数】
     * @param callback
     */
    public static void getCoin(String token, int videoId, int finish, int needGetCoin, String coinId, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", token);
        params.put("videoID", String.valueOf(videoId));

        if (finish > 0) {
            params.put("vFinish", String.valueOf(finish));
        }

        if (needGetCoin > 0) {
            params.put("coinType", String.valueOf(needGetCoin));
        }

        if (coinId != null) {
            params.put("coinID", coinId);
        }

        post(ServerApiEntity.GETCOIN, params, null, callback);

    }

    /**
     * 获取订单列表
     *
     * @param token
     * @param type     订单类型【0进行中1已完成】
     * @param page
     * @param num
     * @param callback
     */
    public static void getBillingList(String token, int type, int page, int num, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", token);
        params.put("oType", String.valueOf(type));
        params.put("cPage", String.valueOf(page));
        params.put("perNum", String.valueOf(num));

        post(ServerApiEntity.BILLING, params, null, callback);
    }

    /**
     * 获取等级列表
     *
     * @param token
     * @param callback
     */
    public static void getLevelList(String token, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", token);

        post(ServerApiEntity.LEVEL, params, null, callback);
    }


    /**
     * 升级
     *
     * @param token
     * @param levelId
     * @param callback
     */
    public static void upLevel(String token, int levelId, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", token);
        params.put("levelID", String.valueOf(levelId));

        post(ServerApiEntity.UPLEVEL, params, null, callback);
    }

    /**
     * 提现接口
     *
     * @param token
     * @param money
     * @param psw
     * @param callback
     */
    public static void withdraw(String token, int money, String psw, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", token);
        params.put("cashMoney", String.valueOf(money));
        params.put("cashPwd", psw);


        post(ServerApiEntity.withdraw, params, null, callback);
    }


    /**
     * 设置或修改提现密码
     *
     * @param token
     * @param psw
     * @param confirm
     * @param callback
     */
    public static void modifyWithdrawPsw(String token, String psw, String confirm, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", token);
        params.put("coinPwd", psw);
        params.put("coinPwdConfirmPwd", confirm);

        post(ServerApiEntity.WITHDRAWPSW, params, null, callback);
    }


    /**
     * 实名认证
     *
     * @param token
     * @param name
     * @param idCode
     * @param place
     * @param callback
     */
    public static void certificate(String token, String name, String idCode, String place, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", token);
        params.put("realName", name);
        params.put("cardNo", idCode);
        params.put("nativePlace", place);

        post(ServerApiEntity.CERTIFICATION, params, null, callback);
    }

    /**
     * 上传视频
     *
     * @param token
     * @param video
     * @param videoName
     * @param callback
     */
    public static void uploadVideo(String token, File video, String videoName, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", token);
        params.put("videoTitle", videoName);

        List<PostFormBuilder.FileInput> inputs = null;
        if (video != null) {
            inputs = new ArrayList<>();
            PostFormBuilder.FileInput input = new PostFormBuilder.FileInput("video", "video.mp4", video);
            inputs.add(input);
        }

        post(ServerApiEntity.VIDEOUPLOAD, params, inputs, callback);

    }

    /**
     * 获取我的视频列表
     *
     * @param token
     * @param pageNum
     * @param pageSize
     * @param elseMemberId 其他会员ID【参数存在时会覆盖登录者ID】
     * @param callback
     */
    public static void getMyVideoList(String token, int pageNum, int pageSize, int elseMemberId, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", token);
        params.put("cPage", String.valueOf(pageNum));
        params.put("perNum", String.valueOf(pageSize));

        if (elseMemberId >= 0) {
            params.put("memberID", String.valueOf(elseMemberId));
        }

        post(ServerApiEntity.MYVIDEO, params, null, callback);
    }


    /**
     * 获取我喜欢的视频列表
     *
     * @param token
     * @param pageNum
     * @param pageSize
     * @param callback
     */
    public static void getFavorVideoList(String token, int pageNum, int pageSize, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", token);
        params.put("cPage", String.valueOf(pageNum));
        params.put("perNum", String.valueOf(pageSize));

        post(ServerApiEntity.FAVORVIDEO, params, null, callback);
    }

    /**
     * 发送评论接口
     *
     * @param token
     * @param id
     * @param content
     * @param callback
     */
    public static void postComment(String token, int id, String content, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", token);
        params.put("videoID", String.valueOf(id));
        params.put("commentDetail", content);

        post(ServerApiEntity.POST_COMMENT, params, null, callback);
    }


    /**
     * 获取评论列表接口
     *
     * @param token
     * @param id
     * @param pageNum
     * @param pageSize
     * @param callback
     */
    public static void getCommentList(String token, int id, int pageNum, int pageSize, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", token);
        params.put("cPage", String.valueOf(pageNum));
        params.put("perNum", String.valueOf(pageSize));
        params.put("videoID", String.valueOf(id));

        post(ServerApiEntity.COMMENTLIST, params, null, callback);
    }


    /**
     * 获取我的粉丝或者我的关注
     *
     * @param token
     * @param type     关注类型【1我的关注2我的粉丝】
     * @param pageNum
     * @param pageSize
     * @param callback
     */
    public static void getMyFollowsOrFans(String token, int type, int pageNum, int pageSize, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", token);
        params.put("cPage", String.valueOf(pageNum));
        params.put("perNum", String.valueOf(pageSize));
        params.put("fType", String.valueOf(type));

        post(ServerApiEntity.MY_FANS_OR_FOLLOW, params, null, callback);
    }


    /**
     * 获取我邀请的下线
     *
     * @param token
     * @param pageNum
     * @param pageSize
     * @param callback
     */
    public static void getMyInvites(String token, int pageNum, int pageSize, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", token);
        params.put("cPage", String.valueOf(pageNum));
        params.put("perNum", String.valueOf(pageSize));

        post(ServerApiEntity.MY_INVITE, params, null, callback);
    }

    /**
     * 上传用户cid
     *
     * @param token
     * @param cid
     * @param callback
     */
    public static void pushCid(String token, String cid, AutoLoginOutCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mtoken", token);
        params.put("mcid", cid);

        post(ServerApiEntity.PUSH_CID, params, null, callback);
    }


    /**
     * 所有接口统一调用
     *
     * @param entity
     * @param params
     * @param callback
     */
    private static void post(ServerApiEntity entity, HashMap<String, String> params, List<PostFormBuilder.FileInput> fileInputs, AutoLoginOutCallback callback) {
        PostFormBuilder builder = OkHttpUtils.post().url(URL_HOST + entity.PATH)
                .tag(entity.TAG);

        if (fileInputs != null && fileInputs.size() > 0) {
            for (PostFormBuilder.FileInput input : fileInputs) {
                builder.addFile(input.key, input.filename, input.file);
            }
        }

        builder.params(params)
                .build().execute(callback);
    }
}
