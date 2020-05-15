package cn.nano.main.server;

public enum ServerApiEntity {

    LOGIN("login", "login/index"),
    REGISTER("register", "register/index"),
    VIDEOLIST("tag", "video/index"),
    FOLLOW("follow", "follow/index"),
    SELFINFO("selfinfo", "home/index"),
    OTHERINFO("otherinfo", "home/noself"),
    MODIFYINFO("modifyinfo", "home/myInfoUpdate"),
    CONFIGINFO("configinfo", "home/basicInfo"),
    GETCOIN("get_coin", "coin/index"),
    BILLING("billing", "level/levelOrder"),
    LEVEL("level", "level/index"),
    UPLEVEL("up_level", "level/buyLevel"),
    withdraw("withdraw", "coin/cashCoin"),
    WITHDRAWPSW("withdrawpsw", "home/coinPassword"),
    CERTIFICATION("cert", "home/memberIdentity"),
    VIDEOUPLOAD("video_upload", "video/videoUpload"),
    MYVIDEO("my_video", "home/myVideo"),
    FAVORVIDEO("favor_video", "home/myLikeVideo"),
    COMMENTLIST("comment_list", "comment/videoComment"),
    POST_COMMENT("post_comment", "comment/index"),
    MY_FANS_OR_FOLLOW("follow_or_fans", "follow/fllowOrFans"),
    MY_INVITE("my_invite", "home/getInviterList"),
    PUSH_CID("push_cid", "message/memberCid");


    String TAG;
    String PATH;

    ServerApiEntity(String tag, String path) {
        this.TAG = tag;
        this.PATH = path;
    }
}
