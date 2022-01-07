package services;

import org.apache.commons.lang.StringUtils;
import play.mvc.Http;
import vn.m2m.common.models.User;
import vn.m2m.utils.DateUtil;
import vn.m2m.utils.ParseUtil;
import vn.m2m.utils.PasswordHash;
import vn.m2m.utils.UserHelper;

import javax.inject.Singleton;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Singleton
public class UserService extends AbstractService{
    public User authenticateUser(String username, String password) {
        User user = userDAO.getByUsername(username);
        if(user !=null) {
            String encryptPassword = user.getPassword();
            boolean isValidatePassword = false;
            try {
                isValidatePassword = PasswordHash.validatePassword(password, encryptPassword);
            }
            catch (NoSuchAlgorithmException |InvalidKeySpecException ex){
                ex.printStackTrace();
            }

            //logger.debug("username:{} - userId:{}",user.getUsername(), user.getId());
            //logger.debug("isActive:{} - isValidatePassword:{}",user.isActive(), isValidatePassword);
            if (isValidatePassword) {
                //logger.debug("isActive & login pass");
                addSessionIdToUser(user);
                //logger.debug("login success");
                return user;
            }
            //logger.debug("login false");
        }
        //logger.debug("not have user:{}", email);
        return null;
    }

    public void cleanUserSessionInCookie(Http.Context ctx){
        Http.Session session = ctx.session();
        String sessionId = session.get(UserHelper.SessionData.sessionId);
        session.clear();
        deleteSessionId(sessionId);
        ctx.clearLang();
        clearAllCookie(ctx);
    }
    public void clearAllCookie(Http.Context ctx){
        Http.Cookies cookies = ctx.request().cookies();
        String host = ctx.request().host();

        String rootHost = host;
        if(host !=null) {
            rootHost = host.replaceFirst(host.split("\\.")[0], "");
        }
        for(Http.Cookie cookie: cookies){
//            ctx.response().discardCookie(cookie.name());
            logger.debug("cookie - name:{},domain:{},path:{}, domainRoot:{}",cookie.name(), cookie.domain(), cookie.path(), rootHost);

            ctx.response().discardCookie(cookie.name());
            ctx.response().discardCookie(cookie.name(),cookie.path(),rootHost);
        }

    }
    public void addUserSessionToCookie(Http.Session session, User user, boolean isApp){
        String email = user.getEmail();
        int maxlengthName = (email.length()>=8)? 8: email.length();
        String name = email.substring(0, maxlengthName);
        String sessionId = session.get(UserHelper.SessionData.sessionId);
        if(org.springframework.util.StringUtils.isEmpty(sessionId)){
            sessionId = UserHelper.genaralSessionId();
            session.put(UserHelper.SessionData.sessionId, sessionId);
            user.setSessionId(sessionId);
            userDAO.save(user);
        }

        session.put(UserHelper.SessionData.email, email);
        session.put(UserHelper.SessionData.name, name);
        session.put(UserHelper.SessionData.avatar, user.getAvatarLinkPath());
        session.put(UserHelper.SessionData.lastModified, "" + ParseUtil.parseDate(user.getLastModified()));
        if(isApp==true) {
            session.put(UserHelper.SessionData.isApp, "1");
        }
    }

    public User addSessionIdToUser(User user){
        String sessionId = UserHelper.genaralSessionId();
        user.setSessionId(sessionId);
        user.setCreateDate(DateUtil.now());
        userDAO.save(user);
        return user;
    }
    public void updateUserProfile(User user){
        userDAO.save(user);
    }

    public boolean isEmailExisted(String email){
        User user = userDAO.getByEmail(email);
        if (user != null){
            return true;
        }
        else{
            return false;
        }
    }

    public User registerUser(String email, String password){
        User user = new User(email, password);
        userDAO.saveUser(user);
        return user;
    }

    public void deleteSessionId(String sessionId){
        if(StringUtils.isEmpty(sessionId)){
            return;
        }
        User user = userDAO.getBySessionId(sessionId);
        if(user != null){
            logger.info("Call deleteSessionId - userId = {} - sessionID = {}",user.getId(),sessionId);
            userDAO.deleteSessionId(user,sessionId);
        }
    }

    public User getUserBySession(String sessionId){
        return userDAO.getBySessionId(sessionId);
    }

    public void updateProfile(User user){
        userDAO.saveUser(user);
    }


}
