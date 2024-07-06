package cn.yfd.springframework.test.bean;

import java.util.Random;

public class UserService4 implements IUserService{


    @Override
    public String queryUserInfo() {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "羊粪蛋, 100001, 北京";
    }

    @Override
    public String register(String username) {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "注册用户: "+ username + " success!";
    }
}
