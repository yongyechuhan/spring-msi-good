package com.example.springjdsale;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class HttpUtils {

    private static final String COOKIE = "__jdu=16531872872011170887892; shshshfpa=c6bf4d9e-52a7-655a-57e3-18e6d2fdfd74-1653187288; shshshfpb=wYakAi18x54k7u92wiDn3tQ; pinId=Q52uBe6Q_y8NXZKjZxABLA; areaId=2; pin=%E7%8B%AC%E7%81%AF%E7%85%A7%E5%AD%A4%E5%BD%B1; unick=%E7%8B%AC%E7%81%AF%E7%85%A7%E5%AD%A4%E5%BD%B1; _tp=DoWz869YaYYvSlJeaxSDRZPteOBWnfGBD0LkH1bNrNmVWaeLKhCS5NNyei89Yk0P; _pst=%E7%8B%AC%E7%81%AF%E7%85%A7%E5%AD%A4%E5%BD%B1; user-key=7fcb94b3-a107-40f4-a59c-9c46de0ec39f; ipLoc-djd=2-2830-51826-0.975242668; ipLocation=%u4e0a%u6d77; unpl=JF8EAMpnNSttDB5UUBhWGRBAHlpcWwhbGUQGPzJRBlhdTFdXHAEcQhh7XlVdXhRLFx9uYhRXXFNIUw4bCysSEXteXVdZDEsWC2tXVgQFDQ8VXURJQlZAFDNVCV9dSRZRZjJWBFtdT1xWSAYYRRMfDlAKDlhCR1FpMjVkXlh7VAQrAhwWEktUUl1UDkgWA29nA1VUUU1dDRgyGiIXe21kXlkNTR4DX2Y1VW0aHwgDHwYeERgGXVNaXwhCEQBmYQZVXVhLUgQSCx0bGEhtVW5e; TrackID=1IRSvIKD_p0ACV1ST4QnyGznzLtZGxwKKACT2OWA9WhZqrYOZ8DAr7uPzJHxVrsG9PHmi1elgZ2Rw6tS0F_q_il_SHs51NNJSXsJDiEX62V8; ceshi3.com=201; shshshfp=9353fbfab8b37d8f743ffe394a107595; answer-code=\"\"; __jdv=76161171|baidu-pinzhuan|t_288551095_baidupinzhuan|cpc|0f3d30c8dba7459bb52f2eb5eba8ac7d_0_ed1d2e33ad694dbcb4adec4462c626a9|1667565539081; __jdc=122270672; cn=1; __jda=122270672.16531872872011170887892.1653187287.1667564779.1667569000.16; __jdb=122270672.1.16531872872011170887892|16.1667569000; thor=19FC4B6B69635200572AD2079EF159ECF98EF7550FB8CFD061E12727916E68F7245B6A3ABB2C233892F3E1B76FF4B2B5F401DC7DB290E781F097D3E33A3A0295643192A90EA18742380E0F70FD2D0DE2259B63F5701218141E0E71BC68953E06484F9879B15C8AC1901D90EFDEAB2EFC771AE50F936CE16612123615833CF8E7; JSESSIONID=CFC1394AAB40D72BAB81FCD0D25258A6.s1; 3AB9D23F7A4B3C9B=2YGRYGWE4N7ZPR35GV74ROZR2LOCPBWDH47UYO3L464TC7AIOXTCPU7E6IHVP2CAX3T5WABLF4A44J5O2RPPMLTRMQ";
    private static final String ITEM_ID = "10062682325270";

    private static final String ADD_ITEM_TO_CART = "https://cart.jd.com/gate.action?pid="+ITEM_ID+"&pcount=1&ptype=1";

    private static final String DELETE_ITEM_FROM_CART = "https://cart.jd.com/cart/miniCartServiceNew.action?method=RemoveProduct&type=1&cartId="+ITEM_ID;

    private static final String GET_ORDER = "https://trade.jd.com/shopping/order/getOrderInfo.action";

    private static final String SUBMIT_ORDER = "https://trade.jd.com/shopping/order/submitOrder.action?&presaleStockSign=1";
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

    public static HttpGet getHttpGetMethod(String requestUrl) {
        HttpGet httpGet = new HttpGet(requestUrl);
        addCommonHeader(httpGet);
        return httpGet;
    }

    public static HttpPost getHttpPostMethod(String requestUrl){
        HttpPost httpPost = new HttpPost(requestUrl);
        addCommonHeader(httpPost);
        return httpPost;
    }

    public static void addCommonHeader(HttpRequestBase httpRequestBase) {
        httpRequestBase.addHeader("cookie", COOKIE);
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
        log.info("get order message:{}", JSON.parseObject(message).get("message"));

        int sendEmailNum = 0;
        if (!message.contains("无货") && !message.contains("请稍后再试") && sendEmailNum < SEND_EMAIL_MAX) {
            for (int i = 0; i < 3; i++) {
                EmailUtils.sendEmail("及时查看订单信息");
                sendEmailNum++;
            }
            return true;
        }

        return false;
    }
}
