package cn.yfd.springframework.test.bean;

import cn.yfd.springframework.stereotype.Component;

import java.util.Random;

@Component("userService5")
public class UserService5 implements IUserService{

    private String token;

    @Override
    public String queryUserInfo() {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "羊粪蛋，100001，深圳";
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
        return "UserService5#token = { "+token + "}";
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }
}


















