package cn.nano.main.server.result;

import java.util.List;

public class FollowListResult extends BaseResult {


    /**
     * data : {"followList":[{"member_id":2,"nick_name":"","member_avatar":""}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<MemberSimpleResult> followList;

        public List<MemberSimpleResult> getFollowList() {
            return followList;
        }

        public void setFollowList(List<MemberSimpleResult> followList) {
            this.followList = followList;
        }
    }
}
