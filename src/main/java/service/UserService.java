package service;

import base.context.BlogsContext;
import dao.UserDao;
import entity.User;
import util.PageInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    // 登录（管理员）
    public User login(String username, String pwd) {
        return userDao.login(username, pwd);
    }

    // 验证是否注册了博客（管理员）
    public boolean chekcAdminRole() {
        return userDao.chekcAdminRole();
    }

    // 注册博客（管理员）
    public int registerUser(User u) {
        return userDao.registerUser(u);
    }

    // 获得博客主页上的各种数据
    public Map<String, Object> getMainInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        // 文章数
        int numArticle = BlogsContext.getInstance().getArticleDao().getArticleCount();
        // 评论数
        int numComment = BlogsContext.getInstance().getCommentDao().getCommentCount();
        // 用户数
        int numUser = userDao.getUserCount();
        // 标签数
        int numTag = BlogsContext.getInstance().getTagDao().getTagCount();
        map.put("article", numArticle);
        map.put("comment", numComment);
        map.put("user", numUser);
        map.put("tag", numTag);
        return map;
    }

    // 分页查询所有用户
    public List<User> getUsers(PageInfo pi) {
        return userDao.getUsers(pi);
    }

    // 删除用户
    public int deleteUserById(int id) {
        // 查询用户名
        User u = userDao.getUserById(id);
        // 删除用户之前先删除他的评论
        BlogsContext.getInstance().getCommentDao().deleteCommentByUserId(id, u.getUserName());
        // 维护文章评论数
        BlogsContext.getInstance().getArticleDao().updateArticleCommentRelation();
        // 删除用户
        return userDao.deleteUserById(id);
    }
}
