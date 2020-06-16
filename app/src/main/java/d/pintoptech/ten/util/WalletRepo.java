package d.pintoptech.ten.util;

/**
 * Created by PINTOP TECHNOLOGIES LIMITED on 4/23/2019.
 */

public class WalletRepo {
    String dday;
    String dmonth;
    String dyear;
    String ddesc;
    String dtime;
    String damount;
    String dtype;

    public WalletRepo(String dday,String dmonth,String dyear, String ddesc, String dtime, String damount, String dtype) {
        this.dday = dday;
        this.dmonth = dmonth;
        this.dyear = dyear;
        this.ddesc = ddesc;
        this.dtime = dtime;
        this.damount = damount;
        this.dtype = dtype;
    }

    public String getDday() {
        return dday;
    }
    public void setDday(String dday) {
        this.dday = dday;
    }

    public String getDmonth() {
        return dmonth;
    }
    public void setDmonth(String dmonth) {
        this.dmonth = dmonth;
    }

    public String getDyear() {
        return dyear;
    }
    public void setDyear(String dyear) {
        this.dyear = dyear;
    }

    public String getDdesc() {
        return ddesc;
    }
    public void setDdesc(String ddesc) {
        this.ddesc = ddesc;
    }

    public String getDtime() {
        return dtime;
    }
    public void setDtime(String dtime) {
        this.dtime = dtime;
    }

    public String getDamount() {
        return damount;
    }
    public void setDamount(String damount) {
        this.damount = damount;
    }

    public String getDtype() {
        return dtype;
    }
    public void setDtype(String dtype) {
        this.dtype = dtype;
    }
}
