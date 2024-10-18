package com.mrzhang.mieapiinterface;

import com.mrzhang.mieapiclientsdk.client.MieApiClient;
import com.mrzhang.mieapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MieApiInterfaceApplicationTests {

    @Resource
    private MieApiClient mieApiClient;

    @Test
    void contextLoads() {
        String result = mieApiClient.getNameByGet("yupi");
        User user = new User();
        user.setUsername("作_者 【程序员_xiaoyang}");
        String usernameByPost = mieApiClient.getUsernameByPost(user);
        System.out.println(result);
        System.out.println(usernameByPost);
    }
}
