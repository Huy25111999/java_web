package vn.m2m.utils;

import com.typesafe.config.Config;
import vn.m2m.common.models.User;
import vn.m2m.common.models.UserAdmin;
import vn.m2m.models.Group;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

public class UserHelper {
    public class SessionData{
        public static final String sessionId = "sessionId";
        public static final String email = "email";
        public static final String avatar = "avatar";
        public static final String name = "name"; // substring email 12 character
        public static final String TIMEOUT_KEY = "session.timeout";
        public static final String language = "language";
        public static final String lastModified = "lastModified";
        public static final String isApp = "isApp";
        public static final String subMenu = "subMenu";
        public static final String sessionEmailLogin = "emaillogin";
    }

    public static String linkGoogleMapApi(){
        //src="https://maps.googleapis.com/maps/api/js?v=3&key=AIzaSyD94Yo1rr5OQWWkSGNVGW84nA3vZLIdfbA&lang=vi"
        String apiKeyRandom = getGoogleApiKeyRandom();
        String linkapi = "https://maps.googleapis.com/maps/api/js?v=3&key="+apiKeyRandom+"&lang=vi";
        return linkapi;
    }
    public static String getGoogleApiKeyRandom(){
        //random from 1-9

        int rand = StringUtil.randomNumber(1,maxGoogleApiBrowserKey);
        String keyRandom = getAppConfigString(StaticContent.GOOGLE_API_BROWSER_KEY + rand);

//        System.out.println("GoogleApiBrowserKeyMax:"+maxGoogleApiBrowserKey);
//        System.out.println("GoogleApiBrowserKeyRandom:"+keyRandom);

        return keyRandom;
    }

    public static String genaralSessionId() {
        return StringUtil.getTimeUUIDString();
    }
    public static String generateFilename(String contentType) {
        return StringUtil.getTimeUUIDString() + "." + contentType;
    }
    public static String generalUserID() {
        return generalID();
    }
    public static String generalID() {
        return StringUtil.getTimeUUIDString();
    }

    public static String hashPassword(String password){
        String passwordHash = "";
        try {
            passwordHash = PasswordHash.createHash(password);
        }
        catch(NoSuchAlgorithmException |InvalidKeySpecException ex){
            ex.printStackTrace();
        }
        return passwordHash;
    }

    public static boolean validPassword(String password, String passwordHash){
        try{
            return PasswordHash.validatePassword(password, passwordHash);
        }
        catch (NoSuchAlgorithmException |InvalidKeySpecException ex){
            ex.printStackTrace();
        }
        return false;
    }

    public static String generalUserPassword() {
        try {
            return SecretUtil.keyGenHex512();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }
    public static String generalNonce() {
        return StringUtil.getTimeUUIDString();
    }
    public static String generateAuthenCode(){
        return Long.toString(StringUtil.generateRandom(8));
    }

    public static String generateSSRC(){
        try{
            String ssrc=Integer.toString(StringUtil.randomNumber());
            return ssrc;
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }

    public static Config getAppConfig(){
        return AppHelper.getAppConfig();
    }
    public static String getAppConfigString(String key){
        String value = getAppConfig().getString(key);
        return value;
    }
    public static Integer getAppConfigInt(String key){
        return getAppConfig().getInt(key);
    }
    public static Long getConfigLong(String key){
        return getAppConfig().getLong(key);
    }
    public static Double getAppConfigDouble(String key){
        return getAppConfig().getDouble(key);
    }

    public static Boolean getAppConfigBoolean(String key){
        return getAppConfig().getBoolean(key);
    }

    public static boolean isSessionTimeOut(String sessionId){
        long timeSession = DateUtil.convertStringByteArrayToDate(sessionId);
        Date dateSession = DateUtil.convertDate(timeSession);
        Date currentDate = DateUtil.now();
        int timeout = getAppConfigInt(SessionData.TIMEOUT_KEY);
//        logger.info("dateSession: {}",dateSession);
//        logger.info("timeout: {}",timeout);


        if(DateUtil.diffMinutes(currentDate,dateSession)>timeout){
            return true;
        }
        return false;
    }


    public class StaticContent{
        public static final String WEB_LINK_ROOT_KEY ="web.link.root";
        public static final String LINK_AVATAR_USERADMIN_KEY ="content.link.avatar.useradmin";
        public static final String LINK_AVATAR_DEFAULT ="content.default.linkavatar";
        public static final String FOLDER_AVATAR_USER_KEY ="content.folder.avatar.useradmin";

        //ais map
        public static final String GOOGLE_API_BROWSER_KEY ="google.api.browserkey";
        public static final String GOOGLE_API_BROWSER_KEY_MAX ="google.api.browserkeymax";

//        public static final String LINK_IMG_AVATAR ="content.folder.avatar";

        public static final String MAP_DISPLAY_BTS ="map.displayBTS";

    }
    public static String imgAvatarLinkPath = "/data/content/image";

    //phan quyen
    private static boolean isSAdmin(User user){
        if (user.getRole()== User.Roles.sadmin.getCode()){return true;}
        return false;
    }
    private static boolean isAdmin(User user){
        if (user.getRole()== User.Roles.admin.getCode()){return true;}
        return false;
    }
    private static boolean isSMod(User user){
        if (user.getRole()== User.Roles.supermod.getCode()){return true;}
        return false;
    }
    private static boolean isMod(User user){
        if (user.getRole()== User.Roles.mod.getCode()){return true;}
        return false;
    }
    private static boolean isUser(User user){
        if (user.getRole()== User.Roles.user.getCode()){return true;}
        return false;
    }

    // Root Group
    public static boolean isSAdminRootPermit(User user){
        if (isUserGroupRoot(user)
                && isSAdmin(user)){return true;}
        return false;
    }
    public static boolean isAdminRootPermit(User user){
        if ((isUserGroupRoot(user)
                && (isSAdmin(user)||isAdmin(user)))){return true;}
        return false;
    }
    public static boolean isSModRootPermit(User user){
        if ((isUserGroupRoot(user)
                && (isSAdmin(user)||isAdmin(user)||isSMod(user)))){return true;}
        return false;
    }
    public static boolean isModRootPermit(User user){
        if ((isUserGroupRoot(user)
                && (isSAdmin(user)||isAdmin(user)||isSMod(user)||isMod(user)))){return true;}
        return false;
    }
    public static boolean isUserRootPermit(User user){
        if ((isUserGroupRoot(user)
                && (isSAdmin(user)||isAdmin(user)||isSMod(user)||isMod(user)||isUser(user)))){return true;}
        return false;
    }

    // Normal Group
    public static boolean isSAdminPermit(User user){
        if (isSAdmin(user)){return true;}
        return false;
    }
    public static boolean isAdminPermit(User user){
        if(user == null){
            return false;
        }
        if (isSAdmin(user)||isAdmin(user)){
            return true;
        }
        return false;
    }
    public static boolean isSModPermit(User user){
        if (isSAdmin(user)||isAdmin(user)||isSMod(user)){return true;}
        return false;
    }
    public static boolean isModPermit(User user){
        if (isSAdmin(user)||isAdmin(user)||isSMod(user)||isMod(user)){return true;}
        return false;
    }
    public static boolean isUserPermit(User user){
        if (isSAdmin(user)||isAdmin(user)||isSMod(user)||isMod(user)||isUser(user)){return true;}
        return false;
    }

    public static boolean isUserGroupRoot(User user){
        if(user.getGroupId().equals("c2a27560da3811e7b4bdac162d123aea")){
            return true;
        }
        return false;
    }

    public static boolean isUserGroupRoot(User user, Group group){
        if(user.getGroupId().equals(group.getId()) && group.getGroupRole()==Group.Roles.root.getCode()){
            return true;
        }
        return false;
    }

    public static String generateUniqueFilename(String filename) {
        return StringUtil.getTimeUUIDString() +"-"+ filename;
    }

    public static String webLinkRoot = AppHelper.getAppConfigString(StaticContent.WEB_LINK_ROOT_KEY);


    public static String avatarUserLinkPath = webLinkRoot + AppHelper.getAppConfigString(StaticContent.LINK_AVATAR_USERADMIN_KEY);
    public static String avatarDefaultLinkPath = AppHelper.getAppConfigString(StaticContent.LINK_AVATAR_DEFAULT);

    public static String avatarUserFolderPath = AppHelper.getAppConfigString(StaticContent.FOLDER_AVATAR_USER_KEY);

    public static Integer maxGoogleApiBrowserKey = getAppConfigInt(StaticContent.GOOGLE_API_BROWSER_KEY_MAX);

    public class reCaptcha{
        public static final String SITE_KEY ="recaptcha.sitekey";
        public static final String SECRET_KEY ="recaptcha.secretkey";
    }

    public static String reCaptchaSiteKey = AppHelper.getAppConfigString(reCaptcha.SITE_KEY);
    public static String reCaptchaSecretKey = AppHelper.getAppConfigString(reCaptcha.SECRET_KEY);

    public static final int MAX_AUTHEN_FAILED=5; //5 time
}
