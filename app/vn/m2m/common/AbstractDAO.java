package vn.m2m.common;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import play.Logger;
import vn.m2m.common.models.forms.SearchCondition;
import vn.m2m.common.models.forms.SearchFilter;
import vn.m2m.utils.ParseUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by tannb on 11/02/2016.
 */
public abstract class AbstractDAO<KeyType, T> extends AbstractCache<KeyType, T> {
    protected Logger.ALogger logger = Logger.of(AbstractDAO.class);

    protected abstract MongoTemplate mongoTempl() throws Exception;

    public AbstractDAO(Class<KeyType> keyTypeClass, Class<T> type){
        super(keyTypeClass, type);

    }

    // ---------------------- Function DAO---------------------

    public void save(T obj) {
        save(getKey(obj), obj);
    }

    public void update(T obj) {
        save(getKey(obj), obj);
    }

    public Set<String> getListCollections() {
        Set<String> colls = new HashSet<>();
        try {
            colls = mongoTempl().getCollectionNames();
        }catch (Exception e){
            e.printStackTrace();
        }
        return colls;
    }

    public void save(KeyType key, T obj) {
        if (obj == null) {
            return;
        }
        try {
            if(isUseMongo) {
                mongoTempl().save(obj, collectionName);
            }

            if (isUseCache)
            {
                try {
                    putCache(key, obj);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error("Error description:",ex);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error description:",ex);
        }
        return;
    }

    public void insert(KeyType key, T obj) {
        if (obj == null) {
            return;
        }
        try {
            if(isUseMongo) {
                mongoTempl().insert(obj, collectionName);
            }

            if (isUseCache)
            {
                try {
                    putCache(key, obj);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error("Error description:",ex);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error description:",ex);
        }
        return;
    }

    public void insertLog(T obj) {
        if (obj == null) {
            return;
        }
        try {
            mongoTempl().insert(obj, collectionName);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error description:",ex);
        }
        return;
    }

    public T getByKey(KeyType key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        T obj = null;
        if (isUseCache)
        {
//            System.out.println("We're using cache "+mapCacheName+" with the key "+key);
            try {
                obj = getCacheByKey(key);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:",ex);
            }
        }

        if (obj == null) {
            if(isUseMongo) {
                try {
                    Query query = new Query(Criteria.where("_id").is(key));
                    obj = mongoTempl().findOne(query, tClass, collectionName);
                    if (isUseCache) {
                        try {
                            if (obj != null) {
                                putCache(key, obj);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            logger.error("Error description:",ex);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error("Error description:",ex);
                }
            }
        }

        return obj;
    }

    public boolean isExistsKey(KeyType key){
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        try {
            Query query = new Query(Criteria.where("_id").is(key));
            query.fields().include("_id");
            query.limit(1);
            return mongoTempl().exists(query,collectionName);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error description:",ex);
            return false;
        }
    }

    public void deleteByKey(KeyType key) {
        if(isUseMongo) {
            Query query = new Query(Criteria.where("_id").is(key));
            try {
                mongoTempl().remove(query, tClass, collectionName);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:",ex);
            }
        }

        if (isUseCache) {
            try {
                deleteCacheByKey(key);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:",ex);
            }
        }
    }

    public void deleteByFieldName(String fieldName, String fieldValue) {
        if (isUseCache) {
            try {
                List<T> objList = getByFieldname(fieldName,fieldValue);
                if(objList != null) {
                    for (T obj : objList) {
                        deleteCache(obj);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:",ex);
            }
        }

        if(isUseMongo) {
            Query query = new Query(Criteria.where(fieldName).is(fieldValue));
            try {
                mongoTempl().remove(query, tClass, collectionName);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:",ex);
            }
        }
    }

    public List<T> getAll() {
        List<T> objList=new ArrayList<T>();
        try {
            objList = mongoTempl().findAll(tClass, collectionName);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error description:",ex);
        }
        /*if (isUseCache)
        {
            try {
                objList = getAllCache();
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:",ex);
            }
        }

        if (objList == null || objList.isEmpty()) {
            if(isUseMongo) {
                try {
                    objList = mongoTempl().findAll(tClass, collectionName);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error("Error description:",ex);
                }
            }
        }*/
        return objList;
    }


    public List<KeyType> getAllKeyList() {
        try {
            Query query = new Query();
            query.fields().include("_id");
            List<T> objList = mongoTempl().find(query,tClass, collectionName);
            if(objList!=null && !objList.isEmpty()) {
                List<KeyType> keyList = objList.stream().map(obj -> getKey(obj)).collect(Collectors.toList());
                return keyList;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error description:", ex);
        }
        return new ArrayList<>();
    }

    public Set<KeyType> getAllKeySet() {
        try {
            Query query = new Query();
            query.fields().include("_id");
            List<T> objList = mongoTempl().find(query,tClass, collectionName);
            if(objList!=null && !objList.isEmpty()) {
                Set<KeyType> keySet = objList.stream().map(obj -> getKey(obj)).collect(Collectors.toSet());
                return keySet;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error description:", ex);
        }
        return new HashSet<>();
    }

    public List<KeyType> getKeyListByFieldname(String fieldName,Object fieldValue) {
        if(StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(fieldValue)){
            return new ArrayList<KeyType>();
        }

        try {
            Query query = new Query();
            query.addCriteria(Criteria.where(fieldName).is(fieldValue));
            query.fields().include("_id");
            List<T> objList = mongoTempl().find(query,tClass, collectionName);
            if(objList!=null && !objList.isEmpty()) {
                List<KeyType> keyList = objList.stream().map(obj -> getKey(obj)).collect(Collectors.toList());
                return keyList;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error description:", ex);
        }
        return new ArrayList<>();
    }

    public Set<KeyType> getKeySetByFieldname(String fieldName,Object fieldValue) {
        if(StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(fieldValue)){
            return new HashSet<>();
        }

        try {
            Query query = new Query();
            query.addCriteria(Criteria.where(fieldName).is(fieldValue));
            query.fields().include("_id");
            List<T> objList = mongoTempl().find(query,tClass, collectionName);
            if(objList!=null && !objList.isEmpty()) {
                Set<KeyType> keySet = objList.stream().map(obj -> getKey(obj)).collect(Collectors.toSet());
                return keySet;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error description:", ex);
        }
        return new HashSet<>();
    }

    public List<T> getByKeys(Collection<KeyType> collection) {
        List<T> objList=new ArrayList<T>();
        try {
            Query query = new Query(Criteria.where("_id").in(collection));
            objList = mongoTempl().find(query, tClass, collectionName);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return objList;
    }

    public Map<KeyType,T> getAllMap(){
        Map<KeyType,T> objMap = new HashMap<KeyType,T>();
        List<T> objList = getAll();
        if(objList==null || objList.isEmpty()){
            return objMap;
        }
        for(T obj: objList){
            objMap.put(getKey(obj),obj);
        }
        return objMap;
    }

    public void saveAll(Map<KeyType, T> objMap) {
        if (objMap == null || objMap.isEmpty()) {
            return;
        }
        try {
            for(KeyType key: objMap.keySet()){
                save(key, objMap.get(key));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error description:",ex);
        }
        return;
    }

    public long CountQuerry()
    {
        long num=0;
        if(isUseMongo) {
            try {
                num = mongoTempl().getCollection(collectionName).count();
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:",ex);
            }
        }
        return num;
    }

    public List<T> getByPage(int page, int pagesize){
        List<T> objList=new ArrayList<T>();
        if(isUseMongo) {
            try {
                Query query = new Query();
                query.skip(pagesize * (page - 1));
                query.limit(pagesize);
                objList = mongoTempl().find(query, tClass, collectionName);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:",ex);
            }
        }
        return objList;
    }

    public List<T> getByDate(String dateFieldName, Date stardate, Date enddate){
        List<T> objList=new ArrayList<T>();
        if(isUseMongo) {
            try {
                Query query = new Query(Criteria.where(dateFieldName).gte(stardate).lte(enddate));
                objList = mongoTempl().find(query, tClass, collectionName);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:",ex);
            }
        }
        return objList;
    }

    public List<T> searchInField(String fieldName,String searchKey){
        List<T> objList=new ArrayList<T>();
        if(isUseMongo) {
            try {
                Query query = new Query();
                query.addCriteria(Criteria.where(fieldName).regex(searchKey, "i"));
                objList = mongoTempl().find(query, tClass, collectionName);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:",ex);
            }
        }
        return objList;
    }

    public List<T> getByFieldname(String fieldName,Object fieldValue){
        if(StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(fieldValue)){
            return new ArrayList<T>();
        }

        List<T> resultList=new ArrayList<T>();
        if(isUseMongo) {
            try {
                Query query = new Query();
                query.addCriteria(Criteria.where(fieldName).is(fieldValue));
                resultList = mongoTempl().find(query, tClass, collectionName);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:",ex);
            }
        }
        return resultList;
    }

    public List<T> getByFieldAndPaging(String fieldName,String fieldValue,int page,int pagesize){
        List<T> resultList=new ArrayList<T>();
        if(isUseMongo) {
            try {
                Query query = new Query();
                query.addCriteria(Criteria.where(fieldName).is(fieldValue));
                query.skip(pagesize * (page - 1));
                query.limit(pagesize);
                resultList = mongoTempl().find(query, tClass, collectionName);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:",ex);
            }
        }
        return resultList;
    }

    public List<T> getByFieldAndPagingSort(String fieldName,String fieldValue,int page, int pagesize, String fieldNameSort, boolean isDesc){
        List<T> resultList=new ArrayList<T>();
        if(isUseMongo) {
            try {
                Query query = new Query();
                query.addCriteria(Criteria.where(fieldName).is(fieldValue));
                query.skip(pagesize * (page - 1));
                query.limit(pagesize);

                if (isDesc) {
                    query.with(new Sort(Sort.Direction.DESC, fieldNameSort));
                } else {
                    query.with(new Sort(Sort.Direction.ASC, fieldNameSort));
                }

                resultList = mongoTempl().find(query, tClass, collectionName);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:",ex);
            }
        }
        return resultList;
    }

    public  long countByFieldname(String fieldName,String fieldValue){
        long totalResult =0;
        if(StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(fieldValue)){
            return totalResult;
        }

        if(isUseMongo) {
            try {
                Query query = new Query();
                query.addCriteria(Criteria.where(fieldName).is(fieldValue));
                totalResult = mongoTempl().count(query, tClass, collectionName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return totalResult;
    }

    public T getOneByFieldname(String fieldName,Object fieldValue){
        if(StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(fieldValue)){
            return null;
        }

        T obj = null;
        if (isUseCache)
        {
            try {
                obj = getCacheFieldNameByKey(fieldName,fieldValue);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:",ex);
            }
        }

        if (obj == null) {
            if (isUseMongo) {
                try {
                    Query query = new Query();
                    query.addCriteria(Criteria.where(fieldName).is(fieldValue));
                    obj = mongoTempl().findOne(query, tClass, collectionName);
                    if (isUseCache) {
                        try {
                            if (obj != null) {
                                putCacheFieldName(fieldName,fieldValue, obj);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            logger.error("Error description:",ex);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error("Error description:", ex);
                }
            }
        }
        return obj;
    }

    public List<T> getByFieldnames(LinkedHashMap<String, Object> fields){
        if(fields==null || fields.isEmpty()){
            return new ArrayList<T>();
        }

        List<T> resultList=new ArrayList<T>();
        if (isUseMongo) {
            try {
                Query query = new Query();
                List<Criteria> criterias = fields.entrySet().stream().map(
                        (entry) -> Criteria.where(entry.getKey()).is(entry.getValue())
                ).collect(Collectors.toList());

                Criteria criteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));

                query.addCriteria(criteria);
                resultList = mongoTempl().find(query, tClass, collectionName);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:", ex);
            }
        }

        return resultList;
    }

    public T getOneByFieldnames(LinkedHashMap<String, Object> fields){
        if(fields==null || fields.isEmpty()){
            return null;
        }

        T obj = null;
        if (isUseCache)
        {
            try {
                obj = getCacheFieldNameByKey(fields);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("Error description:",ex);
            }
        }

        if (obj == null) {
            if (isUseMongo) {
                try {
                    Query query = new Query();
                    List<Criteria> criterias = fields.entrySet().stream().map(
                            (entry) -> Criteria.where(entry.getKey()).is(entry.getValue())
                    ).collect(Collectors.toList());

                    Criteria criteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));

                    query.addCriteria(criteria);
                    obj = mongoTempl().findOne(query, tClass, collectionName);
                    if (isUseCache) {
                        try {
                            if (obj != null) {
                                putCacheFieldName(fields, obj);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            logger.error("Error description:",ex);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error("Error description:", ex);
                }
            }
        }
        return obj;
    }

    public void setConditionListToQuery(Query query,List<SearchCondition> searchConditionList){
        Integer conditionItems = 0;
        List<Criteria> criterias = new ArrayList<Criteria>();
        while (conditionItems < searchConditionList.size()) {
            SearchCondition condition = new SearchCondition();
            condition = searchConditionList.get(conditionItems);
            if (condition.getMulCondition() != 1) {
                List<SearchCondition> conditionMulList = new ArrayList<SearchCondition>();
                Integer multiConditionIndex = 0;
                while (multiConditionIndex < condition.getMulCondition()) {
                    SearchCondition searchConditionMulti = new SearchCondition();
                    searchConditionMulti = searchConditionList.get(conditionItems);
                    conditionMulList.add(searchConditionMulti);
                    conditionItems++;
                    multiConditionIndex++;
                }
                List<Criteria> mulcriterias = new ArrayList<Criteria>();
                mulcriterias = createMultiCriteria(conditionMulList);
                for(Criteria i : mulcriterias){
                    criterias.add(i);
                }
            } else {
                criterias.add(createCriteria(condition));
                conditionItems++;
            }
        }
        if(criterias.size() > 0){
            Criteria criteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
            query.addCriteria(criteria);
        }

    }

    public List<T> searchAndQueryWithoutPaging(SearchFilter searchFilter){
        List<SearchCondition> searchConditionList =searchFilter.getConditionList();
        List<T> objList=new ArrayList<T>();
        List<Criteria> criterias = new ArrayList<Criteria>();
        if(isUseMongo) {
            try {
                Query query = new Query();
                Integer conditionItems = 0;
                while (conditionItems < searchConditionList.size()) {
                    SearchCondition condition = new SearchCondition();
                    condition = searchConditionList.get(conditionItems);
                    if (condition.getMulCondition() != 1) {
                        List<SearchCondition> conditionMulList = new ArrayList<SearchCondition>();
                        Integer multiConditionIndex = 0;
                        while (multiConditionIndex < condition.getMulCondition()) {
                            SearchCondition searchConditionMulti = new SearchCondition();
                            searchConditionMulti = searchConditionList.get(conditionItems);
                            conditionMulList.add(searchConditionMulti);
                            conditionItems++;
                            multiConditionIndex++;
                        }
//                        query.addCriteria(createMultiCriteria(conditionMulList));
                        List<Criteria> mulcriterias = new ArrayList<Criteria>();
                        mulcriterias = createMultiCriteria(conditionMulList);
                        for(Criteria i : mulcriterias){
                            criterias.add(i);
                        }
//                        conditionItems++;
                    } else {
//                        query.addCriteria(createCriteria(condition));
                        criterias.add(createCriteria(condition));
                        conditionItems++;
                    }
                }
                if(criterias.size() > 0){
                    Criteria criteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
                    query.addCriteria(criteria);
                }

                if (!StringUtils.isEmpty(searchFilter.getSortFieldName())) {
                    if (searchFilter.isDesc()) {
                        query.with(new Sort(Sort.Direction.DESC, searchFilter.getSortFieldName()));
                    } else {
                        query.with(new Sort(Sort.Direction.ASC, searchFilter.getSortFieldName()));
                    }
                }
                objList = mongoTempl().find(query, tClass, collectionName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return objList;
    }

    public List<T> searchAndQuery(SearchFilter searchFilter){
        int page = searchFilter.getPage();
        int pagesize =searchFilter.getPageSize();
        List<SearchCondition> searchConditionList =searchFilter.getConditionList();
        List<T> objList=new ArrayList<T>();
        if(isUseMongo) {
            try {
                Query query = new Query();
                setConditionListToQuery(query,searchConditionList);
                query.skip(pagesize * (page - 1));
                query.limit(pagesize);
                if (!StringUtils.isEmpty(searchFilter.getSortFieldName())) {
                    if (searchFilter.isDesc()) {
                        query.with(new Sort(Sort.Direction.DESC, searchFilter.getSortFieldName()));
                    } else {
                        query.with(new Sort(Sort.Direction.ASC, searchFilter.getSortFieldName()));
                    }
                }
                objList = mongoTempl().find(query, tClass, collectionName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return objList;
    }
    public List<Criteria> createMultiCriteria (List<SearchCondition> searchConditionList){
        Criteria criteria = new Criteria();
        Integer mulConditionTotal = searchConditionList.size();
        if (0== mulConditionTotal) {
            return  null;
        }
        List<Criteria> mulCriteriaList = new ArrayList<Criteria>();
        for(SearchCondition searchMulCondition : searchConditionList){
            Criteria criteriaTemp = Criteria.where(searchConditionList.get(0).getFieldName());
            String valueStr = "";
            Object value = searchMulCondition.getFieldValue();
            switch (searchMulCondition.getCompQueryOp()) {
                case eq:
                    criteriaTemp.is(value);
                    break;
                case ne:
                    criteriaTemp.ne(value);
                    break;
                case gt:
                    criteriaTemp.gt(value);
                    break;
                case lt:
                    criteriaTemp.lt(value);
                    break;
                case gte:
                    criteriaTemp.gte(value);
                    break;
                case lte:
                    criteriaTemp.lte(value);
                    break;
                case like:
                    valueStr = ParseUtil.parseString(value);
                    criteriaTemp.regex(valueStr,"i");
                    break;
                case nlike:
                    valueStr = ParseUtil.parseString(value);
                    criteriaTemp.not().regex(valueStr,"i");
                    break;
                default:
            }
            mulCriteriaList.add(criteriaTemp);
        }

        /*switch (mulCriteriaList.size()) {

            case 2:
                criteria.andOperator(mulCriteriaList.get(0), mulCriteriaList.get(1));
                break;
            case 3:
                criteria.andOperator(mulCriteriaList.get(0), mulCriteriaList.get(1), mulCriteriaList.get(2));
                break;
            case 4:
                criteria.andOperator(mulCriteriaList.get(0), mulCriteriaList.get(1), mulCriteriaList.get(2),mulCriteriaList.get(3));
                break;
            case 5:
                criteria.andOperator(mulCriteriaList.get(0), mulCriteriaList.get(1), mulCriteriaList.get(2), mulCriteriaList.get(3),mulCriteriaList.get(4));
                break;
            case 6:
                criteria.andOperator(mulCriteriaList.get(0), mulCriteriaList.get(1), mulCriteriaList.get(2), mulCriteriaList.get(3), mulCriteriaList.get(4),mulCriteriaList.get(5));
                break;
            default:
        }*/

        return mulCriteriaList;
    }

    public Criteria createCriteria(SearchCondition condition){

        Object value = condition.getFieldValue();

        String valueStr = "";
        Criteria criteria = Criteria.where(condition.getFieldName());
        // eq, ne, gt, lt, gte , lte, like, nlike, in
        switch (condition.getCompQueryOp()) {
            case eq:
                criteria.is(value);
                break;
            case ne:
                criteria.ne(value);
                break;
            case gt:
                criteria.gt(value);
                break;
            case lt:
                criteria.lt(value);
                break;
            case gte:
                criteria.gte(value);
                break;
            case lte:
                criteria.lte(value);
                break;
            case like:
                valueStr = ParseUtil.parseString(value);
                valueStr=valueStr.trim();
                criteria.regex(valueStr,"i");
                break;
            case nlike:
                valueStr = ParseUtil.parseString(value);
                valueStr=valueStr.trim();
                criteria.not().regex(valueStr,"i");
                break;
            case in:
                Collection collection = (Collection) condition.getFieldValue();
                criteria.in(collection);
                break;
            default:

        }

        return criteria;
    }
    public  long countOnQuery(SearchFilter searchFilter){
        long TotalResult =0;
        if(isUseMongo) {
            List<SearchCondition> searchConditionList = searchFilter.getConditionList();
            try {
                Query query = new Query();
                setConditionListToQuery(query,searchConditionList);
                // TotalResult = mongoTempl().count(query, DeviceGPS.class, "DeviceGPS");
                TotalResult = mongoTempl().count(query, tClass, collectionName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TotalResult;
    }


    // ---------------------- Function DAO---------------------
}
