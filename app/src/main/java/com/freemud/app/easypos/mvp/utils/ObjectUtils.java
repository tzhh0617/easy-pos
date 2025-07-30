package com.freemud.app.easypos.mvp.utils;

import com.google.gson.Gson;
import com.jess.arms.utils.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shuyuanbo on 2022/3/17.
 * Description:
 */
public class ObjectUtils {

   public static <T> T copyObjectDeep(T obj, Gson gson, Type type) {
      T copy = null;
      copy = gson.fromJson(gson.toJson(obj), type);
      return copy;
   }

   public static <T> List<T> copyListDeep(T obj, Gson gson, Type type) {
      List<T> copy = null;
      copy = gson.fromJson(gson.toJson(obj), type);
      return copy;
   }

   public static <T> T copyObjectDeep(T obj, Type type) {
      T copy = null;
      Gson gson = new Gson();
      copy = gson.fromJson(gson.toJson(obj), type);
      return copy;
   }

   public static Map<String, Object> transObjectToMap(Object object) {
      if (object == null)
         return null;


      HashMap<String, Object> arrayMap = new HashMap<>();

      Field[] declaredFields = object.getClass().getFields();
      for (Field field : declaredFields) {
         field.setAccessible(true);
         try {
            if (!isBasicDataStyle(field.get(object))) {
               continue;
            }
            if (field.getName().startsWith("PARCELABLE") || field.getName().equals("CONTENTS_FILE_DESCRIPTOR")) {
               //过滤实现序列话接口的父类字段
               continue;
            }
            arrayMap.put(field.getName(), field.get(object));
         } catch (IllegalAccessException e) {
            e.printStackTrace();
         }
      }
      return arrayMap;
   }

   public static Map<String, Object> transFormToMap(String formStr, String splitStr1, String splitStr2) {
      Map<String, Object> mapTemp = new HashMap<>();
      try {

         if (!formStr.contains(splitStr1) || !formStr.contains(splitStr2)) {
            return null;
         } else {
            String[] mapStr = formStr.split(splitStr1);
            for (int i = 0; i < mapStr.length; i++) {
               String tempStr = mapStr[i];
               String value = "null";
               if (tempStr.split(splitStr2).length > 1) {
                  value = tempStr.split(splitStr2)[1];
               }
               mapTemp.put(tempStr.split(splitStr2)[0], value);
            }
         }
      } catch (Exception e) {
         LogUtils.debugInfo("异常字符串" + formStr);
      }

      return mapTemp;
   }

   private static boolean isBasicDataStyle(Object object) {
      if (object instanceof Integer || object instanceof String ||
              object instanceof Float || object instanceof Boolean
              || object instanceof Double || object instanceof Date) {
         return true;
      }
      return false;

   }
}
