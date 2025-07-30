package com.freemud.app.easypos.mvp.utils;

import com.freemud.app.easypos.common.base.MyBaseFragment;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/*******************************************************************************************
 *
 * @author: yuanbo.shu
 * @date： 2022/1/28 0:23
 * @version 1.0
 * @description:
 * Version    Date       ModifiedBy                 Content
 * 1.0      2022/1/28       yuanbo.shu                             
 *******************************************************************************************
 */
public class FragmentUtils {
    public static void changeFragment(FragmentActivity context, MyBaseFragment fromFragment, MyBaseFragment toFragment, int viewId) {
        FragmentManager fm = context.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!fromFragment.isAdded()) {
            //初始
            ft.add(viewId, fromFragment).commit();
            return;
        }
        if (!toFragment.isAdded()) {
            ft.hide(fromFragment).add(viewId, toFragment).commit();
        } else {
            ft.hide(fromFragment).show(toFragment).commit();
        }
    }

    public static void changeFragmentAddTo(FragmentActivity context, MyBaseFragment fromFragment, MyBaseFragment toFragment, int viewId) {
        FragmentManager fm = context.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!fromFragment.isAdded()) {
            //初始
            ft.add(viewId, toFragment).commit();
            return;
        }
        if (!toFragment.isAdded()) {
            ft.hide(fromFragment).add(viewId, toFragment).commit();
        } else {
            ft.hide(fromFragment).show(toFragment).commit();
        }
    }
}
