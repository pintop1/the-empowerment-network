package d.pintoptech.ten.util;

/**
 * Created by PINTOP TECHNOLOGIES LIMITED on 4/23/2019.
 */

public class MembersRepo {
    String name;
    String did;
    String avatar;
    String phone;

    public MembersRepo(String name,String did,String avatar, String phone) {
        this.name = name;
        this.did = did;
        this.avatar = avatar;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDid() {
        return did;
    }
    public void setDid(String did) {
        this.did = did;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
