package com.freemud.app.easypos.mvp.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

/*******************************************************************************************
 *
 * @author: yuanbo.shu
 * @date： 2021/7/21 1:29
 * @version 1.0
 * @description:
 * Version    Date       ModifiedBy                 Content
 * 1.0      2021/7/21       yuanbo.shu                             
 *******************************************************************************************
 */
public class KeyboardUtils {
    /**
     * 显示软键盘
     *
     * @param editText
     */
    public static void showKeyboard(AppCompatEditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 判空防止空指针
        if (inputMethodManager != null) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            // 请求获取焦点
            editText.requestFocus();
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param editText
     */
    public static void hideKeyboard(AppCompatEditText editText) {
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 判空防止空指针
        if (imm != null) {
            // 清除焦点
            editText.clearFocus();
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        // 判空防止空指针
        if (imm != null) {
            // 清除焦点
            view.clearFocus();
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
