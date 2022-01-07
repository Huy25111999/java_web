package controllers;

import org.springframework.util.StringUtils;
import play.Logger;
import play.data.FormFactory;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import services.UserService;
import vn.m2m.common.dao.UserDAO;
import vn.m2m.common.models.User;
import vn.m2m.common.models.User;
import vn.m2m.models.Group;
import vn.m2m.models.GroupDAO;
import vn.m2m.utils.UserHelper;

import javax.inject.Inject;
import java.util.Arrays;

public class AbstractController extends Controller {
    public Logger.ALogger logger = Logger.of(AbstractController.class);
    @Inject
    public MessagesApi messagesApi;
    @Inject
    public FormFactory formFactory;

    @Inject
    public UserDAO userDAO;

    @Inject
    public GroupDAO groupDAO;

    @Inject
    public UserService userService;

    public Messages getMessages() {
        Messages messages = messagesApi.preferred(request());
        return messages;
    }

    public String getLangCode(){
        String lang = ctx().lang().code();
        logger.info("lang: {}",lang);
        if (StringUtils.isEmpty(lang))
        {
            return "vi";
        }
        return lang;
    }

    public void addUserSessionToCookie(User userAdmin){
        userService.addUserSessionToCookie(session(), userAdmin, false);
    }

    public User getUserSession(){
        String sessionId = session().get(UserHelper.SessionData.sessionId);

        if(StringUtils.isEmpty(sessionId)){
            logger.info("getUserSession - sessionId isEmpty");
            userService.cleanUserSessionInCookie(Http.Context.current());
            return null;
        }
        if(UserHelper.isSessionTimeOut(sessionId)){
            // session expired
            logger.info("getUserSession - session expired - session.clear():");
            userService.cleanUserSessionInCookie(Http.Context.current());
            return null;
        }
        logger.debug("getUserSession - sessionId: {}",sessionId);
        User user = userService.getUserBySession(sessionId);
        if(user==null){
            logger.debug("getUserSession - getUserBySession({}) is null",sessionId);
            userService.cleanUserSessionInCookie(Http.Context.current());
        }
        return user;
    }

    public void updateUserProfile(User user){
        userService.updateUserProfile(user);
        userService.addUserSessionToCookie(session(), user, false);
    }

    public boolean isGroupRoot(Group group){
        if (group==null){
            return false;
        }
        return group.getGroupRole()== Group.Roles.root.getCode();
    }

    public boolean isUserPossessGroup(User user,Group group){
        if (group==null||user==null){
            return false;
        }
        String userGroupId = user.getGroupId();
        return Arrays.asList(group.getPath()).contains(userGroupId);
    }

    public boolean isGroupRootUser(){
        User userSession = getUserSession();
        Group groupSession = groupDAO.getByKey(userSession.getGroupId());
//        logger.debug("groupSession {}",Json.toJson(groupSession));

        if (groupSession.getGroupRole() == Group.Roles.root.getCode()){
            return  true;
        }
        return false;
    }

}
