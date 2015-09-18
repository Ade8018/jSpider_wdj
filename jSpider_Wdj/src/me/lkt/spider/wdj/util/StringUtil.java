package me.lkt.spider.wdj.util;

public class StringUtil {
	public static String clearUnexpected(String ori){
		if (ori!=null) {
			ori.replace("\n", "");
			ori.replace(" ", "");
			return ori.trim();
		}
		return null;
	}
}
