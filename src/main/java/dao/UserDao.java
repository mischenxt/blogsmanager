package dao;

import entity.User;
import util.DBUtils;
import util.PageInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * �������ж��û�������ݿ����
 *
 * @author Tedu
 */
public class UserDao {
    // ���������װ��rs������user�����︳ֵ�Ĺ���
    public User mapUser(ResultSet rs) throws SQLException {
        User u = null;
        int oId = rs.getInt(1);
        String userEmail = rs.getString(2);
        String userName = rs.getString(3);
        String userURL = rs.getString(4);
        String userPassword = rs.getString(5);
        String userRole = rs.getString(6);
        int userArticleCount = rs.getInt(7);
        int userPublishedArticleCount = rs.getInt(8);
        String userAvatar = rs.getString(9);
        u = new User(oId, userEmail, userName, userURL, userPassword, userRole, userArticleCount, userPublishedArticleCount, userAvatar);
        return u;
    }

    // �û��������¼����������Ա��¼��
    public User login(String username, String pwd) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "SELECT "
                    + "oId,userEmail,userName,userURL,"
                    + "userPassword,userRole,userArticleCount,"
                    + "userPublishedArticleCount,userAvatar "
                    + "FROM "
                    + "blogs_user "
                    + "WHERE "
                    + "username=? "
                    + "AND  "
                    + "userpassword=? "
                    + "AND "
                    + "userrole='adminrole'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, pwd);
            ResultSet rs = ps.executeQuery();
            User u = null;
            while (rs.next()) {
                u = mapUser(rs);
            }
            return u;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ����ǲ��ǿ�ͨ��΢���ķ���
    // ������ݿ���û��userrole='adminRole'�����ݷ��ؼ�
    public boolean chekcAdminRole() {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "SELECT "
                    + "1 "
                    + "FROM "
                    + "blogs_user "
                    + "WHERE "
                    + "userrole='adminRole'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            boolean b = false;
            while (rs.next()) {
                b = true;
            }
            return b;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ע���û��ķ���������Աע�ᣩ
    public int registerUser(User u) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "INSERT INTO "
                    + "blogs_user "
                    + "VALUES"
                    + "(NULL,?,?,'',?,?,0,0,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, u.getUserEmail());
            ps.setString(2, u.getUserName());
            ps.setString(3, u.getUserPassword());
            ps.setString(4, u.getUserRole());
            ps.setString(5, u.getUserAvatar());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ����û���
    public int getUserCount() {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "SELECT "
                    + "COUNT(1) "
                    + "FROM "
                    + "blogs_user";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int num = 0;
            while (rs.next()) {
                num = rs.getInt(1);
            }
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ��ҳ��ѯ�û��б�
    public List<User> getUsers(PageInfo pi) {
        try (Connection conn = DBUtils.getConn()) {
            // ִ�в�ѯ������
            int count = getUserCount();
            // setCountΪ��������ֵ�����Զ�������ҳ��
            pi.setCount(count);
            // ��ֹ��ѯ����ҳ��
            if (pi.getPageNum() > pi.getTotal())
                pi.setPageNum(pi.getTotal());
            if (pi.getPageNum() < 1)
                pi.setPageNum(1);
            String sql = "SELECT "
                    + "oId,userEmail,userName,userURL,"
                    + "userPassword,userRole,userArticleCount,"
                    + "userPublishedArticleCount,userAvatar "
                    + "FROM "
                    + "blogs_user "
                    + "LIMIT ?,?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, (pi.getPageNum() - 1) * pi.getPageSize());
            ps.setInt(2, pi.getPageSize());
            ResultSet rs = ps.executeQuery();
            List<User> list = new ArrayList<User>();
            while (rs.next()) {
                User u = mapUser(rs);
                list.add(u);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ɾ���û�
    public int deleteUserById(int id) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "DELETE "
                    + "FROM "
                    + "blogs_user "
                    + "WHERE "
                    + "oid=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ��id��ѯ�û�
    public User getUserById(int id) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "SELECT "
                    + "oId,userEmail,userName,userURL,"
                    + "userPassword,userRole,userArticleCount,"
                    + "userPublishedArticleCount,userAvatar "
                    + "FROM "
                    + "blogs_user "
                    + "WHERE "
                    + "oid=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            User u = null;
            while (rs.next()) {
                u = mapUser(rs);
            }
            return u;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
