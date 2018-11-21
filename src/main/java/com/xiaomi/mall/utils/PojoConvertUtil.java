package com.xiaomi.mall.utils;



import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @authour lcy
 * @create 2018-11-19
 */
public class PojoConvertUtil {
    private static Lock initLock = new ReentrantLock();

    private static Map<String, BeanCopier> beanCopierMap = new HashMap<String, BeanCopier>();

    /**
     * 初始化 BeanCopier
     *
     * @param source
     * @param target
     * @return
     */
    private static BeanCopier initCopier(Class source, Class target) {
        initLock.lock();
        BeanCopier find = beanCopierMap.get(source.getName() + "_" + target.getName());
        if (find != null) {
            initLock.unlock();
            return find;
        }
        BeanCopier beanCopier = BeanCopier.create(source, target, false);
        beanCopierMap.put(source.getName() + "_" + target.getName(), beanCopier);
        initLock.unlock();
        return beanCopier;
    }


    /**
     * 获取BeanCopier
     *
     * @param source
     * @param target
     * @return
     */
    private static BeanCopier getBeanCopier(Class source, Class target) {
        BeanCopier beanCopier = beanCopierMap.get(source.getClass().getName() + "_" + target.getName());
        if (beanCopier != null) {
            return beanCopier;
        }
        return initCopier(source, target);
    }


    /**
     * Pojo 类型转换（浅复制，字段名&类型相同则被复制）
     *
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T convert(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        BeanCopier beanCopier = getBeanCopier(source.getClass(), targetClass);
        try {
            T target = targetClass.newInstance();
            beanCopier.copy(source, target, null);
            return target;

        } catch (Exception e) {
            throw new RuntimeException("对象拷贝失败" + source + "_" + targetClass);
        }
    }

    public static Object convert(Object source, Object target) {
        if (source == null) {
            return null;
        }
        BeanCopier beanCopier = BeanCopier.create(source.getClass(), target.getClass(), true);
        try {
            beanCopier.copy(source,target, (value, t, context)->{
                if(value == null) {
                    return BeanMap.create(target).get(getPropertyName(String.valueOf(context)));
                }
                return value;
            });
            return target;

        } catch (Exception e) {
            e.printStackTrace();
//            throw new RuntimeException("对象拷贝失败" + source + "_" + targetClass);
        }
        return null;
    }
    private static String getPropertyName(String methodName) {//setAge ---> age
        char[] newChar = new char[methodName.length() - 3];
        System.arraycopy(methodName.toCharArray(), 3, newChar, 0, methodName.length() - 3);
        newChar[0] = Character.toLowerCase(newChar[0]);
        return String.valueOf(newChar);
    }
    /**
     * Pojo 类型转换（浅复制，字段名&类型相同则被复制）
     *
     * @param source
     * @param targetClass
     * @param <E>
     * @return
     */
    public static <E> List<E> convert(List source, Class<E> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            if (source.isEmpty()) {
                return source.getClass().newInstance();
            }
            List result = source.getClass().newInstance();

            for (Object each : source) {
                result.add(convert(each, targetClass));
            }
            return result;
        } catch (Exception e) {
//            log.error("对象拷贝失败,{}",e);
            throw new RuntimeException("对象拷贝失败" + source + "_" + targetClass);
        }
    }

    /**
     *
     */
    public static <E> Object  getFiledValue(E source,String filed){
        Field field= null;
        Object value=null;
        try {
            field = source.getClass().getDeclaredField(filed);
            field.setAccessible(true);
            value=field.get(source);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }


    /**
     * 合并两个list,如果对象已经有了，就执行更新操作，最后返回一个list
     * @param source
     * @param target
     * @param <E>
     * @return
     */
    public static  <E> List<E>  convertList(List<E> source, List<E> target,String filed){
        try{
            if (source.size()>target.size()){

            }else {
                source.forEach((sourceItem)->{
                    String sourceValue=(String)getFiledValue(sourceItem,filed);
                    boolean flag=true;
                    for (int i=0;i<target.size();i++) {
                        String targetValue=(String)getFiledValue(target.get(i),filed);
                        if (sourceValue.equals(targetValue)){
                            flag=false;
                            E e = (E)convert(sourceItem, target.get(i));
                            if (e != null) {
                                target.set(i,e);
                            }
                        }
                    }
                    if (flag){
                        target.add(sourceItem);
                    }
                });
            }
        }catch (Exception e){
            throw new RuntimeException("对象拷贝失败");
        }
        return null;
    }

    /**
     * Pojo 类型转换（浅复制，字段名&类型相同则被复制）
     *
     * @param
     * @param
     * @param
     * @return
     */
//    public static <T> PageList<T> convert(PageList source,Class<T> targetClass){
//        if(source==null){
//            return null;
//        }
//        List<T> list  = convert((List)source,targetClass);
//        PageList<T> result = new PageList<>(source.getPaginator());
//        result.addAll(list);
//        return result;
//    }
    public static void main(String[] args) {

//        List<UserSaveReq> reqList = new ArrayList<>();
//        Date start = new Date();
//        System.out.println("startTime:" + start.getTime());
//        for (int i = 0; i < 10000000; i++) {
//            UserSaveReq req = new UserSaveReq();
//            req.setPassword("123");
//            req.setUserName("name" + 1);
//            reqList.add(req);
//        }
//        List<User> users = PojoConvertUtil.convert(reqList, User.class);
//        Date endTime = new Date();
//        System.out.println("endTime:" + endTime.getTime());
//        System.out.println("Total:" + (endTime.getTime() - start.getTime()));


//        List<User> userList=new ArrayList<>();
//        List<ReqUser> reqUserList=new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//            ReqUser user1=new ReqUser();
//            user1.setName("王学疆"+i);
//            user1.setPassword("123");
//            reqUserList.add(user1);
//        }
//        for (int i = 0; i < 2; i++) {
//            User user=new User();
//            user.setName("李冰洁"+i);
//            user.setPassword("123456");
//            userList.add(user);
//        }

//        convertList(userList,userList,"name");


    }
}
