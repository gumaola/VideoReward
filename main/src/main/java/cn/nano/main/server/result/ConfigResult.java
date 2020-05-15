package cn.nano.main.server.result;

public class ConfigResult extends BaseResult {

    /**
     * data : {"notice":{"notice_id":1,"notice_title":"系统公告","notice_detail":"公告详情"},"coin":{"id":"1","coin":50,"time":300},"isUpload":1,"isComment":1,"cost":1}
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
         * notice : {"notice_id":1,"notice_title":"系统公告","notice_detail":"公告详情"}
         * coin : {"id":"1","coin":50,"time":300}
         * isUpload : 1
         * isComment : 1
         * cost : 1
         */

        private NoticeBean notice;
        private CoinConfigBean coin;
        private int isUpload;
        private int isComment;
        private float cost;

        public NoticeBean getNotice() {
            return notice;
        }

        public void setNotice(NoticeBean notice) {
            this.notice = notice;
        }

        public CoinConfigBean getCoin() {
            return coin;
        }

        public void setCoin(CoinConfigBean coin) {
            this.coin = coin;
        }

        public int getIsUpload() {
            return isUpload;
        }

        public void setIsUpload(int isUpload) {
            this.isUpload = isUpload;
        }

        public int getIsComment() {
            return isComment;
        }

        public void setIsComment(int isComment) {
            this.isComment = isComment;
        }

        public float getCost() {
            return cost;
        }

        public void setCost(float cost) {
            this.cost = cost;
        }

        public static class NoticeBean {
            /**
             * notice_id : 1
             * notice_title : 系统公告
             * notice_detail : 公告详情
             */

            private int notice_id;
            private String notice_title;
            private String notice_detail;

            public int getNotice_id() {
                return notice_id;
            }

            public void setNotice_id(int notice_id) {
                this.notice_id = notice_id;
            }

            public String getNotice_title() {
                return notice_title;
            }

            public void setNotice_title(String notice_title) {
                this.notice_title = notice_title;
            }

            public String getNotice_detail() {
                return notice_detail;
            }

            public void setNotice_detail(String notice_detail) {
                this.notice_detail = notice_detail;
            }
        }
    }
}
