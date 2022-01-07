package vn.m2m.common.models.forms;

import org.springframework.data.annotation.Id;
import vn.m2m.common.HazelcastTTL;
import vn.m2m.common.ModelData;
import vn.m2m.utils.DateUtil;
import vn.m2m.utils.StaticData;

import java.io.Serializable;
import java.util.Date;

@ModelData(collection = "UserCaptcha", mapCacheName = "UserCaptcha", mapCacheTTL = HazelcastTTL.UserAdminCaptcha)
public class UserCaptcha implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String email;

    private int numberAuthenFailed;
    private boolean turnOnCaptcha;
    private boolean passedCaptcha=false;

    private Date createDate;
    private Date lastModified;

    public UserCaptcha(String email) {
        this.email = email;
        this.numberAuthenFailed=0;
        this.turnOnCaptcha=false;
        this.createDate= DateUtil.now();
        this.lastModified= DateUtil.now();
    }

    public UserCaptcha() {
        this.createDate= DateUtil.now();
        this.lastModified= DateUtil.now();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumberAuthenFailed() {
        return numberAuthenFailed;
    }

    public void setNumberAuthenFailed(int numberAuthenFailed) {
        this.numberAuthenFailed = numberAuthenFailed;
    }

    public boolean isTurnOnCaptcha() {
        return turnOnCaptcha;
    }

    public void setTurnOnCaptcha(boolean turnOnCaptcha) {
        this.turnOnCaptcha = turnOnCaptcha;
    }

    public boolean isPassedCaptcha() {
        return passedCaptcha;
    }

    public void setPassedCaptcha(boolean passedCaptcha) {
        this.passedCaptcha = passedCaptcha;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
