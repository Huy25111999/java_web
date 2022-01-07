package vn.m2m.common.dao;


import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import vn.m2m.common.AbstractDAO;
import vn.m2m.common.HazelcastTTL;
import vn.m2m.common.models.UserAdmin;
import vn.m2m.config.HazelcastConfig;
import vn.m2m.config.MongoConfig;
import vn.m2m.utils.DateUtil;
import vn.m2m.utils.StringUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Singleton
public class UserAdminDAO extends AbstractDAO<String, UserAdmin> {
    public static final String UserAdminCollection = "UserAdmin";
    @Inject
    public MongoConfig mongoConfig;

    @Inject
    private HazelcastConfig hazelcastConfig;

//    @Inject
//    public UserAdminCache userAdminCache;

    private final String EMAIL_USERADMINID_MAP = "EmailUserAdminIdMap";
    private final String SESSIONID_USERADMINID_MAP = "SessionIdUserAdminIdMap";

    private static String idField = "_id";
    private static String sessionIdField = "sessionId";
    private static String emailField = "email";
    private static String createdDateField ="createdDate";
    private static String lastModifiedField ="lastModified";
    private static String usernameField = "username";
    private static String phoneField = "phone";

    protected MongoTemplate mongoTempl() throws Exception {
        return mongoConfig.getMongoTemplate();
    }

    protected HazelcastInstance hazelcastInstance(){
        return hazelcastConfig.getHazelcastInstance();
    }

    public UserAdminDAO(){
        super(String.class, UserAdmin.class);

    }

    private synchronized IMap<String, String> getEmailMap() {
       return hazelcastInstance().getMap(EMAIL_USERADMINID_MAP);
    }

    private synchronized IMap<String, String> getSessionIdMap() {
        return hazelcastInstance().getMap(SESSIONID_USERADMINID_MAP);
    }

    public UserAdmin saveUserAdmin(UserAdmin userAdmin){
        if(userAdmin == null){
            return null;
        }
        try {
            userAdmin.setLastModified(DateUtil.now());
            save(userAdmin);
            try {
                putUserAdminCache(userAdmin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userAdmin;
    }

    public void deleteSessionId(UserAdmin userAdmin){
        try {
            Query query = new Query(Criteria.where(idField).is(userAdmin.getId()));
            Update update = Update.update(sessionIdField, StringUtil.blank);
            mongoTempl().updateFirst(query, update, UserAdmin.class, UserAdminCollection);

            try {
                getSessionIdMap().delete(userAdmin.getSessionId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void putUserAdminCache(UserAdmin userAdmin){
        if(userAdmin!=null){
            putCache(userAdmin);
            getEmailMap().put(userAdmin.getEmail(), userAdmin.getId(), HazelcastTTL.TTL_1_DAY, TimeUnit.SECONDS);
            if(!StringUtils.isEmpty(userAdmin.getSessionId())){
                getSessionIdMap().put(userAdmin.getSessionId(),userAdmin.getId(), HazelcastTTL.TTL_1_DAY, TimeUnit.SECONDS);
            }
        }
    }

    public UserAdmin getByEmail(String email){
        if(StringUtils.isEmpty(email)){
            return null;
        }
        UserAdmin userAdmin=null;
        try {
            String userId= getEmailMap().get(email);
            if(!StringUtils.isEmpty(userId)){
                return getByKey(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(userAdmin==null){
            try {
                Query query = new Query(Criteria.where(emailField).is(email));
                userAdmin = mongoTempl().findOne(query, UserAdmin.class, UserAdminCollection);

                try {
                    if(userAdmin!=null) {
                        putUserAdminCache(userAdmin);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return userAdmin;

    }

    public UserAdmin getBySessionId(String sessionId){
        if(StringUtils.isEmpty(sessionId)){
            return null;
        }
        UserAdmin userAdmin=null;
        try {
            String userId= getSessionIdMap().get(sessionId);
            if(!StringUtils.isEmpty(userId)){
                return getByKey(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(userAdmin==null){
            try {
                Query query = new Query(Criteria.where(sessionIdField).is(sessionId));
                userAdmin = mongoTempl().findOne(query, UserAdmin.class, UserAdminCollection);

                try {
                    if(userAdmin!=null) {
                        putUserAdminCache(userAdmin);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return userAdmin;

    }

    public UserAdmin getByPhone(String phone){
        if(StringUtils.isEmpty(phone)){
            return null;
        }
        UserAdmin userAdmin=null;

        if(userAdmin==null){
            try {
                Query query = new Query(Criteria.where(phoneField).is(phone));
                userAdmin = mongoTempl().findOne(query, UserAdmin.class, UserAdminCollection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return userAdmin;

    }

}
