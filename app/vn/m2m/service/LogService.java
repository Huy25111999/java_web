package vn.m2m.service;


import vn.m2m.models.ServerLogDAO;
import vn.m2m.models.ServerLog;
import vn.m2m.config.AppfileConfig;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Instant;

@Singleton
public class LogService {

    private final AppfileConfig appfileConfig;
    private final ServerLogDAO serverLogDAO;

    @Inject
    public LogService(AppfileConfig appfileConfig, ServerLogDAO serverLogDAO) {
        this.appfileConfig = appfileConfig;
        this.serverLogDAO = serverLogDAO;
    }

    public void loggingStartServer(){
        String appName= appfileConfig.serverAppname;
        String logContent= appName+" server started successfully";
        ServerLog serverLog = new ServerLog();
        serverLog.setUsrId("0");
        serverLog.setIsSuccess(true);
        serverLog.setLogContent(logContent);
        serverLog.setNewObj(null);
        serverLog.setOldObj(null);
//        serverLogDAO.save(serverLog);
    }

    public void loggingStopServer(Instant start, Instant stop){
        Long runningTime = stop.getEpochSecond() - start.getEpochSecond();
        String appName= appfileConfig.serverAppname;
        String logContent= appName+" server stopped successfully begin:"+start+" end:"+stop+"("+runningTime+"s)";
        ServerLog serverLog = new ServerLog();
        serverLog.setUsrId("0");
        serverLog.setIsSuccess(true);
        serverLog.setLogContent(logContent);
        serverLog.setNewObj(null);
        serverLog.setOldObj(null);
//        serverLogDAO.save(serverLog);
    }
}
