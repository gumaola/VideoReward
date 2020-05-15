package cn.nano.main.server.result;

import java.util.List;

public class SingleVideoListResult extends BaseResult {

    private List<VideoInfo> data;

    public List<VideoInfo> getData() {
        return data;
    }

    public void setData(List<VideoInfo> data) {
        this.data = data;
    }
}
