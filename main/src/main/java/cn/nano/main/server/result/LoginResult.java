package cn.nano.main.server.result;

import com.google.gson.annotations.SerializedName;

public class LoginResult extends BaseResult {

    /**
     * data : {"mtoken":"bf1b672b1f6da55cde0accdc832465d9"}
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
         * mtoken : bf1b672b1f6da55cde0accdc832465d9
         */

        private String mtoken;
        private int isIdentity;//0未认证，1认证

        public String getMtoken() {
            return mtoken;
        }

        public void setMtoken(String mtoken) {
            this.mtoken = mtoken;
        }

        public int getIsIdentity() {
            return isIdentity;
        }

        public void setIsIdentity(int isIdentity) {
            this.isIdentity = isIdentity;
        }
    }
}
