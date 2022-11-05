package com.example.springjdsale;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class HttpUtils {

    private static final String COOKIE = "__jdu=16531872872011170887892; shshshfpa=c6bf4d9e-52a7-655a-57e3-18e6d2fdfd74-1653187288; shshshfpb=wYakAi18x54k7u92wiDn3tQ; pinId=Q52uBe6Q_y8NXZKjZxABLA; areaId=2; pin=%E7%8B%AC%E7%81%AF%E7%85%A7%E5%AD%A4%E5%BD%B1; unick=%E7%8B%AC%E7%81%AF%E7%85%A7%E5%AD%A4%E5%BD%B1; _tp=DoWz869YaYYvSlJeaxSDRZPteOBWnfGBD0LkH1bNrNmVWaeLKhCS5NNyei89Yk0P; _pst=%E7%8B%AC%E7%81%AF%E7%85%A7%E5%AD%A4%E5%BD%B1; user-key=7fcb94b3-a107-40f4-a59c-9c46de0ec39f; ipLoc-djd=2-2830-51826-0.975242668; ipLocation=%u4e0a%u6d77; unpl=JF8EAMpnNSttDB5UUBhWGRBAHlpcWwhbGUQGPzJRBlhdTFdXHAEcQhh7XlVdXhRLFx9uYhRXXFNIUw4bCysSEXteXVdZDEsWC2tXVgQFDQ8VXURJQlZAFDNVCV9dSRZRZjJWBFtdT1xWSAYYRRMfDlAKDlhCR1FpMjVkXlh7VAQrAhwWEktUUl1UDkgWA29nA1VUUU1dDRgyGiIXe21kXlkNTR4DX2Y1VW0aHwgDHwYeERgGXVNaXwhCEQBmYQZVXVhLUgQSCx0bGEhtVW5e; TrackID=1IRSvIKD_p0ACV1ST4QnyGznzLtZGxwKKACT2OWA9WhZqrYOZ8DAr7uPzJHxVrsG9PHmi1elgZ2Rw6tS0F_q_il_SHs51NNJSXsJDiEX62V8; ceshi3.com=201; shshshfp=9353fbfab8b37d8f743ffe394a107595; answer-code=\"\"; __jdv=76161171|baidu-pinzhuan|t_288551095_baidupinzhuan|cpc|0f3d30c8dba7459bb52f2eb5eba8ac7d_0_ed1d2e33ad694dbcb4adec4462c626a9|1667565539081; __jdc=122270672; cn=1; __jda=122270672.16531872872011170887892.1653187287.1667564779.1667569000.16; __jdb=122270672.1.16531872872011170887892|16.1667569000; thor=19FC4B6B69635200572AD2079EF159ECF98EF7550FB8CFD061E12727916E68F7245B6A3ABB2C233892F3E1B76FF4B2B5F401DC7DB290E781F097D3E33A3A0295643192A90EA18742380E0F70FD2D0DE2259B63F5701218141E0E71BC68953E06484F9879B15C8AC1901D90EFDEAB2EFC771AE50F936CE16612123615833CF8E7; JSESSIONID=CFC1394AAB40D72BAB81FCD0D25258A6.s1; 3AB9D23F7A4B3C9B=2YGRYGWE4N7ZPR35GV74ROZR2LOCPBWDH47UYO3L464TC7AIOXTCPU7E6IHVP2CAX3T5WABLF4A44J5O2RPPMLTRMQ";

    private static final String MSI_COOKIE = "UM_distinctid=18433870bba77-00a90f43f8cdb1-18525635-1ea000-18433870bbb9cb; cna=196ac18247de478892ca091b4382253b; CNZZDATA1280929406=396867983-1667310553-https%253A%252F%252Fwww.baidu.com%252F%7C1667610691; resetUrl=/ProductDefault?goodsId=357; Authorization=bearer%20eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMjcuMC4wLjE6MzAwMlwvYXBpXC91c2VyTW9iaWxlTG9naW4iLCJpYXQiOjE2Njc2MTEyODQsImV4cCI6MTY2NzY1NDQ4NCwibmJmIjoxNjY3NjExMjg0LCJqdGkiOiIxVDdqQm5jYjFxTUtDTzNyIiwic3ViIjoxNDkxOSwicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.XqiJKXVwbFFmW4Jv6oiLV_h2vOMwScgDox28aWXsEcc; userInfo={%22id%22:14919%2C%22username%22:%22%E5%BE%AE%E6%98%9F%E7%94%A8%E6%88%B78652%22%2C%22nickname%22:%22%E5%BE%AE%E6%98%9F%E7%94%A8%E6%88%B78652%22%2C%22mobile%22:%2215801888652%22%2C%22avatar%22:%22https://oss-store.msi.com/goods/165061122618822515.png%22%2C%22gender%22:0%2C%22birthday%22:null%2C%22money%22:%220.00%22%2C%22score%22:100%2C%22successions%22:1%2C%22maxsuccessions%22:1%2C%22prevtime%22:1667611284%2C%22logintime%22:null%2C%22jointime%22:null%2C%22created_at%22:%222022-10-22T03:09:28.000000Z%22%2C%22updated_at%22:%222022-11-01T15:14:14.000000Z%22%2C%22status%22:null%2C%22verification%22:%22%22%2C%22deleted_at%22:null%2C%22city_name%22:null%2C%22province_id%22:null%2C%22city_id%22:null%2C%22area_id%22:null%2C%22is_new_send_coupon%22:3%2C%22email%22:null%2C%22level%22:1%2C%22waitPay%22:0%2C%22waitReceiving%22:0%2C%22waitComment%22:0%2C%22collection%22:0%2C%22isSign%22:0%2C%22sign_give_score%22:100%2C%22level_show%22:%22%E9%9D%92%E9%93%9C%22%2C%22user_oauth%22:{%22id%22:11178%2C%22user_id%22:14919%2C%22platform%22:%22wechat%22%2C%22unionid%22:%22o76qi5wXrmM3NLVBYafqGtbxqaUg%22%2C%22openid%22:%22oxj_c5PAP7NfgYeajRaIPm2QwuSc%22%2C%22nickname%22:%22%E5%BE%AE%E6%98%9F%E7%94%A8%E6%88%B78652%22%2C%22sex%22:0%2C%22country%22:null%2C%22province%22:null%2C%22city%22:null%2C%22headimgurl%22:%22https://oss-store.msi.com/goods/165061122618822515.png%22%2C%22logintime%22:null%2C%22logincount%22:0%2C%22created_at%22:%222022-10-31%2014:23:15%22%2C%22updated_at%22:%222022-10-31T14:23:15.000000Z%22%2C%22pc_openid%22:null%2C%22h5_openid%22:null}}; Hm_lvt_12b6f3589916b46ae78ab99b5e9a9a88=1667315576,1667319115,1667611175,1667612530; acw_tc=7793941c16676129829004756e1b41f4bc6dc174203ac18d8693c5d6ed; Hm_lpvt_12b6f3589916b46ae78ab99b5e9a9a88=1667613462; activeIndex=1";
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
        return sendHttpMethod(submitOrder);
    }

    public static String submitMsiOrder() {
        HttpPost httpPost = new HttpPost(MSI_SUBMIT_ORDER);
        addCommonHeader(httpPost, MSI_COOKIE);
        httpPost.addHeader("authorization", "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMjcuMC4wLjE6MzAwMlwvYXBpXC91c2VyTW9iaWxlTG9naW4iLCJpYXQiOjE2Njc2MTEyODQsImV4cCI6MTY2NzY1NDQ4NCwibmJmIjoxNjY3NjExMjg0LCJqdGkiOiIxVDdqQm5jYjFxTUtDTzNyIiwic3ViIjoxNDkxOSwicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.XqiJKXVwbFFmW4Jv6oiLV_h2vOMwScgDox28aWXsEcc");
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
            EmailUtils.sendEmail("及时查看订单信息");
            return true;
        } else {
            message = JSON.parseObject(message).getString("message");
            log.info("get order message:{}", message);

            if (!message.contains("无货") && !message.contains("请稍后再试") && !message.contains("获取用户订单信息失败")) {
                EmailUtils.sendEmail("及时查看订单信息");
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

            if (!message.equals("库存不足")) {
                EmailUtils.sendEmail("及时查看微星商城订单信息");
                return true;
            }
        }

        return false;
    }
}
