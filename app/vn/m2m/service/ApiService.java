package vn.m2m.service;

import akka.http.javadsl.model.HttpRequest;
import akka.japi.Function;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.appjsons.ErrorCodeApp;
import org.springframework.util.StringUtils;
import play.Logger;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.shaded.ahc.io.netty.util.concurrent.Promise;
import vn.m2m.models.ApiResult;
import vn.m2m.utils.DateUtil;
import vn.m2m.utils.ObjectUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.CompletionStage;

/**
 * Created by vtk-anhlt166 on 4/2/21.
 */
@Singleton
public class ApiService {
    public Logger.ALogger logger = Logger.of(ApiService.class);
    @Inject
    public WSClient ws;
    @Inject
    public ApiService(WSClient ws){
        this.ws = ws;
    }

    public String convertSha256(String data, String key){
        byte[] hmacSha256 = null;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }

        logger.debug("bytesToHex(hmacSha256) :{}", bytesToHex(hmacSha256));
        return bytesToHex(hmacSha256);
    }

    public String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0, v; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public ApiResult getAccount(String apiKey, String secretKey){
        long timestamp = DateUtil.nowGMT().getTime();
        logger.info("timestamp :{}", timestamp);
        String sha256 = convertSha256("recvWindow=10000&timestamp="+timestamp, secretKey);
        String urlBinance = "https://api.binance.com/api/v3/account?recvWindow=10000&timestamp=" + timestamp + "&signature=" + sha256;
        logger.info("getAccount : {}", urlBinance);
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlBinance);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("X-MBX-APIKEY", apiKey);
            conn.setRequestMethod("GET");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
            logger.info("status : {}", conn.getResponseCode());
            logger.info("result : {}", result.toString());

            ApiResult apiResult =new ApiResult();
            apiResult.setResultStatus(conn.getResponseCode());
            apiResult.setJsonData(ObjectUtil.jsonStrToJsonNode(result.toString()));
            return apiResult;
        }catch (Exception e){
            e.printStackTrace();
            logger.info("Exception:{}", e);
            ApiResult apiResult =new ApiResult();
            apiResult.setSuccess(false);
            apiResult.setErrorName(ErrorCodeApp.UnknowError.getName());

            return apiResult;
        }
    }

    public ApiResult getPriceTiket(String symbol){
        String url = "https://api.binance.com/api/v3/ticker/price?symbol=" + symbol;
        try {
            WSRequest request = ws.url(url);
            CompletionStage<ApiResult> responseCompletionStage = request.setRequestTimeout(Duration.ofMillis(10000))
                    .get()
                    .thenApply(wsResponse -> getApiResult(wsResponse));
            ApiResult apiResult=responseCompletionStage.toCompletableFuture().get();
            apiResult.setErrorName("get price");
            apiResult.setSuccess(true);

            return apiResult;
        }catch (Exception e){
            e.printStackTrace();
            logger.info("Exception:{}", e);
            ApiResult apiResult =new ApiResult();
            apiResult.setSuccess(false);
            apiResult.setErrorName(ErrorCodeApp.UnknowError.getName());

            return apiResult;
        }
    }

    public ApiResult sendApiJson(){
        //call api authen -> get token
        String url = "https://us1.unwiredlabs.com/v2/process.php";
        ArrayList<JsonNode> arrayListCells = new ArrayList<>();
        JsonNode cells = Json.newObject()
                .put("lac", 43300)
                .put("cid", 75430933)
                .put("psc", 0);
        arrayListCells.add(cells);
        ArrayList<String> arrayListWifi = new ArrayList<>();
        JsonNode data = Json.newObject()
                .put("token", "284a3628cddb31")
                .put("radio", "nbiot")
                .put("address", 1)
                .put("mcc", 452)
                .put("mnc", 4)
                .putPOJO("wifi", arrayListWifi)
                .putPOJO("cells", arrayListCells);
        logger.info("sendApiJson :{}", data);
        try {
            WSRequest request = ws.url(url);
            CompletionStage<ApiResult> responseCompletionStage = request.setRequestTimeout(Duration.ofMillis(10000))
                    .setContentType("application/json")
                    .post(data)
                    .thenApply(wsResponse -> getApiResult(wsResponse));
            ApiResult apiResult=responseCompletionStage.toCompletableFuture().get();
            apiResult.setErrorName("get token");
            apiResult.setSuccess(true);

            return apiResult;
        }catch (Exception e){
            e.printStackTrace();
            logger.info("Exception:{}", e);
            ApiResult apiResult =new ApiResult();
            apiResult.setSuccess(false);
            apiResult.setErrorName(ErrorCodeApp.UnknowError.getName());

            return apiResult;
        }
    }

    private ApiResult getApiResult(WSResponse wsResponse){
        logger.info("wsResponse: {}",wsResponse.getBody());
        logger.info("wsResponse status: {}",wsResponse.getStatus());
        int responeStatus=wsResponse.getStatus();

        ApiResult apiResult = new ApiResult();

        apiResult.setResultStatus(responeStatus);
        apiResult.setBody(wsResponse.getBody());
        apiResult.setJsonData(ObjectUtil.jsonStrToJsonNode(wsResponse.getBody()));

        if (responeStatus==200){
            apiResult.setSuccess(true);
        }else{
            apiResult.setSuccess(false);
        }

        return apiResult;
    }

}
