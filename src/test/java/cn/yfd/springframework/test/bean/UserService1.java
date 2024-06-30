package cn.yfd.springframework.test.bean;

import cn.yfd.springframework.Beans.factory.DisposableBean;
import cn.yfd.springframework.Beans.factory.InitializingBean;

public class UserService1 implements InitializingBean, DisposableBean {

    private String uid;
    private String company;
    private String location;
    private UserDao1 userDao1;

    @Override
    public void destroy() throws Exception {
        System.out.println("执行userService.destroy方法");
    }

    @Override
    public void afterPropertySet() throws Exception {
        System.out.println("执行userService.afterPropertySet方法");
    }

    public String queryUserInfo() {
        return userDao1.queryUserName(uid) + "," + company + "," + location;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UserDao1 getUserDao() {
        return userDao1;
    }

    public void setUserDao(UserDao1 userDao) {
        this.userDao1 = userDao;
    }
}
