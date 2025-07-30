package com.freemud.app.easypos.mvp.widget.common;

/**
 * Created by shuyuanbo on 2023/8/10.
 * Description:
 */
public class CommonPop {
   public interface OnDialogCallBack {
      default void onNegative(){};

      void onPositive();
   }
}
