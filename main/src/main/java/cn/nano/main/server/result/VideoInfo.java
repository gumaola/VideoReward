package cn.nano.main.server.result;

public class VideoInfo {
    /**
     * video_id : 49
     * video_title : 是一生一世呀
     * video_img : http://47.57.22.49/uploads/video/158762204843436.jpg
     * video_url : http://47.57.22.49/uploads/video/158762204843436.mp4
     * support_num : 0
     * comment_num : 0
     * forward_num : 0
     * is_coin : 1
     * is_follow : 2
     */

    private int video_id;
    private String video_title;
    private String video_img;
    private String video_url;
    private int support_num;
    private int comment_num;
    private int forward_num;
    private int is_coin;
    private int is_follow;

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_img() {
        return video_img;
    }

    public void setVideo_img(String video_img) {
        this.video_img = video_img;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getSupport_num() {
        return support_num;
    }

    public void setSupport_num(int support_num) {
        this.support_num = support_num;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public int getForward_num() {
        return forward_num;
    }

    public void setForward_num(int forward_num) {
        this.forward_num = forward_num;
    }

    public int getIs_coin() {
        return is_coin;
    }

    public void setIs_coin(int is_coin) {
        this.is_coin = is_coin;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }
}
