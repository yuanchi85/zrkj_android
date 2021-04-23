package zrkj.project.util;

import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    /**
     * 将制定对象转换为Json字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String jsonStr = gson.toJson(obj);
        return jsonStr;
    }

    /**
     * 将指定的json字符串解析为指定类型对象
     * @param jsonStr json字符串
     * @param clazz   Class类型
     * @return
     */
    public static <T> T parseJson(String jsonStr, Class<T> clazz) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        T result = gson.fromJson(jsonStr, clazz);
        return result;
    }

    /**
     * 将指定的Object字符串解析为指定类型对象
     *
     * @param jsonStr Object字符串
     * @param clazz   Class类型
     * @param <T>
     * @return
     */
    public static <T> T paresEntity(Object jsonStr, Class<T> clazz) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String gsons = gson.toJson(jsonStr);
        T result = parseJson(gsons, clazz);
        return result;
    }

    public static <T> T paresNoDateEntity(Object jsonStr, Class<T> clazz) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String gsons = gson.toJson(jsonStr);
        T result = gson.fromJson(gsons, clazz);
        return result;
    }

    public static <T> List<T> parseJsonList(String jsonStr, Class<T> clazz) {
        System.err.println("jsonstr:" + jsonStr);
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(jsonStr).getAsJsonArray();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ArrayList<T> list = new ArrayList<>();

        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            T t = gson.fromJson(user, clazz);
            list.add(t);
        }
        return list;
    }

    /**
     * @param jsonStr Object字符串
     * @param clazz   Class类型
     * @param <T>
     * @return
     */
    public static <T> List<T> parseGsonStringList(Object jsonStr, Class<T> clazz) {
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        //将JSON的String 转成一个JsonArray对象
        String gsons = gson.toJson(jsonStr);
        JsonArray jsonArray = parser.parse(gsons).getAsJsonArray();
        ArrayList<T> list = new ArrayList<>();
        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            T t = gson.fromJson(user, clazz);
            list.add(t);
        }
        return list;
    }

    public static <T> List<T> parseGsonStringList1(Object jsonStr, Class<T> clazz) {
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        //将JSON的String 转成一个JsonArray对象
        String gsons = gson.toJson(jsonStr);
        JsonArray jsonArray = parser.parse(gsons).getAsJsonArray();
        ArrayList<T> list = new ArrayList<>();
        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            T t = gson.fromJson(user, clazz);
            list.add(t);
        }
        return list;
    }



    /**
     * 将制定的Object(JsonArray)转制定的List
     *
     * @param jsonStr Object(JsonArray)
     * @param clazz   Class类型
     * @param <T>
     * @return
     */
    public static <T> List<T> parseGsonStringLists(Object jsonStr, Class<T> clazz) {
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        JsonArray jsonArray = parser.parse(jsonStr.toString()).getAsJsonArray();
        ArrayList<T> list = new ArrayList<>();
        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            T t = gson.fromJson(user, clazz);
            list.add(t);
        }
        return list;
    }

   /* public static <T> List<T>  fromToJson(Object json, Type listType) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        String gsons = gson.toJson(json);
        JsonArray jsonArray = parser.parse(gsons).getAsJsonArray();
        ArrayList<T> list = new ArrayList<>();
        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            T t = gson.fromJson(user, listType);
            list.add(t);
        }
        return list;
    }*/

    public static <T> T fromToJson(Object obj,Type listType){
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        T t = null;
        t = gson.fromJson(json,listType);
        return t;
    }

    /**
     * 将map转实体
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T map2Object(Map<String, Object> map, Class<T> clazz) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (map == null) {
            return null;
        }
        T obj = null;
        try {
            // 使用newInstance来创建对象
            obj = clazz.newInstance();
            // 获取类中的所有字段
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                // 判断是拥有某个修饰符
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                // 当字段使用private修饰时，需要加上
                field.setAccessible(true);
                // 获取参数类型名字
                String filedTypeName = field.getType().getName();
                // 判断是否为时间类型，使用equalsIgnoreCase比较字符串，不区分大小写
                // 给obj的属性赋值
                if (filedTypeName.equalsIgnoreCase("java.util.date")) {
                    String datetimestamp = (String) map.get(field.getName());
                    if (datetimestamp.equalsIgnoreCase("null")) {
                        field.set(obj, null);
                    } else {
                        field.set(obj, sdf.parse(datetimestamp));
                    }
                } else {
                    field.set(obj, map.get(field.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    boolean blen=false;
    public Boolean SwitchYn(Switch swich) {
        swich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                blen= isChecked;
            }
        });
        return blen;
    }


    public static Map<String, Object> obj2Map(Object obj) {

        Map<String, Object> map = new HashMap<String, Object>();
        // System.out.println(obj.getClass());
        // 获取f对象对应类中的所有属性域
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            String varName = fields[i].getName();
            //varName = varName.toLowerCase();//将key置为小写，默认为对象的属性
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o = fields[i].get(obj);
                if (o != null)
                    map.put(varName, o);
                // System.out.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        return map;
    }
    //java对象转map
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }
}