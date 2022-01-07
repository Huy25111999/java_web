package vn.m2m.config;

import com.mongodb.*;
import org.bson.Document;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoAction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteConcernResolver;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import play.Logger;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Singleton
public class MongoConfig extends AbstractMongoConfiguration{

	private final MongoClient myMongoClient;
	private String databaseName;
	private MongoTemplate mongoTemplate;
	public boolean isReplicaSet;

	@Inject
	public MongoConfig(ApplicationLifecycle lifecycle, AppfileConfig appfileConfig) throws Exception{
		this.databaseName = appfileConfig.databaseNameMongo;
		this.myMongoClient = myMongoClient(appfileConfig);
		this.mongoTemplate = initMongoTemplate();
		this.isReplicaSet  = appfileConfig.isReplicaSetMongo;
		lifecycle.addStopHook(() -> {
			myMongoClient.close();
			Logger.info("[Destroy] ----------------------SHUTDOWN MONGO CONNECTION----------------------");
			return CompletableFuture.completedFuture(null);
		});
	}

	public class LogWriteConcernResolver implements WriteConcernResolver {

		@Override
		public WriteConcern resolve(MongoAction action) {
			Logger.debug("LogWriteConcernResolver check");
			String simpleName = action.getEntityType().getSimpleName();

			if (simpleName.contains("Log")) {
				Logger.debug("{} to {} with writeconcern {}",
						action.getMongoActionOperation(), simpleName, WriteConcern.UNACKNOWLEDGED);
				return WriteConcern.UNACKNOWLEDGED;
			}

			return action.getDefaultWriteConcern();
		}
	}

	public MongoClient myMongoClient(AppfileConfig appfileConfig) throws Exception {
		List<ServerAddress> seeds = new ArrayList<ServerAddress>();
		for(String networkMember: Arrays.asList(appfileConfig.networkMembersMongo.split(","))){
			networkMember = networkMember.trim();
			List<String> network = Arrays.asList(networkMember.split(":"));
			String host = network.get(0);
			int port = Integer.parseInt(network.get(1));
			seeds.add(new ServerAddress(host,port));
		}

		MongoCredential credential = MongoCredential.createCredential(appfileConfig.usernameMongo, appfileConfig.databaseNameMongo, appfileConfig.passwordMongo.toCharArray());

		WriteConcern writeConcernLevel =  WriteConcern.W1;
		ReadPreference readPreferenceLevel = ReadPreference.primary();
		if(appfileConfig.isReplicaSetMongo){
			writeConcernLevel = WriteConcern.W2;
			readPreferenceLevel = ReadPreference.nearest();
		}

		MongoClientOptions options = MongoClientOptions.builder()
				.connectionsPerHost(appfileConfig.connectionPerHostMongo) //recommence 40 < 100
				.threadsAllowedToBlockForConnectionMultiplier(appfileConfig.threadAllowBlockMongo) // recommence 1/4 connectionPerHost
				.socketKeepAlive(appfileConfig.socketKeepAliveMongo) // true | false
				.socketTimeout(appfileConfig.socketTimeoutMongo) //millisecond
				.connectTimeout(appfileConfig.connectTimeoutMongo) //millisecond
				.writeConcern(writeConcernLevel)
				.readPreference(readPreferenceLevel)
				.build();

		MongoClient client = new MongoClient(seeds, Arrays.asList(credential), options);
		Logger.info("[Init] ----------------------MONGO CONNECTION----------------------");
		return client;
	}

	@Override
	protected String getDatabaseName() {
		return databaseName;
	}

//	@Override
//	public Mongo mongo() throws Exception {
//		return mongoClient;
//	}

	@Override
	public MongoClient mongoClient() {
		return myMongoClient;
	}

	private MongoTemplate initMongoTemplate() throws Exception{
		DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
		MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext());

		// Don't save _class to mongo
		mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
		MongoTemplate template = new MongoTemplate(mongoDbFactory(), mappingConverter);
		Logger.info("[Init] ----------------------Config mappingConverter: don't save _class to mongo----------------------");

		WriteConcernResolver writeConcernResolver = new LogWriteConcernResolver();
		template.setWriteConcernResolver(writeConcernResolver);
		Logger.info("[Init] ----------------------Config writeConcernResolver: write log with UNACKNOWLEDGED----------------------");
		return new MongoTemplate(mongoDbFactory(), mappingConverter );
	}

	public MongoTemplate mongoTemplate() throws Exception {
		return mongoTemplate;
	}

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public Document rsStatus(){
		Document result = null;
		if(this.isReplicaSet) {
			result = myMongoClient.getDatabase("admin").runCommand(new Document("replSetGetStatus", 1));
		}
		else {
			result = myMongoClient.getDatabase(this.databaseName).runCommand(new BasicDBObject("dbstats", 1));
		}
		return  result;
	}
}
