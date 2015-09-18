package me.lkt.spider.wdj.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import me.lkt.spider.wdj.entity.AppInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

public class WdjAppHtmlProcessor {
	private AppInfo mAppinfo;
	private List<String> mHrefs;

	public WdjAppHtmlProcessor() {
		mAppinfo = new AppInfo();
		mHrefs = new ArrayList<String>();
	}

	private void process(Document doc) {
		Node nodeHtml = doc.childNode(1);
		Node nodeBody = getChildNodeWithTagAndClass(nodeHtml, "body", null, 0);
		Node nodeDiv_classContainer = getChildNodeWithTagAndClass(nodeBody,
				"div", "container", 0);
		Node nodeDiv_classDetailWrap = getChildNodeWithTagAndClass(
				nodeDiv_classContainer, "div", "detail-wrap ", 0);
		Node nodeDiv_classDetailTop = getChildNodeWithTagAndClass(
				nodeDiv_classDetailWrap, "div", "detail-top clearfix", 0);
		Node nodeDiv_classAppInfo = getChildNodeWithTagAndClass(
				nodeDiv_classDetailTop, "div", "app-info", 0);
		Node nodeP_classAppName = getChildNodeWithTagAndClass(
				nodeDiv_classAppInfo, "h1", "title", 0);
		String appName = nodeP_classAppName.childNode(0).toString();
		appName = StringUtil.clearUnexpected(appName);
		mAppinfo.setApp_name(appName);

		Node nodeDiv_classNumList = getChildNodeWithTagAndClass(
				nodeDiv_classDetailTop, "div", "num-list", 0);
		Node nodeSpan_classItem = getChildNodeWithTagAndClass(
				nodeDiv_classNumList, "span", "item", 0);
		Node nodeI_ItemPropInteractionCount = getChildNodeWithTagAndClass(
				nodeSpan_classItem, "i", null, 0);
		String strCount = nodeI_ItemPropInteractionCount.childNode(0)
				.toString();
		strCount = StringUtil.clearUnexpected(strCount);
		mAppinfo.setDownload_count(strCount);

		Node nodeDiv_classColsClearFix = getChildNodeWithTagAndClass(
				nodeDiv_classDetailWrap, "div", "cols clearfix", 0);
		Node nodeDiv_classColRight = getChildNodeWithTagAndClass(
				nodeDiv_classColsClearFix, "div", "col-right", 0);
		Node nodeDiv_classInfos = getChildNodeWithTagAndClass(
				nodeDiv_classColRight, "div", "infos", 0);
		Node nodeDL_classInfosList = getChildNodeWithTagAndClass(
				nodeDiv_classInfos, "dl", "infos-list", 0);
		Node nodeSize = nodeDL_classInfosList.childNode(3);
		String strSize = nodeSize.childNode(0).toString();
		strSize = StringUtil.clearUnexpected(strSize);
		mAppinfo.setSize(strSize);

		Node nodeCate = nodeDL_classInfosList.childNode(7);
		String strCate = nodeCate.childNode(1).childNode(0).toString();
		strCate = StringUtil.clearUnexpected(strCate);
		mAppinfo.setCategory(strCate);

		Node nodeUpdateTime = nodeDL_classInfosList.childNode(11);
		String strUpdateTime = nodeUpdateTime.childNode(1).childNode(0)
				.toString();
		strUpdateTime = StringUtil.clearUnexpected(strUpdateTime);
		mAppinfo.setUpdate_time(strUpdateTime);

		Node nodeVersion = nodeDL_classInfosList.childNode(15);
		String strVersion = nodeVersion.childNode(0).toString();
		strVersion = StringUtil.clearUnexpected(strVersion);
		mAppinfo.setVersion(strVersion);

		Node nodeRequest = nodeDL_classInfosList.childNode(19);
		String strRequest = nodeRequest.childNode(0).toString();
		strRequest = StringUtil.clearUnexpected(strRequest);
		mAppinfo.setRequest(strRequest);

		Node nodePkgName = nodeDL_classInfosList.childNode(21);
		String strPkgName = nodePkgName.childNode(3).childNode(0).toString();
		strPkgName = StringUtil.clearUnexpected(strPkgName);
		mAppinfo.setPackage_name(strPkgName);

		Node nodeWebSite = nodeDL_classInfosList.childNode(25);
		String strWebSite = nodeWebSite.childNode(3).childNode(1).childNode(0)
				.toString();
		strWebSite = StringUtil.clearUnexpected(strWebSite);
		mAppinfo.setWebsite(strWebSite);

		Node nodeFrom = nodeDL_classInfosList.childNode(29);
		String strFrom = nodeFrom.childNode(0).toString();
		strFrom = StringUtil.clearUnexpected(strFrom);
		mAppinfo.setFrom(strFrom);

		// ul class="side-list"
		Node nodeUl_classSideListRela = getChildNodeWithTagAndClass(
				nodeDiv_classInfos, "ul", "side-list", 0);
		List<Node> nodes = nodeUl_classSideListRela.childNodes();
		for (Node node : nodes) {
			if (node.childNodeSize() > 1) {
				Node child = node.childNode(1);
				if (child != null && child.hasAttr("href")) {
					mHrefs.add(child.attr("href"));
				}
			}
		}
		Node nodeRl_classSideListHot = getChildNodeWithTagAndClass(
				nodeDiv_classInfos, "ul", "side-list", 1);
		nodes = nodeRl_classSideListHot.childNodes();
		for (Node node : nodes) {
			if (node.childNodeSize() > 1) {
				Node child = node.childNode(1);
				if (child != null && child.hasAttr("href")) {
					mHrefs.add(child.attr("href"));
				}
			}
		}
	}

	private static Node getChildNodeWithTagAndClass(Node nodeHtml, String tag,
			String clsValue, int index) {
		int i = 0;
		List<Node> nodes = nodeHtml.childNodes();
		for (Node node : nodes) {
			String nodename = node.nodeName();
			if (tag != null) {
				if (nodename != null && nodename.equals(tag)) {
					if (clsValue != null) {
						String clsV = node.attr("class");
						if (clsValue.equals(clsV)) {
							if (i == index)
								return node;
							else
								i++;
						}
					} else {
						if (i == index)
							return node;
						else
							i++;
					}
				}
			} else if (clsValue != null) {
				String clsV = node.attr("class");
				if (clsValue.equals(clsV)) {
					if (i == index)
						return node;
					else
						i++;
				}
			}
		}
		return null;
	}

	public void process(String url) {
		mAppinfo = new AppInfo();
		mHrefs = new ArrayList<String>();
		String html = getUrlHtml(url);
		if (html != null) {
			process(Jsoup.parse(html));
		}
	}

	private String getUrlHtml(String url) {
		String html = null;
		if (url != null) {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			get.setHeader(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; InfoPath.3; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET4.0C; .NET4.0E)");
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 5000);
			HttpConnectionParams.setSoTimeout(params, 5000);
			InputStream is = null;
			BufferedReader br = null;
			try {
				HttpResponse resp = client.execute(get);
				if (resp != null && resp.getStatusLine() != null
						&& resp.getStatusLine().getStatusCode() == 200) {
					HttpEntity ent = resp.getEntity();
					if (ent != null) {
						is = ent.getContent();
						br = new BufferedReader(new InputStreamReader(is,
								"UTF-8"));
						html = br.readLine();
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return html;
	}

	public AppInfo getAppinfo() {
		return mAppinfo;
	}

	public List<String> getHrefs() {
		return mHrefs;
	}

}
