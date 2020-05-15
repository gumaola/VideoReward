package cn.nano.main.server.result;

import java.util.List;

public class MemberInfoResult extends BaseResult {


    /**
     * data : {"mInfo":{"member_id":8,"member_avatar":"ddd","nick_name":"ddd","member_age":0,"member_level":0,"member_sex":0,"member_city":"dd","coin_num":0,"fans_num":0,"promo_code":"00000Q","promo_img":"http://47.57.22.49/uploads/poster/8.png","follow_num":0,"like_vieo_num":1,"self_video_num":0,"member_autograph":"dddd"},"myVideo":[{"video_id":3,"video_title":"这个就是厉害了","video_img":"http://47.57.22.49/uploads/video/158730257462271.jpg","support_num":1}],"likeVideo":[{"video_id":3,"video_title":"这个就是厉害了","video_img":"http://47.57.22.49/uploads/video/158730257462271.jpg","support_num":1},{"video_id":2,"video_title":"贺岁视频","video_img":"http://47.57.22.49/uploads/video/158527538112425.jpg","support_num":1}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * mInfo : {"member_id":8,"member_avatar":"ddd","nick_name":"ddd","member_age":0,"member_level":0,"member_sex":0,"member_city":"dd","coin_num":0,"fans_num":0,"promo_code":"00000Q","promo_img":"http://47.57.22.49/uploads/poster/8.png","follow_num":0,"like_vieo_num":1,"self_video_num":0,"member_autograph":"dddd"}
         * myVideo : [{"video_id":3,"video_title":"这个就是厉害了","video_img":"http://47.57.22.49/uploads/video/158730257462271.jpg","support_num":1}]
         * likeVideo : [{"video_id":3,"video_title":"这个就是厉害了","video_img":"http://47.57.22.49/uploads/video/158730257462271.jpg","support_num":1},{"video_id":2,"video_title":"贺岁视频","video_img":"http://47.57.22.49/uploads/video/158527538112425.jpg","support_num":1}]
         */

        private MemberInfo mInfo;
        private List<VideoInfo> myVideo;
        private List<VideoInfo> likeVideo;

        public MemberInfo getMInfo() {
            return mInfo;
        }

        public void setMInfo(MemberInfo mInfo) {
            this.mInfo = mInfo;
        }

        public List<VideoInfo> getMyVideo() {
            return myVideo;
        }

        public void setMyVideo(List<VideoInfo> myVideo) {
            this.myVideo = myVideo;
        }

        public List<VideoInfo> getLikeVideo() {
            return likeVideo;
        }

        public void setLikeVideo(List<VideoInfo> likeVideo) {
            this.likeVideo = likeVideo;
        }
    }
}
