package utils.mongodb.build_query;

import lombok.NonNull;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KijiKokoroReflection {

    private KijiKokoroReflection(){
        this.tree = new ConcurrentHashMap<>();
    }

    private Map<String, Object> tree = null;

    public static final KijiKokoroReflection instance (){
     return new KijiKokoroReflection();
    }


    public void getNotNull(Object object) throws RuntimeException, IllegalAccessException {
        try {
            if (null != object) {
                Class<? extends Object> clazz = object.getClass();
                for (Field f : clazz.getDeclaredFields()) {
                    f.setAccessible(true);
                    if (null != f.get(object)) {
                        if (checkPrimitiveType(f.getType())) {
//                            if (ObjectId.class.isAssignableFrom(f.getType())) {
//                                tree.put(f.getName(), ((ObjectId) f.get(object)).toHexString());
//                            } else {
                                tree.put(f.getName(), f.get(object));
//                            }
                        } else {
                            if (f.getType().equals(List.class) || f.getType().equals(ArrayList.class)) {
                                List<Object> list;
                                if (f.getType().equals(List.class)) {
                                    list = ((List) f.get(object));
                                } else {
                                    ArrayList arrayList = ((ArrayList) f.get(object));
                                    list = new ArrayList<>();
                                    for (Object y : arrayList) {
                                        if (checkPrimitiveType(y.getClass())) {
                                            list.add(y);
                                        } else {
                                            KijiKokoroReflection tmp = KijiKokoroReflection.instance();
                                            tmp.getNotNull(y);
                                            list.add(tmp.geTreeMap());
                                        }
                                    }
                                }
                                tree.put(f.getName(), list);
                                continue;
                            } else if (f.getType().equals(Map.class) || f.getType().equals(HashMap.class)) {
                                Map<String, Object> map = ((HashMap) f.get(object));
                                HashMap x = new HashMap();
                                for (Map.Entry<String, Object> item : map.entrySet()) {
                                    if (checkPrimitiveType(item.getValue().getClass())) {
                                        x.put(item.getKey(), item.getValue());
                                    } else {
                                        KijiKokoroReflection tmp = KijiKokoroReflection.instance();
                                        tmp.getNotNull(item.getValue());
                                        x.put(item.getKey(), tmp.geTreeMap());
                                    }
                                }
                                tree.put(f.getName(), x);
                                continue;
                            } else if (f.getType().getClass().getSimpleName().toLowerCase().contains("class") && f.getType().getClass().isInstance(Object.class)) {
                                if (f.get(object) instanceof Long || f.get(object) instanceof Integer || f.get(object) instanceof String) {
                                    Object data = f.get(object);
                                    tree.put(f.getName(), data);
                                    continue;
                                } else {
                                    KijiKokoroReflection tmp = KijiKokoroReflection.instance();
                                    if (f != null) {
                                        tmp.getNotNull(f.get(object));
                                        tree.put(f.getName(), tmp.geTreeMap());
                                    }
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public Map geTreeMap() {
        return this.tree;
    }

    public void freeMap() {
        this.tree.clear();
    }

    private static boolean checkPrimitiveType(Class clazz) {
        if (String.class.isAssignableFrom(clazz)
                || Long.class.isAssignableFrom(clazz)
                || Integer.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz)
                || Double.class.isAssignableFrom(clazz)
                || Float.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz)
                || int.class.isAssignableFrom(clazz)
                || double.class.isAssignableFrom(clazz)
                || float.class.isAssignableFrom(clazz)
                || boolean.class.isAssignableFrom(clazz)
                || char.class.isAssignableFrom(clazz)
                || long.class.isAssignableFrom(clazz)
                || ObjectId.class.isAssignableFrom(clazz)
                || BigDecimal.class.isAssignableFrom(clazz)
                || BigInteger.class.isAssignableFrom(clazz)
        )
            return true;
        return false;
    }

    public Document buildQuerySet(Object object) throws IllegalAccessException, RuntimeException {
        this.getNotNull(object);
        Map<String, Object> data = this.geTreeMap();
        Document queryItem = new Document();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            queryItem.put(entry.getKey(), entry.getValue());
        }
        Document query = new Document();
        queryItem.remove("_id");
        query.put("$set", queryItem);
        return query;
    }

    public Document buildQuerySet(Document data) {
        Document query = new Document();
        query.put("$set", data);
        return query;
    }

    //    /**
//     * Function this is return Document of field nested can update
//     *
//     * @param nestedList
//     * @return
//     */
    public Document updateNestedChildElements(Object object, List<NestedElement> nestedList) throws IllegalAccessException, RuntimeException {
        this.getNotNull(object);
        Map<String, Object> data = this.geTreeMap();
        Document queryItem = new Document();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getKey().equals("_id")) {
                continue;
            }
            for (NestedElement nested : nestedList) {
                if (entry.getValue() instanceof ArrayList && entry.getKey().equals(nested.getParent())) {
                    ArrayList<ConcurrentHashMap> list = ((ArrayList) entry.getValue());
                    for (Map<String, Object> mapItem : list) {
                        for (ConcurrentHashMap.Entry<String, Object> entryItem : mapItem.entrySet()) {
                            if (entryItem.getKey().equals(nested.getField())) {
                                queryItem.put(nested.getParent() + ".$." + nested.getField(), entryItem.getValue());
                            }
                        }
                        break;
                    }
                } else {
                    if (!entry.getKey().equals(nested.getParent())) {
                        queryItem.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        Document query = new Document();
        query.put("$set", queryItem);
        return query;
    }

    public Document pushNonUniQueueToNested(@NonNull ConcurrentHashMap<String, Object> data, @NonNull String fieldName) {
        List<Object> each = new ArrayList<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getKey().equals(fieldName)) {
                if (entry.getKey().equals("_id")) {
                    continue;
                }
                if (entry.getValue() instanceof ArrayList || entry.getValue() instanceof List) {
                    ArrayList value = ((ArrayList) entry.getValue());
                    for (Object item : value) {
                        each.add(item);
                    }
                } else if (entry.getValue() instanceof Map) {
                    HashMap<String, Object> map = ((HashMap<String, Object>) entry.getValue());
                    for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                        each.add(entry1.getValue());
                    }
                } else {
                    each.add(entry.getValue());
                }
            }
        }
        Document query = new Document();
        Document itemQuery = new Document();
        itemQuery.put(fieldName, new Document("$each", each));
        query.put("$push", itemQuery);
        return query;
    }

    public Document pushUniQueueToNested(@NonNull ConcurrentHashMap<String, Object> data, @NonNull String fieldName) throws RuntimeException, Exception {
        Document query = new Document();
        query.put("$addToSet", pushNonUniQueueToNested(data, fieldName).get("$push"));
        return query;
    }
}
