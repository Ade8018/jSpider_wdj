package me.lkt.spider.wdj;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import me.lkt.spider.wdj.db.DBHelper;
import me.lkt.spider.wdj.entity.AppInfo;
import me.lkt.spider.wdj.util.WdjAppHtmlProcessor;

public class Main {
	public static final String WDJ_WEB_ROOT = "http://www.wandoujia.com";
	public static final String WDJ_ENTRY_METHOD_APP = "/apps/com.android.dazhihui";
	private static DBHelper dbhelper;
	private static Queue<String> mMethods = new LinkedList<String>();

	public static void main(String[] args) {
		dbhelper = new DBHelper();
		WdjAppHtmlProcessor processor = new WdjAppHtmlProcessor();
		mMethods.offer(WDJ_ENTRY_METHOD_APP);
		while (mMethods.size() > 0) {
			processor.process(WDJ_WEB_ROOT + mMethods.poll());
			AppInfo ai = processor.getAppinfo();
			dbhelper.insertAppInfo(ai);
			List<String> methods = processor.getHrefs();
			if (methods != null) {
				for (int i = 0; i < methods.size(); i++) {
					String meth = methods.get(i);
					if (meth != null) {
						String pkgName = meth
								.substring(meth.lastIndexOf("/") + 1);
						if (!dbhelper.packageExists(pkgName)) {
							mMethods.offer(meth);
						}
					}
				}
			}
		}

		if (dbhelper != null) {
			dbhelper.close();
		}

	}
}
