package dao;

import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * �������ж�ѡ��������ݿ����
 *
 * @author Tedu
 */
public class OptionDao {
    // ��ѯ��������
    public Map<String, String> getOptions() {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "SELECT "
                    + "optionName,optionValue "
                    + "FROM "
                    + "blogs_option";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Map<String, String> map = new HashMap<String, String>();
            while (rs.next()) {
                map.put(rs.getString(1), rs.getString(2));
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // �޸Ĳ�������
    public int updateOption(Entry<String, String> option) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "UPDATE "
                    + "blogs_option "
                    + "SET "
                    + "optionvalue=? "
                    + "WHERE "
                    + "optionname=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, option.getValue());
            ps.setString(2, option.getKey());
            int num = ps.executeUpdate();
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
