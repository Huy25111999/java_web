package vn.m2m.common.cache;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import vn.m2m.common.AbstractCache;
import vn.m2m.common.AbstractDAO;
import vn.m2m.common.models.UserAdmin;
import vn.m2m.config.HazelcastConfig;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class UserAdminCache extends AbstractCache<String, UserAdmin> {
    @Inject
    private HazelcastConfig hazelcastConfig;
    public HazelcastInstance hazelcastInstance(){
        return hazelcastConfig.getHazelcastInstance();
    }

    public UserAdminCache() {
        super(String.class, UserAdmin.class);
    }
//
//    private final String USERADMIN_MAP = "UserAdmin";
//    private final String EMAIL_USERADMINID_MAP = "EmailUserAdminIdMap";
//    private final String SESSIONID_USERADMINID_MAP = "SessionIdUserAdminIdMap";
//
//    private final int USERADMIN_MAP_TTL = 1 * 24 * 3600; // 1 day
//    private final int EMAIL_USERADMINID_MAP_TTL = 1 * 24 * 3600; // 1 day
//    private final int SESSIONID_USERADMINID_MAP_TTL = 1 * 24 * 3600; // 1 day
//
//    // ---------------------- IMAP ------------------------------
//    private IMap<String, UserAdmin> userAdminMap;
//    private IMap<String, String> emailUserAdminIdMap;
//    private IMap<String, String> sessionIdUserAdminIdMap;
//
//    private synchronized IMap<String, UserAdmin> getUserAdminMap() {
//        try {
//            if (userAdminMap == null) {
//                userAdminMap = hazelcastInstance().getMap(
//                        USERADMIN_MAP);
//            }
//            return userAdminMap;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private synchronized IMap<String, String> getEmailUserAdminIdMap() {
//        try {
//            if (emailUserAdminIdMap == null) {
//                emailUserAdminIdMap = hazelcastInstance().getMap(
//                        EMAIL_USERADMINID_MAP);
//            }
//            return emailUserAdminIdMap;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private synchronized IMap<String, String> getSessionIdUserAdminIdMap() {
//        try {
//            if (sessionIdUserAdminIdMap == null) {
//                sessionIdUserAdminIdMap = hazelcastInstance().getMap(
//                        SESSIONID_USERADMINID_MAP);
//            }
//            return sessionIdUserAdminIdMap;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//    // ---------------------- UserAdminMap ---------------------------
//    public void putUserAdminMap(UserAdmin userAdmin){
//        if(userAdmin!=null){
//            getUserAdminMap().put(userAdmin.getId(), userAdmin,USERADMIN_MAP_TTL, TimeUnit.SECONDS);
//            getEmailUserAdminIdMap().put(userAdmin.getEmail(), userAdmin.getId(),EMAIL_USERADMINID_MAP_TTL, TimeUnit.SECONDS);
//            if(!StringUtils.isEmpty(userAdmin.getSessionId())){
//                getSessionIdUserAdminIdMap().put(userAdmin.getSessionId(),userAdmin.getId(), SESSIONID_USERADMINID_MAP_TTL, TimeUnit.SECONDS);
//            }
//        }
//    }
//
//    public UserAdmin getUseAdminById(String id){
//        return getUserAdminMap().get(id);
//    }
//
//
//    public UserAdmin getUserAdminByEmail(String email){
//        String userId= getEmailUserAdminIdMap().get(email);
//        if(!StringUtils.isEmpty(userId)){
//            return getUseAdminById(userId);
//        }
//        return null;
//    }
//
//    public UserAdmin getUserAdminBySessionId(String sessionId){
//        String userId= getSessionIdUserAdminIdMap().get(sessionId);
//        if(!StringUtils.isEmpty(userId)){
//            return getUseAdminById(userId);
//        }
//        return null;
//    }
//
//    public void deleteSessionId(String sessionId){
//        getSessionIdUserAdminIdMap().delete(sessionId);
//    }
//
//    public void deleteUserAdminMap(String id){
//        getUserAdminMap().delete(id);
//    }
//
//    public void deleteEmailUserAdminIdMap(String email){
//        getEmailUserAdminIdMap().delete(email);
//    }
//
//
//
//    // ---------------------- UserMap ---------------------------
//
//    // ---------------------- clearCache ------------------------
//    public void clearCache(){
//        getUserAdminMap().destroy();
//        getEmailUserAdminIdMap().destroy();
//        getSessionIdUserAdminIdMap().destroy();
//    }
//    // ---------------------- clearCache ------------------------

}
