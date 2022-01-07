package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.ajaxjsons.AbstractAjaxJsonModel;
import models.forms.UserForm;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import vn.m2m.common.dao.UserDAO;
import vn.m2m.common.models.User;
import vn.m2m.common.models.forms.*;
import vn.m2m.config.AppfileConfig;
import vn.m2m.models.ApiResult;
import vn.m2m.models.Group;
import vn.m2m.service.ApiService;
import vn.m2m.utils.*;

import javax.inject.Inject;
import java.util.*;

import static vn.m2m.utils.SearchUtil.*;

@Security.Authenticated(Secured.class)
public class UserController extends AbstractController {
    @Inject
    public UserDAO userDAO;
    @Inject
    AppfileConfig appfileConfig;
    @Inject
    public ApiService apiService;

    public Result userList(){
        User user = getUserSession();
        if(!UserHelper.isAdminPermit(user)){
            flash("failed",getMessages().at("You are not permitsion"));
            return redirect(routes.Application.login());
        }

        SearchObjectData searchObjectData = new SearchObjectData();
        SearchFilter searchFilter = new SearchFilter();

        List<String> searchArray = new ArrayList<String>();
        searchArray.add(SEARCH_STRING_TYPE);
        searchArray.add("username");
        searchArray.add(SEARCH_INT_TYPE);
        searchArray.add("role");

        searchFilter = InitConditionListData(searchArray,"role"); // last field is sort field name

        List<Object> objectList = new ArrayList<Object>();
        objectList =(List<Object>)(List) userDAO.searchAndQuery(searchFilter);

        long countTotal = userDAO.countOnQuery(searchFilter);
        searchObjectData = PagingDataAfterSearch(searchFilter, countTotal);
        searchObjectData.setObjectList(objectList);

        return ok(views.html.user.UserManageList.render(user, searchObjectData, searchArray));
    }

    public Result userFilterList() {
        Form<SearchFilterForm> searchGenericFormForm = formFactory.form(SearchFilterForm.class);
        SearchFilterForm searchFilterForm = searchGenericFormForm.bindFromRequest().get();

        return userFilterList(searchFilterForm);
    }

    public Result userFilterList(SearchFilterForm searchFilterForm) {
        User userSession = getUserSession();
        if(!UserHelper.isAdminPermit(userSession)){
            flash("failed",getMessages().at("You are not permitsion"));
            return redirect(routes.Application.login());
        }

        List<String> searchArray = new ArrayList<String>();
        searchArray.add(SEARCH_STRING_TYPE);
        searchArray.add("username");
        searchArray.add(SEARCH_INT_TYPE);
        searchArray.add("role");

        SearchFilter searchFilter = new SearchFilter();
        searchFilter = InitConditionListDataPOST(searchArray, searchFilterForm);
        searchFilter.setIsDesc(true);
        searchFilter.setSortFieldName("role");

        List<Object> objectList = new ArrayList<Object>();
        objectList =(List<Object>)(List) userDAO.searchAndQuery(searchFilter);

        long countTotal = userDAO.countOnQuery(searchFilter);
        SearchObjectData searchObjectData = new SearchObjectData();
        searchObjectData = PagingDataAfterSearchPOST(searchFilter, searchFilterForm,countTotal);
        searchObjectData.setObjectList(objectList);

        searchArray = new ArrayList<String>();
        searchArray.add(SEARCH_STRING_TYPE);
        searchArray.add("username");
        searchArray.add(SEARCH_INT_TYPE);
        searchArray.add("role");

        return ok(views.html.user.UserManageList.render(userSession, searchObjectData, searchArray));
    }

    public Result exportUserList(){
        User userSession = getUserSession();
        AbstractAjaxJsonModel respone = new AbstractAjaxJsonModel();
        respone.setIssuccess(true);

        if(!UserHelper.isAdminPermit(userSession)){
            respone.setIssuccess(false);
            respone.setResultString("Not permission!");
            return ok(Json.toJson(respone));
        }

        List<String> searchArray = new ArrayList<String>();
        searchArray.add(SEARCH_STRING_TYPE);
        searchArray.add("id");
        searchArray.add(SEARCH_STRING_TYPE);
        searchArray.add("username");

        Form<SearchFilterForm> searchGenericFormForm = formFactory.form(SearchFilterForm.class);
        SearchFilterForm searchFilterForm = searchGenericFormForm.bindFromRequest().get();
        searchFilterForm.setFilter(searchArray);

        SearchFilter searchFilter = new SearchFilter();
        searchFilter = InitConditionListDataPOST(searchArray, searchFilterForm);

        List<User> resultList = userDAO.searchAndQueryWithoutPaging(searchFilter);

        return ok(Json.toJson(resultList));
    }

    public Result editUser(){
        User userSession = getUserSession();
        if(!UserHelper.isAdminPermit(userSession)){
            flash("failed",getMessages().at("Not have permission"));
            return redirect(routes.Application.login());
        }

        Form<UserForm> formRequest = formFactory.form(UserForm.class).bindFromRequest();
        if (formRequest.hasErrors()) {
            logger.debug("form error");
            flash("failed", getMessages().at("form.error"));
            return redirect(routes.UserController.userList());
        }

        UserForm userForm = formRequest.get();
        logger.debug("userForm:{}", Json.toJson(userForm));
        String username = userForm.getUsername();
        String password = userForm.getPassword();
        String repeatPassword = userForm.getRepeatpassword();

        if(StringUtils.isEmpty(password) || StringUtils.isEmpty(repeatPassword)){
            flash("failed", getMessages().at("Pass không được bỏ trống"));
            return redirect(routes.UserController.userList());
        }
        if(!password.equals(repeatPassword)){
            flash("failed",getMessages().at("Mật khẩu lặp lại không đúng"));
            return redirect(routes.UserController.userList());
        }

        logger.debug("name :{}", username);
        User user = userDAO.getByUsername(username);
        if(user == null){
            flash("failed", getMessages().at("User null"));
            return redirect(routes.UserController.userList());
        } else {
            user.setPassword(UserHelper.hashPassword(password));
            userDAO.save(user);
        }

        flash("success", getMessages().at("Đổi mật khẩu thành công"));
        return redirect(routes.UserController.userList());
    }

    public Result deleteUser(){
        User userSession = getUserSession();
        if(!UserHelper.isAdminPermit(userSession)){
            flash("failed",getMessages().at("Not have permission"));
            return redirect(routes.Application.login());
        }

        Form<UserForm> formRequest = formFactory.form(UserForm.class).bindFromRequest();
        if (formRequest.hasErrors()) {
            logger.debug("form error");
            flash("failed", getMessages().at("form.error"));
            return redirect(routes.UserController.userList());
        }

        UserForm userForm = formRequest.get();
        String id = userForm.getId();

        User user = userDAO.getByKey(id);
        if(user == null){
            flash("failed", getMessages().at("user null"));
            return redirect(routes.UserController.userList());
        } else {
            userDAO.deleteByKey(id);
        }

        flash("success", getMessages().at("Xóa thành công"));
        return redirect(routes.UserController.userList());
    }

    public Result addUser() {
        User userSession = getUserSession();


        AbstractAjaxJsonModel respone = new AbstractAjaxJsonModel();
        respone.setIssuccess(true);

        if(!UserHelper.isAdminPermit(userSession)){
            respone.setIssuccess(false);
            respone.setErrorString(getMessages().at("noti.donthavepermission"));
            return ok(Json.toJson(respone));
        }

        JsonNode jsonGroup = request().body().asJson();
        String username = jsonGroup.get("username").asText();
        String password= jsonGroup.get("password").asText();
        String repeatpassword= jsonGroup.get("passwordRepeat").asText();
        int role = jsonGroup.get("role").asInt();

        if (org.springframework.util.StringUtils.isEmpty(username)
                || org.springframework.util.StringUtils.isEmpty(password)
                || org.springframework.util.StringUtils.isEmpty(repeatpassword)
                ){
            respone.setIssuccess(false);
            respone.setErrorString(getMessages().at("noti.validation"));
            return ok(Json.toJson(respone));
        }

        if(!StringUtil.validateStrongPass(password)){
            respone.setIssuccess(false);
            respone.setErrorString(getMessages().at("noti.valid.PasswordNotComplicated"));
            return ok(Json.toJson(respone));
        }

        if(!password.equals(repeatpassword)) {
            respone.setIssuccess(false);
            respone.setErrorString(getMessages().at("noti.valid.repeatpass"));
            return ok(Json.toJson(respone));
        }


        if (userDAO.getByUsername(username)!= null){
            respone.setIssuccess(false);
            respone.setErrorString(getMessages().at("manage.user.usernameExisted"));
            return ok(Json.toJson(respone));
        }


        User newUser = new User(username,password);
        newUser.setRole(role);
        userDAO.save(newUser);

        respone.setResultString(getMessages().at("noti.Addsuccess"));
        return ok(Json.toJson(respone));
    }

    public Result delUsers() {
        User userSession = getUserSession();

        AbstractAjaxJsonModel respone = new AbstractAjaxJsonModel();
        respone.setIssuccess(true);

        if(!UserHelper.isAdminPermit(userSession)){
            respone.setIssuccess(false);
            respone.setErrorString(getMessages().at("noti.donthavepermission"));
            return ok(Json.toJson(respone));
        }

        JsonNode jsonGroup = request().body().asJson();
        String listUsrid = jsonGroup.get("listUsrid").asText();

        if (org.springframework.util.StringUtils.isEmpty(listUsrid)){
            respone.setIssuccess(false);
            respone.setErrorString(getMessages().at("form.error"));
            return ok(Json.toJson(respone));
        }

        List<String> listId = Arrays.asList(listUsrid.split("\\s*,\\s*"));
        List<String> listUsername = new ArrayList<String>();
        //check permission
        for (String id:listId){
            User userDel=userDAO.getByKey(id);
            if (userDel==null){
                respone.setIssuccess(false);
                respone.setErrorString(getMessages().at("noti.notFoundUser"));
                return ok(Json.toJson(respone));
            }

            if (userSession.getId().equals(id)){   //khong the xoa chinh minh
                respone.setIssuccess(false);
                respone.setErrorString(getMessages().at("manage.user.cannotdeleteyourself"));
                return ok(Json.toJson(respone));
            }

            listUsername.add(userDel.getUsername());
            userDAO.deleteUser(userDel);

        }
        respone.setResultString(getMessages().at("noti.Deletesuccess"));
        return ok(Json.toJson(respone));
    }
}
