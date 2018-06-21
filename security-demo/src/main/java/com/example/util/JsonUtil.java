package com.example.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author SongQingWei
 * @description json 工具类
 * @date 2018年04月20 10:16
 */
@Slf4j
public class JsonUtil {

    private static JsonUtil instance = null;
    private static ObjectMapper mapper = null;

    private JsonUtil() {
        mapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private static ObjectMapper getInstance() {
        if (instance == null) {
            synchronized (JsonUtil.class) {
                if (instance == null) {
                    instance = new JsonUtil();
                }
            }
        }
        return mapper;
    }

    /**
     * 将java对象转换成json字符串
     *
     * @param obj 准备转换的对象
     * @return json字符串
     */
    public static String toString(Object obj) {
        try {
            ObjectMapper objectMapper = getInstance();
            return objectMapper.writeValueAsString(obj);
        }
        catch (Exception e) {
            log.error("JsonUtils toString error", e);
        }
        return null;
    }

    /**
     * 将java对象转换成json字符串
     *
     * @param obj 准备转换的对象
     * @return json字符串
     */
    public static byte[] toByte(Object obj) {
        try {
            ObjectMapper objectMapper = getInstance();
            return objectMapper.writeValueAsBytes(obj);
        }
        catch (Exception e) {
            log.error("JsonUtils toByte error", e);
        }
        return null;
    }

    /**
     * 将json字符串转换成java对象
     * @param json 准备转换的json字符串
     * @param cls  准备转换的类
     * @return t
     */
    public static <T> T toBean(byte[] json, Class<T> cls) {
        try {
            ObjectMapper objectMapper = getInstance();
            return objectMapper.readValue(json, cls);
        }
        catch (Exception e) {
            log.error("JsonUtils byte[] toBean error", e);
        }
        return null;
    }

    /**
     * 将json字符串转换成java对象
     * @param json 准备转换的json字符串
     * @param cls  准备转换的类
     * @return t
     */
    public static <T> T toBean(String json, Class<T> cls) {
        try {
            ObjectMapper objectMapper = getInstance();
            return objectMapper.readValue(json, cls);
        }
        catch (Exception e) {
            log.error("JsonUtils String toBean error Json {}",json, e);
        }
        return null;
    }

    /**
     * 将json字符串转换成List java对象
     * @param json 准备转换的json字符串
     * @param cls  准备转换的类
     * @return t
     */
    public static <T> List<T> toList(String json, Class<T> cls)  {
        try {
            getInstance();
            JavaType javaType = getCollectionType(ArrayList.class, cls);
            return mapper.readValue(json, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取泛型的Collection Type
     * @param collectionClass 泛型的Collection
     * @param elementClasses 元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public static JavaType getCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClasses) {
        return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClasses);
    }

    /**
     * 将json数据转换成pojo对象list
     * Title: jsonToList
     * Description:
     * @param jsonData json数据
     * @param beanType 对象中的object类型
     * @return list
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, beanType);
        List<T> list = null;
        try {
            list = mapper.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
