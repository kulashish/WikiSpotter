/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.ac.iitb.wikispotter.trie;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mysql.jdbc.Driver;

/**
 * 
 * @author pararth
 */
public class QueryResults {
	public static Connection conn = null;
	public static PreparedStatement ps = null;
	public static PreparedStatement ps1 = null;
	public static ResultSet rs = null;
	public static ResultSet rs1 = null;

	public static int get_result_mapping() {
		try {
			ps1 = conn
					.prepareStatement("select temp_id_mapping.id A, Page.name B from "
							+ "(temp_id_mapping join temp_query_mapping on "
							+ "temp_id_mapping.id = temp_query_mapping.id) join Page on "
							+ "temp_id_mapping.mapping = Page.id");
			rs1 = ps1.executeQuery();
			while (rs1.next()) {

			}
		} catch (SQLException e) {
			System.err.println("AAAerror: " + e);
			return -1;
		}
		return 1;

	}

	public static int build_query_mapping(ArrayList<Integer> queries) {
		try {
			ps1 = conn
					.prepareStatement("insert into temp_query_mapping(id) values(?)");
			for (Integer q : queries) {
				ps1.setInt(1, q);
				ps1.executeUpdate();
			}

		} catch (SQLException e) {
			System.err.println("EEEerror: " + e);
			return -1;
		}
		return 1;

	}

	private static int delete_table(String s) throws IOException {
		try {
			System.out.println("Deleting from table " + s);
			ps = conn.prepareStatement("delete from " + s);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println("error: " + e);
			return -1;
		}
		return 1;
	}

	public static HashMap<Integer, ArrayList<String>> get(
			ArrayList<Integer> queries) throws IOException {
		HashMap<Integer, ArrayList<String>> ans = new HashMap<Integer, ArrayList<String>>();
		// ArrayList<String> temp = new ArrayList<String>();
		for (Integer q : queries) {
			ans.put(q, new ArrayList<String>());
		}
		try {
			// DatabaseConnector connector = DatabaseConnector.getInstance();
			// conn = connector.getConnection();
			Driver d = new Driver();
			conn = DriverManager
					.getConnection("jdbc:mysql://10.129.1.91:3306/wikispot?"
							+ "user=pararth&password=pararth123");
			delete_table("temp_query_mapping");
			build_query_mapping(queries);

			try {
				ps1 = conn
						.prepareStatement("select temp_id_mapping.id A, Page.name B from "
								+ "(temp_id_mapping join temp_query_mapping on "
								+ "temp_id_mapping.id = temp_query_mapping.id) join Page on "
								+ "temp_id_mapping.mapping = Page.id");
				rs1 = ps1.executeQuery();
				while (rs1.next()) {
					ans.get(rs1.getInt("A")).add(rs1.getString("B"));
				}
				return ans;
			} catch (SQLException e) {
				System.err.println("BBBerror: " + e);
				return null;
			} finally {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			}
		} catch (SQLException e) {
			System.err.println("CCCerror: " + e);
			return null;
		}
	}

	public static void main(String[] args) throws IOException {
		ArrayList<Integer> queries = new ArrayList<Integer>();
		for (int i = 1; i < 5; i++)
			queries.add(i);
		HashMap<Integer, ArrayList<String>> ans = get(queries);
		System.out.println("Results:" + ans.size());
	}

}
