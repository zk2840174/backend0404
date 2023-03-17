package org.zerock.backend.util;


import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class FcmUtilTests {

    @Autowired
    FcmUtil fcmUtil;

    @Test
    public void test1(){
        log.info(fcmUtil);

    }

    @Test
    public void testSend1() {
        String fcmToken = "cXPI5_w-Bol8syEDiwLOrP:APA91bFmm2Mg354-PLTmM-wrN7gZNPxBSeJ99r0lDW_pgOC4NhjOohQf9J9YlTNv1QGObBg7A0O-xgOQI0T4h_0sqcaKWrF9szbseCOQgqeJe9tlPPzcU1xdS3FlfwIpVMoSmwwGCZo5";

        try {
            fcmUtil.sendNotification(fcmToken,"Test","Test");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
