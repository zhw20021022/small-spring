package cn.yfd.springframework.test.bean;

import java.util.HashMap;
import java.util.Map;

public class UserDao1 {
    private static Map<String, String> hashMap = new HashMap<>();

    public void initDataMethod(){
        System.out.println("执行:init-method");
        hashMap.put("100001","羊粪dan");
        hashMap.put("100002", "bullShit");
        hashMap.put("100003","谢尔比");
    }

    public void destroyDataMethod(){
        System.out.println("执行:destroy-method");
        hashMap.clear();
    }

    public String queryUserName(String uid){
        return hashMap.get(uid);
    }
}
