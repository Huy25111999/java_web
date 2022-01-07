package vn.m2m.models;

import org.springframework.data.annotation.Id;
import vn.m2m.common.ModelData;
import vn.m2m.utils.DateUtil;
import vn.m2m.utils.StringUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ModelData(collection = "Group", mapCacheName = "", mapCacheTTL = 86400)
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Roles {
        root(1),
        common(2);

        private final int code;

        private static final Map<Integer, Roles> roleByCode = new HashMap<Integer, Roles>();

        static {
            for(Roles role: Roles.values()){
                roleByCode.put(role.getCode(),role);
            }
        }

        private Roles(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name();
        }

        public static Roles fromCode(int code){
            return roleByCode.get(code);
        }

        public static String getNameByCode(int code){
            Roles role = roleByCode.get(code);
            if(role!=null){
                return role.getName();
            }
            return "";
        }

    }

    @Id
    private String id;
    private String name;
    private String[] path;
    private String address;
    private String phone;
    private String email;

    private String orgId;

    private Date createDate;
    private Date lastModified;
    private boolean active = true;
    private int groupRole= Roles.common.getCode();
    private boolean masterGroup=false;

    private String med; //VIETTEL, VNPT...

    private boolean groupTest = false;

    public Group() {
        this.id = StringUtil.getTimeUUIDString();
        Date now = DateUtil.now();
        this.createDate = now;
        this.lastModified = now;
    }

    public Group(String name, String[] path, String address, String phone, String email, String orgId) {
        this.id = StringUtil.getTimeUUIDString();
        this.name = name;
        this.path = path;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.orgId = orgId;
        Date now = DateUtil.now();
        this.createDate = now;
        this.lastModified = now;
    }

    public String getParent(){
        String parentId = "#";
        if(path.length == 2){
            parentId = path[0] + "";
        }else if(path.length == 3){
            parentId = path[1] + "";
        }else if(path.length == 4){
            parentId = path[2] + "";
        }else if(path.length == 5){
            parentId = path[3] + "";
        }
        return parentId;
    }

    public boolean isRoot(){
        if(groupRole == Group.Roles.root.getCode()){
            return true;
        }
        else {
            return false;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getPath() {
        return path;
    }

    public void setPath(String[] path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getGroupRole() {
        return groupRole;
    }

    public void setGroupRole(int groupRole) {
        this.groupRole = groupRole;
    }

    public boolean isMasterGroup() {
        return masterGroup;
    }

    public void setMasterGroup(boolean masterGroup) {
        this.masterGroup = masterGroup;
    }

    public String getMed() {
        return med;
    }

    public void setMed(String med) {
        this.med = med;
    }

    public boolean isGroupTest() {
        return groupTest;
    }

    public void setGroupTest(boolean groupTest) {
        this.groupTest = groupTest;
    }
}

