package vn.m2m.models;

import org.springframework.data.annotation.Id;
import vn.m2m.common.HazelcastTTL;
import vn.m2m.common.ModelData;
import vn.m2m.utils.DateUtil;

import java.io.Serializable;
import java.util.Date;

@ModelData(collection = "Config", mapCacheName = "", mapCacheTTL = HazelcastTTL.TTL_1_DAY)
public class Config implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String key;
    private String value;

    public Config(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Config() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
