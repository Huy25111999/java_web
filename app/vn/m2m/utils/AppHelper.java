package vn.m2m.utils;

import com.typesafe.config.Config;
import play.Application;

public class AppHelper {
    public static Config getAppConfig(){
        return
                play.api.Play.current().injector().instanceOf(Application.class).config();
    }

    public static String getAppConfigString(String key){
        return getAppConfig().getString(key);
    }
    public static Integer getAppConfigInt(String key){
        return getAppConfig().getInt(key);
    }
    public static Long getConfigLong(String key){
        return getAppConfig().getLong(key);
    }
    public static Double getAppConfigDouble(String key){
        return getAppConfig().getDouble(key);
    }

    public static Boolean getAppConfigBoolean(String key){
        return getAppConfig().getBoolean(key);
    }
}
