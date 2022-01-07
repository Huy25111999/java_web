package vn.m2m.common.cache;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.util.StringUtils;
import vn.m2m.common.models.User;
import vn.m2m.common.models.UserAdmin;
import vn.m2m.config.HazelcastConfig;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class UserCache {
    @Inject
    private HazelcastConfig hazelcastConfig;
    private HazelcastInstance hazelcastInstance(){
        return hazelcastConfig.getHazelcastInstance();
    }

    private final String USER_MAP = "UserMap";
    private final String USERADMIN_MAP = "UserAdminMap";
    private final String EMAIL_USERADMINID_MAP = "EmailUserAdminIdMap";
    private final String SESSIONID_USERADMINID_MAP = "SessionIdUserAdminIdMap";
    private final String SESSIONID_USERID_MAP = "SessionIdUserIdMap";

    private final int USER_MAP_TTL = 1 * 24 * 3600; // 1 day
    private final int USERADMIN_MAP_TTL = 1 * 24 * 3600; // 1 day
    private final int EMAIL_USERADMINID_MAP_TTL = 1 * 24 * 3600; // 1 day
    private final int SESSIONID_USERADMINID_MAP_TTL = 1 * 24 * 3600; // 1 day
    private final int SESSIONID_USERID_MAP_TTL = 1 * 24 * 3600; // 1 day

    // ---------------------- IMAP ------------------------------
    private IMap<String, User> userMap;
    private IMap<String, UserAdmin> userAdminMap;
    private IMap<String, String> emailUserAdminIdMap;
    private IMap<String, String> sessionIdUserAdminIdMap;
    private IMap<String, String> sessionIdUserIdMap;

    private synchronized IMap<String, User> getUserMap() {
        try {
            if (userMap == null) {
                userMap = hazelcastInstance().getMap(USER_MAP);
            }
            return userMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private synchronized IMap<String, String> getSessionIdUserIdMap() {
        try {
            if (sessionIdUserIdMap == null) {
                sessionIdUserIdMap = hazelcastInstance().getMap(
                        SESSIONID_USERID_MAP);
            }
            return sessionIdUserIdMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private synchronized IMap<String, UserAdmin> getUserAdminMap() {
        try {
            if (userAdminMap == null) {
                userAdminMap = hazelcastInstance().getMap(
                        USERADMIN_MAP);
            }
            return userAdminMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private synchronized IMap<String, String> getEmailUserAdminIdMap() {
        try {
            if (emailUserAdminIdMap == null) {
                emailUserAdminIdMap = hazelcastInstance().getMap(
                        EMAIL_USERADMINID_MAP);
            }
            return emailUserAdminIdMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private synchronized IMap<String, String> getSessionIdUserAdminIdMap() {
        try {
            if (sessionIdUserAdminIdMap == null) {
                sessionIdUserAdminIdMap = hazelcastInstance().getMap(
                        SESSIONID_USERADMINID_MAP);
            }
            return sessionIdUserAdminIdMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // ---------------------- UserAdminMap ---------------------------
    public void putUserAdminMap(UserAdmin userAdmin){
        if(userAdmin!=null){
            getUserAdminMap().put(userAdmin.getId(), userAdmin,USERADMIN_MAP_TTL, TimeUnit.SECONDS);
            getEmailUserAdminIdMap().put(userAdmin.getEmail(), userAdmin.getId(),EMAIL_USERADMINID_MAP_TTL, TimeUnit.SECONDS);
            if(!StringUtils.isEmpty(userAdmin.getSessionId())){
                getSessionIdUserAdminIdMap().put(userAdmin.getSessionId(),userAdmin.getId(), SESSIONID_USERADMINID_MAP_TTL, TimeUnit.SECONDS);
            }
        }
    }

    public UserAdmin getUseAdminById(String id){
        return getUserAdminMap().get(id);
    }

    public UserAdmin getUserAdminByEmail(String email){
        String userId= getEmailUserAdminIdMap().get(email);
        if(!StringUtils.isEmpty(userId)){
            return getUseAdminById(userId);
        }
        return null;
    }

    public UserAdmin getUserAdminBySessionId(String sessionId){
        String userId = getSessionIdUserAdminIdMap().get(sessionId);
        if(!StringUtils.isEmpty(userId)){
            return getUseAdminById(userId);
        }
        return null;
    }

    public void deleteSessionId(String sessionId){
        getSessionIdUserAdminIdMap().delete(sessionId);
    }

    public void deleteUserAdminMap(String id){
        getUserAdminMap().delete(id);
    }

    public void deleteEmailUserAdminIdMap(String email){
        getEmailUserAdminIdMap().delete(email);
    }

    // ---------------------- UserMap ---------------------------



    public void putUserMap(User user){
        if(user!=null){
            getUserMap().put(user.getId(), user,USER_MAP_TTL, TimeUnit.SECONDS);
//            getEmailUserIdMap().put(user.getEmail(), user.getId(),EMAIL_USERID_MAP_TTL, TimeUnit.SECONDS);

//            if(!StringUtils.isEmpty(user.getPhone())){
//                getPhoneUserIdMap().put(user.getPhone(), user.getId(),PHONE_USERID_MAP_TTL, TimeUnit.SECONDS);
//
//                if (user.isMobileValidate())
//                {
//                    getPhoneValidUserIdMap().put(user.getPhone(), user.getId(),PHONEVALID_USERID_MAP_TTL, TimeUnit.SECONDS);
//                }
//
//
//            }

            if(!StringUtils.isEmpty(user.getSessionId())){
                getSessionIdUserIdMap().put(user.getSessionId(),user.getId(), SESSIONID_USERID_MAP_TTL, TimeUnit.SECONDS);
            }
        }
    }


    public User getUserById(String id){
        return getUserMap().get(id);
    }


    // ---------------------- clearCache ------------------------
    public void clearCache(){
        getUserAdminMap().destroy();
        getEmailUserAdminIdMap().destroy();
        getSessionIdUserAdminIdMap().destroy();
    }
    // ---------------------- clearCache ------------------------

}
