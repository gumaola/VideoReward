package cn.nano.main.server.result;

public class MemberSimpleResult {
    /**
     * member_id : 7
     * login_phone : 15680828701
     * nick_name : null
     * member_avatar : null
     */

    private int member_id;
    private String login_phone;
    private String nick_name;
    private String member_avatar;

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getLogin_phone() {
        return login_phone;
    }

    public void setLogin_phone(String login_phone) {
        this.login_phone = login_phone;
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
