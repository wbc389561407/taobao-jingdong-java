package com.jiujiu;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SwisseUtil {

    // 无需登录，直接获取数据
    private static String url ="https://www.hnpwholesale.com.au/Mall/Swisse.aspx";

    public static void main(String[] args) {

        System.out.println("start >>>>>");
        ExcelUtil excelUtil = new ExcelUtil("d:/swisse"+System.currentTimeMillis()+".xlsx");
        String[] title = {"详情页地址","图片地址", "商品名称", "美元", "发货", "保质期", "重量", "服务", "详情图地址"};
        // 把 title 转换为 list
        List<String> titleList = Arrays.asList(title);
        excelUtil.setOneRowData(titleList,0); //写入标题行
        List<List<String>> data = getData(url); // 获取首页信息

//        List<String> strings = data.get(1);
//        List<String> data1 = getDetailData(strings.get(0));
//        excelUtil.setOneRowData(data1);

        for (int i = 1; i < data.size(); i++) {
            List<String> strings = data.get(i); //拿到一个主页 信息
            List<String> data1 = getDetailData(strings.get(0)); // 获取第一个字段 详情页 访问页面 获取详情页信息
            excelUtil.setOneRowData(data1); //写入详情页信息
        }

        // 写出数据 关闭流
        excelUtil.writeAndClose();
    System.out.println("end >>>>>");
    }

    private static List<String> getDetailData(String url) {

        List<String> list = new ArrayList<>();
        list.add(url);
        Document doc = HTMLUtil.getDocumentByURL(url);
        //图片
        Element elementById = doc.getElementById("midimg");
        String attr = elementById.attr("src");
        list.add("https://www.hnpwholesale.com.au"+attr);
        // https://www.hnpwholesale.com.au/UpFile/Product/9311770605090.jpg

        //商品名称
        Element bMb5 = doc.getElementsByClass("b mb5").first();
        String name = bMb5.text();
        list.add(name);

        //商城价
        Element sysItemPrice = doc.getElementsByClass("sys_item_price").first();
        String price = sysItemPrice.text();
        list.add(price);


        Elements elementsByClass = doc.getElementsByClass("pt10 f13");
//        elementsByClass.forEach(element -> {
//            String text = element.text();
//        });

        int size = list.size(); //长度

        list.add("");//发货
        list.add("");//保质期
        list.add("");//重量

        for (int i = 0; i < elementsByClass.size()/2; i++) {
            // 发货: 保质期: 重量:

            String title = elementsByClass.get(i*2).text();
            System.out.println(title);
            int index = 0;
            if(title.contains("发货")){
                index = size;
                String text = elementsByClass.get(i*2+1).text();
                list.add(index,text);
                list.remove(index+1);
            }

            if(title.contains("保质期")){
                index = size+1;
                String text = elementsByClass.get(i*2+1).text();
                list.add(index,text);
                list.remove(index+1);
            }

            if(title.contains("重量")){
                index = size+2;
                String text = elementsByClass.get(i*2+1).text();
                list.add(index,text);
                list.remove(index+1);
            }

        }

        //服务:
        Elements elementsByClass1 = doc.getElementsByClass("pt15 f13");
        for (int i = 0; i < elementsByClass1.size(); i++) {
            if(i%2==1){
                Element element = elementsByClass1.get(i);
                String text = element.text();
                list.add(text);
            }
        }
        //详情图
        Elements elementsByTag = doc.getElementsByClass("dec").first().getElementsByTag("p");

        for (int i = 0; i < elementsByTag.size(); i++) {
            Element element = elementsByTag.get(i);
            Elements elementsByTag1 = element.getElementsByTag("img");
            System.out.println(elementsByTag1.size());
            Element first = elementsByTag1.first();
            if(first != null){
                String attr1 = first.attr("src");
                if(!attr1.contains("http")){
                    attr1 = "https://www.hnpwholesale.com.au"+attr1;
                }
                System.out.println(attr1);
                list.add(attr1);
            }

        }

        return list;
    }

    //获取主页信息 https://www.hnpwholesale.com.au/Mall/Swisse.aspx
    private static List<List<String>> getData(String url) {
        ArrayList<List<String>> reList = new ArrayList<>();
        String[] title = {"主页网址","图片地址", "商品名称", "美元", "人民币"};
        // 把 title 转换为 list
        List<String> titleList = Arrays.asList(title);
        reList.add(titleList);
        // https://www.hnpwholesale.com.au/Mall/DetailSwisse.aspx?id=A630E805538C9113
//        String url = "https://www.hnpwholesale.com.au/Mall/Swisse.aspx";
        HttpRequest get = HttpUtil.createGet(url);
        get.header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36 Edg/102.0.1245.39");
        String body = get.execute().body();

        //解析 html 获取数据
        Document doc = Jsoup.parse(body);
        Element insideList = doc.getElementsByClass("inside_list").first();
        insideList.getElementsByTag("li").forEach(element -> {
            List<String> strings = new ArrayList<>();
            Element a = element.getElementsByTag("a").first();
            String attr1 = a.attr("onclick");
            String replace = attr1.replace("prodis('", "").replace("')", "");
            strings.add("https://www.hnpwholesale.com.au/Mall/"+replace);
            String attr = a.getElementsByTag("img").first().attr("src");
            strings.add(attr);
            //获取产品名称
            String name = element.getElementsByClass("pro-list-name").first().getElementsByTag("a").text();
            strings.add(name);

            //获取价格
            //获取产品名称
            Element price2 = element.getElementsByClass("price2").first();
            String text = price2.text();//美元和 人民币  $25.60≈ ¥126.74

            String[] split = text.split("≈");
            String usd = split[0].replace("$", "").trim();
            String rmb = split[1].replace("¥", "").trim();
            strings.add(usd);
            strings.add(rmb);
            reList.add(strings);
        });
        return reList;

//        StringBuilder stringBuilder = new StringBuilder();
//        // gl-item
//        elementById.getElementsByClass("gl-item").forEach(element -> {
//            String attr = element.attr("data-sku");
//            stringBuilder.append(attr).append(",");
////            // 商品图片
////            Elements img = element.getElementsByClass("p-img");
////            String src = img.get(0).getElementsByTag("img").get(0).attr("src");
//            // 商品价格
//            Elements pPrice = element.getElementsByClass("p-price");
//            String price = pPrice.get(0).getElementsByTag("i").get(0).text();
//            // 商品名称
//            Elements pName = element.getElementsByClass("p-name");
//            String name = pName.get(0).getElementsByTag("em").get(0).text();
//            // 商品链接
//            Elements pCommit = element.getElementsByClass("p-commit");
//            String href = pCommit.get(0).getElementsByTag("a").get(0).attr("href");
//            // 商品店铺
//            Elements pShop = element.getElementsByClass("p-shop");
//            String shop = pShop.get(0).getElementsByTag("a").get(0).text();
////            // 商品评论数
////            Elements pCommit1 = element.getElementsByClass("p-commit");
////            String commit = pCommit1.get(0).getElementsByTag("a").get(0).attr("id");
//            System.out.println("====================================");
//        });
//        stringBuilder.deleteCharAt(stringBuilder.length() - 1);





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
