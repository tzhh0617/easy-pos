package com.freemud.app.easypos.mvp.utils;

import java.util.List;

/**
 * Created by shuyuanbo on 2022/7/12.
 * Description:
 */
public class ArrayUtils {

   public static boolean contains(Object[] arr, Object obj) {
      if (arr == null || arr.length == 0) {
         return false;
      }
      boolean isHave = false;
      for (int i = 0; i < arr.length;i++) {
          Object item = arr[i];
          if (item instanceof String) {
             if (item.equals(String.valueOf(obj))) {
                isHave = true;
                break;
             }
          }else {
             if (item == obj) {
                isHave = true;
                break;
             }
          }
      }
      return isHave;
   }

   public static String listToArrStrBySplit(List<String> list, String split) {
      if (list == null || list.size() == 0) {
         return null;
      }
      StringBuffer sb = new StringBuffer();
      for (String item : list) {
         sb.append(item).append(split);
      }
      return sb.substring(0, sb.length()-1);
   }
}
