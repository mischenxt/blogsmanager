package dao;

import entity.Link;
import util.DBUtils;
import util.PageInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理所有对链接类的数据库操作
 *
 * @author Tedu
 */
public class LinkDao {
    // 这个方法封装了rs对象往link对象里赋值的过程
    public Link mapLink(ResultSet rs) throws SQLException {
        //获得所有属性值
        Link l = null;
        int oId = rs.getInt(1);
        String linkAddress = rs.getString(2);
        String linkDescription = rs.getString(3);
        int linkOrder = rs.getInt(4);
        String linkTitle = rs.getString(5);
        //实例化一个连接对象
        l = new Link(oId, linkAddress, linkDescription, linkOrder, linkTitle);
        return l;
    }

    // 删除链接
    public int deleteLinkById(int lid) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "DELETE "
                    + "FROM "
                    + "blogs_link "
                    + "WHERE "
                    + "oid=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, lid);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // 分页查询链接列表
    public List<Link> getLinks(PageInfo pi) {
        try (Connection conn = DBUtils.getConn()) {
            // 查询符合条件的行数
            int count = getLinksCount();
            // setCount为总条数赋值，会自动计算总页数
            pi.setCount(count);
            // 防止查询超限页数
            if (pi.getPageNum() > pi.getTotal())
                pi.setPageNum(pi.getTotal());
            if (pi.getPageNum() < 1)
                pi.setPageNum(1);
            String sql = "SELECT "
                    + "oId,linkAddress,linkDescription,linkOrder,linkTitle "
                    + "FROM "
                    + "blogs_link "
                    + "ORDER BY "
                    + "linkOrder "
                    + "LIMIT ?,?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, (pi.getPageNum() - 1) * pi.getPageSize());
            ps.setInt(2, pi.getPageSize());
            ResultSet rs = ps.executeQuery();
            List<Link> list = new ArrayList<Link>();
            while (rs.next()) {
                Link l = mapLink(rs);
                list.add(l);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    //查询链接总数
    private int getLinksCount() {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "SELECT "
                    + "COUNT(1) "
                    + "FROM "
                    + "blogs_link";
            // 执行查询总条数
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            int num = 0;
            while (rs.next()) {
                num = rs.getInt(1);
            }
            return num;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // 新增链接
    public int addLink(Link l) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "INSERT "
                    + "INTO "
                    + "blogs_link "
                    + "VALUES"
                    + "(NULL,?,?,?,?)";
            //获得新链接的linkorder排序
            int linkOrder = getLinkOrder();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, l.getLinkAddress());
            ps.setString(2, l.getLinkDescription());
            ps.setInt(3, linkOrder);
            ps.setString(4, l.getLinkTitle());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    //获得新的linkOrder
    private int getLinkOrder() {
        try (Connection conn = DBUtils.getConn()) {
            String sqlid = "SELECT "
                    + "MAX(linkorder)+1 "
                    + "FROM "
                    + "blogs_link";
            PreparedStatement ps = conn.prepareStatement(sqlid);
            ResultSet rs = ps.executeQuery();
            int order = 1;
            if (rs.next())
                order = rs.getInt(1);
            return order;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
