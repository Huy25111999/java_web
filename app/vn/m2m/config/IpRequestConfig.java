package vn.m2m.config;

import play.Configuration;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Singleton
public class IpRequestConfig {
    public class IpRequestMethodConfig{
        // ex: 600/1 minutes, lock in 900 seconds
        private long limit;
        private int times;
        private TimeUnit unit; // DAYS, HOURS, MINUTES, SECONDS, MILLISECONDS
        private boolean islock;
        private long timelock;

        public IpRequestMethodConfig(long limit, int times, TimeUnit unit, boolean islock, long timelock) {
            this.limit = limit;
            this.times = times;
            this.unit = unit;
            this.islock = islock;
            this.timelock = timelock;
        }

        public long getLimit() {
            return limit;
        }

        public void setLimit(long limit) {
            this.limit = limit;
        }

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }

        public TimeUnit getUnit() {
            return unit;
        }

        public void setUnit(TimeUnit unit) {
            this.unit = unit;
        }

        public boolean islock() {
            return islock;
        }

        public void setIslock(boolean islock) {
            this.islock = islock;
        }

        public long getTimelock() {
            return timelock;
        }

        public void setTimelock(long timelock) {
            this.timelock = timelock;
        }
    }

    private HashMap<String, IpRequestMethodConfig> mapIpRequestConfig;

    public HashMap<String, IpRequestMethodConfig> getMapIpRequestConfig() {
        return mapIpRequestConfig;
    }

    @Inject
    public IpRequestConfig(Configuration configuration) throws Exception{
        mapIpRequestConfig = new HashMap<String, IpRequestMethodConfig>();

        List<String> listConfig = configuration.underlying().getStringList("iprequest.list");
        for(String key: listConfig){
            String keyLimit = "iprequest." + key + ".limit";
            String keyTimes = "iprequest." + key + ".times";
            String keyUnit = "iprequest." + key + ".unit";
            String keyIslock = "iprequest." + key + ".islock";
            String keyTimelock = "iprequest." + key + ".timelock";

            long limit = configuration.underlying().getLong(keyLimit);
            int times = configuration.underlying().getInt(keyTimes);
            TimeUnit unit = TimeUnit.valueOf(configuration.underlying().getString(keyUnit));
            boolean islock = configuration.underlying().getBoolean(keyIslock);
            long timelock = configuration.underlying().getLong(keyTimelock);

            IpRequestMethodConfig methodConfig = new IpRequestMethodConfig(limit, times, unit, islock, timelock);
            Logger.debug("mapIpRequestConfig.put {}",key);
            mapIpRequestConfig.put(key, methodConfig);
        }

    }

}
