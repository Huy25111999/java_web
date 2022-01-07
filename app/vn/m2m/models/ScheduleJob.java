package vn.m2m.models;

import java.io.Serializable;

public class ScheduleJob implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;

    public ScheduleJob(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
