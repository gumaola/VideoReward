package cn.nano.main.server.result;

import java.util.List;

public class LevelListResult extends BaseResult {

    /**
     * data : {"levelList":[{"level_id":1,"level_title":"T1","level_coin":100,"level_profit":1,"profit_day":30,"level_icon":"","level_remark":"T1级别说明"},{"level_id":2,"level_title":"T2","level_coin":100,"level_profit":2,"profit_day":30,"level_icon":"","level_remark":"T1级别说明"},{"level_id":3,"level_title":"T3","level_coin":3000,"level_profit":3,"profit_day":60,"level_icon":"","level_remark":"T3"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<LevelListBean> levelList;

        public List<LevelListBean> getLevelList() {
            return levelList;
        }

        public void setLevelList(List<LevelListBean> levelList) {
            this.levelList = levelList;
        }

        public static class LevelListBean {
            /**
             * level_id : 1
             * level_title : T1
             * level_coin : 100
             * level_profit : 1
             * profit_day : 30
             * level_icon :
             * level_remark : T1级别说明
             */

            private int level_id;
            private String level_title;
            private int level_coin;
            private int level_profit;
            private int profit_day;
            private String level_icon;
            private String level_remark;

            public int getLevel_id() {
                return level_id;
            }

            public void setLevel_id(int level_id) {
                this.level_id = level_id;
            }

            public String getLevel_title() {
                return level_title;
            }

            public void setLevel_title(String level_title) {
                this.level_title = level_title;
            }

            public int getLevel_coin() {
                return level_coin;
            }

            public void setLevel_coin(int level_coin) {
                this.level_coin = level_coin;
            }

            public int getLevel_profit() {
                return level_profit;
            }

            public void setLevel_profit(int level_profit) {
                this.level_profit = level_profit;
            }

            public int getProfit_day() {
                return profit_day;
            }

            public void setProfit_day(int profit_day) {
                this.profit_day = profit_day;
            }

            public String getLevel_icon() {
                return level_icon;
            }

            public void setLevel_icon(String level_icon) {
                this.level_icon = level_icon;
            }

            public String getLevel_remark() {
                return level_remark;
            }

            public void setLevel_remark(String level_remark) {
                this.level_remark = level_remark;
            }
        }
    }
}
