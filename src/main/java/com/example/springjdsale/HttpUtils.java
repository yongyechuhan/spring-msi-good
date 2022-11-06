package com.example.springjdsale;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class HttpUtils {

    private static final String COOKIE = "__jdu=1339144719; areaId=2; shshshfpa=9b4f973b-8777-ed81-4797-e927506817f8-1665902103; shshshfpb=u_1-QelhY0Rzf1J-Bf3mdNw; pinId=Q52uBe6Q_y8NXZKjZxABLA; pin=%E7%8B%AC%E7%81%AF%E7%85%A7%E5%AD%A4%E5%BD%B1; unick=%E7%8B%AC%E7%81%AF%E7%85%A7%E5%AD%A4%E5%BD%B1; _tp=DoWz869YaYYvSlJeaxSDRZPteOBWnfGBD0LkH1bNrNmVWaeLKhCS5NNyei89Yk0P; _pst=%E7%8B%AC%E7%81%AF%E7%85%A7%E5%AD%A4%E5%BD%B1; user-key=3ee7ec09-d205-4169-83ae-b6e7cd409c8f; ipLoc-djd=2-2830-51826-0.975242668; ipLocation=%u4e0a%u6d77; mt_xid=V2_52007VwMQUFVYV18ZSRFsV2ACGwddWFRGShwaXBliA0AFQVFbDk1VHgwANAoRUggKWwlIeRpdBmQfElJBWVFLHEsSWQVsAxBiX2hSahhBG10GZgEUUV1oUlMeTw%3D%3D; unpl=JF8EAK9nNSttWh8HVU5SGUIZSwlQWwoJGUcBPDRSBAlRT1MFGwAaERV7XlVdXhRLFx9uYxRXXFNLVA4YCisSEXteXVdZDEsWC2tXVgQFDQ8VXURJQlZAFDNVCV9dSRZRZjJWBFtdT1xWSAYYRRMfDlAKDlhCR1FpMjVkXlh7VAQrAhkQGEtYUVhdAHsWM2hXNWRaX0JQAysDKxMgCQkIW1kMSBAEImcHVlVYTlEDGworEyBI; TrackID=1qa18oWUhJwnh61pxLlLebeg8n_oCQ60V6KVGK60vOsXq5x5HdiTuYvvDH-AdZ7xV7W0Z4PPWkOEVZJG-jZ3miZ7cG7hVmJGCNJYkA3mvnCmvGXEEjVvxKLk5fr4IXrCN; ceshi3.com=201; __jdv=76161171|baidu-pinzhuan|t_288551095_baidupinzhuan|cpc|0f3d30c8dba7459bb52f2eb5eba8ac7d_0_3ebada3a81e54f0ca3bbfae856113024|1667650305917; __jda=122270672.1339144719.1665902100.1667615260.1667650273.17; __jdc=122270672; shshshfp=29fba3182b841ccc74a8361a3c15411c; thor=19FC4B6B69635200572AD2079EF159ECFBA999F0D0C16306AE4CB2CD69F0F99CF040C485E33C4848AB9DC4D5FD854892EDC2712E306AD0C53AAC9188303A0D3B7F725CF2B245C8D8F312BC9F92762FE4B2C7C0D4FD234C5B10DBF5906C0A57C51A37B5DC64088CC259EC4EB3569AF85F6E29D8C7CD3996EAC5977A933B65075E; shshshsID=b7cb5bcffa3fcf3568cee61df298dc4e_6_1667651248738; cn=1; __jdb=122270672.17.1339144719|17.1667650273; 3AB9D23F7A4B3C9B=EELKSX7MEAYX5VX4VU3SKWTCRUNKBYOHIGHWIPYCCOIDBIFEWC6H2SP7Z7XXSHAYN3HB6PLQQ36FN6T4DM3HI7I4WA";

    private static final String MSI_COOKIE = "UM_distinctid=18433870bba77-00a90f43f8cdb1-18525635-1ea000-18433870bbb9cb; cna=196ac18247de478892ca091b4382253b; resetUrl=/ProductDefault?goodsId=357; Hm_lvt_12b6f3589916b46ae78ab99b5e9a9a88=1667612530,1667618250,1667655181,1667655679; bannertype=0; acw_tc=3da0cc2516676995787121043eee0dbbf6f179e33078598c8a5acb80bc; CNZZDATA1280929406=396867983-1667310553-https%253A%252F%252Fwww.baidu.com%252F%7C1667698038; Authorization=bearer%20eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMjcuMC4wLjE6MzAwMlwvYXBpXC91c2VyTW9iaWxlTG9naW4iLCJpYXQiOjE2Njc2OTk2MTQsImV4cCI6MTY2Nzc0MjgxNCwibmJmIjoxNjY3Njk5NjE0LCJqdGkiOiI0WmJuNUVwUlNoWGJsenlsIiwic3ViIjoxNDkxOSwicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.WWFO4k1jSlvepGseGYw4BiMbSAJc98yFeqZ6XnWcsoQ; userInfo={%22id%22:14919%2C%22username%22:%22%E5%BE%AE%E6%98%9F%E7%94%A8%E6%88%B78652%22%2C%22nickname%22:%22%E5%BE%AE%E6%98%9F%E7%94%A8%E6%88%B78652%22%2C%22mobile%22:%2215801888652%22%2C%22avatar%22:%22https://oss-store.msi.com/goods/165061122618822515.png%22%2C%22gender%22:0%2C%22birthday%22:null%2C%22money%22:%220.00%22%2C%22score%22:200%2C%22successions%22:1%2C%22maxsuccessions%22:1%2C%22prevtime%22:1667699614%2C%22logintime%22:null%2C%22jointime%22:null%2C%22created_at%22:%222022-10-22T03:09:28.000000Z%22%2C%22updated_at%22:%222022-11-05T03:17:59.000000Z%22%2C%22status%22:null%2C%22verification%22:%22%22%2C%22deleted_at%22:null%2C%22city_name%22:null%2C%22province_id%22:null%2C%22city_id%22:null%2C%22area_id%22:null%2C%22is_new_send_coupon%22:3%2C%22email%22:null%2C%22level%22:1%2C%22waitPay%22:0%2C%22waitReceiving%22:0%2C%22waitComment%22:0%2C%22collection%22:0%2C%22isSign%22:0%2C%22sign_give_score%22:100%2C%22level_show%22:%22%E9%9D%92%E9%93%9C%22%2C%22user_oauth%22:{%22id%22:11178%2C%22user_id%22:14919%2C%22platform%22:%22wechat%22%2C%22unionid%22:%22o76qi5wXrmM3NLVBYafqGtbxqaUg%22%2C%22openid%22:%22oxj_c5PAP7NfgYeajRaIPm2QwuSc%22%2C%22nickname%22:%22%E5%BE%AE%E6%98%9F%E7%94%A8%E6%88%B78652%22%2C%22sex%22:0%2C%22country%22:null%2C%22province%22:null%2C%22city%22:null%2C%22headimgurl%22:%22https://oss-store.msi.com/goods/165061122618822515.png%22%2C%22logintime%22:null%2C%22logincount%22:0%2C%22created_at%22:%222022-10-31%2014:23:15%22%2C%22updated_at%22:%222022-10-31T14:23:15.000000Z%22%2C%22pc_openid%22:null%2C%22h5_openid%22:null}}; activeIndex=0; Hm_lpvt_12b6f3589916b46ae78ab99b5e9a9a88=1667699619";

    private static final String MSI_TOKEN = "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMjcuMC4wLjE6MzAwMlwvYXBpXC91c2VyTW9iaWxlTG9naW4iLCJpYXQiOjE2Njc2OTk2MTQsImV4cCI6MTY2Nzc0MjgxNCwibmJmIjoxNjY3Njk5NjE0LCJqdGkiOiI0WmJuNUVwUlNoWGJsenlsIiwic3ViIjoxNDkxOSwicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.WWFO4k1jSlvepGseGYw4BiMbSAJc98yFeqZ6XnWcsoQ";

    private static final String ITEM_ID = "10062682325270";

    private static final String ADD_ITEM_TO_CART = "https://cart.jd.com/gate.action?pid="+ITEM_ID+"&pcount=1&ptype=1";

    private static final String DELETE_ITEM_FROM_CART = "https://cart.jd.com/cart/miniCartServiceNew.action?method=RemoveProduct&type=1&cartId="+ITEM_ID;

    private static final String GET_ORDER = "https://trade.jd.com/shopping/order/getOrderInfo.action";

    private static final String SUBMIT_ORDER = "https://trade.jd.com/shopping/order/submitOrder.action?&presaleStockSign=1";

    private static final String MSI_SUBMIT_ORDER = "https://cn-store.msi.com/api/api/orderCreate";

    private static final HttpClient HTTP_CLIENT = HttpClientBuilder.create().build();

    private static final int SEND_EMAIL_MAX = 6;

    public static String addItemToCart() {
        HttpGet getItemToCart = getHttpGetMethod(ADD_ITEM_TO_CART);
        return sendHttpMethod(getItemToCart);
    }

    public static String delItemFromCart() {
        HttpGet delItemFromCart = getHttpGetMethod(DELETE_ITEM_FROM_CART);
        delItemFromCart.addHeader("referer", "https://www.jd.com/");
        return sendHttpMethod(delItemFromCart);
    }

    public static String getOrderAction() {
        HttpGet getOrderAction = getHttpGetMethod(GET_ORDER);
        getOrderAction.addHeader("referer", "https://cart.jd.com/");
        getOrderAction.addHeader("upgrade-insecure-requests", "1");
        return sendHttpMethod(getOrderAction);
    }

    public static String submitOrder() {
        HttpPost submitOrder = getHttpPostMethod(SUBMIT_ORDER);
        submitOrder.addHeader("origin", "https://trade.jd.com");
        submitOrder.addHeader("referer", GET_ORDER);
        submitOrder.setHeader("accept", "application/json");

        UrlEncodedFormEntity reqEntity = null;
        try {
            reqEntity = new UrlEncodedFormEntity(buildSumbitOrderUrlEncodedBody());
        } catch (UnsupportedEncodingException e) {
            log.error("build url encoded form entity failed", e);
        }
        submitOrder.setEntity(reqEntity);
        return sendHttpMethod(submitOrder);
    }

    public static String submitMsiOrder() {
        HttpPost httpPost = new HttpPost(MSI_SUBMIT_ORDER);
        addCommonHeader(httpPost, MSI_COOKIE);
        httpPost.addHeader("authorization", MSI_TOKEN);
        httpPost.addHeader("origin", "https://cn-store.msi.com");
        httpPost.addHeader("referer", "https://cn-store.msi.com/ProductDefault?goodsId=357");

        JSONObject requestData = new JSONObject();
        requestData.put("sku_id", "449");
        requestData.put("goods_id", "357");
        requestData.put("address_id", "2867");
        requestData.put("goods_num", "1");
        requestData.put("platform", "PC");
        requestData.put("remark", "");
        requestData.put("is_invoice", 0);
        requestData.put("type", 1);
        requestData.put("header_type", 1);
        requestData.put("is_score", 0);
        HttpEntity goodsEntity = new StringEntity(requestData.toString(), ContentType.APPLICATION_JSON);

        httpPost.setEntity(goodsEntity);
        return sendHttpMethod(httpPost);
    }

    public static HttpGet getHttpGetMethod(String requestUrl) {
        HttpGet httpGet = new HttpGet(requestUrl);
        addCommonHeader(httpGet, COOKIE);
        return httpGet;
    }

    public static HttpPost getHttpPostMethod(String requestUrl){
        HttpPost httpPost = new HttpPost(requestUrl);
        addCommonHeader(httpPost, COOKIE);
        return httpPost;
    }

    public static void addCommonHeader(HttpRequestBase httpRequestBase, String cookie) {
        httpRequestBase.addHeader("cookie", Optional.ofNullable(cookie).orElse(COOKIE));
        httpRequestBase.addHeader("Accept", "*/*");
        httpRequestBase.addHeader("Connection", "keep-alive");
        httpRequestBase.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
    }

    public static List<NameValuePair> buildSumbitOrderUrlEncodedBody() {
        List<NameValuePair> nameValueBody = new ArrayList<>();

        NameValuePair payPassword = new BasicNameValuePair("submitOrderParam.payPassword", "u31u35u32u33u34u36");
        nameValueBody.add(payPassword);

        NameValuePair remarks = new BasicNameValuePair("vendorRemarks", "[{\"venderId\":\"10493823\",\"remark\":\"\"}]");
        nameValueBody.add(remarks);

        NameValuePair sopNotPutInvoice = new BasicNameValuePair("vsubmitOrderParam.sopNotPutInvoice", "false");
        nameValueBody.add(sopNotPutInvoice);

        NameValuePair trackID = new BasicNameValuePair("submitOrderParam.trackID", "TestTrackId");
        nameValueBody.add(trackID);

        NameValuePair presaleStockSign = new BasicNameValuePair("presaleStockSign", "1");
        nameValueBody.add(presaleStockSign);

        NameValuePair ignorePriceChange = new BasicNameValuePair("submitOrderParam.ignorePriceChange", "0");
        nameValueBody.add(ignorePriceChange);

        NameValuePair btSupport = new BasicNameValuePair("submitOrderParam.btSupport", "0");
        nameValueBody.add(btSupport);

        NameValuePair eid = new BasicNameValuePair("submitOrderParam.eid", "EELKSX7MEAYX5VX4VU3SKWTCRUNKBYOHIGHWIPYCCOIDBIFEWC6H2SP7Z7XXSHAYN3HB6PLQQ36FN6T4DM3HI7I4WA");
        nameValueBody.add(eid);

        NameValuePair fp = new BasicNameValuePair("submitOrderParam.fp", "7b124fcee3b3833543a0a5c8df335fca");
        nameValueBody.add(fp);

        NameValuePair zpjd = new BasicNameValuePair("submitOrderParam.zpjd", "1");
        nameValueBody.add(zpjd);

        return nameValueBody;
    }

    public static String sendHttpMethod(HttpRequestBase httpRequestBase) {
        try {
            HttpResponse httpResponse = HTTP_CLIENT.execute(httpRequestBase);

            return EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            log.error("execute http request base failed", e);
        }

        return "request failed";
    }

    public static boolean submit() {
        // 从购物车中删除
        String delItemMsg = delItemFromCart();
        delItemMsg = Optional.ofNullable(delItemMsg).orElse("").replace("(","").replace(")", "");
        log.info("del item msg:{}", JSON.parseObject(delItemMsg).get("Result"));
        // 重新加入购物车
        String addItemToCartMsg = addItemToCart();
        log.info("add item to cart msg:{}", addItemToCartMsg.contains("<title>商品已成功加入购物车</title>"));
        // 加载订单页
        String orderAction = getOrderAction();
        if (orderAction.contains("刷新太频繁了")) {
            log.error("-----------------------------get order action failed------------------------------");
            return false;
        } else {
            log.info("get order success");
        }
        // 提交订单
        String message = submitOrder();
        message  = Optional.ofNullable(message).orElse("");
        if (message.equals("")) {
//            EmailUtils.sendEmail("及时查看订单信息");
            return true;
        } else {
            message = JSON.parseObject(message).getString("message");
            log.info("get order message:{}", message);

            if (!message.contains("无货") && !message.contains("请稍后再试") && !message.contains("获取用户订单信息失败")) {
//                EmailUtils.sendEmail("及时查看订单信息");
                return true;
            }
        }

        return false;
    }

    public static boolean submitMsi() {
        String message = submitMsiOrder();
        if (message.equals("")) {
            EmailUtils.sendEmail("及时查看微星商城订单信息");
            return true;
        } else {
            message = JSON.parseObject(message).getString("message");
            log.info("get order message:{}", message);

            if (!message.equals("库存不足") && !message.equals("未登录或登录状态失效")) {
                EmailUtils.sendEmail("及时查看微星商城订单信息");
                return true;
            }

            if (message.equals("未登录或登录状态失效")) {
                HttpGet relogin = new HttpGet("https://cn-store.msi.com/ProductDefault?goodsId=357");
                addCommonHeader(relogin, MSI_COOKIE);
                sendHttpMethod(relogin);
                log.info("relogin success");
            }
        }

        return false;
    }
}
