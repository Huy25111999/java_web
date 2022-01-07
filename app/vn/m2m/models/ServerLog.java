package vn.m2m.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import vn.m2m.common.HazelcastTTL;
import vn.m2m.common.ModelData;
import vn.m2m.utils.DateUtil;
import vn.m2m.utils.ObjectUtil;
import vn.m2m.utils.StringUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ModelData(collection = "ServerLog", mapCacheName = "", mapCacheTTL = HazelcastTTL.TTL_1_DAY)
public class ServerLog implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    private String logId;

    private Date logDate;

    private String logContent;

    private String usrId;

    private String oldObj;
    private String newObj;
    private String diff;

    private int action;         //1-new 2-update 3-delete

    private int objName;

    private boolean isSuccess;

    @Transient
    private String email; // only show scala template

    public ServerLog() {
        logId= StringUtil.getTimeUUIDString();
        logDate= DateUtil.now();
    }


    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }


    public String getOldObj() {
        return oldObj;
    }

    public void setOldObj(Object obj) {

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString="";
        try{
            jsonInString = mapper.writeValueAsString(obj);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        this.oldObj = jsonInString;
    }

    public String getNewObj() {
        return newObj;
    }

    public void setNewObj(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString="";
        try{
            jsonInString = mapper.writeValueAsString(obj);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        this.newObj = jsonInString;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getObjName() {
        return objName;
    }

    public void setObjName(int objName) {
        this.objName = objName;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getDiff() {
        return diff;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
