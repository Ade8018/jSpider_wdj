package me.lkt.spider.wdj.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.lkt.spider.wdj.entity.AppInfo;

public class DBHelper {
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Connection mConn = null;
	private Statement mStam = null;

	public DBHelper() {
		try {
			mConn = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/apps_data?"
							+ "user=root&password=123123&useUnicode=true&characterEncoding=UTF-8");
			if (mConn != null) {
				mStam = mConn.createStatement();
			}
			String sql = "CREATE TABLE IF NOT EXISTS wdj_apps("
					+ "'_id' INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "'app_name' TEXT," + "'download_count' TEXT,"
					+ "'size' TEXT," + "'category' TEXT,"
					+ "'update_time' TEXT," + "'version' TEXT,"
					+ "'request' TEXT," + "'package_name' TEXT,"
					+ "'website' TEXT," + "'from' TEXT)";
			execSql(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void execSql(String sql) {
		if (mStam != null) {
			try {
				mStam.execute(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		if (mStam != null) {
			try {
				mStam.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (mConn != null) {
			try {
				mConn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean packageExists(String pkgName) {
		boolean result = false;
		if (pkgName != null) {
			String sql = "select * from wdj_apps where package_name = '"
					+ pkgName + "'";
			try {
				ResultSet set = mStam.executeQuery(sql);
				if (set != null && set.first()) {
					result = true;
				}
				if (set != null) {
					set.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	public void insertAppInfo(AppInfo ai) {
		if (ai == null) {
			return;
		}
		String sql = "INSERT INTO wdj_apps(app_name,download_count,size,category,update_time,version,request,package_name,website,from) "
				+ "values("
				+ ai.getApp_name()
				+ ","
				+ ai.getDownload_count()
				+ ","
				+ ai.getSize()
				+ ","
				+ ai.getCategory()
				+ ","
				+ ai.getUpdate_time()
				+ ","
				+ ai.getVersion()
				+ ","
				+ ai.getRequest()
				+ ","
				+ ai.getPackage_name()
				+ ","
				+ ai.getWebsite() + "," + ai.getFrom() + ")";
		try {
			mStam.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
