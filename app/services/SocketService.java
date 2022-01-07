package services;

import com.fasterxml.jackson.databind.JsonNode;
import models.ajaxjsons.AbstractAjaxJsonModel;
import play.libs.Json;
import vn.m2m.common.models.User;
import vn.m2m.models.ApiResult;
import vn.m2m.service.ApiService;

import javax.inject.Inject;
import java.util.*;

public class SocketService extends AbstractService {
    @Inject
    public ApiService apiService;

    public JsonNode returnDataListCoin(String userId) {
        logger.info("socket for user id :{}", userId);
        AbstractAjaxJsonModel respone = new AbstractAjaxJsonModel();
        respone.setIssuccess(true);

        return Json.toJson(respone);
    }
}
