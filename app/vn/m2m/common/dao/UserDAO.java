package vn.m2m.common.dao;


import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import vn.m2m.common.AbstractDAO;
import vn.m2m.common.HazelcastTTL;
import vn.m2m.common.cache.UserCache;
import vn.m2m.common.models.User;
import vn.m2m.common.models.UserAdmin;
import vn.m2m.config.HazelcastConfig;
import vn.m2m.config.MongoConfig;
import vn.m2m.utils.DateUtil;
import vn.m2m.utils.StringUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Singleton
public class UserDAO extends AbstractDAO<String, User> {
    private static String idField = "_id";
    private static String emailField = "email";
    private static String usernameField = "username";
    private static String sessionIdField = "sessionId";
    private static String groupIdField = "groupId";
    private static String roleField = "role";
    private static String phoneField = "phone";

    @Inject
    public MongoConfig mongoConfig;

    @Inject
    private HazelcastConfig hazelcastConfig;


    protected MongoTemplate mongoTempl() throws Exception {
        return mongoConfig.getMongoTemplate();
    }

    protected HazelcastInstance hazelcastInstance(){
        return hazelcastConfig.getHazelcastInstance();
    }

    public UserDAO(){
        super(String.class, User.class);

    }

    //get all user of certain org - not include child org
    public List<User> getUserByGroupId(String idgroup) {
        List<User> listUserReturn = new ArrayList<User>();
        if (StringUtils.isEmpty(idgroup)){
            return listUserReturn;
        }
        try {
            Query query = new Query(Criteria.where(groupIdField).is(idgroup));
            listUserReturn = (List)mongoTempl().find(query, User.class, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listUserReturn;
    }

    public void putUserCache(User user){
        if(user!=null){
            putCache(user);
            if(!StringUtils.isEmpty(user.getUsername())){
                putCacheFieldName(usernameField, user.getUsername(), user);
            }
            if(!StringUtils.isEmpty(user.getEmail())){
                putCacheFieldName(emailField, user.getEmail(), user);
            }
            if(!StringUtils.isEmpty(user.getSessionId())){
                putCacheFieldName(sessionIdField, user.getSessionId(), user);
            }
        }
    }

    public void deleteUsernameCache(String userName){
        if(!StringUtils.isEmpty(userName)){
            deleteCacheFieldNameByKey(usernameField,userName);

        }
    }

    public void deleteEmailCache(String email){
        if(!StringUtils.isEmpty(email)){
            deleteCacheFieldNameByKey(emailField,email);
        }
    }

    public void deletePhoneCache(String phone){
        if(!StringUtils.isEmpty(phone)){
            deleteCacheFieldNameByKey(phoneField,phone);
        }
    }

    public void deleteRoleCache(int role){
        if(!StringUtils.isEmpty(role)){
            deleteCacheFieldNameByKey(roleField,role);
        }
    }
    public void deleteSessionIdCache(String sessionId){
        if(!StringUtils.isEmpty(sessionId)){
            deleteCacheFieldNameByKey(sessionIdField,sessionId);
        }
    }

    public void deleteSessionId(User user,String sessionId){
        try {
            Query query = new Query(Criteria.where(idField).is(user.getId()));
            Update update = Update.update(sessionIdField, StringUtil.blank);
            mongoTempl().updateFirst(query, update, User.class, collectionName);
            //giangbb -not use cache currently
            try {
                deleteCacheFieldNameByKey(sessionIdField,sessionId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(User user){
        if(user == null){
            return;
        }
        try {
            deleteUsernameCache(user.getUsername());
            deleteEmailCache(user.getEmail());
            deleteSessionIdCache(user.getSessionId());
            deleteByKey(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User saveUser(User user){
        if(user == null){
            return null;
        }
        try {
            user.setLastModified(DateUtil.now());
            save(user);
            try {
                putUserCache(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getByUsername(String username){
        if(StringUtils.isEmpty(username)){
            return null;
        }

        User user= getOneByFieldname(usernameField, username);

        return user;
    }

    public User getByLastMission(String lastMission){
        if(StringUtils.isEmpty(lastMission)){
            return null;
        }

        User user= getOneByFieldname("lastMission", lastMission);

        return user;
    }

    public List<User> getByUsernameAndHost(String username, int host) {
        List<User> listUser = new ArrayList<User>();
        try {
            Query query = new Query(Criteria.where("username").is(username).and("hostInt").is(host));
            listUser = (List)mongoTempl().find(query, User.class, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listUser;
    }

    public User getByEmail(String email){
        if(StringUtils.isEmpty(email)){
            return null;
        }
        User user= getOneByFieldname(emailField, email);

        return user;

    }

    public User getByPhone(String phone){
        if(StringUtils.isEmpty(phone)){
            return null;
        }
        User user= getOneByFieldname(phoneField, phone);

        return user;

    }

    public User getBySessionId(String sessionId){
        if(StringUtils.isEmpty(sessionId)){
            return null;
        }
        User user=getOneByFieldname(sessionIdField, sessionId);;

        return user;

    }

    public long countUserByGroupId(String idgroup) {

        long userList = 0;
        try {
            Query query = new Query(Criteria.where("groupPath").is(idgroup));
            userList = mongoTempl().count(query, User.class, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }
}
