package actors;

import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.appjsons.ErrorCodeApp;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import play.Logger;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import vn.m2m.common.dao.UserDAO;
import vn.m2m.common.models.User;
import vn.m2m.common.models.forms.SearchFilter;
import vn.m2m.config.AppfileConfig;
import vn.m2m.models.*;
import vn.m2m.service.ApiService;
import vn.m2m.utils.DateUtil;
import vn.m2m.utils.ObjectUtil;
import vn.m2m.utils.ParseUtil;
import vn.m2m.utils.StringUtil;

import javax.inject.Inject;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class ActorSchedule extends UntypedActor {
    Logger.ALogger logger = Logger.of(ActorSchedule.class);

    @Inject
    public UserDAO userDAO;
    @Inject
    public AppfileConfig appfileConfig;
    @Inject
    public ConfigDAO configDAO;
    @Inject
    public ApiService apiService;

    @Override
    public void onReceive(Object message) throws Exception {
        logger.debug("Got a crontab job!");
        if (message instanceof ScheduleJob){
            Date currentTime = DateUtil.now();

            int MINUS = Integer.parseInt(DateUtil.getDateByFormat(currentTime, DateUtil.MINUS));
            int SECONDS = Integer.parseInt(DateUtil.getDateByFormat(currentTime, DateUtil.SECOND));
            logger.info("MINUS :{} : SECONDS :{} ", MINUS, SECONDS);

        }
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
