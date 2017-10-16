package com.gameassist.plugin.utils;


import android.content.Context;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by hulimin on 2017/9/14.
 */

public class ReflectionUtils {

    /***
       * 获取私有成员变量的值
     *
     */
    public static Object getValue(Object instance, String fieldName){
        try{
            Field field = instance.getClass().getDeclaredField(fieldName);
           // MyLog.e("field -- " + field.toString());
            field.setAccessible(true); // 参数值为true，禁止访问控制检查
            Object result=field.get(instance);
          //  MyLog.e("getValue --- "+result.toString());
            return  result;
        }catch (Exception e){
            MyLog.e("getValue --- "+e.getMessage());
        }
        return null;
    }



    public static void setValue(Object instance, String fieldName, String value){
        try{
            Field field = instance.getClass().getDeclaredField(fieldName);
            // MyLog.e("field -- " + field.toString());
            field.setAccessible(true); // 参数值为true，禁止访问控制检查
            field.set(instance, value);
            //  MyLog.e("getValue --- "+result.toString());
        }catch (Exception e){
            MyLog.e("setValue --- "+e.getMessage());
        }
    }


    public static Object callStaticMethod(Context context, String className,  String methodName){
        try{
            Class instance=context.getClassLoader().loadClass(className);
            MyLog.e(instance.toString()+"   method:"+methodName);
            Method method = instance.getDeclaredMethod(methodName);
            MyLog.e("method -- " + method.toString());
            method.setAccessible(true);
            return method.invoke(instance);
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }
        return null;
    }


    public static Object callMethod(Object instance, String methodName){
        try{
            Method method = instance.getClass().getDeclaredMethod(methodName);
           // MyLog.e("method -- " + method.toString());
            method.setAccessible(true);
            return method.invoke(instance);
        }catch (Exception e){

        }
        return null;
    }




    public static Object callMethod(Object instance, String methodName, Class[] paramsTypes, Object[] params){
        try{
            Method method = instance.getClass().getDeclaredMethod(methodName, paramsTypes);
           // MyLog.e("method -- " + method.toString());
            method.setAccessible(true);
            return method.invoke(instance, params);
        }catch (Exception e){

        }
        return null;
    }



    // TODO: 2017/10/12  测试得到类中所有的字符串

    public static void   getAllFields(Object model){
        Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        try {
            MyLog.e("leng: "+field.length);
            for (int j = 0; j < field.length; j++) { // 遍历所有属性
                String name = field[j].getName(); // 获取属性的名字
               // MyLog.e("name: "+name);
                String type = field[j].getGenericType().toString(); // 获取属性的类型
                MyLog.e("j:%d, name:%s,   ----- type: "+type, j, name);
                if(type.contains("HashMap")){
                   TestUtils.printHashMap((HashMap) getValue(model,name));
                }else if (type.contains("java.lang.String") && !type.contains("Map")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    String data=(String)getValue(model,name);
                    if(TextUtils.isEmpty(data)){
                        data=" is null";
                    }
                    MyLog.e("data -------- "+data);
                }
            }
        }catch (Exception e){
           MyLog.e(e.getMessage());
        }
    }

}
