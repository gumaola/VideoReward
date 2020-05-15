package cn.nano.main.server.result;

import java.util.List;

public class MyInvitersResult extends BaseResult {

    private List<MemberSimpleResult> data;

    public List<MemberSimpleResult> getData() {
        return data;
    }

    public void setData(List<MemberSimpleResult> data) {
        this.data = data;
    }

}
