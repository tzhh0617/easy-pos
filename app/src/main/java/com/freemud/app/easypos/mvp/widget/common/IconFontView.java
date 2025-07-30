package com.freemud.app.easypos.mvp.widget.common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by shuyuanbo on 2022/6/30.
 * Description:
 */
public class IconFontView extends AppCompatTextView {
   private Context mContext;
   public IconFontView(@NonNull Context context) {
      super(context);
      mContext = context;
      initView();
   }

   public IconFontView(@NonNull Context context, @Nullable AttributeSet attrs) {
      super(context, attrs);
      mContext = context;
      initView();
   }

   private void initView() {
      Typeface iconFont = Typeface.createFromAsset(mContext.getAssets(), "iconfont.ttf");
      setTypeface(iconFont);
   }

}
