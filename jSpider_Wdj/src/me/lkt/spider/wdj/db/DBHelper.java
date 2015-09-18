package me.lkt.spider.wdj.db;

import java.sql.Connection;
import java.sql.DriverManager;
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
		return false;
	}

	public void insertAppInfo(AppInfo ai) {
	}

}
