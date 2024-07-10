package cn.yfd.springframework.test.bean;

import cn.yfd.springframework.Beans.factory.annotation.Autowired;
import cn.yfd.springframework.Beans.factory.annotation.Value;
import cn.yfd.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UserService6 implements IUserService{

    @Value("${token}")
    private String token;

    @Autowired
    private UserDao3 userDao3;

    public String queryUserInfo() {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userDao3.queryUserName("10001") + "，" + token;
    }

    @Override
    public String register(String username) {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "注册用户：" + username + " success！";
    }

    @Override
    public String toString() {
        return "UserService#token = { " + token + " }";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDao3 getUserDao3() {
        return userDao3;
    }

    public void setUserDao3(UserDao3 userDao3) {
        this.userDao3 = userDao3;
    }
}
