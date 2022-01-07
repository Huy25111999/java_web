package vn.m2m.common.models.forms;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class CaptchaResult {
    private boolean success;
    private Date challenge_ts;
    private String hostname;

    @JsonProperty("error-codes")
    private String[] errorCodes;

    public CaptchaResult() {
        success=false;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Date getChallenge_ts() {
        return challenge_ts;
    }

    public void setChallenge_ts(Date challenge_ts) {
        this.challenge_ts = challenge_ts;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String[] getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(String[] errorCodes) {
        this.errorCodes = errorCodes;
    }
}
