package com.freemud.app.easypos.mvp.model;

import java.util.List;

/**
 * Created by shuyuanbo on 2024/1/9.
 * Description:
 */
public class PrintH5Model {

   public PrintH5Model() {
   }

   public PrintH5Model(List<PrintModel> printModelList) {
      this.printModelList = printModelList;
      this.lineWrap = 3;
      this.autoCut = true;
   }

   public List<PrintModel> printModelList;

   public int lineWrap; //走纸行数  打印末尾空几行
   public boolean autoCut; //是否自动切纸
}
