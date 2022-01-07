package vn.m2m.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.*;


public class ObjectUtil {
    public static <T> String Compare(T source, T target) throws IllegalArgumentException, IllegalAccessException {

        if(source == null) {
            throw new IllegalArgumentException("Null argument not excepted at this point");
        }

        Field[] fields = source.getClass().getDeclaredFields();

        Object sourceObject;
        Object targetObject;
        String compare="";
        for(Field field : fields){
            field.setAccessible(true);

            sourceObject = field.get(source);
            targetObject = field.get(target);
//            System.out.println(field.getName()+": "+sourceObject.toString()+" - "+targetObject.toString());
            //Compare the object
            if ((sourceObject == null && targetObject!=null)||
                    (sourceObject !=null && !sourceObject.equals(targetObject))){

                compare+= field.getName()+",";
            }
        }

        if (!StringUtils.isEmpty(compare)){
            compare=compare.substring(0,compare.length()-1);
        }

        return compare;

    }

    /**
     * This method makes a "deep clone" of any Java object it is given.
     */
    public static Object deepClone(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <T> T clone(T object)
    {
        return (T) deepClone(object);
    }

    public static List sortMapByValue(final Map m, boolean asc) {
        List keys = new ArrayList();
        keys.addAll(m.keySet());
        if (asc == true) {
            Collections.sort(keys, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Object v1 = m.get(o1);
                    Object v2 = m.get(o2);
                    if (v1 == null) {
                        return (v2 == null) ? 0 : 1;
                    } else if (v1 instanceof Comparable) {
                        return ((Comparable) v1).compareTo(v2);
                    } else {
                        return 0;
                    }
                }
            });
        } else {
            Collections.sort(keys, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Object v1 = m.get(o1);
                    Object v2 = m.get(o2);
                    if (v2 == null) {
                        return (v1 == null) ? 0 : 1;
                    } else if (v2 instanceof Comparable) {
                        return ((Comparable) v2).compareTo(v1);
                    } else {
                        return 0;
                    }
                }
            });
        }
        return keys;
    }

    public static List sortMapByValue(final Map m) {
        List keys = new ArrayList();
        keys.addAll(m.keySet());

        Collections.sort(keys, new Comparator() {
            public int compare(Object o1, Object o2) {
                Object v1 = m.get(o1);
                Object v2 = m.get(o2);
                if (v1 == null) {
                    return (v2 == null) ? 0 : 1;
                } else if (v1 instanceof Comparable) {
                    return ((Comparable) v1).compareTo(v2);
                } else {
                    return 0;
                }
            }
        });

        return keys;
    }


    public static <T> T jsonStrToObj(String jsonStr, Class<T> type){
        try{
            ObjectMapper mapper = new ObjectMapper();
            T obj = mapper.readValue(jsonStr, type);
            return obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JsonNode jsonStrToJsonNode(String jsonStr){
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonStr);
            return jsonNode;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<JsonNode> jsonNodeFilter(JsonNode jsonNode, String filterName){
        try{
            List<JsonNode> jsonNodeList = jsonNode.findValues(filterName);

            return jsonNodeList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T objectNodeToObj(ObjectNode jsonNode, Class<T> type){
        try{
            ObjectMapper mapper = new ObjectMapper();
            T obj = mapper.treeToValue(jsonNode, type);
//            T obj = mapper.convertValue(jsonNode, type);
            return obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T jsonToObj(JsonNode jsonNode, Class<T> type){
        try{
            ObjectMapper mapper = new ObjectMapper();
            T obj = mapper.treeToValue(jsonNode, type);
//            T obj = mapper.convertValue(jsonNode, type);
            return obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String objToJsonString(Object object){
        String jsonInString="";
        try{
            ObjectMapper mapper = new ObjectMapper();
            jsonInString = mapper.writeValueAsString(object);

        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonInString;
    }

    public static String objToJsonStringIgnoreNull(Object object){
        String jsonInString="";
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            jsonInString = mapper.writeValueAsString(object);

        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonInString;
    }

    public static JsonNode objToJsonNode(Object object){
        JsonNode node=null;
        try{
            ObjectMapper mapper = new ObjectMapper();
//            node = mapper.convertValue(object, JsonNode.class);
            node = mapper.valueToTree(object);
        }catch (Exception e){
            e.printStackTrace();
        }
        return node;
    }

    public static JsonNode objToJsonNodeIgnoreNull(Object object){
        JsonNode node=null;
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//            node = mapper.convertValue(object, JsonNode.class);
            node = mapper.valueToTree(object);
        }catch (Exception e){
            e.printStackTrace();
        }
        return node;
    }

    public static ObjectNode objToObjectNode(Object object){
        ObjectNode node=null;
        try{
            ObjectMapper mapper = new ObjectMapper();
            node = mapper.valueToTree(object);

        }catch (Exception e){
            e.printStackTrace();
        }
        return node;
    }



    //pagination list object
    //return a list that contains paginated lists
    public static <T> List<List<T>> getPages(Collection<T> c, Integer pageSize) {
        if (c == null)
            return Collections.emptyList();
        List<T> list = new ArrayList<T>(c);
        if (pageSize == null || pageSize <= 0 || pageSize > list.size())
            pageSize = list.size();
        int numPages = (int) Math.ceil((double)list.size() / (double)pageSize);     //Math.ceil - round up
        List<List<T>> pages = new ArrayList<List<T>>(numPages);
        for (int pageNum = 0; pageNum < numPages;)
            pages.add(list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size())));
        return pages;
    }

    public static <K,T> List<T> mapToList(Map<K,T> map){
        if(map!=null && !map.isEmpty()){
            return new ArrayList<T>(map.values());
        }
        return new ArrayList<T>();
    }

    public static boolean compareNode(ObjectNode o1,ObjectNode o2){
        if(o1==null && o2==null){
            return true;
        }
        if(o1==null || o2==null){
            return false;
        }

        if(o1.equals(o2)){
            return true;
        }
        else {
            return false;
        }

    }

    private static JsonNode mergeJsonNode(JsonNode mainNode, JsonNode updateNode) {

        Iterator<String> fieldNames = updateNode.fieldNames();

        while (fieldNames.hasNext()) {
            String updatedFieldName = fieldNames.next();
            JsonNode valueToBeUpdated = mainNode.get(updatedFieldName);
            JsonNode updatedValue = updateNode.get(updatedFieldName);

            // If the node is an @ArrayNode
            if (valueToBeUpdated != null && valueToBeUpdated.isArray() &&
                    updatedValue.isArray()) {
                // running a loop for all elements of the updated ArrayNode
                for (int i = 0; i < updatedValue.size(); i++) {
                    JsonNode updatedChildNode = updatedValue.get(i);
                    // Create a new Node in the node that should be updated, if there was no corresponding node in it
                    // Use-case - where the updateNode will have a new element in its Array
                    if (valueToBeUpdated.size() <= i) {
                        ((ArrayNode) valueToBeUpdated).add(updatedChildNode);
                    }
                    // getting reference for the node to be updated
                    JsonNode childNodeToBeUpdated = valueToBeUpdated.get(i);
                    mergeJsonNode(childNodeToBeUpdated, updatedChildNode);
                }
                // if the Node is an @ObjectNode
            } else if (valueToBeUpdated != null && valueToBeUpdated.isObject()) {
                mergeJsonNode(valueToBeUpdated, updatedValue);
            } else {
                if (mainNode instanceof ObjectNode) {
                    ((ObjectNode) mainNode).replace(updatedFieldName, updatedValue);
                }
            }
        }
        return mainNode;
    }

    public static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {

        Iterator<String> fieldNames = updateNode.fieldNames();

        while (fieldNames.hasNext()) {
            String updatedFieldName = fieldNames.next();
            JsonNode valueToBeUpdated = mainNode.get(updatedFieldName);
            JsonNode updatedValue = updateNode.get(updatedFieldName);

            if(valueToBeUpdated != null){
                if(updatedValue==null){
                    continue;
                }
                // If the node is an @ArrayNode
                if (valueToBeUpdated.isArray()) {
                    if(updatedValue.isArray()) {
                        // replace updatedValue
                        if (!valueToBeUpdated.equals(updatedValue)) {
                            ((ObjectNode)mainNode).replace(updatedFieldName, updatedValue);
                        }

//                        // running a loop for all elements of the updated ArrayNode
//                        for (int i = 0; i < updatedValue.size(); i++) {
//                            ObjectNode updatedChildNode = (ObjectNode) updatedValue.get(i);
//                            // Create a new Node in the node that should be updated, if there was no corresponding node in it
//                            // Use-case - where the updateNode will have a new element in its Array
//                            if (valueToBeUpdated.size() <= i) {
//                                valueToBeUpdated.setAll(updatedChildNode);
//                            }
//                            // getting reference for the node to be updated
//                            ObjectNode childNodeToBeUpdated = (ObjectNode) valueToBeUpdated.get(i);
//                            merge(childNodeToBeUpdated, updatedChildNode);
//                        }
                    }
                    else {
                        ((ObjectNode)mainNode).replace(updatedFieldName, updatedValue);
                    }

                    // if the Node is an @ObjectNode
                } else if (valueToBeUpdated.isObject()) {
                    if(updatedValue.isObject()) {
                        merge(valueToBeUpdated, updatedValue);
                    }
                    else {
                        ((ObjectNode)mainNode).replace(updatedFieldName, updatedValue);
                    }
                } else {
                    if(!valueToBeUpdated.equals(updatedValue)){
                        ((ObjectNode)mainNode).replace(updatedFieldName, updatedValue);
                    }
                }
            }
            else {
                if(updatedValue==null){
                    continue;
                }

                ((ObjectNode)mainNode).replace(updatedFieldName, updatedValue);
            }


        }
        return mainNode;
    }

    public static ObjectNode diff(JsonNode mainNode, JsonNode secondNode, ObjectNode deltaNode) {
        Iterator<String> fieldNames = mainNode.fieldNames();
        while (fieldNames.hasNext()) {
            String mainFieldName = fieldNames.next();
            JsonNode mainValue = mainNode.get(mainFieldName);
            JsonNode secondValue = secondNode.get(mainFieldName);

            if(mainValue != null){
                if(secondValue==null){
                    deltaNode.set(mainFieldName, mainValue);
                }
                else if(!mainValue.equals(secondValue)){
                    deltaNode.set(mainFieldName, mainValue);
                }
//                else {
//                    // If the node is an @ArrayNode
//                    if (mainValue.isArray() && secondValue.isArray()) {
//                                                // running a loop for all elements of the updated ArrayNode
//                        for (int i = 0; i < mainValue.size(); i++) {
//                            ObjectNode mainChildNode = (ObjectNode) mainValue.get(i);
//                            for (int j = 0; j < secondValue.size(); j++) {
//                                ObjectNode secondChildNode = (ObjectNode) secondValue.get(j);
//
//                            }
//                        }
//                        // if the Node is an @ObjectNode
//                    } else if (mainValue.isObject() && secondValue.isObject()) {
//                        diff(mainValue, secondValue, deltaNode);
//
//                    } else if(!mainValue.equals(secondValue)){
//                        deltaNode.set(mainFieldName, mainValue);
//                    }
//                }
            }

        }
        return deltaNode;
    }

    public static String timestampObject(long timestamp){
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("timestamp",timestamp);
        return objectNode.toString();
    }

    public static BasicDBObject objectToDBObject(Object obj){
        String json = objToJsonStringIgnoreNull(obj);
        return jsonStringToDBObject(json);
    }

    public static BasicDBList objectToDBList(Object obj){
        String json = objToJsonStringIgnoreNull(obj);
        return jsonStringToDBList(json);
    }


    public static BasicDBObject jsonStringToDBObject(String json){
        BasicDBObject dbObject = BasicDBObject.parse(json);
        return dbObject;
    }

    public static BasicDBList jsonStringToDBList(String json){
        BasicDBList dbList = (BasicDBList) JSON.parse(json);
        return dbList;
    }

    public static <T> T objectToPOJO(Object object, Class<T> type){
        try{
            ObjectMapper mapper = new ObjectMapper();
            T obj = mapper.convertValue(object, type);
            return obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static <T> boolean hasDuplicates(List<T> list){
        int count = list.size();
        T t1,t2;

        for(int i=0;i<count;i++){
            t1 = list.get(i);
            for(int j=i+1;j<count;j++){
                t2 = list.get(j);
                if(t2.equals(t1)){
                    return true;
                }
            }
        }
        return false;
    }

    public static <T> T objectToPOJO(BasicDBObject dbObject, Class<T> type){
        return objectToPOJO(dbObject, type);
    }
}
