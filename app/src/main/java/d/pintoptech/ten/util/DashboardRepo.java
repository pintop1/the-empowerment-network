package d.pintoptech.ten.util;

/**
 * Created by PINTOP TECHNOLOGIES LIMITED on 4/23/2019.
 */

public class DashboardRepo {
    String dimage;
    String dname;
    String daction;
    String dtime;
    String did;
    String status;

    public DashboardRepo(String dimage,String dname,String daction, String dtime, String did, String status) {
        this.dimage = dimage;
        this.dname = dname;
        this.daction = daction;
        this.dtime = dtime;
        this.did = did;
    }

    public String getDimage() {
        return dimage;
    }
    public void setDimage(String dimage) {
        this.dimage = dimage;
    }

    public String getDname() {
        return dname;
    }
    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDaction() {
        return daction;
    }
    public void setDaction(String daction) {
        this.daction = daction;
    }

    public String getDtime() {
        return dtime;
    }
    public void setDtime(String dtime) {
        this.dtime = dtime;
    }

    public String getDid() {
        return did;
    }
    public void setDid(String did) {
        this.did = did;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
