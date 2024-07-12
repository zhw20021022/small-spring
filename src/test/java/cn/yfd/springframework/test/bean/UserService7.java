package cn.yfd.springframework.test.bean;

import java.util.Random;

public class UserService7 implements IUserService {

    private String token;

    @Override
    public String queryUserInfo() {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "yfd，100001，深圳，" + token;

    }

    @Override
    public String register(String username) {
        return null;
    }
}
