package cn.yfd.springframework.test.bean;

import java.util.HashMap;
import java.util.Map;

public class UserDao {
    private static Map<String, String> map = new HashMap<>();

    static{
        map.put("100001","羊粪dan");
        map.put("100002", "bullShit");
        map.put("100003","谢尔比");
    }

    public String queryUserName(String uid){
        return map.get(uid);
    }
}
