package com.jiujiu;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class HttpTest {
    public static void main(String[] args) {
        //爬取 http://ss.manmanbuy.com/Default.aspx?key={key}&PageID=1

        String key = "蓝牙耳机";

        String encode = URLUtil.encode(key, CharsetUtil.CHARSET_GBK);

        String body = HttpUtil.get("http://ss.manmanbuy.com/Default.aspx?key="+encode+"&PageID=1");
//        System.out.println(body);
        // 处理返回的数据

        Document doc = Jsoup.parse(body);

        doc.body().getElementsByClass("bjlineSmall").forEach(element -> {
//            System.out.println(element.toString());
            element.getElementsByClass("pic").forEach(element1 -> {
                // 获取 <div class="pic">
                // <a class="shenqingGY" href="/jdDetail.aspx?originalUrl=https://item.jd.com/100032329020.html&amp;skuid=100032329020&amp;proid=3055464096" target="_blank" onclick="uploadEvent('小米Redmi Buds3青春版 真无线蓝牙耳机 入耳式耳机 蓝牙耳机 小米无线耳机 蓝牙5.2 苹果华为手机通用','1','京东商城','/jdDetail.aspx?originalUrl=https://item.jd.com/100032329020.html&amp;skuid=100032329020&amp;proid=3055464096','蓝牙耳机','99',this)"><img onerror="this.src='http://misc.manmanbuy.com/images/none.gif'" src="https://img10.360buyimg.com/n7/jfs/t1/204000/30/24880/539552/62d137ecE476792ca/0f3dfffb6faccea1.jpg"></a>
                //</div> 中 https://img10.360buyimg.com/n7/jfs/t1/204000/30/24880/539552/62d137ecE476792ca/0f3dfffb6faccea1.jpg
                String substring = element1.toString().substring(element1.toString().indexOf("src=\"") + 5, element1.toString().indexOf("\"></a>"));
                System.out.println(substring);
            });
            element.getElementsByClass("title").forEach(element1 -> {
                // 获取 <div class="title">
                // <div class="t">
                //  <a class="shenqingGY" href="/jdDetail.aspx?originalUrl=https://item.jd.com/100032329020.html&amp;skuid=100032329020&amp;proid=3055464096" target="_blank" onclick="uploadEvent('小米Redmi Buds3青春版 真无线蓝牙耳机 入耳式耳机 蓝牙耳机 小米无线耳机 蓝牙5.2 苹果华为手机通用','1','京东商城','/jdDetail.aspx?originalUrl=https://item.jd.com/100032329020.html&amp;skuid=100032329020&amp;proid=3055464096','蓝牙耳机','99',this)">小米Redmi Buds3青春版 真无线<font class="spnamehighword">蓝牙</font><font class="spnamehighword">耳机</font> 入耳式<font class="spnamehighword">耳机</font> <font class="spnamehighword">蓝牙</font><font class="spnamehighword">耳机</font> 小米无线<font class="spnamehighword">耳机</font> <font class="spnamehighword">蓝牙</font>5.2 苹果华为手机通用</a> <span> <span class="priceyouhui"></span></span>
                // </div>
                // <div class="addfav">
                //  <a v="favPrice&amp;bjid=3055464096" class="iframefav" href="#iframemain"><i></i>收藏&amp;降价通知</a>
                // </div>
                //</div>
                // 中的  小米Redmi Buds3青|春版 真无线蓝牙耳机 入耳式耳机 蓝牙耳机 小米无线耳机 蓝牙5.2 苹果华为手机通用
                String string = element1.toString();
                int start = string.indexOf("onclick=\"uploadEvent('") + 22;
                int end = string.indexOf("'",start);
                String substring = string.substring(start, end);
                System.out.println(substring);
            });
            element.getElementsByClass("cost").forEach(element1 -> {
//                System.out.println(element1.toString());
                element1.getElementsByClass("listpricespan").forEach(element2 -> {
                    System.out.println(element2.text());
                });
            });
            element.getElementsByClass("comment").forEach(element1 -> {
                element1.getElementsByClass("f12sc").forEach(element2 -> {
                    System.out.println(element2.text());
                });
            });
            element.getElementsByClass("mall").forEach(element1 -> {
                element1.getElementsByClass("shenqingGY").forEach(element2 -> {
                    System.out.println(element2.text());
                });
                element1.getElementsByClass("AreaZY").forEach(element2 -> {
                    System.out.println(element2.text());
                });
//                System.out.println(element1.toString());
            });
//            element.getElementsByClass("clear").forEach(element1 -> {
//                System.out.println(element1.toString());
//            });

        });

//        Element element = doc.select("div[class=bjlineSmall]").get(0);
//        System.out.println(element.toString());

    }

}
