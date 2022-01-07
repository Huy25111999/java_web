package services;

import org.apache.commons.lang.StringUtils;
import play.mvc.Http;
import vn.m2m.common.models.UserAdmin;
import vn.m2m.utils.ParseUtil;
import vn.m2m.utils.PasswordHash;
import vn.m2m.utils.UserHelper;

import javax.inject.Singleton;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Singleton
public class UserAdminService extends AbstractService{
    public UserAdmin authenticateUserAdmin(String email, String password) {
        UserAdmin userAdmin = userAdminDAO.getByEmail(email);
        if(userAdmin !=null) {
            String encryptPassword = userAdmin.getPassword();
            boolean isValidatePassword = false;
            try {
                isValidatePassword = PasswordHash.validatePassword(password, encryptPassword);
            }
            catch (NoSuchAlgorithmException |InvalidKeySpecException ex){
                ex.printStackTrace();
            }

            //logger.debug("username:{} - userId:{}",user.getUsername(), user.getId());
            //logger.debug("isActive:{} - isValidatePassword:{}",user.isActive(), isValidatePassword);
            if (userAdmin.isActive() && isValidatePassword) {
                //logger.debug("isActive & login pass");
                addSessionIdToUserAdmin(userAdmin);
                //logger.debug("login success");
                return userAdmin;
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
    }
    public void addUserAdminSessionToCookie(Http.Session session, UserAdmin userAdmin, boolean isApp){
        String email = userAdmin.getEmail();
        int maxlengthName = (email.length()>=8)? 8: email.length();
        String name = email.substring(0, maxlengthName);
        String sessionId = session.get(UserHelper.SessionData.sessionId);
        if(org.springframework.util.StringUtils.isEmpty(sessionId)){
            sessionId = UserHelper.genaralSessionId();
            session.put(UserHelper.SessionData.sessionId, sessionId);
            userAdmin.setSessionId(sessionId);
            userAdminDAO.saveUserAdmin(userAdmin);
        }

        session.put(UserHelper.SessionData.email, email);
        session.put(UserHelper.SessionData.name, name);
        session.put(UserHelper.SessionData.avatar, userAdmin.getAvatarLinkPath());
        session.put(UserHelper.SessionData.lastModified, "" + ParseUtil.parseDate(userAdmin.getLastModified()));
        if(isApp==true) {
            session.put(UserHelper.SessionData.isApp, "1");
        }
    }

    public UserAdmin addSessionIdToUserAdmin(UserAdmin userAdmin){
        String sessionId = UserHelper.genaralSessionId();
        userAdmin.setSessionId(sessionId);
        userAdminDAO.saveUserAdmin(userAdmin);
        return userAdmin;
    }
    public void updateUserAdminProfile(UserAdmin userAdmin){
        userAdminDAO.save(userAdmin);
    }

    public boolean isEmailExisted(String email){
        UserAdmin userAdmin = userAdminDAO.getByEmail(email);
        if (userAdmin != null){
            return true;
        }
        else{
            return false;
        }
    }

    public UserAdmin registerUserAdmin(String email, String password,int role){
        UserAdmin userAdmin = new UserAdmin(email, password,role);
        userAdminDAO.saveUserAdmin(userAdmin);
        return userAdmin;
    }

    public void deleteSessionId(String sessionId){
        if(StringUtils.isEmpty(sessionId)){
            return;
        }
        UserAdmin userAdmin = userAdminDAO.getBySessionId(sessionId);
        if(userAdmin != null){
            userAdminDAO.deleteSessionId(userAdmin);
        }
    }

    public UserAdmin getUserAdminBySession(String sessionId){
        return userAdminDAO.getBySessionId(sessionId);
    }

    public void updateAdminProfile(UserAdmin userAdmin){
        userAdminDAO.saveUserAdmin(userAdmin);
    }


}
