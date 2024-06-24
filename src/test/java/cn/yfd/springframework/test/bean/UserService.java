package cn.yfd.springframework.test.bean;

public class UserService {

    private UserDao userDao;

    private String uid;
    private String name;

    public UserService(){
    }
    public UserService(String name) {
        this.name = name;
    }

    public String queryUserInfo(){
        return userDao.queryUserName(uid);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
