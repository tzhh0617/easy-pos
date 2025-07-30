package com.freemud.app.easypos.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.freemud.app.easypos.R;
import com.freemud.app.easypos.common.base.MyBaseActivityNoP;
import com.freemud.app.easypos.databinding.ActivityLaunchBinding;
import com.freemud.app.easypos.mvp.model.constant.SpKey;
import com.freemud.app.easypos.mvp.ui.capture.CustomScanAct;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.DataHelper;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by shuyuanbo on 2023/5/9.
 * Description:
 */
public class LaunchAct extends MyBaseActivityNoP<ActivityLaunchBinding> {
   public static final int ACT_CODE_READ_PHONE = -101;

   @Override
   protected ActivityLaunchBinding getContentView() {
      return ActivityLaunchBinding.inflate(getLayoutInflater());
   }

   @Override
   public void setupActivityComponent(@NonNull AppComponent appComponent) {

   }

   @Override
   public void initView(Bundle savedInstanceState) {
      String saveH5 = DataHelper.getStringSF(this, SpKey.H5_URL);
      mBinding.etH5.setText(saveH5);
   }

   @Override
   public void initData(Bundle savedInstanceState) {
      XXPermissions.with(this)
              .permission(Permission.READ_PHONE_STATE)
              .request(new OnPermissionCallback() {
                 @Override
                 public void onGranted(List<String> permissions, boolean all) {
                    if (!all) {
                       showMessage("应用需要获取您的设备号权限,请去设置-应用中打开");
                    }else {
                    }
                 }
              });

      mBinding.button.setOnClickListener(view -> {
         String etH5 = mBinding.etH5.getText().toString().trim();
         if (TextUtils.isEmpty(etH5)) {
            showMessage("h5地址不能为空");
            return;
         }
         Intent intent = new Intent(this,CommonWebAct.class);
         intent.putExtra("data",etH5);
         startActivity(intent);
         finish();
      });

      mBinding.buttonPrintH5.setOnClickListener(view -> {
         Intent intent = new Intent(this,CommonWebAct.class);
         intent.putExtra("data","wc8nhd52-5173.asse.devtunnels.ms");
         startActivity(intent);
         finish();
      });

      mBinding.buttonPrint.setOnClickListener(view -> {
         Intent intent = new Intent(this,CommonWebAct.class);
         intent.putExtra("isTest",true);
         startActivity(intent);
         finish();
      });
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

   }
}
