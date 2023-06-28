package com.jiujiu;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class JdUtil {


    //完成一个搜索
    public static void getData(String key, ExcelUtil excelUtil1) {
        int startIndex = excelUtil1.getAllRowCount();
//        String key = "Swisse 男士番茄红素";

        String encode = URLUtil.encode(key);
        System.out.println(encode);
        HttpRequest get = HttpUtil.createGet("https://search.jd.com/s_new.php?keyword="+encode+"&page=1");
        get.header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36 Edg/102.0.1245.39");
        String body = get.execute().body();
        System.out.println(body);

        //解析 html 获取数据
        Document doc = Jsoup.parse(body);
        Element elementById = doc.getElementById("J_goodsList");
        StringBuilder sku = new StringBuilder();
        // gl-item
        Elements elementsByClass = elementById.getElementsByClass("gl-item");
        elementsByClass.forEach(element -> {
            String attr = element.attr("data-sku");
            sku.append(attr).append(",");

            List<String> list = new ArrayList<>();
//            // 商品图片
//            Elements img = element.getElementsByClass("p-img");
//            String src = img.get(0).getElementsByTag("img").get(0).attr("src");
//            System.out.println(src);
            // 商品名称
            Elements pName = element.getElementsByClass("p-name");
            String name = pName.get(0).getElementsByTag("em").get(0).text();
            System.out.println(name);
            // 商品店铺
            Elements pShop = element.getElementsByClass("p-shop");
            String shop = pShop.get(0).getElementsByTag("a").get(0).text();
            System.out.println(shop);
            // 商品价格
            Elements pPrice = element.getElementsByClass("p-price");
            String price = pPrice.get(0).getElementsByTag("i").get(0).text();
            System.out.println(price);
            // 商品链接
            Elements pCommit = element.getElementsByClass("p-commit");
            String href = pCommit.get(0).getElementsByTag("a").get(0).attr("href");
            System.out.println(href);
            //String[] title = {"关键词", "搜索关键词", "商品名称", "店铺名称", "商品价格", "商品销量", "商品图片", "店铺链接"};
            list.add(key);
            list.add(key);
            list.add(name);
            list.add(shop);
            list.add(price);
            list.add("");
            list.add("");
            //如果 href 是 // 开头 就改为 http
            if (href.startsWith("//")) {
                href = "http:" + href;
            }
            list.add(href);

            excelUtil1.setOneRowData(list);

//            // 商品评论数
//            Elements pCommit1 = element.getElementsByClass("p-commit");
//            String commit = pCommit1.get(0).getElementsByTag("a").get(0).attr("id");
//            System.out.println(commit);
            System.out.println("====================================");
        });
        sku.deleteCharAt(sku.length() - 1);
        // 查询出 销量 写入 表格 https://club.jd.com/comment/productCommentSummaries.action?referenceIds=68884676325
        HttpRequest get1 = HttpUtil.createGet("https://club.jd.com/comment/productCommentSummaries.action?referenceIds=" + sku);
        get.header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36 Edg/102.0.1245.39");
        String body1 = get1.execute().body();
        System.out.println(body1);
        JSONObject jsonObject = new JSONObject(body1);
        JSONArray jsonArray = jsonObject.getJSONArray("CommentsCount");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = new JSONObject(jsonArray.get(i));
            String commentCountStr = jsonObject1.getStr("CommentCountStr");
            System.out.println(commentCountStr);
            //去掉加好 和 把 万 字转换为 0000
            commentCountStr = commentCountStr.replace("+", "").replace("万", "0000");
            // 写入表格
            excelUtil1.setIntVal(Integer.parseInt(commentCountStr), startIndex++, 5);
        }
    }

    private static String taobaoEncode(String key) {
        StringBuilder stringBuilder = new StringBuilder();

        String[] split = key.split(" ");
        for (String s : split) {
            String encode = URLUtil.encode(s);
            stringBuilder.append(encode).append("+");
        }
        key = stringBuilder.substring(0, stringBuilder.length() - 1);
        return key;
    }
}
