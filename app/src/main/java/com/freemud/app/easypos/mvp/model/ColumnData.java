package com.freemud.app.easypos.mvp.model;

/**
 * Created by shuyuanbo on 2024/1/9.
 * Description:
 */
public class ColumnData {
   public ColumnData() {
   }

   public ColumnData(String[] texts, int[] weights, int[] aligns) {
      this.texts = texts;
      this.weights = weights;
      this.aligns = aligns;
      this.isBold = false;
   }

   public ColumnData(String[] texts, int[] weights, int[] aligns, boolean isBold) {
      this.texts = texts;
      this.weights = weights;
      this.aligns = aligns;
      this.isBold = isBold;
   }

   public ColumnData(String[] texts, int[] weights, int[] aligns, int size, boolean isBold) {
      this.texts = texts;
      this.weights = weights;
      this.aligns = aligns;
      this.size = size;
      this.isBold = isBold;
   }

   public String[] texts;  //各列文字
   public int[] weights;   //各列宽度权重
   public int[] aligns; //各列对齐方式 0左1中2右

   public float size;   //字体大小
   public boolean isBold;  //文字是否加粗
}
