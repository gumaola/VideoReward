package cn.nano.main.server.result;

import java.util.List;

public class BillingListResult extends BaseResult {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * levo_id : 4
         * levo_no : 20200411151130505499
         * level_id : 1
         * profit_day : 30
         * level_profit : 1.00
         * withdraw_money : 20
         * level_time : 2020-05-11 15:11:30
         * levo_status : 0
         * levo_finsh : 0
         * finish_day : 0
         * member_id : 1
         * levo_time : 2020-04-12 15:11:30
         * level_money : 1000
         */

        private int levo_id;
        private String levo_no;
        private int level_id;
        private int profit_day;
        private String level_profit;
        private int withdraw_money;
        private String level_time;
        private int levo_status;
        private int levo_finsh;
        private int finish_day;
        private int member_id;
        private String levo_time;
        private int level_money;

        public int getLevo_id() {
            return levo_id;
        }

        public void setLevo_id(int levo_id) {
            this.levo_id = levo_id;
        }

        public String getLevo_no() {
            return levo_no;
        }

        public void setLevo_no(String levo_no) {
            this.levo_no = levo_no;
        }

        public int getLevel_id() {
            return level_id;
        }

        public void setLevel_id(int level_id) {
            this.level_id = level_id;
        }

        public int getProfit_day() {
            return profit_day;
        }

        public void setProfit_day(int profit_day) {
            this.profit_day = profit_day;
        }

        public String getLevel_profit() {
            return level_profit;
        }

        public void setLevel_profit(String level_profit) {
            this.level_profit = level_profit;
        }

        public int getWithdraw_money() {
            return withdraw_money;
        }

        public void setWithdraw_money(int withdraw_money) {
            this.withdraw_money = withdraw_money;
        }

        public String getLevel_time() {
            return level_time;
        }

        public void setLevel_time(String level_time) {
            this.level_time = level_time;
        }

        public int getLevo_status() {
            return levo_status;
        }

        public void setLevo_status(int levo_status) {
            this.levo_status = levo_status;
        }

        public int getLevo_finsh() {
            return levo_finsh;
        }

        public void setLevo_finsh(int levo_finsh) {
            this.levo_finsh = levo_finsh;
        }

        public int getFinish_day() {
            return finish_day;
        }

        public void setFinish_day(int finish_day) {
            this.finish_day = finish_day;
        }

        public int getMember_id() {
            return member_id;
        }

        public void setMember_id(int member_id) {
            this.member_id = member_id;
        }

        public String getLevo_time() {
            return levo_time;
        }

        public void setLevo_time(String levo_time) {
            this.levo_time = levo_time;
        }

        public int getLevel_money() {
            return level_money;
        }

        public void setLevel_money(int level_money) {
            this.level_money = level_money;
        }
    }
}
