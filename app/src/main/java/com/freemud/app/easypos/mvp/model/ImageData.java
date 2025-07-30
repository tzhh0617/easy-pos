package com.freemud.app.easypos.mvp.model;

/**
 * Created by shuyuanbo on 2024/1/10.
 * Description:
 */
public class ImageData {
   public ImageData() {
   }

   public ImageData(String url, int alignment) {
      this.url = url;
      this.alignment = alignment;
   }

   public ImageData(String url, int width, int height) {
      this.url = url;
      this.width = width;
      this.height = height;
   }

   public String url;
   public int alignment;
   public int width;
   public int height;
}
