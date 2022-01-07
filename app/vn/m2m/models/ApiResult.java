package vn.m2m.models;

import com.fasterxml.jackson.databind.JsonNode;
import vn.m2m.common.HazelcastTTL;
import vn.m2m.common.ModelData;

@ModelData(collection = "ApiResult", mapCacheName = "", mapCacheTTL = HazelcastTTL.TTL_1_DAY)
public class ApiResult {
    private static final long serialVersionUID = 1L; //1L

    private boolean success;
    private int errorCode;
    private String errorName;
    private String resultText;
    private JsonNode jsonData;
    private int resultStatus;
    private Object body;

    public ApiResult(){

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public JsonNode getJsonData() {
        return jsonData;
    }

    public void setJsonData(JsonNode jsonData) {
        this.jsonData = jsonData;
    }

    public int getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(int resultStatus) {
        this.resultStatus = resultStatus;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
