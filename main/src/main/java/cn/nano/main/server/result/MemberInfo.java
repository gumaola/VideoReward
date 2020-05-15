package cn.nano.main.server.result;

public class MemberInfo {
    /**
     * member_id : 8
     * member_avatar : ddd
     * nick_name : ddd
     * member_age : 0
     * member_level : 0
     * member_sex : 0
     * member_city : dd
     * coin_num : 0
     * fans_num : 0
     * promo_code : 00000Q
     * promo_img : http://47.57.22.49/uploads/poster/8.png
     * follow_num : 0
     * like_vieo_num : 1
     * self_video_num : 0
     * member_autograph : dddd
     */

    private int member_id;
    private String member_avatar;
    private String nick_name;
    private int member_age;
    private int member_level;
    private int member_sex;
    private String member_city;
    private int coin_num;
    private int fans_num;
    private String promo_code;
    private String promo_img;
    private int follow_num;
    private int like_vieo_num;
    private int self_video_num;
    private String member_autograph;
    private String member_birthday;

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getMember_avatar() {
        return member_avatar;
    }

    public void setMember_avatar(String member_avatar) {
        this.member_avatar = member_avatar;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getMember_age() {
        return member_age;
    }

    public void setMember_age(int member_age) {
        this.member_age = member_age;
    }

    public int getMember_level() {
        return member_level;
    }

    public void setMember_level(int member_level) {
        this.member_level = member_level;
    }

    public int getMember_sex() {
        return member_sex;
    }

    public void setMember_sex(int member_sex) {
        this.member_sex = member_sex;
    }

    public String getMember_city() {
        return member_city;
    }

    public void setMember_city(String member_city) {
        this.member_city = member_city;
    }

    public int getCoin_num() {
        return coin_num;
    }

    public void setCoin_num(int coin_num) {
        this.coin_num = coin_num;
    }

    public int getFans_num() {
        return fans_num;
    }

    public void setFans_num(int fans_num) {
        this.fans_num = fans_num;
    }

    public String getPromo_code() {
        return promo_code;
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;
    }

    public String getPromo_img() {
        return promo_img;
    }

    public void setPromo_img(String promo_img) {
        this.promo_img = promo_img;
    }

    public int getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(int follow_num) {
        this.follow_num = follow_num;
    }

    public int getLike_vieo_num() {
        return like_vieo_num;
    }

    public void setLike_vieo_num(int like_vieo_num) {
        this.like_vieo_num = like_vieo_num;
    }

    public int getSelf_video_num() {
        return self_video_num;
    }

    public void setSelf_video_num(int self_video_num) {
        this.self_video_num = self_video_num;
    }

    public String getMember_autograph() {
        return member_autograph;
    }

    public void setMember_autograph(String member_autograph) {
        this.member_autograph = member_autograph;
    }

    public String getMember_birthday() {
        return member_birthday;
    }

    public void setMember_birthday(String member_birthday) {
        this.member_birthday = member_birthday;
    }
}
