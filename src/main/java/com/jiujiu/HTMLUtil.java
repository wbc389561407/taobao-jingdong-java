package com.jiujiu;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLUtil {

    // 通过url获取document
    public static Document getDocumentByURL(String url) {
        HttpRequest get = HttpUtil.createGet(url);
        get.header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36 Edg/102.0.1245.39");
        String body = get.execute().body();
        Document doc = Jsoup.parse(body);
        return doc;
    }

}
