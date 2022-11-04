package com.example.springjdsale;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Random;

@Slf4j
public class HttpUtilsTest {

    @Test
    public void testSubmitOrder() {
        while(true) {
            try {
                Random random = new Random();
                int randomSleepTime = random.nextInt(10);
                randomSleepTime = 5000 + randomSleepTime * 100;

                Thread.sleep(randomSleepTime);
            } catch (InterruptedException e) {
                log.error("thread sleep failed", e);
            }

            boolean submitResult = HttpUtils.submit();
            if (submitResult) {
                break;
            }
        }
    }

    @Test
    public void testSendEmail() {
        EmailUtils.sendEmail("正在抢购中，请及时关注订单");
    }

    @Test
    public void testLog() {
        log.debug("test log context");
    }
}
