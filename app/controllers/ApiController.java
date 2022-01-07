package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.ajaxjsons.AbstractAjaxJsonModel;
import play.libs.Json;
import play.mvc.Result;
import services.MailerService;
import vn.m2m.common.models.User;
import vn.m2m.config.AppfileConfig;
import vn.m2m.utils.DateUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiController extends AbstractController {
    @Inject
    public MailerService mailerService;
    @Inject
    public AppfileConfig appfileConfig;

    public Result sendMail(){
        AbstractAjaxJsonModel respone = new AbstractAjaxJsonModel();
        respone.setIssuccess(true);

        JsonNode json = request().body().asJson();
        String key = json.get("key").asText();
        String title = json.get("title").asText();
        String body = json.get("body").asText();
        String emailList = json.get("emailList").asText();

        if(!key.equals("123456!@#$%^")){
            respone.setIssuccess(false);
            respone.setContent("Error");
            return ok(Json.toJson(respone));
        }

        List<String> myList = new ArrayList<String>();
        myList = Arrays.asList(emailList.split(","));

//        String emailTitle = "NAP MONEY - " + username + DateUtil.getDateByFormat(DateUtil.now(), DateUtil.DATE_TIME_CONVERT);
//        String emailBody = "Dear, " +
//                "\r\nTên: " + username +
//                "\r\nVip: " + soTienNap
//                ;
//        List<String> toAddresses=new ArrayList<String>();
//        toAddresses.add("123@gmail.com");
        try {
            mailerService.sendEmail(appfileConfig.serverEmail,
                    myList,
                    title,
                    null,
                    body,
                    null
            );
        } catch (Exception e) {
            e.printStackTrace();
            respone.setIssuccess(false);
            respone.setContent("Exception");
            return ok(Json.toJson(respone));
        }

        return ok(Json.toJson(respone));
    }
}
