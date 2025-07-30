package com.freemud.app.easypos.mvp.model.api;


import com.freemud.app.easypos.BuildConfig;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shuyuanbo on 2022/6/29.
 * Description:
 */
public interface Api {
    String APP_DOMAIN = BuildConfig.SERVER_URL;


}
