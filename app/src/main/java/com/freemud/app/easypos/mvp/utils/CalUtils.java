package com.freemud.app.easypos.mvp.utils;

/**
 * Created by shuyuanbo on 2022/8/17.
 * Description:
 */
public class CalUtils {

   /**
    * 十进制转2进制
    * @param ten
    * @return
    */
   public static String ten2Two(int ten) {
      String two = Integer.toBinaryString(ten);
      return two;
   }

   /**
    * 二进制转十进制
    * @param two "0001"
    * @return
    */
   public static int two2Ten(String two) {
      int ten = Integer.parseInt(two, 2);
      return ten;
   }


}
