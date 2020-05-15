package cn.nano.main.server.result;

import java.util.List;

public class VideoListResult extends BaseResult {

    /**
     * data : {"vlist":[{"vInfo":{"video_id":2,"video_title":"贺岁视频","video_img":"/uploads/video/158527538112425.jpg","video_url":"/uploads/video/158527538112425.mp4","support_num":0,"comment_num":0,"forward_num":null,"is_follow":2},"mInfo":{"member_id":2,"nick_name":null,"member_avatar":null,"is_follow":1}},{"vInfo":{"video_id":1,"video_title":"新年贺词","video_img":"/uploads/video/158520830935809.jpg","video_url":"/uploads/video/158520830935809.mp4","support_num":1,"comment_num":0,"forward_num":null,"is_follow":1},"mInfo":{"member_id":2,"nick_name":null,"member_avatar":null,"is_follow":1}}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<VlistBean> vlist;

        public List<VlistBean> getVlist() {
            return vlist;
        }

        public void setVlist(List<VlistBean> vlist) {
            this.vlist = vlist;
        }

        public static class VlistBean {
            /**
             * vInfo : {"video_id":2,"video_title":"贺岁视频","video_img":"/uploads/video/158527538112425.jpg","video_url":"/uploads/video/158527538112425.mp4","support_num":0,"comment_num":0,"forward_num":null,"is_follow":2}
             * mInfo : {"member_id":2,"nick_name":null,"member_avatar":null,"is_follow":1}
             */

            private boolean isPlayComplete;

            private boolean isRefresh;

            private VideoInfo vInfo;
            private MInfoBean mInfo;

            public VideoInfo getVInfo() {
                return vInfo;
            }

            public void setVInfo(VideoInfo vInfo) {
                this.vInfo = vInfo;
            }

            public MInfoBean getMInfo() {
                return mInfo;
            }

            public void setMInfo(MInfoBean mInfo) {
                this.mInfo = mInfo;
            }

            public boolean isRefresh() {
                return isRefresh;
            }

            public void setRefresh(boolean refresh) {
                isRefresh = refresh;
            }

            public boolean isPlayComplete() {
                return isPlayComplete;
            }

            public void setPlayComplete(boolean playComplete) {
                isPlayComplete = playComplete;
            }


            public static class MInfoBean {
                /**
                 * member_id : 2
                 * nick_name : null
                 * member_avatar : null
                 * is_follow : 1
                 */

                private int member_id;
                private String nick_name;
                private String member_avatar;
                private int is_follow;

                public int getMember_id() {
                    return member_id;
                }

                public void setMember_id(int member_id) {
                    this.member_id = member_id;
                }

                public String getNick_name() {
                    return nick_name;
                }

                public void setNick_name(String nick_name) {
                    this.nick_name = nick_name;
                }

                public String getMember_avatar() {
                    return member_avatar;
                }

                public void setMember_avatar(String member_avatar) {
                    this.member_avatar = member_avatar;
                }

                public int getIs_follow() {
                    return is_follow;
                }

                public void setIs_follow(int is_follow) {
                    this.is_follow = is_follow;
                }
            }
        }
    }
}
