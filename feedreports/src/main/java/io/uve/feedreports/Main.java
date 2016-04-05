package io.uve.feedreports;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import io.uve.mybatis.output.MyBatisConnectionFactory;
import io.uve.mybatis.output.ReportTarget;
import io.uve.mybatis.output.ReportTargetDAO;

public class Main {

	private static DateGenerator dateGenerate = Enum.valueOf(DateGenerator.class, "REQUIREDDATE");
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://*******/******";
	private static String usr = "********";
	private static String pwd = "********";

	public static void main(String[] args) {

		String date = dateGenerate.getDate();
		Connection con = null;
		Statement sql;

		 String[] platforms = {"all", "iphone", "android", "ipad"};
//		String[] platforms = { "all", "iphone" };
		 String[] prs = {"Bidfeed", "Aim", "Brand", "Apploft", "FanstopExtend", "AddFans", "TopFans", "WAX", "bo", "PromoteFans"};
//		String[] prs = { "Bidfeed" };
		int requests = 0;
		int uv = 0;
		int imps = 0;
		int imp_uv = 0;
		int imp_groups = 0;
		int inventory = 0;
		long unread_status = 0;

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			return;
		}

		for (String platform : platforms) {
			if (!"all".equals(platform)) {
				for (String pr : prs) {
					try {
						con = DriverManager.getConnection(url, usr, pwd);
						sql = con.createStatement();
						String getRequests = "select sum(pv) from daily_product_pv_report where `service_name`=\"main_feed\" and `date`=\""
								+ date + "\" and `platform`=\"" + platform + "\" and `pr`=\"" + pr + "\"";
						String getUv = "select sum(uv) from daily_product_pv_report where `service_name`=\"main_feed\" and `date`=\""
								+ date + "\" and `platform`=\"" + platform + "\" and `pr`=\"" + pr + "\"";
						String getImps = "select sum(pv) from uve_daily_report_test where `service_name`=\"main_feed\" and `hc`=\"1\" and `date`=\""
								+ date + "\" and `platform`=\"" + platform + "\" and `pr` like \"%" + pr + "%\"";
						String getImps_uv = "select sum(uv) from uve_daily_report_test where `service_name`=\"main_feed\" and `hc`=\"1\" and `date`=\""
								+ date + "\" and `platform`=\"" + platform + "\" and `pr` like \"%" + pr + "%\"";
						String getImp_groups = "select sum(impression_pv) from uve_daily_report_impression where `service_name`=\"main_feed\" and `date`=\""
								+ date + "\" and `platform`=\"" + platform + "\" and `pr`=\"" + pr + "\"";
						String getInventory = "select sum(available_pos_sum) from uve_daily_report where `service_name`=\"main_feed\" and `date`=\""
								+ date + "\" and `platform`=\"" + platform + "\"";
						String getUnread_status = "select sum(unread_status_sum) from uve_daily_report where `service_name`=\"main_feed\" and `date`=\""
								+ date + "\" and `platform`=\"" + platform + "\"";

						System.out.println("Requests:      " + getRequests);
						System.out.println("uv:            " + getUv);
						System.out.println("Imps:          " + getImps);
						System.out.println("Imps_uv:       " + getImps_uv);
						System.out.println("Imp_groups:    " + getImp_groups);
						System.out.println("Inventory:     " + getInventory);
						System.out.println("Unread_status: " + getUnread_status);

						ResultSet rs1 = sql.executeQuery(getRequests);
						while (rs1.next()) {
							requests = rs1.getInt(1);
						}

						ResultSet rs2 = sql.executeQuery(getUv);
						while (rs2.next()) {
							uv = rs2.getInt(1);
						}

						ResultSet rs3 = sql.executeQuery(getImps);
						while (rs3.next()) {
							imps = rs3.getInt(1);
						}

						ResultSet rs4 = sql.executeQuery(getImps_uv);
						while (rs4.next()) {
							imp_uv = rs4.getInt(1);
						}

						ResultSet rs5 = sql.executeQuery(getImp_groups);
						while (rs5.next()) {
							imp_groups = rs5.getInt(1);
						}

						ResultSet rs6 = sql.executeQuery(getInventory);
						while (rs6.next()) {
							inventory = rs6.getInt(1);
						}

						ResultSet rs7 = sql.executeQuery(getUnread_status);
						while (rs7.next()) {
							unread_status = rs7.getLong(1);
						}

						System.out.println("Requests: " + requests);
						System.out.println("uv: " + uv);
						System.out.println("imps: " + imps);
						System.out.println("imp_uv: " + imp_uv);
						System.out.println("imp_groups: " + imp_groups);
						System.out.println("inventory: " + inventory);
						System.out.println("unread_status: " + unread_status);
						
						ReportTarget feed_daily_data = new ReportTarget();
						 
						feed_daily_data.setDate(date); 
						feed_daily_data.setPlatform(platform);
						feed_daily_data.setPr(pr); 
						feed_daily_data.setRequests(requests);
						feed_daily_data.setUv(uv); 
						feed_daily_data.setImps(imps);
						feed_daily_data.setImp_uv(imp_uv);
						feed_daily_data.setImp_groups(imp_groups);
						feed_daily_data.setInventory(inventory);
						feed_daily_data.setUnread_status(unread_status);
						 
						ReportTargetDAO readTimeDAO = new ReportTargetDAO(
						MyBatisConnectionFactory.getSqlSessionFactory());
						 readTimeDAO.replace(feed_daily_data);
						con.close();
					} catch (SQLException e) {
						System.out.println("SQL Exception occurs!");
						return;
					}
				}

				// 计算当pr为all时的统计，即不考虑产品线的情况
				try {
					String pr = "all";
					con = DriverManager.getConnection(url, usr, pwd);
					sql = con.createStatement();
					String getRequests2 = "select sum(pv) from uve_daily_report where `service_name`=\"main_feed\" and `date`=\""
							+ date + "\" and `platform`=\"" + platform + "\"";
					String getUv2 = "select sum(uv) from uve_daily_report where `service_name`=\"main_feed\" and `date`=\""
							+ date + "\" and `platform`=\"" + platform + "\"";
					String getImps2 = "select sum(pv) from uve_daily_report_test where `service_name`=\"main_feed\" and `hc`=\"1\" and `date`=\""
							+ date + "\" and `platform`=\"" + platform + "\"";
					String getImps_uv2 = "select sum(uv) from uve_daily_report_test where `service_name`=\"main_feed\" and `hc`=\"1\" and `date`=\""
							+ date + "\" and `platform`=\"" + platform + "\"";
					String getImp_groups2 = "select sum(impression_pv) from uve_daily_report_impression where `service_name`=\"main_feed\" and `date`=\""
							+ date + "\" and `platform`=\"" + platform + "\"";
					String getInventory2 = "select sum(available_pos_sum) from uve_daily_report where `service_name`=\"main_feed\" and `date`=\""
							+ date + "\" and `platform`=\"" + platform + "\"";
					String getUnread_status2 = "select sum(unread_status_sum) from uve_daily_report where `service_name`=\"main_feed\" and `date`=\""
							+ date + "\" and `platform`=\"" + platform + "\"";

					System.out.println("Requests2:      " + getRequests2);
					System.out.println("uv2:            " + getUv2);
					System.out.println("Imps2:          " + getImps2);
					System.out.println("Imps_uv2:       " + getImps_uv2);
					System.out.println("Imp_groups2:    " + getImp_groups2);
					System.out.println("Inventory2:     " + getInventory2);
					System.out.println("Unread_status2: " + getUnread_status2);

					ResultSet rs1 = sql.executeQuery(getRequests2);
					while (rs1.next()) {
						requests = rs1.getInt(1);
					}

					ResultSet rs2 = sql.executeQuery(getUv2);
					while (rs2.next()) {
						uv = rs2.getInt(1);
					}

					ResultSet rs3 = sql.executeQuery(getImps2);
					while (rs3.next()) {
						imps = rs3.getInt(1);
					}

					ResultSet rs4 = sql.executeQuery(getImps_uv2);
					while (rs4.next()) {
						imp_uv = rs4.getInt(1);
					}

					ResultSet rs5 = sql.executeQuery(getImp_groups2);
					while (rs5.next()) {
						imp_groups = rs5.getInt(1);
					}

					ResultSet rs6 = sql.executeQuery(getInventory2);
					while (rs6.next()) {
						inventory = rs6.getInt(1);
					}

					ResultSet rs7 = sql.executeQuery(getUnread_status2);
					while (rs7.next()) {
						unread_status = rs7.getLong(1);
					}

					System.out.println("Requests2: " + requests);
					System.out.println("uv2: " + uv);
					System.out.println("imps2: " + imps);
					System.out.println("imp_uv2: " + imp_uv);
					System.out.println("imp_groups2: " + imp_groups);
					System.out.println("inventory2: " + inventory);
					System.out.println("unread_status2: " + unread_status);
					
					ReportTarget feed_daily_data = new ReportTarget();
					 
					feed_daily_data.setDate(date); 
					feed_daily_data.setPlatform(platform);
					feed_daily_data.setPr(pr); 
					feed_daily_data.setRequests(requests);
					feed_daily_data.setUv(uv); 
					feed_daily_data.setImps(imps);
					feed_daily_data.setImp_uv(imp_uv);
					feed_daily_data.setImp_groups(imp_groups);
					feed_daily_data.setInventory(inventory);
					feed_daily_data.setUnread_status(unread_status);
					 
					ReportTargetDAO readTimeDAO = new ReportTargetDAO(
					MyBatisConnectionFactory.getSqlSessionFactory());
					 readTimeDAO.replace(feed_daily_data);
					con.close();
				} catch (SQLException e) {
					System.out.println("SQL Exception occurs!");
					return;
				}
			} else {
				//platform is all, meaning that all platform
				for (String pr : prs) {
					try {
						con = DriverManager.getConnection(url, usr, pwd);
						sql = con.createStatement();
						String getRequests = "select sum(pv) from daily_product_pv_report where `service_name`=\"main_feed\" and `date`=\""
								+ date + "\" and `pr`=\"" + pr + "\"";
						String getUv = "select sum(uv) from daily_product_pv_report where `service_name`=\"main_feed\" and `date`=\""
								+ date + "\" and `pr`=\"" + pr + "\"";
						String getImps = "select sum(pv) from uve_daily_report_test where `service_name`=\"main_feed\" and `hc`=\"1\" and `date`=\""
								+ date + "\" and `pr` like \"%" + pr + "%\"";
						String getImps_uv = "select sum(uv) from uve_daily_report_test where `service_name`=\"main_feed\" and `hc`=\"1\" and `date`=\""
								+ date + "\" and `pr` like \"%" + pr + "%\"";
						String getImp_groups = "select sum(impression_pv) from uve_daily_report_impression where `service_name`=\"main_feed\" and `date`=\""
								+ date + "\" and `pr`=\"" + pr + "\"";
						String getInventory = "select sum(available_pos_sum) from uve_daily_report where `service_name`=\"main_feed\" and `date`=\""
								+ date + "\"";
						String getUnread_status = "select sum(unread_status_sum) from uve_daily_report where `service_name`=\"main_feed\" and `date`=\""
								+ date + "\"";

						System.out.println("Requests:      " + getRequests);
						System.out.println("uv:            " + getUv);
						System.out.println("Imps:          " + getImps);
						System.out.println("Imps_uv:       " + getImps_uv);
						System.out.println("Imp_groups:    " + getImp_groups);
						System.out.println("Inventory:     " + getInventory);
						System.out.println("Unread_status: " + getUnread_status);

						ResultSet rs1 = sql.executeQuery(getRequests);
						while (rs1.next()) {
							requests = rs1.getInt(1);
						}

						ResultSet rs2 = sql.executeQuery(getUv);
						while (rs2.next()) {
							uv = rs2.getInt(1);
						}

						ResultSet rs3 = sql.executeQuery(getImps);
						while (rs3.next()) {
							imps = rs3.getInt(1);
						}

						ResultSet rs4 = sql.executeQuery(getImps_uv);
						while (rs4.next()) {
							imp_uv = rs4.getInt(1);
						}

						ResultSet rs5 = sql.executeQuery(getImp_groups);
						while (rs5.next()) {
							imp_groups = rs5.getInt(1);
						}

						ResultSet rs6 = sql.executeQuery(getInventory);
						while (rs6.next()) {
							inventory = rs6.getInt(1);
						}

						ResultSet rs7 = sql.executeQuery(getUnread_status);
						while (rs7.next()) {
							unread_status = rs7.getLong(1);
						}

						System.out.println("Requests: " + requests);
						System.out.println("uv: " + uv);
						System.out.println("imps: " + imps);
						System.out.println("imp_uv: " + imp_uv);
						System.out.println("imp_groups: " + imp_groups);
						System.out.println("inventory: " + inventory);
						System.out.println("unread_status: " + unread_status);
						
						ReportTarget feed_daily_data = new ReportTarget();
						 
						feed_daily_data.setDate(date); 
						feed_daily_data.setPlatform(platform);
						feed_daily_data.setPr(pr); 
						feed_daily_data.setRequests(requests);
						feed_daily_data.setUv(uv); 
						feed_daily_data.setImps(imps);
						feed_daily_data.setImp_uv(imp_uv);
						feed_daily_data.setImp_groups(imp_groups);
						feed_daily_data.setInventory(inventory);
						feed_daily_data.setUnread_status(unread_status);
						 
						ReportTargetDAO readTimeDAO = new ReportTargetDAO(
						MyBatisConnectionFactory.getSqlSessionFactory());
						 readTimeDAO.replace(feed_daily_data);
						con.close();
					} catch (SQLException e) {
						System.out.println("SQL Exception occurs!");
						return;
					}
				}

				// 计算当pr为all时的统计，即不考虑产品线的情况
				try {
					String pr = "all";
					con = DriverManager.getConnection(url, usr, pwd);
					sql = con.createStatement();
					String getRequests2 = "select sum(pv) from uve_daily_report where `service_name`=\"main_feed\" and `date`=\""
							+ date + "\"";
					String getUv2 = "select sum(uv) from uve_daily_report where `service_name`=\"main_feed\" and `date`=\""
							+ date + "\"";
					String getImps2 = "select sum(pv) from uve_daily_report_test where `service_name`=\"main_feed\" and `hc`=\"1\" and `date`=\""
							+ date + "\"";
					String getImps_uv2 = "select sum(uv) from uve_daily_report_test where `service_name`=\"main_feed\" and `hc`=\"1\" and `date`=\""
							+ date + "\"";
					String getImp_groups2 = "select sum(impression_pv) from uve_daily_report_impression where `service_name`=\"main_feed\" and `date`=\""
							+ date + "\"";
					String getInventory2 = "select sum(available_pos_sum) from uve_daily_report where `service_name`=\"main_feed\" and `date`=\""
							+ date + "\"";
					String getUnread_status2 = "select sum(unread_status_sum) from uve_daily_report where `service_name`=\"main_feed\" and `date`=\""
							+ date + "\"";

					System.out.println("Requests2:      " + getRequests2);
					System.out.println("uv2:            " + getUv2);
					System.out.println("Imps2:          " + getImps2);
					System.out.println("Imps_uv2:       " + getImps_uv2);
					System.out.println("Imp_groups2:    " + getImp_groups2);
					System.out.println("Inventory2:     " + getInventory2);
					System.out.println("Unread_status2: " + getUnread_status2);

					ResultSet rs1 = sql.executeQuery(getRequests2);
					while (rs1.next()) {
						requests = rs1.getInt(1);
					}

					ResultSet rs2 = sql.executeQuery(getUv2);
					while (rs2.next()) {
						uv = rs2.getInt(1);
					}

					ResultSet rs3 = sql.executeQuery(getImps2);
					while (rs3.next()) {
						imps = rs3.getInt(1);
					}

					ResultSet rs4 = sql.executeQuery(getImps_uv2);
					while (rs4.next()) {
						imp_uv = rs4.getInt(1);
					}

					ResultSet rs5 = sql.executeQuery(getImp_groups2);
					while (rs5.next()) {
						imp_groups = rs5.getInt(1);
					}

					ResultSet rs6 = sql.executeQuery(getInventory2);
					while (rs6.next()) {
						inventory = rs6.getInt(1);
					}

					ResultSet rs7 = sql.executeQuery(getUnread_status2);
					while (rs7.next()) {
						unread_status = rs7.getLong(1);
					}

					System.out.println("Requests2: " + requests);
					System.out.println("uv2: " + uv);
					System.out.println("imps2: " + imps);
					System.out.println("imp_uv2: " + imp_uv);
					System.out.println("imp_groups2: " + imp_groups);
					System.out.println("inventory2: " + inventory);
					System.out.println("unread_status2: " + unread_status);
					
					ReportTarget feed_daily_data = new ReportTarget();
					 
					feed_daily_data.setDate(date); 
					feed_daily_data.setPlatform(platform);
					feed_daily_data.setPr(pr); 
					feed_daily_data.setRequests(requests);
					feed_daily_data.setUv(uv); 
					feed_daily_data.setImps(imps);
					feed_daily_data.setImp_uv(imp_uv);
					feed_daily_data.setImp_groups(imp_groups);
					feed_daily_data.setInventory(inventory);
					feed_daily_data.setUnread_status(unread_status);
					 
					ReportTargetDAO readTimeDAO = new ReportTargetDAO(
					MyBatisConnectionFactory.getSqlSessionFactory());
					 readTimeDAO.replace(feed_daily_data);
					con.close();
				} catch (SQLException e) {
					System.out.println("SQL Exception occurs!");
					return;
				}
			}
		}
	}
}
