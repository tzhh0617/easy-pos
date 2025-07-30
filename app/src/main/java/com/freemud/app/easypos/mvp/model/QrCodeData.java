package com.freemud.app.easypos.mvp.model;

/**
 * Created by shuyuanbo on 2024/1/9.
 * Description:
 */
public class QrCodeData {

   public QrCodeData() {
   }

   public QrCodeData(String qrcode, int size, int errorLevel) {
      this.qrcode = qrcode;
      this.size = size;
      this.errorLevel = errorLevel;
   }

   public String qrcode;
   public int size;  //4-16
   public int errorLevel;  //二维码纠错级别 0 7% 1 15% 2 25% 3 30%
}
