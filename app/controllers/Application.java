package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import play.mvc.Result;
import vn.m2m.common.dao.UserCaptchaDAO;
import vn.m2m.common.models.User;
import vn.m2m.common.models.forms.CaptchaResult;
import vn.m2m.common.models.forms.LoginForm;
import vn.m2m.models.RegisterForm;

import vn.m2m.utils.StringUtil;
import vn.m2m.utils.UserHelper;

import javax.inject.Inject;
import java.net.InetAddress;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletionStage;

public class Application extends AbstractController {
    public Logger.ALogger logger = Logger.of(Application.class);

    @Inject
    public UserCaptchaDAO userCaptchaDAO;

    @Inject
    WSClient ws;

//    @AddCSRFToken
    public Result login() {
        User user = getUserSession();
        if (user == null) {
            flash("false", getMessages().at("login.youMustRegister"));
            return ok(views.html.login.Login.render(""));
        } else {
            return redirect(routes.UserController.userList());
        }
    }

//    @RequireCSRFCheck
    public Result authenticate() {
        Optional<String> referer = request().header("referer");//request().getHeader("referer");
        logger.info("referer:{}",referer.get());
        Form<LoginForm> formRequest = formFactory.form(LoginForm.class);
        if (formRequest.hasErrors()) {
            flash("failed",getMessages().at("form.error"));
            return redirect(routes.Application.login());
        }
        LoginForm loginForm = formRequest.bindFromRequest().get();
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        session().put(UserHelper.SessionData.sessionEmailLogin, username);

        User user = userService.authenticateUser(username, password);
        if(user == null){
            flash("failed", getMessages().at("login.errorUsernameOrPass"));
            return redirect(
                    routes.Application.login()
            );
        }

        addUserSessionToCookie(user);

        flash("success", getMessages().at("login.loginSuccess"));
        if(UserHelper.isAdminPermit(user)){
            return redirect(routes.UserController.userList());
        } else {
            return redirect(routes.UserController.userList());
        }
    }

//    @AddCSRFToken
    public Result logout() {
        Http.Session session = Http.Context.current().session();
        String sessionId = session.get(UserHelper.SessionData.sessionId);
        logger.info("Call logout - sessionId = {}",sessionId);

        userService.cleanUserSessionInCookie(Http.Context.current());

        flash("success", getMessages().at("logout.logoutSuccess"));
        return redirect(
                routes.Application.login()
        );
    }

    public CaptchaResult validCaptcha(String response, String remoteip){
        CaptchaResult captchaResult=new CaptchaResult();
        try {
            String url="https://www.google.com/recaptcha/api/siteverify";
            WSRequest request = ws.url(url);
            Duration timeout = Duration.ofMillis(3000);
            CompletionStage<CaptchaResult> responseCompletionStage = request.setRequestTimeout(timeout)
                    .addQueryParameter("secret", UserHelper.reCaptchaSecretKey)
                    .addQueryParameter("response", response)
                    .addQueryParameter("remoteip", remoteip)
                    .post("content")
                    .thenApply(wsResponse -> getCaptchaResult(wsResponse));
            captchaResult=responseCompletionStage.toCompletableFuture().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return captchaResult;
    }

    private CaptchaResult getCaptchaResult(WSResponse wsResponse){
        String responetext=wsResponse.getBody();
        CaptchaResult captchaResult= new CaptchaResult();

        try{
            ObjectMapper objectMapper=new ObjectMapper();
            captchaResult = (CaptchaResult)objectMapper.readValue(responetext,CaptchaResult.class);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return captchaResult;
    }

    //test
    public Result createAccount() {
        String username ="admin";
        String email = username + "@gmail.com";
        String pass = "123456aA@";

        User userCheck = userDAO.getByEmail(email);
        if(userCheck == null){
            User user = new User(username, email, pass);
            user.setRole(User.Roles.sadmin.getCode());
            userDAO.save(user);

            flash("success", getMessages().at("create success user " + username));
            return ok(views.html.login.Login.render("true"));
        } else {
            flash("failed", getMessages().at("create FAILED user!"));
            return ok(views.html.login.Login.render("false"));
        }
    }

}
