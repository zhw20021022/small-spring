package cn.yfd.springframework.test.bean;

import cn.yfd.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDao3 {

    private static Map<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("10001", "羊粪蛋，北京，亦庄");
        hashMap.put("10002", "八杯水，上海，尖沙咀");
        hashMap.put("10003", "阿毛，天津，东丽区");
    }

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }

}
