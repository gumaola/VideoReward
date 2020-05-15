package cn.nano.main.server.result;

import java.util.List;

public class CommentListResult extends BaseResult {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * comment_id : 1
         * video_id : 1
         * comment_detail : 111
         * comment_time : 2020-04-22 23:52:42
         * member_id : 2
         * comment_num : 0
         * nick_name : 波波老湿11
         * member_avatar : http://47.57.22.49/uploads/avatar/20200406/354018326707b297ac00d0eea5c23b1d.jpg
         */

        private int comment_id;
        private int video_id;
        private String comment_detail;
        private String comment_time;
        private int member_id;
        private int comment_num;
        private String nick_name;
        private String member_avatar;

        public int getComment_id() {
            return comment_id;
        }

        public void setComment_id(int comment_id) {
            this.comment_id = comment_id;
        }

        public int getVideo_id() {
            return video_id;
        }

        public void setVideo_id(int video_id) {
            this.video_id = video_id;
        }

        public String getComment_detail() {
            return comment_detail;
        }

        public void setComment_detail(String comment_detail) {
            this.comment_detail = comment_detail;
        }

        public String getComment_time() {
            return comment_time;
        }

        public void setComment_time(String comment_time) {
            this.comment_time = comment_time;
        }

        public int getMember_id() {
            return member_id;
        }

        public void setMember_id(int member_id) {
            this.member_id = member_id;
        }

        public int getComment_num() {
            return comment_num;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
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
    }
}
