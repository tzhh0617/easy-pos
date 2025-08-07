package com.freemud.app.easypos.mvp.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.freemud.app.easypos.common.base.MyBaseActivityNoP;
import com.freemud.app.easypos.common.receiver.ScreenStatusReceiver;
import com.freemud.app.easypos.databinding.ActivityCommonWebBinding;
import com.freemud.app.easypos.BuildConfig;
import com.freemud.app.easypos.R;
import com.freemud.app.easypos.mvp.model.BarCodeData;
import com.freemud.app.easypos.mvp.model.ColumnData;
import com.freemud.app.easypos.mvp.model.ImageData;
import com.freemud.app.easypos.mvp.model.PrintH5Model;
import com.freemud.app.easypos.mvp.model.PrintModel;
import com.freemud.app.easypos.mvp.model.QrCodeData;
import com.freemud.app.easypos.mvp.model.TextData;
import com.freemud.app.easypos.mvp.model.constant.PrintType;
import com.freemud.app.easypos.mvp.model.constant.SpKey;
import com.freemud.app.easypos.mvp.ui.capture.CustomScanAct;
import com.freemud.app.easypos.mvp.utils.DeviceUtils;
import com.freemud.app.easypos.mvp.utils.fmdatatrans.PrintDataUtils;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.DataHelper;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import dagger.Binds;

/**
 * Created by shuyuanbo on 2023/5/9.
 * Description:
 */
public class CommonWebAct extends MyBaseActivityNoP<ActivityCommonWebBinding> {
    public static final int ACT_CODE_CAMERA = 101;
    private String mUrl;

    private boolean isTest;

    private final Handler hideHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    hideBg();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private ScreenStatusReceiver mScreenReceiver;

    private InnerPrinterCallback callback;

    private SunmiPrinterService printerService;

    private InnerResultCallback printCallback;

    private List<PrintModel> printModelList = new ArrayList<>();

    private Gson mGson = new Gson();

    private int imgTransCount = 0;      //需要转化的图片数量
    private Map<String, Bitmap> imgBitmap = new HashMap<>();
    private PrintH5Model mPrintH5Model;

    // 副屏相关
    private DisplayManager mDisplayManager;
    private WebSubScreen mSubScreen;
    private Display mSecondaryDisplay;

    @Override
    protected ActivityCommonWebBinding getContentView() {
        return ActivityCommonWebBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initData();
    }

    private void initData() {
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


        Uri uri = Uri.parse(BuildConfig.H5_BASE_URL)
                .buildUpon()
                .appendQueryParameter("t", String.valueOf(System.currentTimeMillis()))
                .build();
        mBinding.webview.loadUrl(uri.toString());

//        mBinding.webview.loadUrl(BuildConfig.H5_BASE_URL + "?t="+System.currentTimeMillis());
//        mBinding.webview.loadUrl("https://" + mUrl + "?=" + System.currentTimeMillis());
        registScreenReceiver();
        initPrint();
        initDisplayManager();
    }

    private void initPrint() {
        if (callback == null) {
            callback = new InnerPrinterCallback() {
                @Override
                protected void onConnected(SunmiPrinterService service) {
                    //连接上
                    printerService = service;
                }

                @Override
                protected void onDisconnected() {
                    //断开重连
                    initPrint();
                }
            };
        }
        if (printCallback == null) {
            printCallback = new InnerResultCallback() {

                @Override
                public void onRunResult(boolean isSuccess) throws RemoteException {

                }

                @Override
                public void onReturnString(String result) throws RemoteException {

                }

                @Override
                public void onRaiseException(int code, String msg) throws RemoteException {

                }

                @Override
                public void onPrintResult(int code, String msg) throws RemoteException {

                }
            };
        }
        try {
            boolean result = InnerPrinterManager.getInstance().bindService(this, callback);
            Log.d("syb", "链接打印机结果" + result);
        } catch (InnerPrinterException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 注册屏幕息屏唤醒事件
     */
    public void registScreenReceiver() {
        if (mScreenReceiver == null) {
            mScreenReceiver = new ScreenStatusReceiver();
            IntentFilter screenStatusIF = new IntentFilter();
            screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
            screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(mScreenReceiver, screenStatusIF);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        mBinding.image.setVisibility(View.VISIBLE);
        mBinding.image.setOnClickListener(view -> {
//            doCustomScan();
        });
        hideHandler.sendEmptyMessageDelayed(1, 2000);
        mBinding.webview.getSettings().setDomStorageEnabled(true);
        mBinding.webview.getSettings().setJavaScriptEnabled(true);
        mBinding.webview.setWebViewClient(new MyWebViewClient());
        mBinding.webview.getSettings().setAllowFileAccess(true);
        mBinding.webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        mBinding.webview.addJavascriptInterface(new AndroidJsBridgeInterface(), "FMEPosClass");
        mBinding.webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
    }

    @Override
    protected void initListener() {
        mBinding.btnPrintText.setOnClickListener(view -> {
            PrintModel model = new PrintModel();
            model.type = PrintType.TYPE_TEXT;
            TextData textData = new TextData("这是一段文字", 24, 1, true);
            model.text = textData;
            printModelList.clear();
            printModelList.add(model);
            PrintH5Model printH5Model = new PrintH5Model(printModelList);
            print(printH5Model);
        });
        mBinding.btnPrintCode.setOnClickListener(view -> {
            PrintModel model = new PrintModel();
            model.type = PrintType.TYPE_BARCODE;
            BarCodeData barCodeData = new BarCodeData("123456789", 8, 162, 2, 3);
            model.barCode = barCodeData;
            printModelList.clear();
            printModelList.add(model);
            PrintH5Model printH5Model = new PrintH5Model(printModelList);
            print(printH5Model);
        });

        mBinding.btnPrintQrcode.setOnClickListener(view -> {
            PrintModel model = new PrintModel();
            model.type = PrintType.TYPE_QRCODE;
            QrCodeData qrCodeData = new QrCodeData("https://baidu.com", 4, 3);
            model.qrCode = qrCodeData;
            printModelList.clear();
            printModelList.add(model);
            PrintH5Model printH5Model = new PrintH5Model(printModelList);
            print(printH5Model);
        });

        mBinding.btnPrintImg.setOnClickListener(view -> {
            PrintModel model = new PrintModel();
            model.type = PrintType.TYPE_IMAGE;
            ImageData imageData = new ImageData("https://blog-10039692.file.myqcloud.com/1494387499331_2961_1494387499640.png", 1);
            model.image = imageData;
            printModelList.clear();
            printModelList.add(model);
            PrintH5Model printH5Model = new PrintH5Model(printModelList);
            countImgTransform(printH5Model);
        });

        mBinding.btnPrintTemp.setOnClickListener(view -> {
            PrintH5Model printH5Model = new PrintH5Model(PrintDataUtils.getDiageoPrintData());
            String printJson = mGson.toJson(printH5Model);
            Log.d("syb", printJson);
            countImgTransform(printH5Model);
        });

        mBinding.btnOpenDrawer.setOnClickListener(view -> {
            openDrawer();
        });
    }

    private void hideBg() {
        mBinding.image.setVisibility(View.GONE);
    }


    private void callScanResultListener(String code) {
        mBinding.webview.loadUrl("javascript:onScanResult( \'" + code + "\' )");
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mUrl = getIntent().getStringExtra("data");
        isTest = getIntent().getBooleanExtra("isTest", false);
        mBinding.boxPrint.setVisibility(isTest ? View.VISIBLE : View.GONE);
        DataHelper.setStringSF(this, SpKey.H5_URL, mUrl);
        initData();
    }

    public void doCustomScan() {
        XXPermissions.with(this)
                .permission(Permission.CAMERA)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (!all) {
                            showMessage("扫码需要授权拍照权限,请去设置-应用中打开");
                        } else {
                            startActivityForResult(new Intent(CommonWebAct.this, CustomScanAct.class), ACT_CODE_CAMERA);
                        }
                    }
                });
    }

    public String getSn() {
        return DeviceUtils.getSerialNumber(this);
    }

    public void doScan() {
        Intent intent = new Intent("com.summi.scan");
        intent.putExtra("PLAY_SOUND", true);// 扫描完成声音提示 默认true
        intent.putExtra("PLAY_VIBRATE", false);
//扫描完成震动,默认false，目前M1硬件支持震动可用该配置，V1不支持
        intent.putExtra("IDENTIFY_MORE_CODE", false);// 识别画面中多个二维码，默认false
        intent.putExtra("IS_SHOW_SETTING", true);// 是否显示右上角设置按钮，默认true
        intent.putExtra("IS_SHOW_ALBUM", true);// 是否显示从相册选择图片按钮，默认true
        intent.putExtra("IDENTIFY_INVERSE", true);// 允许识读反色二维码，默认true
        intent.putExtra("IS_EAN_8_ENABLE", true);//允许识读EAN-8码，默认true：允许
        intent.putExtra("IS_UPC_E_ENABLE", true);//允许识读UPC-E码，默认true：允许
        intent.putExtra("IS_ISBN_10_ENABLE", false);//允许识读ISBN-10 (from EAN-13)码，默认false：不允许
        intent.putExtra("IS_CODE_11_ENABLE", true);//允许识读CODE-11码，默认false：不允许
        intent.putExtra("IS_UPC_A_ENABLE", true);//允许识读UPC-A码，默认true：允许
        intent.putExtra("IS_EAN_13_ENABLE", true);//允许识读AN-13码，默认true：允许
        intent.putExtra("IS_ISBN_13_ENABLE", true);//允许识读ISBN-13 (from EAN-13)码，默认true：允许
        intent.putExtra("IS_INTERLEAVED_2_OF_5_ENABLE", true);//允许识读Interleaved 2 of 5码，默认false：不允许
        intent.putExtra("IS_CODE_128_ENABLE", true);//允许识读ECode 128码，默认true：允许
        intent.putExtra("IS_CODABAR_ENABLE", true);//允许识读Codabar码，默认true：允许
        intent.putExtra("IS_CODE_39_ENABLE", true);//允许识读Code 39码，默认true：允许
        intent.putExtra("IS_CODE_93_ENABLE", true);//允许识读Code 93码，默认true：允许
        intent.putExtra("IS_DATABAR_ENABLE", true);//允许识读DataBar (RSS-14)码，默认true：允许
        intent.putExtra("IS_DATABAR_EXP_ENABLE", true);//允许识读DataBar Expanded码，默认true：允许
        intent.putExtra("IS_Micro_PDF417_ENABLE", true);//允许识读Micro PDF417码，默认true：允许
        intent.putExtra("IS_MicroQR_ENABLE", true);//允许识读Micro QR Code码，默认true：允许
        intent.putExtra("IS_OPEN_LIGHT", true);// 是否显示闪光灯，默认false
        intent.putExtra("SCAN_MODE", false);// 是否是循环模式，默认false
        intent.putExtra("IS_QR_CODE_ENABLE", true);// 允许识读QR码，默认true
        intent.putExtra("IS_PDF417_ENABLE", true);// 允许识读PDF417码，默认false
        intent.putExtra("IS_DATA_MATRIX_ENABLE", true);// 允许识读DataMatrix码，默认false
        intent.putExtra("IS_AZTEC_ENABLE", true);// 允许识读AZTEC码，默认false
        intent.putExtra("IS_Hanxin_ENABLE", false);// 允许识读Hanxin码，默认false
        startActivityForResult(intent, ACT_CODE_CAMERA);
    }

    private class AndroidJsBridgeInterface {
        @JavascriptInterface
        public void scanCode() {
            doCustomScan();
        }

        @JavascriptInterface
        public void openCash() {
            openDrawer();
        }

        @JavascriptInterface
        public String getDeviceSn() {
            return getSn();
        }

        @JavascriptInterface
        public void beginPrint(String printModel) {
            PrintH5Model printH5Model = mGson.fromJson(printModel, PrintH5Model.class);
            showMessage("调用本地打印功能");
            countImgTransform(printH5Model);
        }

        @JavascriptInterface
        public String isSubScreen() {
            return "false";
        }

        @JavascriptInterface
        public void openSubScreen(String url) {
            CommonWebAct.this.openSubScreen(url);
        }

        @JavascriptInterface
        public void closeSubScreen() {
            CommonWebAct.this.closeSubScreen();
        }

        @JavascriptInterface
        public boolean isSubScreenShowing() {
            return CommonWebAct.this.isSubScreenShowing();
        }

        @JavascriptInterface
        public void sendDataToSubScreen(String data) {
             CommonWebAct.this.sendDataToSubScreen(data);
        }
    }


    public class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //do your custom
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @SuppressLint("WebViewClientOnReceivedSslError")
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACT_CODE_CAMERA) {
            if (data != null) {
//                Bundle bundle = data.getExtras();
//                ArrayList result = (ArrayList) bundle.getSerializable("data");
//                Iterator it = result.iterator();
//                while (it.hasNext()) {
//                    HashMap hashMap = (HashMap) it.next();
//                    String code = (String) hashMap.get("VALUE");
//                    String type = (String) hashMap.get("TYPE");
//                    callScanResultListener(code);
//                }
                String result = data.getStringExtra("SCAN_RESULT");
                callScanResultListener(result);
            } else {
                callScanResultListener("");
            }

        }
    }

    @Override
    protected void onDestroy() {
        if (mScreenReceiver != null) {
            unregisterReceiver(mScreenReceiver);
        }
        hideHandler.removeCallbacksAndMessages(null);
        
        // 清理副屏资源
        if (mSubScreen != null && mSubScreen.isShowing()) {
            mSubScreen.dismiss();
            mSubScreen = null;
        }
        
        super.onDestroy();
    }

    private void openDrawer() {
        if (printerService == null) return;
        try {
            boolean isOpen = printerService.getDrawerStatus();
            if (isOpen) {
                showMessage("钱箱已打开");
                return;
            }
            printerService.openDrawer(printCallback);
        } catch (RemoteException e) {
            showMessage("设备不支持钱箱开关功能");
        }
    }

    /**
     * 初始化显示管理器
     */
    private void initDisplayManager() {
        mDisplayManager = (DisplayManager) getSystemService(DISPLAY_SERVICE);
        if (mDisplayManager != null) {
            findSecondaryDisplay();
        }
    }

    /**
     * 查找副屏显示器
     */
    private void findSecondaryDisplay() {
        Display[] displays = mDisplayManager.getDisplays();
        for (Display display : displays) {
            if ((display.getFlags() & Display.FLAG_SECURE) != 0
                && (display.getFlags() & Display.FLAG_SUPPORTS_PROTECTED_BUFFERS) != 0
                && (display.getFlags() & Display.FLAG_PRESENTATION) != 0) {
                mSecondaryDisplay = display;
                return;
            }
        }
    }

    /**
     * 打开副屏并显示指定URL
     * @param url 要在副屏显示的URL
     */
    public void openSubScreen(String url) {
        if (mSecondaryDisplay == null) {
            findSecondaryDisplay();
        }
        
        if (mSecondaryDisplay == null) {
            showMessage("未检测到副屏设备");
            return;
        }

        if (mSubScreen != null && mSubScreen.isShowing()) {
            // 如果副屏已经打开，切换URL
            mSubScreen.switchUrl(url);
            showMessage("副屏URL已切换");
        } else {
            // 创建并显示副屏
            mSubScreen = new WebSubScreen(this, mSecondaryDisplay, url);
            mSubScreen.show();
            showMessage("副屏已打开");
        }
    }

    /**
     * 关闭副屏
     */
    public void closeSubScreen() {
        if (mSubScreen != null && mSubScreen.isShowing()) {
            mSubScreen.dismiss();
            mSubScreen = null;
            showMessage("副屏已关闭");
        } else {
            showMessage("副屏未打开");
        }
    }

    /**
     * 检查副屏是否正在显示
     * @return 是否正在显示副屏
     */
    public boolean isSubScreenShowing() {
        return mSubScreen != null && mSubScreen.isShowing();
    }

    public void sendDataToSubScreen(String data) {
        if (mSubScreen != null && mSubScreen.isShowing()) {
            mSubScreen.sendDataToSubScreen(data);
        }
    }

    private void countImgTransform(PrintH5Model printH5Model) {
        mPrintH5Model = printH5Model;
        imgTransCount = 0;
        int imgNum = 0;
        if (imgBitmap.size() > 20) {
            imgBitmap.clear();
        }
        if (printH5Model == null || printH5Model.printModelList == null) {
            return;
        }
        for (PrintModel printModel : printH5Model.printModelList) {
            if (printModel.type != PrintType.TYPE_IMAGE || printModel.image == null
                    || TextUtils.isEmpty(printModel.image.url)) {
                continue;
            }
            imgNum += 1;
            imgTransCount += 1;
            createBitmapFromUrl(printModel.image.url, printModel.image.width, printModel.image.height);
        }
        if (imgNum == 0) {
            print(mPrintH5Model);
        }
    }

    public void print(PrintH5Model printH5Model) {
        if (printerService == null) return;
        if (printH5Model == null ||
                printH5Model.printModelList == null || printH5Model.printModelList.size() == 0)
            return;
        try {
            printerService.enterPrinterBuffer(true);
            for (PrintModel model : printH5Model.printModelList) {
                switch (model.type) {
                    case PrintType.TYPE_TEXT:
                        printText(model);
                        break;
                    case PrintType.TYPE_BARCODE:
                        printBarCode(model);
                        break;
                    case PrintType.TYPE_QRCODE:
                        printQrCode(model);
                        break;
                    case PrintType.TYPE_COLUMN:
                        printColumn(model);
                        break;
                    case PrintType.TYPE_IMAGE:
                        printImage(model);
                        break;
                }
            }

            if (printH5Model.lineWrap != 0) {
                printerService.lineWrap(printH5Model.lineWrap, printCallback);
            }
            if (printH5Model.autoCut) {
                printerService.cutPaper(printCallback);
            }
            printerService.exitPrinterBuffer(true);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void printText(PrintModel model) {
        TextData textData = model.text;
        try {
            if (textData.isBold) {
                printerService.sendRAWData(new byte[]{0x1B, 0x45, 0x1}, printCallback);
            } else {
                printerService.sendRAWData(new byte[]{0x1B, 0x45, 0x0}, printCallback);
            }
            printerService.setFontSize(textData.fontSize != 0 ? textData.fontSize : 24f, printCallback);
            printerService.setAlignment(textData.alignment, printCallback);
            printerService.printText(textData.text, printCallback);
            if (!textData.text.contains("\n")) {
                printerService.commitPrinterBuffer();
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void printBarCode(PrintModel model) {
        BarCodeData barCodeData = model.barCode;
        try {
            printerService.setAlignment(1, printCallback);
            printerService.printBarCode(barCodeData.code, barCodeData.type,
                    barCodeData.height, barCodeData.width, barCodeData.textPosition, printCallback);
            printerService.setAlignment(0, printCallback);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void printQrCode(PrintModel model) {
        QrCodeData qrCodeData = model.qrCode;
        try {
            printerService.setAlignment(1, printCallback);
            printerService.printQRCode(qrCodeData.qrcode, qrCodeData.size,
                    qrCodeData.errorLevel, printCallback);
            printerService.setAlignment(0, printCallback);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void printColumn(PrintModel model) {
        ColumnData columnData = model.column;
        try {
            if (columnData.isBold) {
                printerService.sendRAWData(new byte[]{0x1B, 0x45, 0x1}, printCallback);
            } else {
                printerService.sendRAWData(new byte[]{0x1B, 0x45, 0x0}, printCallback);
            }
            printerService.setFontSize(columnData.size != 0 ? columnData.size : 16f, printCallback);
            printerService.printColumnsString(columnData.texts, columnData.weights,
                    columnData.aligns, printCallback);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void printImage(PrintModel model) {
        ImageData imageData = model.image;
        if (imgBitmap.get(imageData.url) == null) return;
        try {
            printerService.setAlignment(imageData.alignment, printCallback);
            printerService.printBitmap(imgBitmap.get(imageData.url), printCallback);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void createBitmapFromUrl(String url, int width, int height) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap resizedBitmap = resource;
                        if (width > 0 && height > 0) {
                            resizedBitmap = Bitmap.createScaledBitmap(resource, width, height, false);
                        }

                        imgTransCount -= 1;
                        imgBitmap.put(url, resizedBitmap);
                        if (imgTransCount == 0) {
                            print(mPrintH5Model);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle clearing if necessary
                    }
                });
    }
}
