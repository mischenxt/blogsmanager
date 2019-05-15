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

    // ��¼������Ա��
    public User login(String username, String pwd) {
        return userDao.login(username, pwd);
    }

    // ��֤�Ƿ�ע���˲��ͣ�����Ա��
    public boolean chekcAdminRole() {
        return userDao.chekcAdminRole();
    }

    // ע�Ჩ�ͣ�����Ա��
    public int registerUser(User u) {
        return userDao.registerUser(u);
    }

    // ��ò�����ҳ�ϵĸ�������
    public Map<String, Object> getMainInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        // ������
        int numArticle = BlogsContext.getInstance().getArticleDao().getArticleCount();
        // ������
        int numComment = BlogsContext.getInstance().getCommentDao().getCommentCount();
        // �û���
        int numUser = userDao.getUserCount();
        // ��ǩ��
        int numTag = BlogsContext.getInstance().getTagDao().getTagCount();
        map.put("article", numArticle);
        map.put("comment", numComment);
        map.put("user", numUser);
        map.put("tag", numTag);
        return map;
    }

    // ��ҳ��ѯ�����û�
    public List<User> getUsers(PageInfo pi) {
        return userDao.getUsers(pi);
    }

    // ɾ���û�
    public int deleteUserById(int id) {
        // ��ѯ�û���
        User u = userDao.getUserById(id);
        // ɾ���û�֮ǰ��ɾ����������
        BlogsContext.getInstance().getCommentDao().deleteCommentByUserId(id, u.getUserName());
        // ά������������
        BlogsContext.getInstance().getArticleDao().updateArticleCommentRelation();
        // ɾ���û�
        return userDao.deleteUserById(id);
    }
}
