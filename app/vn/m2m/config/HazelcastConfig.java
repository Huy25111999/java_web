package vn.m2m.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import play.Logger;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Singleton
public class HazelcastConfig {

	public static String PREFIX_CACHE ="AIS_";
	public static String SUFFIX_FIELDNAME_CACHE ="_FIELDNAME";
	public static String DELIMITER_CACHE ="_";
	private HazelcastInstance hazelcastInstance;
	private ClientConfig clientConfig;

	@Inject
	public HazelcastConfig(ApplicationLifecycle lifecycle, AppfileConfig appfileConfig){
		PREFIX_CACHE = appfileConfig.prefixNameHzc;
		clientConfig = clientConfig(appfileConfig);
		hazelcastInstance = init();
		lifecycle.addStopHook(() -> {
			hazelcastInstance.getLifecycleService().shutdown();
			Logger.info("[Destroy] ----------------------SHUTDOWN HAZELCAST CONNECTION----------------------");
			return CompletableFuture.completedFuture(null);
		});
	}

	private ClientConfig clientConfig(AppfileConfig appfileConfig){
		List<String> networklist = Arrays.asList(appfileConfig.networkMembersHzc.split(","));
		String[] networkArray = networklist.toArray(new String[0]);

		ClientConfig clientConfig = new ClientConfig();
		// tannb: set classloader for isDev environment (fix bug class not found)
		clientConfig.setClassLoader(appfileConfig.getClass().getClassLoader());
		clientConfig.getGroupConfig().setName(appfileConfig.groupNameHzc)
				.setPassword(appfileConfig.groupPasswordHzc);
		clientConfig.getNetworkConfig().addAddress(networkArray)
				.setConnectionAttemptPeriod(appfileConfig.connectionAttemptPeriodHzc)
				.setConnectionAttemptLimit(appfileConfig.connectionAttemptLimitHzc)
				.setConnectionTimeout(appfileConfig.connectionTimeoutHzc)
				.setRedoOperation(appfileConfig.isRedoOperationHzc)
				.setSmartRouting(appfileConfig.isSmartRoutingHzc);
		clientConfig.setExecutorPoolSize(appfileConfig.executorPoolSizeHzc);

		return clientConfig;
	}

	private HazelcastInstance init() {
		HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
		Logger.info("[Init] ----------------------HAZELCAST CONNECTION----------------------");
		Logger.info("[Init] ----------------------HAZELCAST PREFIX {} ----------------------", PREFIX_CACHE);
		Logger.debug("[DEBUG] ----------------------HAZELCAST OK----------------------");
		return hazelcastInstance;
	}

	public HazelcastInstance getHazelcastInstance(){
		return hazelcastInstance;
	}

	// ---------------------- clearCache ------------------------
	@SuppressWarnings("rawtypes")
	public void clearMapCache(String mapName) {
		Map map = getHazelcastInstance().getMap(mapName);
		if (map != null) {
			map.clear();
		}
	}
	// ---------------------- clearCache ------------------------
}
