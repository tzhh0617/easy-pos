/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.freemud.app.easypos.common;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jess.arms.http.GlobalHttpHandler;
import com.jess.arms.utils.DataHelper;
import com.tencent.bugly.crashreport.biz.UserInfoBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * ================================================
 * 展示 {@link GlobalHttpHandler} 的用法
 * <p>
 * Created by JessYan on 04/09/2017 17:06
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class GlobalHttpHandlerImpl implements GlobalHttpHandler {
    private Context context;
    private Gson mGson;

    public GlobalHttpHandlerImpl(Context context) {
        this.context = context;
        this.mGson = new Gson();
    }

    /**
     * 这里可以先客户端一步拿到每一次 Http 请求的结果, 可以先解析成 Json, 再做一些操作, 如检测到 token 过期后
     * 重新请求 token, 并重新执行请求
     *
     * @param httpResult 服务器返回的结果 (已被框架自动转换为字符串)
     * @param chain      {@link Interceptor.Chain}
     * @param response   {@link Response}
     * @return {@link Response}
     */
    @NonNull
    @Override
    public Response onHttpResultResponse(@Nullable String httpResult, @NonNull Interceptor.Chain chain, @NonNull Response response) {


        /* 这里如果发现 token 过期, 可以先请求最新的 token, 然后在拿新的 token 放入 Request 里去重新请求
        注意在这个回调之前已经调用过 proceed(), 所以这里必须自己去建立网络请求, 如使用 Okhttp 使用新的 Request 去请求
        create a new request and modify it accordingly using the new token
        Request newRequest = chain.request().newBuilder().header("token", newToken)
                             .build();

        retry the request

        response.body().close();
        如果使用 Okhttp 将新的请求, 请求成功后, 再将 Okhttp 返回的 Response return 出去即可
        如果不需要返回新的结果, 则直接把参数 response 返回出去即可*/

        return response;
    }

    /**
     * 这里可以在请求服务器之前拿到 {@link Request}, 做一些操作比如给 {@link Request} 统一添加 token 或者 header 以及参数加密等操作
     *
     * @param chain   {@link Interceptor.Chain}
     * @param request {@link Request}
     * @return {@link Request}
     */
    @NonNull
    @Override
    public Request onHttpRequestBefore(@NonNull Interceptor.Chain chain, @NonNull Request request) {
        /* 如果需要在请求服务器之前做一些操作, 则重新构建一个做过操作的 Request 并 return, 如增加 Header、Params 等请求信息, 不做操作则直接返回参数 request
        return chain.request().newBuilder().header("token", tokenId)
                              .build(); */
//        String screen = DataHelper.getStringSF(context, SpKey.SCREEN_WH);
//        String url = request.url().toString().toLowerCase();
//        Headers.Builder extraHeaders = new Headers.Builder();
//        extraHeaders.add("version", BuildConfig.VERSION_NAME);
//        extraHeaders.add("os", String.valueOf(Build.VERSION.SDK_INT));
//        extraHeaders.add("from", "android");
//        extraHeaders.add("screen",screen);
//        extraHeaders.add("model", Build.MODEL);
//        extraHeaders.add("VER","2");
//        extraHeaders.add("timestamp", String.valueOf(System.currentTimeMillis()));
//        if (url.contains("login") || url.contains("sendsmscode") || url.contains("payment")) {
//            //这里不需要额外处理的 判断条件扩展
//            return chain.request().newBuilder().method(request.method(), request.body()).headers(extraHeaders.build()).build();
//        }
//        String token = DataHelper.getStringSF(context, SpKey.TOKEN);
//
//        extraHeaders.add("token", token);
//        Request oldRequest = chain.request();
//        String storeInfoStr = DataHelper.getStringSF(context, SpKey.STORE_INFO);
//        if (TextUtils.isEmpty(storeInfoStr)) {
//            return chain.request().newBuilder().method(request.method(), request.body()).headers(extraHeaders.build()).build();
//        }
//        StoreBean storeBean = mGson.fromJson(storeInfoStr, StoreBean.class);
//        String partnerId = storeBean.partnerId;
//        String storeCode = storeBean.storeCode;
//        String storeId = storeBean.storeId;
//        String version = BuildConfig.VERSION_NAME;
//        if (request.method().equalsIgnoreCase("get")) {
//            HttpUrl modifieUrl =oldRequest.url().newBuilder()
//                    .addQueryParameter("partnerId",partnerId)
//                    .addQueryParameter("storeCode",storeCode)
//                    .addQueryParameter("storeId",storeId)
//                    .addQueryParameter("ver",version)
//                    .build();
//            return chain.request().newBuilder().url(modifieUrl).headers(extraHeaders.build()).build();
//        }
//        RequestBody newBody = null;
//        LoginRes userInfo = MyApp.getInstance().getUserInfo();
//        String oldBody = bodyToString(request);
//        try {
//            JSONObject jsonObject = new JSONObject(oldBody);
//            if (jsonObject.isNull("partnerId")) {
//                jsonObject.put("partnerId", partnerId);
//            }
//            jsonObject.put("storeCode", storeCode);
//            jsonObject.put("storeId", storeId);
//            jsonObject.put("ver", version);
//            jsonObject.put("storeIdList", storeId);
//            if (userInfo != null) {
//                jsonObject.put("operator", userInfo.userName);
//            }
//            newBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
//            return chain.request().newBuilder().method(request.method(), newBody).headers(extraHeaders.build()).build();
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return request;
//        }
        return request;

    }

    private static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            RequestBody body = copy.body();
            if (body != null) {
                body.writeTo(buffer);
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
