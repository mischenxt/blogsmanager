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
 * �������ж�����������ݿ����
 *
 * @author Tedu
 */
public class LinkDao {
    // ���������װ��rs������link�����︳ֵ�Ĺ���
    public Link mapLink(ResultSet rs) throws SQLException {
        //�����������ֵ
        Link l = null;
        int oId = rs.getInt(1);
        String linkAddress = rs.getString(2);
        String linkDescription = rs.getString(3);
        int linkOrder = rs.getInt(4);
        String linkTitle = rs.getString(5);
        //ʵ����һ�����Ӷ���
        l = new Link(oId, linkAddress, linkDescription, linkOrder, linkTitle);
        return l;
    }

    // ɾ������
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

    // ��ҳ��ѯ�����б�
    public List<Link> getLinks(PageInfo pi) {
        try (Connection conn = DBUtils.getConn()) {
            // ��ѯ��������������
            int count = getLinksCount();
            // setCountΪ��������ֵ�����Զ�������ҳ��
            pi.setCount(count);
            // ��ֹ��ѯ����ҳ��
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

    //��ѯ��������
    private int getLinksCount() {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "SELECT "
                    + "COUNT(1) "
                    + "FROM "
                    + "blogs_link";
            // ִ�в�ѯ������
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

    // ��������
    public int addLink(Link l) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "INSERT "
                    + "INTO "
                    + "blogs_link "
                    + "VALUES"
                    + "(NULL,?,?,?,?)";
            //��������ӵ�linkorder����
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

    //����µ�linkOrder
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
