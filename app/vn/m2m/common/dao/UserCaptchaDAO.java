package vn.m2m.common.dao;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.StringUtils;
import vn.m2m.common.AbstractDAO;
import vn.m2m.common.models.forms.UserCaptcha;
import vn.m2m.config.HazelcastConfig;
import vn.m2m.config.MongoConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserCaptchaDAO extends AbstractDAO<String, UserCaptcha> {
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

    public UserCaptchaDAO(){
        super(String.class, UserCaptcha.class);

    }

    public UserCaptcha getByUsername(String login){
        if(StringUtils.isEmpty(login)){
            return null;
        }

        UserCaptcha dataModel= getOneByFieldname("login", login);

        return dataModel;
    }

}
