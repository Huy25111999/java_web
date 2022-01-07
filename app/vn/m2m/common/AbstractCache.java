package vn.m2m.common;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.data.annotation.Id;
import org.springframework.util.StringUtils;
import play.Logger;
import vn.m2m.config.HazelcastConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * Created by tannb on 11/02/2016.
 */
public abstract class AbstractCache<KeyType, T> {
    Logger.ALogger logger = Logger.of(AbstractCache.class);
    protected Class<KeyType> keyTypeClass;
    protected Class<T> tClass;

    protected String collectionName;
    protected String mapCacheName;
    protected int mapCacheTTL;
    protected boolean isUseCache;
    protected boolean isUseMongo;

    protected String mapCacheNameFieldName;

    protected abstract HazelcastInstance hazelcastInstance();

    public KeyType getKey(T obj){

        // find @Id in super class
        if(tClass.getSuperclass()!=null){
            // do something with current's fields
            Class<?> sClass = tClass.getSuperclass();
            for (Field f: sClass.getDeclaredFields()) {             //->get all fields
                Annotation annotation = f.getAnnotation(Id.class);

                if (annotation instanceof Id) {
                    try {
                        f.setAccessible(true);
                        return (KeyType) f.get(obj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // find @Id in current class
        for (Field f: tClass.getDeclaredFields()) {             //->get all fields
            Annotation annotation = f.getAnnotation(Id.class);

            if (annotation instanceof Id) {
                try {
                    f.setAccessible(true);
                    return (KeyType) f.get(obj);
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                    logger.error("Error description:",ex);

                }
            }
        }

        return null;
    }


    public AbstractCache(Class<KeyType> keyTypeClass, Class<T> type){
        this.keyTypeClass = keyTypeClass;
        this.tClass = type;
        if (tClass.isAnnotationPresent(ModelData.class)) {
            Annotation annotation = tClass.getAnnotation(ModelData.class);
            ModelData modelData = (ModelData) annotation;
            this.collectionName = modelData.collection();
            isUseMongo=true;
            if(StringUtils.isEmpty(collectionName)){
                isUseMongo=false;
            }

            this.mapCacheName = modelData.mapCacheName();
            isUseCache=true;
            if (StringUtils.isEmpty(this.mapCacheName))
            {
                isUseCache=false;
            }
            else {
                this.mapCacheName = HazelcastConfig.PREFIX_CACHE + this.mapCacheName;
                this.mapCacheNameFieldName = this.mapCacheName + HazelcastConfig.SUFFIX_FIELDNAME_CACHE;
            }
            this.mapCacheTTL = modelData.mapCacheTTL();
        }

    }

    // ---------------------- IMAP ------------------------------
    private IMap<KeyType, T> cacheMap;

    private synchronized IMap<KeyType, T> getCacheMap() {
        try {
            if (cacheMap == null) {
                cacheMap = hazelcastInstance().getMap(mapCacheName);
            }
            return cacheMap;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error description:",ex);
        }
        return null;
    }
    // ---------------------- IMAP ------------------------------

    // ---------------------- Function Cache---------------------
    public void putCache(T obj){
        putCache(getKey(obj), obj);
    }
    public void putCache(KeyType key, T obj){
        if (StringUtils.isEmpty(key)) {
            return;
        }
        getCacheMap().put(key, obj, mapCacheTTL, TimeUnit.SECONDS);
    }

    public void putCache(KeyType key, T obj, long ttl){
        if (StringUtils.isEmpty(key)) {
            return;
        }
        getCacheMap().put(key, obj, ttl, TimeUnit.SECONDS);
    }

    public void putAllCache(Map<KeyType, T> objMap){
        if (objMap == null || objMap.isEmpty()) {
            return;
        }
        getCacheMap().putAll(objMap);
    }

    public void putAllCache(List<T> objList){
        if (objList == null || objList.isEmpty()) {
            return;
        }
        for(T entry:objList){
            putCache(entry);
        }
    }

    public void deleteCache(T obj)
    {
        deleteCacheByKey(getKey(obj));
    }

    public void deleteCacheByKey(KeyType key){
        if (StringUtils.isEmpty(key)) {
            return;
        }
        getCacheMap().delete(key);
    }

    public T getCacheByKey(KeyType key){
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return getCacheMap().get(key);
    }

    public T getAndRemoveCacheByKey(KeyType key){
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return getCacheMap().remove(key);
    }

    public List<T> getAllCache(){
        return new ArrayList<T>(getCacheMap().values());
    }

    public Map<KeyType, T> getAllCache(Set<KeyType> keys){
        Map<KeyType, T> map = getCacheMap().getAll(keys);
        if(map==null){
            return new HashMap<KeyType, T>();
        }
        else{
            return map;
        }
    }

    public List<T> getAllListCache(Set<KeyType> keys){
        Map<KeyType, T> map = getAllCache(keys);
        return new ArrayList<T>(map.values());
    }

    public void clearCache(){
        getCacheMap().clear();
    }
    // ---------------------- Function Cache---------------------

    // ---------------------- IMAP Cache Fieldname------------------------------
    private IMap<String, KeyType> cacheMapFieldname;

    private synchronized IMap<String, KeyType> getCacheMapFieldname() {
        try {
            if (cacheMapFieldname == null) {
                cacheMapFieldname = hazelcastInstance().getMap(mapCacheNameFieldName);
            }
            return cacheMapFieldname;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error description:",ex);
        }
        return null;
    }

    // ---------------------- IMAP Cache Fieldname------------------------------

    // ---------------------- Function Cache Fieldname---------------------
    public void putCacheFieldName(String fieldName, Object fieldValue, T obj){
        if (StringUtils.isEmpty(fieldName)||StringUtils.isEmpty(fieldValue)) {
            return;
        }
        String key = fieldName + HazelcastConfig.DELIMITER_CACHE+ String.valueOf(fieldValue);

        KeyType id = getKey(obj);
        if(id ==null){
            return;
        }

        getCacheMapFieldname().put(key, id, mapCacheTTL, TimeUnit.SECONDS);
    }

    public T getCacheFieldNameByKey(String fieldName, Object fieldValue){
        if (StringUtils.isEmpty(fieldName)||StringUtils.isEmpty(fieldValue)) {
            return null;
        }
        String key = fieldName + HazelcastConfig.DELIMITER_CACHE+String.valueOf(fieldValue);
        KeyType id =  getCacheMapFieldname().get(key);

        if(id==null){
            return null;
        }

        return getCacheByKey(id);
    }

    public void deleteCacheFieldNameByKey(String fieldName, Object fieldValue){
        if (StringUtils.isEmpty(fieldName)||StringUtils.isEmpty(fieldValue)) {
            return;
        }
        String key = fieldName + HazelcastConfig.DELIMITER_CACHE+String.valueOf(fieldValue);
        if(StringUtils.isEmpty(key)){
            getCacheMapFieldname().delete(key);
        }
    }

    public void clearCacheFieldName(){
        getCacheMapFieldname().clear();
    }

    public void putCacheFieldName(LinkedHashMap<String, Object> fields, T obj){
        if(fields==null || fields.isEmpty()){
            return;
        }
        String key = fields.entrySet().stream().map(
                (entry) -> entry.getKey() + HazelcastConfig.DELIMITER_CACHE + entry.getValue()
        ).collect(Collectors.joining(HazelcastConfig.DELIMITER_CACHE));

        KeyType id = getKey(obj);
        if(id ==null){
            return;
        }

        getCacheMapFieldname().put(key, id, mapCacheTTL, TimeUnit.SECONDS);
    }

    public T getCacheFieldNameByKey(LinkedHashMap<String, Object> fields){
        if(fields==null || fields.isEmpty()){
            return null;
        }
        String key = fields.entrySet().stream().map(
                (entry) -> entry.getKey() + HazelcastConfig.DELIMITER_CACHE + entry.getValue()
        ).collect(Collectors.joining(HazelcastConfig.DELIMITER_CACHE));

        KeyType id = getCacheMapFieldname().get(key);
        if(id ==null){
            return null;
        }

        return getCacheByKey(id);
    }

    public void deleteCacheFieldNameByKey(LinkedHashMap<String, Object> fields){
        if(fields==null || fields.isEmpty()){
            return;
        }
        String key = fields.entrySet().stream().map(
                (entry) -> entry.getKey() + HazelcastConfig.DELIMITER_CACHE + entry.getValue()
        ).collect(Collectors.joining(HazelcastConfig.DELIMITER_CACHE));

        getCacheMapFieldname().delete(key);
    }

    // ---------------------- Function Cache Fieldname---------------------

    public boolean lockObject(String name){
        if (name != null && !name.equals("")) {
            Lock lock = hazelcastInstance().getLock(name);
            if(lock.tryLock()){
                return true;
            }
        }
        return false;
    }

    public boolean lockObject(String name, long time){
        if (name != null && !name.equals("")) {
            Lock lock = hazelcastInstance().getLock(name);
            try {
                if(lock.tryLock(time, TimeUnit.SECONDS)){
                    return true;
                }
            }
            catch (InterruptedException ex){
                ex.printStackTrace();
                lock.unlock();
                return false;
            }

        }
        return false;
    }

    public void unlockObject(String name){
        if (name != null && !name.equals("")) {
            Lock lock = hazelcastInstance().getLock(name);
            lock.unlock();
        }
    }
}
