package vn.m2m.common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.util.StringUtils;
import vn.m2m.common.HazelcastTTL;
import vn.m2m.common.ModelData;
import vn.m2m.utils.DateUtil;
import vn.m2m.utils.UserHelper;

import java.io.Serializable;
import java.util.*;

@ModelData(collection = "User", mapCacheName = "", mapCacheTTL = HazelcastTTL.TTL_1_DAY)
public class User implements Serializable {
	private static final long serialVersionUID = 8L;
	@Id
	private String id;
	private String username;
	@Indexed(unique = true)
	private String email;
	@JsonIgnore
	private String password;
	private boolean active=true;
	private String phone;

	@JsonIgnore
	private String apiKey;
	@JsonIgnore
	private String secretKey;
	private double usdt;
	private double btc;
	private double eth;

	private String groupId;
	private String[] groupPath;
	private String groupName;

	@JsonIgnore
	private String authenCode;
	@JsonIgnore
	private Date dateauthen;
	@JsonIgnore
	private Date dateLockAuthen;
	@JsonIgnore
	private int sendAuthenTime=0;       //Number of SMS authencode have been send to phone      //max=5;
	private int tryAuthenCode=0;

	private boolean mobileValidate=false;
	private boolean emailValidate=false;

	private String avatar;

	private int role;
	@JsonIgnore
	private String sessionId;
	@JsonIgnore
	private Date createDate;
	private Date lastModified;

	public enum Roles {
		none(0),
		sadmin(100),
		admin(4),
		supermod(3),
		mod(2),
		user(1);

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

	public User(){
		Date now = DateUtil.now();
		this.createDate = now;
		this.lastModified = now;
	}

	public User(String username, String email, String password) {
		Date now = DateUtil.now();
		this.id = UserHelper.generalUserID();
		this.username=username;
		this.email = email;
		this.password = UserHelper.hashPassword(password);
		this.createDate = now;
		this.lastModified = now;
	}

	public User(String username, String password) {
		Date now = DateUtil.now();
		this.id = UserHelper.generalUserID();
		this.username = username;
//		this.email = username + "@gmail.com";
		this.password = UserHelper.hashPassword(password);
		this.createDate = now;
		this.lastModified = now;
	}

	public String getAvatarLinkPath(){
		if (StringUtils.isEmpty(avatar))
			return UserHelper.avatarDefaultLinkPath;
		else
			return UserHelper.avatarUserLinkPath + "/" + avatar;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAuthenCode() {
		return authenCode;
	}

	public void setAuthenCode(String authenCode) {
		this.authenCode = authenCode;
	}

	public Date getDateauthen() {
		return dateauthen;
	}

	public void setDateauthen(Date dateauthen) {
		this.dateauthen = dateauthen;
	}

	public Date getDateLockAuthen() {
		return dateLockAuthen;
	}

	public void setDateLockAuthen(Date dateLockAuthen) {
		this.dateLockAuthen = dateLockAuthen;
	}

	public int getSendAuthenTime() {
		return sendAuthenTime;
	}

	public void setSendAuthenTime(int sendAuthenTime) {
		this.sendAuthenTime = sendAuthenTime;
	}

	public int getTryAuthenCode() {
		return tryAuthenCode;
	}

	public void setTryAuthenCode(int tryAuthenCode) {
		this.tryAuthenCode = tryAuthenCode;
	}

	public boolean isMobileValidate() {
		return mobileValidate;
	}

	public void setMobileValidate(boolean mobileValidate) {
		this.mobileValidate = mobileValidate;
	}

	public boolean isEmailValidate() {
		return emailValidate;
	}

	public void setEmailValidate(boolean emailValidate) {
		this.emailValidate = emailValidate;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String[] getGroupPath() {
		return groupPath;
	}

	public void setGroupPath(String[] groupPath) {
		this.groupPath = groupPath;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public double getUsdt() {
		return usdt;
	}

	public void setUsdt(double usdt) {
		this.usdt = usdt;
	}

	public double getBtc() {
		return btc;
	}

	public void setBtc(double btc) {
		this.btc = btc;
	}

	public double getEth() {
		return eth;
	}

	public void setEth(double eth) {
		this.eth = eth;
	}
}
