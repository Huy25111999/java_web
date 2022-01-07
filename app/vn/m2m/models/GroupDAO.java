package vn.m2m.models;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import vn.m2m.common.AbstractDAO;
import vn.m2m.config.HazelcastConfig;
import vn.m2m.config.MongoConfig;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class GroupDAO extends AbstractDAO<String, Group> {
    private static String pathField = "path";
    private static String idField = "id";
    private static String groupRoleField ="groupRole";
    private static String groupTestField = "groupTest";

    @Inject
    public MongoConfig mongoConfig;

    @Inject
    private HazelcastConfig hazelcastConfig;

    protected MongoTemplate mongoTempl() throws Exception {
        return mongoConfig.mongoTemplate();
    }

    protected HazelcastInstance hazelcastInstance() {
        return hazelcastConfig.getHazelcastInstance();
    }

    public GroupDAO() {
        super(String.class, Group.class);

    }

    public Group getGroupById(String groupId) {
        return getOneByFieldname(idField, groupId);
    }

    public List<Group> getAllGroupsByID(String id) {
        List<Group> groups = null;
        try {
            Query query = new Query(Criteria.where(pathField).is(id));
            groups = mongoTempl().find(query, Group.class, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups;

    }

    public List<Group> getGroupsUntest() {
        List<Group> groups = null;
        try {
            Query query = new Query(Criteria.where(groupTestField).is(false));
            groups = mongoTempl().find(query, Group.class, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups;

    }

    public List<Group> getChildGroupsByID(String id) {

        List<Group> groups = null;
        try {
            Query query = new Query(Criteria.where("path").is(id).and("_id").ne(id));
            groups = mongoTempl().find(query, Group.class, collectionName);

            //int a=1;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return groups;

    }

    public List<Group> getListOtherGroups(String id) {

        List<Group> groups = null;
        try {
            Query query = new Query(Criteria.where(idField).ne(id));
            groups = mongoTempl().find(query, Group.class, collectionName);

            //int a=1;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return groups;

    }

    public Group getGroupByID(String id) {

        Group group = null;
        try {
            Query query = new Query(Criteria.where("_id").is(id));
            group = mongoTempl().findOne(query, Group.class, collectionName);
            //int a=1;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return group;

    }

    public Group getGroupByName(String name) {

        Group group = null;

        try {

            Query query = new Query(Criteria.where("name").is(name));
            group = mongoTempl().findOne(query, Group.class, collectionName);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return group;

    }

    public void deleteGroupById(String id) {
        if(isUseMongo) {
            Query query = new Query(Criteria.where("_id").is(id));
            try {
                mongoTempl().remove(query, Group.class, collectionName);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:",ex);
            }
        }
//        if (isUseCache) {
//            try {
//                deleteCacheByKey(id);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                logger.error("Error description:",ex);
//            }
//        }
    }

    public Set<String> allOrgIds(){
        List<Group> groups = getByFieldname(groupRoleField, 1);
        Set<String> orgIds = groups.stream()
                .filter(
                        entry -> !"root".equals(entry.getName())
                )
                .map(
                        entry -> entry.getOrgId()
                )
                .collect(Collectors.toSet());

        if(orgIds==null){
            return new HashSet<String>();
        }
        return orgIds;
    }

    public List<Group> getGroupsByPathWithPaging(String idgroup,int page,
                                                 int pagesize,String sortFieldName,boolean isDesc) {
        List<Group> groups = null;
        try {
            Query query = new Query(Criteria.where(pathField).is(idgroup));
            query.skip(pagesize * (page - 1));
            query.limit(pagesize);
            if (!StringUtils.isEmpty(sortFieldName)) {
                if (isDesc) {
                    query.with(new Sort(Sort.Direction.DESC, sortFieldName));
                } else {
                    query.with(new Sort(Sort.Direction.ASC, sortFieldName));
                }
            }
            groups = mongoTempl().find(query, Group.class, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups;
    }

    public List<Group> getAllGroupsByPathWithPaging(int page,int pagesize,String sortFieldName,boolean isDesc) {
        List<Group> groups = null;
        try {
            Query query = new Query();
            query.skip(pagesize * (page - 1));
            query.limit(pagesize);
            if (!StringUtils.isEmpty(sortFieldName)) {
                if (isDesc) {
                    query.with(new Sort(Sort.Direction.DESC, sortFieldName));
                } else {
                    query.with(new Sort(Sort.Direction.ASC, sortFieldName));
                }
            }
            groups = mongoTempl().find(query, Group.class, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups;
    }

    public List<Group> getAllGroupsUnTestByPathWithPaging(int page,int pagesize,String sortFieldName,boolean isDesc) {
        List<Group> groups = null;
        try {
            Query query = new Query(Criteria.where(groupTestField).is(false));
            query.skip(pagesize * (page - 1));
            query.limit(pagesize);
            if (!StringUtils.isEmpty(sortFieldName)) {
                if (isDesc) {
                    query.with(new Sort(Sort.Direction.DESC, sortFieldName));
                } else {
                    query.with(new Sort(Sort.Direction.ASC, sortFieldName));
                }
            }
            groups = mongoTempl().find(query, Group.class, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups;
    }

    public List<Group> getAllGroupById(String groupId) {
        List<Group> groups = new ArrayList<>();
        try {
            Query query = new Query(Criteria.where("path").is(groupId));
            groups = mongoTempl().find(query, Group.class, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return groups;
    }
}
