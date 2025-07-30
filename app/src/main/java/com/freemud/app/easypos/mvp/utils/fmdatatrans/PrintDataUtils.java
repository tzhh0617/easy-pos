package com.freemud.app.easypos.mvp.utils.fmdatatrans;

import com.freemud.app.easypos.mvp.model.ColumnData;
import com.freemud.app.easypos.mvp.model.ImageData;
import com.freemud.app.easypos.mvp.model.PrintModel;
import com.freemud.app.easypos.mvp.model.QrCodeData;
import com.freemud.app.easypos.mvp.model.TextData;
import com.freemud.app.easypos.mvp.model.constant.PrintType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuyuanbo on 2024/1/9.
 * Description:
 */
public class PrintDataUtils {

   public static List<PrintModel> getDiageoPrintData() {
      List<PrintModel> printModelList = new ArrayList<>();
      PrintModel printModel = new PrintModel();
      printModel.type = PrintType.TYPE_TEXT;
      printModel.text = new TextData("DIAGEO\n\n",48,1,true);

      PrintModel printModel2 = new PrintModel();
      printModel2.type = PrintType.TYPE_TEXT;
      printModel2.text = new TextData("最强品牌就是我\n\n",36,1,true);

      PrintModel printModel3 = new PrintModel();
      printModel3.type = PrintType.TYPE_COLUMN;
      printModel3.column = new ColumnData(new String[]{"销售小票","NO283"},new int[]{1,1},new int[]{0,2},24,true);


      PrintModel printModel4 = new PrintModel();
      printModel4.type = PrintType.TYPE_COLUMN;
      printModel4.column = new ColumnData(new String[]{"上海XXX门店","门店号：238293292"},new int[]{1,1},new int[]{0,2},16,false);


      PrintModel printModel5 = new PrintModel();
      printModel5.type = PrintType.TYPE_COLUMN;
      printModel5.column = new ColumnData(new String[]{"日期时间：2024.01.09 16:40:28","收银员：50012"},new int[]{2,1},new int[]{0,2},16,false);


      PrintModel printModel6_1 = new PrintModel();
      printModel6_1.type = PrintType.TYPE_TEXT;
      printModel6_1.text = new TextData("订单号：384722389242942894829422\n",16,0,false);

      PrintModel printModel7 = new PrintModel();
      printModel7.type = PrintType.TYPE_TEXT;
      printModel7.text = new TextData(getLineStr() + "\n",16,1,false);

      PrintModel printModel8 = new PrintModel();
      printModel8.type = PrintType.TYPE_COLUMN;
      printModel8.column = new ColumnData(new String[]{"商品名称","数量","单价"},new int[]{4,1,2},new int[]{0,1,1});

      PrintModel printModel9 = new PrintModel();
      printModel9.type = PrintType.TYPE_COLUMN;
      printModel9.column = new ColumnData(new String[]{"好吃的汉堡很好吃","1","￥22.00"},new int[]{4,1,2},new int[]{0,1,1});

      PrintModel printModel10 = new PrintModel();
      printModel10.type = PrintType.TYPE_COLUMN;
      printModel10.column = new ColumnData(new String[]{"好吃的汉堡很好吃","1","￥22222.00"},new int[]{4,1,2},new int[]{0,1,1});

      PrintModel printModel11 = new PrintModel();
      printModel11.type = PrintType.TYPE_TEXT;
      printModel11.text = new TextData(getLineStr() + "\n",16,1,false);

      PrintModel printModel12 = new PrintModel();
      printModel12.type = PrintType.TYPE_COLUMN;
      printModel12.column = new ColumnData(new String[]{"优惠抵扣：","","￥44.00"},new int[]{4,1,2},new int[]{0,1,1},false);

      PrintModel printModel13 = new PrintModel();
      printModel13.type = PrintType.TYPE_COLUMN;
      printModel13.column = new ColumnData(new String[]{"金额汇总:","2","￥22222.00"},new int[]{4,1,2},new int[]{0,1,1}, true);

      PrintModel printModel14 = new PrintModel();
      printModel14.type = PrintType.TYPE_TEXT;
      printModel14.text = new TextData(getLineStr() + "\n",16,1,false);

      PrintModel printModel15_1 = new PrintModel();
      printModel15_1.type = PrintType.TYPE_COLUMN;
      printModel15_1.column = new ColumnData(new String[]{"微信支付：","","￥22222.00"},new int[]{4,1,2},new int[]{0,1,1},16,true);

      PrintModel printModel16 = new PrintModel();
      printModel16.type = PrintType.TYPE_TEXT;
      printModel16.text = new TextData("感谢光临！\n\n\n",16,1,true);

      PrintModel printModel17 = new PrintModel();
      printModel17.type = PrintType.TYPE_TEXT;
      printModel17.text = new TextData("商品质量问题七日内退换!\n售后服务电话021-123456789\n此单为唯一购物凭证小票！\n\n\n\n",16,1,false);

      PrintModel printModel18 = new PrintModel();
      printModel18.type = PrintType.TYPE_TEXT;
      printModel18.text = new TextData("扫码进入小程序，给您更多惊喜\n",16,1,true);

      PrintModel printModel19 = new PrintModel();
      printModel19.type = PrintType.TYPE_QRCODE;
      printModel19.qrCode = new QrCodeData("扫码进入小程序，给您更多惊喜",4,3);

      PrintModel printModel20 = new PrintModel();
      printModel20.type = PrintType.TYPE_TEXT;
      printModel20.text = new TextData("扫码开票，有效期7个工作日\n",16,1,true);


      PrintModel printModel22 = new PrintModel();
      printModel22.type = PrintType.TYPE_IMAGE;
      printModel22.image = new ImageData("https://blog-10039692.file.myqcloud.com/1494387499331_2961_1494387499640.png",1);

      printModelList.add(printModel);
      printModelList.add(printModel2);
      printModelList.add(printModel3);
      printModelList.add(printModel4);
      printModelList.add(printModel5);
      printModelList.add(printModel6_1);
      printModelList.add(printModel7);
      printModelList.add(printModel8);
      printModelList.add(printModel9);
      printModelList.add(printModel10);
      printModelList.add(printModel11);
      printModelList.add(printModel12);
      printModelList.add(printModel13);
      printModelList.add(printModel14);
      printModelList.add(printModel15_1);
      printModelList.add(printModel16);
      printModelList.add(printModel17);
      printModelList.add(printModel18);
      printModelList.add(printModel19);
      printModelList.add(printModel20);
      printModelList.add(printModel22);
      return printModelList;
   }

   public static String getLineStr() {
      String tmp = "";
      int count = 69;   //46  69
      for (int i = 0; i <= count; i++) {
         tmp += "-";
      }
      return tmp;
   }
}
