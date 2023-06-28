package com.jiujiu;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JdTest {

    public static void main(String[] args) {


        System.out.println("Hello World!");
        // https://search.jd.com/Search?keyword=%E4%BF%9D%E9%99%A9%E7%AE%B1%20%E5%AE%B6%E7%94%A8&enc=utf-8&pvid=75170e601f224d5a9942e1abbc73d01c
        getData();





    }

    private static void getData() {
        // http 访问 https://s.taobao.com/search?q=Swisse+%E7%94%B7%E5%A3%AB%E7%95%AA%E8%8C%84%E7%BA%A2%E7%B4%A0&imgfile=&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20230627&ie=utf8

//        String s = HttpUtil.get("https://s.taobao.com/search?q=Swisse+%E7%94%B7%E5%A3%AB%E7%95%AA%E8%8C%84%E7%BA%A2%E7%B4%A0&imgfile=&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20230627&ie=utf8");
//        System.out.println(s);
        //男士番茄红素
//        String key = "Swisse 男士番茄红素";
        String key = "Swisse 男士番茄红素";
        String encode = URLUtil.encode(key);
        System.out.println(encode);
//        HttpRequest get = HttpUtil.createGet("https://search.jd.com/Search?keyword="+encode+"&enc=utf-8&pvid=75170e601f224d5a9942e1abbc73d01c");
        HttpRequest get = HttpUtil.createGet("https://search.jd.com/s_new.php?keyword="+encode+"&page=1");
//        HttpRequest get = HttpUtil.createGet("https://h5api.m.tmall.hk/h5/mtop.taobao.pcdetail.data.get/1.0/?jsv=2.6.1&appKey=12574478&t=1687844649986&sign=2308264d63f2406c6adda9e34223eddc&api=mtop.taobao.pcdetail.data.get&v=1.0&isSec=0&ecode=0&timeout=10000&ttid=2022%40taobao_litepc_9.17.0&AntiFlood=true&AntiCreep=true&dataType=json&valueType=string&preventFallback=true&type=json&data=%7B%22id%22%3A%22636955025711%22%2C%22detail_v%22%3A%223.3.2%22%2C%22exParams%22%3A%22%7B%5C%22abbucket%5C%22%3A%5C%2210%5C%22%2C%5C%22id%5C%22%3A%5C%22636955025711%5C%22%2C%5C%22ns%5C%22%3A%5C%221%5C%22%2C%5C%22skuId%5C%22%3A%5C%225080033791340%5C%22%2C%5C%22spm%5C%22%3A%5C%22a230r.1.14.3.4e6c17da51gqnC%5C%22%2C%5C%22queryParams%5C%22%3A%5C%22abbucket%3D10%26id%3D636955025711%26ns%3D1%26skuId%3D5080033791340%26spm%3Da230r.1.14.3.4e6c17da51gqnC%5C%22%2C%5C%22domain%5C%22%3A%5C%22https%3A%2F%2Fdetail.tmall.hk%5C%22%2C%5C%22path_name%5C%22%3A%5C%22%2Fhk%2Fitem.htm%5C%22%7D%22%7D");
        get.header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36 Edg/102.0.1245.39");
//        get.cookie("cna=Tn8EHZmVrjcCATyzACBGpCYA; tfstk=cCjRBF_LtSVkJ7FH03UmL81hP6_dZaVpGYOnvgeGPMKjn_odiPjGXYM0PpOww-C..; ");
        String body = get.execute().body();
        System.out.println(body);

        //解析 html 获取数据
        Document doc = Jsoup.parse(body);
        Element elementById = doc.getElementById("J_goodsList");
        StringBuilder stringBuilder = new StringBuilder();
        // gl-item
        elementById.getElementsByClass("gl-item").forEach(element -> {
            String attr = element.attr("data-sku");
            stringBuilder.append(attr).append(",");
//            // 商品图片
//            Elements img = element.getElementsByClass("p-img");
//            String src = img.get(0).getElementsByTag("img").get(0).attr("src");
//            System.out.println(src);
            // 商品价格
            Elements pPrice = element.getElementsByClass("p-price");
            String price = pPrice.get(0).getElementsByTag("i").get(0).text();
            System.out.println(price);
            // 商品名称
            Elements pName = element.getElementsByClass("p-name");
            String name = pName.get(0).getElementsByTag("em").get(0).text();
            System.out.println(name);
            // 商品链接
            Elements pCommit = element.getElementsByClass("p-commit");
            String href = pCommit.get(0).getElementsByTag("a").get(0).attr("href");
            System.out.println(href);
            // 商品店铺
            Elements pShop = element.getElementsByClass("p-shop");
            String shop = pShop.get(0).getElementsByTag("a").get(0).text();
            System.out.println(shop);
//            // 商品评论数
//            Elements pCommit1 = element.getElementsByClass("p-commit");
//            String commit = pCommit1.get(0).getElementsByTag("a").get(0).attr("id");
//            System.out.println(commit);
            System.out.println("====================================");
        });
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);





        //g_page_config =
        //g_srp_loadCss();
        // 从 body 中 获取 g_page_config =  和 g_srp_loadCss(); 之间的数据
//        int i = body.indexOf("g_page_config = ");
//        int i1 = body.indexOf("g_srp_loadCss();");
//        String substring = body.substring(i + 16, i1);
//
//        String trim = substring.trim();
//        String substring1 = trim.substring(0,trim.length() - 1);
//        System.out.println(substring1);
//        JSONObject jsonObject = JSONUtil.parseObj(substring1);
//        JSONArray jsonArray = jsonObject.getJSONObject("mods").getJSONObject("itemlist")
//                .getJSONObject("data").getJSONArray("auctions");
//
//        for (int i2 = 0; i2 < jsonArray.size(); i2++) {
//            JSONObject jsonObject1 = jsonArray.getJSONObject(i2);
//            System.out.println(jsonObject1.get("raw_title"));
//            System.out.println(jsonObject1.get("shopName"));
//            System.out.println(jsonObject1.get("view_price"));
//            System.out.println(jsonObject1.get("view_sales"));
//            System.out.println(jsonObject1.get("pic_url"));
//            System.out.println(jsonObject1.get("detail_url"));
//        }
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
