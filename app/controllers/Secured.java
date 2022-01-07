package controllers;

import com.google.inject.Inject;
import org.springframework.util.StringUtils;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.UserService;
import vn.m2m.common.models.User;
import vn.m2m.utils.ParseUtil;
import vn.m2m.utils.UserHelper;

public class Secured extends Security.Authenticator {
    Logger.ALogger logger = Logger.of(Secured.class);
    @Inject
    UserService userService;

    @Override
    public String getUsername(Http.Context ctx) {
        Http.Session session = ctx.session();
        String sessionId = session.get(UserHelper.SessionData.sessionId);
        if(StringUtils.isEmpty(sessionId)){
            logger.info("sessionId empty");
            return null;
        }
//        if(UserHelper.isSessionTimeOut(sessionId)){
//            // session expired
//            logger.info("session expired - session.clear():");
//            userService.cleanUserSessionInCookie(ctx);
//            return null;
//        }
        User currentUser = getCurrentUser(sessionId);
        if(currentUser!= null){
            String lang = session.get(UserHelper.SessionData.language);
            if(!StringUtils.isEmpty(lang)){
                ctx.changeLang(lang);
            }
            else
            {
                ctx.changeLang("vi");
            }

            String lastModified = session.get(UserHelper.SessionData.lastModified);
            if(StringUtils.isEmpty(lastModified)
                    || !lastModified.equals(""+ ParseUtil.parseDate(currentUser.getLastModified()))){
                userService.addUserSessionToCookie(session, currentUser, false);
            }

            return currentUser.getEmail();
        }
        logger.info("currentUser null");
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        userService.cleanUserSessionInCookie(ctx);
        return redirect(routes.Application.login());
    }

    public User getCurrentUser() {
        String sessionId = Http.Context.current().session().get(UserHelper.SessionData.sessionId);
        return getCurrentUser(sessionId);
    }
    private User getCurrentUser(String sessionId) {
        if(StringUtils.isEmpty(sessionId))
        {
            return null;
        }
        User currentUser = userService.getUserBySession(sessionId);
        return currentUser;
    }
    // Access rights



}
