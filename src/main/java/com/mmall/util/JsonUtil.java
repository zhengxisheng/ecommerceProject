package com.mmall.util;

import com.google.common.collect.Lists;
import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by fanlinglong on 2018/3/1.
 * json工具类
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //对象字段全部在序列化在json字符串中
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        //取消转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        //所有的日期格式统一为标准格式,即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAY));
        //忽略在json字符串中存在,但在java对象中不存在对应属性的情况,防止转化失败报错
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    /**
     *  对象转json
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2String(T obj){
        if (obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("Parse Object to String error:{}",e);
            return null;
        }
    }

    /**
     *  对象转格式化json
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2StringPretty(T obj){
        if (obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("Parse Object to String error:{}",e);
            return null;
        }
    }

    /**
     *  简单json转对象
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T String2Obj(String str,Class<T> clazz){
        if (StringUtils.isEmpty(str) || clazz == null){
            return null;
        }
        try {
            return clazz.equals(String.class)? (T)str : objectMapper.readValue(str,clazz);
        } catch (IOException e) {
            log.warn("Parse String to Object error:{}",e);
            return null;
        }
    }

    /**
     *  复杂json转对象
     * @param str
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T String2Obj(String str, TypeReference<T> typeReference){
        if (StringUtils.isEmpty(str) || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class)?  str : objectMapper.readValue(str,typeReference));
        } catch (IOException e) {
            log.warn("Parse String to Object error:{}",e);
            return null;
        }
    }

    public static void main(String[] args) {
        User user = new User();
        user.setId(1);
        user.setUsername("郑喜生");
        user.setCreateTime(new Date());
        User user2 = new User();
        user2.setId(2);
        user2.setUsername("test");
        List<User> userList = Lists.newArrayList();
        userList.add(user);
        userList.add(user2);
        String str = JsonUtil.obj2StringPretty(userList);
        List<User> userList1 = JsonUtil.String2Obj(str, new TypeReference<List<User>>() {
        });
        log.info(userList1.toString());
    }
}
