package d.pintoptech.ten.util;

/**
 * Created by PINTOP TECHNOLOGIES LIMITED on 4/23/2019.
 */

public class GroupsRepo {
    String id;
    String thumb;
    String group_name;
    String desc;
    String counts;

    public GroupsRepo(String id,String thumb,String group_name, String desc, String counts) {
        this.id = id;
        this.thumb = thumb;
        this.group_name = group_name;
        this.desc = desc;
        this.counts = counts;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getGroupName() {
        return group_name;
    }
    public void setGroupName(String group_name) {
        this.group_name = group_name;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCounts() {
        return counts;
    }
    public void setCounts(String counts) {
        this.counts = counts;
    }
}
