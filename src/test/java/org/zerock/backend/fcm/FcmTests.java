package org.zerock.backend.fcm;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.backend.service.SampleFcmService;

@SpringBootTest
@Log4j2
public class FcmTests {

    @Autowired
    private SampleFcmService sampleFcmService;

    @Test
    public void test1() {

        log.info(sampleFcmService);

    }
}
