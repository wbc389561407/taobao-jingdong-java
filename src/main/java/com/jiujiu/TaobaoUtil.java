package com.jiujiu;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.List;

public class TaobaoUtil {

    public static String cookie;


    /**
     * 传入 一个 关键词 和 一个 excelUtil 查询 关键词的数据 写入excel
     * @param key  电视机
     */
    public static List<List<String>> getTbData(String key) {
        String searchKey = key.replace("单品", "");
        //传入关键词 返回查询到数据 如果长度为 0  则 减少 关键词 重新查询
        JSONArray jsonArray = getTaobaoData(searchKey);
        if(jsonArray.size()==0){
            // 去掉 空白后面的一段
            searchKey = searchKey.substring(0,searchKey.lastIndexOf(" "));
            jsonArray = getTaobaoData(searchKey);
        }

        List<List<String>> reList = new ArrayList<>();
        for (int i2 = 0; i2 < jsonArray.size(); i2++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i2);
            List list = new ArrayList<>();
            list.add(key);
            list.add(searchKey);
            list.add(jsonObject1.get("raw_title").toString());
            list.add(jsonObject1.get("shopName").toString());
            list.add(jsonObject1.get("view_price").toString());
            list.add(jsonObject1.get("view_sales").toString().replace("人付款","").replace("+",""));
            list.add(jsonObject1.get("pic_url").toString());
            list.add(jsonObject1.get("detail_url").toString());
            reList.add(list);
        }
        return reList;
    }


    /**
     * 传入 一个 关键词 和 一个 excelUtil 查询 关键词的数据 写入excel
     * @param key  电视机
     * @param excelUtil1
     */
    public static int getTbData(String key, ExcelUtil excelUtil1) {
        String searchKey = key.replace("单品", "");
        //传入关键词 返回查询到数据 如果长度为 0  则 减少 关键词 重新查询
        JSONArray jsonArray = getTaobaoData(searchKey);
        if(jsonArray.size()==0){
            // 去掉 空白后面的一段
            searchKey = searchKey.substring(0,searchKey.lastIndexOf(" "));
            jsonArray = getTaobaoData(searchKey);
        }

        for (int i2 = 0; i2 < jsonArray.size(); i2++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i2);
            List<String> list = new ArrayList<>();
            list.add(key);
            list.add(searchKey);
            list.add(jsonObject1.get("raw_title").toString());
            list.add(jsonObject1.get("shopName").toString());
            list.add(jsonObject1.get("view_price").toString());
            list.add(jsonObject1.get("view_sales").toString().replace("人付款","").replace("+",""));
            list.add(jsonObject1.get("pic_url").toString());
            list.add(jsonObject1.get("detail_url").toString());
            excelUtil1.setOneRowData(list);
//            System.out.println(jsonObject1.get("raw_title"));
//            System.out.println(jsonObject1.get("shopName"));
//            System.out.println(jsonObject1.get("view_price"));
//            System.out.println(jsonObject1.get("view_sales"));
//            System.out.println(jsonObject1.get("pic_url"));
//            System.out.println(jsonObject1.get("detail_url"));
        }
        return jsonArray.size();
    }

    private static JSONArray getTaobaoData(String searchKey) {
        String encode = taobaoEncode(searchKey);
        HttpRequest get = HttpUtil.createGet("https://s.taobao.com/search?q="+encode+"&imgfile=&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20230627&ie=utf8");
//        HttpRequest get = HttpUtil.createGet("https://h5api.m.tmall.hk/h5/mtop.taobao.pcdetail.data.get/1.0/?jsv=2.6.1&appKey=12574478&t=1687844649986&sign=2308264d63f2406c6adda9e34223eddc&api=mtop.taobao.pcdetail.data.get&v=1.0&isSec=0&ecode=0&timeout=10000&ttid=2022%40taobao_litepc_9.17.0&AntiFlood=true&AntiCreep=true&dataType=json&valueType=string&preventFallback=true&type=json&data=%7B%22id%22%3A%22636955025711%22%2C%22detail_v%22%3A%223.3.2%22%2C%22exParams%22%3A%22%7B%5C%22abbucket%5C%22%3A%5C%2210%5C%22%2C%5C%22id%5C%22%3A%5C%22636955025711%5C%22%2C%5C%22ns%5C%22%3A%5C%221%5C%22%2C%5C%22skuId%5C%22%3A%5C%225080033791340%5C%22%2C%5C%22spm%5C%22%3A%5C%22a230r.1.14.3.4e6c17da51gqnC%5C%22%2C%5C%22queryParams%5C%22%3A%5C%22abbucket%3D10%26id%3D636955025711%26ns%3D1%26skuId%3D5080033791340%26spm%3Da230r.1.14.3.4e6c17da51gqnC%5C%22%2C%5C%22domain%5C%22%3A%5C%22https%3A%2F%2Fdetail.tmall.hk%5C%22%2C%5C%22path_name%5C%22%3A%5C%22%2Fhk%2Fitem.htm%5C%22%7D%22%7D");
        get.header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36 Edg/102.0.1245.39");
//        get.cookie("cna=Tn8EHZmVrjcCATyzACBGpCYA; dnk=%5Cu604B%5Cu9B42wbc; uc1=cookie15=VT5L2FSpMGV7TQ%3D%3D&pas=0&cookie16=Vq8l%2BKCLySLZMFWHxqs8fwqnEw%3D%3D&cookie14=Uoe8jRMKL2aMnA%3D%3D&existShop=false&cookie21=VFC%2FuZ9ajCbF8%2BYBpbBdiw%3D%3D; tracknick=%5Cu604B%5Cu9B42wbc; lid=%E6%81%8B%E9%AD%82wbc; _l_g_=Ug%3D%3D; unb=831510719; cookie1=AiczeJm9CUJs3bPYUnftUe%2BdnQ5jX%2FXAVARO2t6hKTY%3D; login=true; cookie17=W8rpM8T%2B%2BbMx; cookie2=207665e35ce2ccd4df0e597c6ab873f5; _nk_=%5Cu604B%5Cu9B42wbc; sgcookie=E100d%2F%2FShFJkp6Eu1kElCnHIC%2B8dHhi5cB313Dburi3v3HWQ6Uv4ZURbR5vHJiy0L1%2F7EwulSHbcqanywKra1Y23iE%2FY%2FS%2FD51WADXQro2ALaFxxm1VZqHoLzKMz4bs1P8Fe; cancelledSubSites=empty; t=af3e6c39a2aeb2bfb95a1a4b36b2b599; sg=c91; csg=a0bb5202; _tb_token_=5678353396a9d; xlly_s=1; _m_h5_tk=f355faa7399760ddedc810d623167707_1687850466388; _m_h5_tk_enc=eea2f7a1c69585554881925f9608edbf; x5sec=7b22617365727665723b32223a226263373566653731363736326234363366333463626130333063373331333838434f667536615147454d5848394b4f797636377638514561437a677a4d5455784d4463784f5473784d506d33684d594351414d3d227d; isg=BPn5nPABuXIwg2XrLKz6xNPoCGXTBu24NRmSfxsu5CCeohk0Y1LZiRD7JKZUGoXw; l=fBIA2r67NAfPsLf0BO5CFurza77OuIRb4sPzaNbMiIEGa12lsF1wQNC1QHsJ-dtjgT1UHetPKyB8wdLHR3xpvxDDBIwAlLI-nxmXZeVh.; tfstk=cCjRBF_LtSVkJ7FH03UmL81hP6_dZaVpGYOnvgeGPMKjn_odiPjGXYM0PpOww-C..");
        get.cookie(cookie);
        String body = get.execute().body();
        System.out.println(body);

        //g_page_config =
        //g_srp_loadCss();
        // 从 body 中 获取 g_page_config =  和 g_srp_loadCss(); 之间的数据
        int i = body.indexOf("g_page_config = ");
        int i1 = body.indexOf("g_srp_loadCss();");
        String substring = body.substring(i + 16, i1);

        String trim = substring.trim();
        String substring1 = trim.substring(0,trim.length() - 1);
        System.out.println(substring1);
        JSONObject jsonObject = JSONUtil.parseObj(substring1);
        JSONArray jsonArray = jsonObject.getJSONObject("mods").getJSONObject("itemlist")
                .getJSONObject("data").getJSONArray("auctions");
        return jsonArray;
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
