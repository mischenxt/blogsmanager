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
 * 处理所有对用户类的数据库操作
 *
 * @author Tedu
 */
public class UserDao {
    // 这个方法封装了rs对象往user对象里赋值的过程
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

    // 用户名密码登录方法（管理员登录）
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

    // 检查是不是开通的微博的方法
    // 如果数据库中没有userrole='adminRole'的数据返回假
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

    // 注册用户的方法（管理员注册）
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

    // 获得用户数
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

    // 分页查询用户列表
    public List<User> getUsers(PageInfo pi) {
        try (Connection conn = DBUtils.getConn()) {
            // 执行查询总条数
            int count = getUserCount();
            // setCount为总条数赋值，会自动计算总页数
            pi.setCount(count);
            // 防止查询超限页数
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

    // 删除用户
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

    // 按id查询用户
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
