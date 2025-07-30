package com.freemud.app.easypos.mvp.model;

/**
 * Created by shuyuanbo on 2024/1/9.
 * Description:
 */
public class BarCodeData {

   public BarCodeData() {
   }

   public BarCodeData(String code, int type, int height, int width, int textPosition) {
      this.code = code;
      this.type = type;
      this.height = height;
      this.width = width;
      this.textPosition = textPosition;
   }

   public String code;
   public int type;  // 0 -> UPC-A 1 -> UPC-E 2 -> JAN13(EAN13) 3 -> JAN8(EAN8) 4 -> CODE39
      // 5 -> ITF  6 -> CODABAR  7 -> CODE93  8 -> CODE128

   public int height;   //条码高度 1-255 默认162
   public int width;    //条码宽度 2-6 默认2
   public int textPosition;   //文字位置 0-3  0 -> 不打印 1 -> 上方 2 ->下方  3->上下方
}
