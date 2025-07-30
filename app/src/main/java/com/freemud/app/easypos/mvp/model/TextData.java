package com.freemud.app.easypos.mvp.model;

/**
 * Created by shuyuanbo on 2024/1/9.
 * Description:
 */
public class TextData {
   public TextData() {
   }

   public TextData(String text, int fontSize, int alignment, boolean isBold) {
      this.text = text;
      this.fontSize = fontSize;
      this.alignment = alignment;
      this.isBold = isBold;
   }

   public String text;  // 超出一行自动换行
   public int fontSize; //设置后全局变化
   public int alignment;   //对齐方式 0居左 1居中 2居右
   public boolean isBold;  //是否加粗
}
