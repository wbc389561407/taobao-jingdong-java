package com.jiujiu;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class TaobaoTest {

    public static void main(String[] args) {


        System.out.println("Hello World!");

        // taobao 输入用户名 密码 获取 cookie
        // https://login.taobao.com/member/login.jhtml?redirectURL=https%3A%2F%2Fwww.taobao.com%2F



//        getData();



    }

    private static void getData() {
        // http 访问 https://s.taobao.com/search?q=Swisse+%E7%94%B7%E5%A3%AB%E7%95%AA%E8%8C%84%E7%BA%A2%E7%B4%A0&imgfile=&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20230627&ie=utf8

//        String s = HttpUtil.get("https://s.taobao.com/search?q=Swisse+%E7%94%B7%E5%A3%AB%E7%95%AA%E8%8C%84%E7%BA%A2%E7%B4%A0&imgfile=&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20230627&ie=utf8");
//        System.out.println(s);
        //男士番茄红素
//        String key = "Swisse 男士番茄红素";
        String key = "Swisse 月见草 200粒";
        String encode = taobaoEncode(key);
        HttpRequest get = HttpUtil.createGet("https://s.taobao.com/search?q="+encode+"&imgfile=&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20230627&ie=utf8");
//        HttpRequest get = HttpUtil.createGet("https://h5api.m.tmall.hk/h5/mtop.taobao.pcdetail.data.get/1.0/?jsv=2.6.1&appKey=12574478&t=1687844649986&sign=2308264d63f2406c6adda9e34223eddc&api=mtop.taobao.pcdetail.data.get&v=1.0&isSec=0&ecode=0&timeout=10000&ttid=2022%40taobao_litepc_9.17.0&AntiFlood=true&AntiCreep=true&dataType=json&valueType=string&preventFallback=true&type=json&data=%7B%22id%22%3A%22636955025711%22%2C%22detail_v%22%3A%223.3.2%22%2C%22exParams%22%3A%22%7B%5C%22abbucket%5C%22%3A%5C%2210%5C%22%2C%5C%22id%5C%22%3A%5C%22636955025711%5C%22%2C%5C%22ns%5C%22%3A%5C%221%5C%22%2C%5C%22skuId%5C%22%3A%5C%225080033791340%5C%22%2C%5C%22spm%5C%22%3A%5C%22a230r.1.14.3.4e6c17da51gqnC%5C%22%2C%5C%22queryParams%5C%22%3A%5C%22abbucket%3D10%26id%3D636955025711%26ns%3D1%26skuId%3D5080033791340%26spm%3Da230r.1.14.3.4e6c17da51gqnC%5C%22%2C%5C%22domain%5C%22%3A%5C%22https%3A%2F%2Fdetail.tmall.hk%5C%22%2C%5C%22path_name%5C%22%3A%5C%22%2Fhk%2Fitem.htm%5C%22%7D%22%7D");
        get.header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36 Edg/102.0.1245.39");
        //修改为自己的cookie  修改为自己的cookie 修改为自己的cookie 修改为自己的cookie
        get.cookie("cna=Tn8EHZmVrjcCATyzACBGpCYA; xlly_s=1; cookie2=207665e35ce2ccd4df0e597c6ab873f5; t=af3e6c39a2aeb2bfb95a1a4b36b2b599; _samesite_flag_=true; sgcookie=E100d%2F%2FShFJkp6Eu1kElCnHIC%2B8dHhi5cB313Dburi3v3HWQ6Uv4ZURbR5vHJiy0L1%2F7EwulSHbcqanywKra1Y23iE%2FY%2FS%2FD51WADXQro2ALaFxxm1VZqHoLzKMz4bs1P8Fe; unb=831510719; uc3=id2=W8rpM8T%2B%2BbMx&nk2=okeDkJQ5hw%3D%3D&lg2=V32FPkk%2Fw0dUvg%3D%3D&vt3=F8dCsGO28E%2BlnST7Noc%3D; csg=a0bb5202; lgc=%5Cu604B%5Cu9B42wbc; cancelledSubSites=empty; cookie17=W8rpM8T%2B%2BbMx; dnk=%5Cu604B%5Cu9B42wbc; skt=9dc1a84457d6d094; existShop=MTY4NzgzNjEwNw%3D%3D; uc4=nk4=0%40oEX6cb9zus%2BnMkwJX579fBrH&id4=0%40WenjbJJ%2FYeBL1ZJOhedwBi%2FpvTg%3D; publishItemObj=Ng%3D%3D; tracknick=%5Cu604B%5Cu9B42wbc; _cc_=WqG3DMC9EA%3D%3D; _l_g_=Ug%3D%3D; sg=c91; _nk_=%5Cu604B%5Cu9B42wbc; cookie1=AiczeJm9CUJs3bPYUnftUe%2BdnQ5jX%2FXAVARO2t6hKTY%3D; mt=ci=4_1; thw=cn; _tb_token_=fb7636d4e4e60; _m_h5_tk=17f9e2f9ab2d6b71f001c5d571c005d7_1687861196155; _m_h5_tk_enc=2f5c9cf15787f4d8aad8fb76953ae432; uc1=cookie16=VT5L2FSpNgq6fDudInPRgavC%2BQ%3D%3D&cookie14=Uoe8jRMMjAHXmQ%3D%3D&pas=0&cookie15=URm48syIIVrSKA%3D%3D&existShop=false&cookie21=VT5L2FSpdet1EftGlDZ1Vg%3D%3D; x5sec=7b22617365727665723b32223a223166366531656236343733376638366665383930333531653764323864616335434b617036715147454f543174642b653471474343786f4c4f444d784e5445774e7a45354f7a4977374a654d30774e4141773d3d227d; JSESSIONID=DBD5A8DA8ED137293DE0B9B48AEAA114; l=fBIKJE8qNp3r9ihWBO5Clurza779wIOb8sPzaNbMiIEGa1zltF_s8NC1QXoySdtjgTfxOetPKyB8wdnv7Fzdg2HvCbKrCyCkOY96-bpU-L5..; tfstk=c2-fBv96ASVbr5ogmsMyA7WedBSNZd2CCq1DhX60Z7T-Papfi7rFOjkHS5INBT1..; isg=BPf3mb10X1QBa9vQUYKHg9JphutBvMseR3PMoUmlIkYt-BY6UY-Ebqma2limEKOW");
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

        for (int i2 = 0; i2 < jsonArray.size(); i2++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i2);
            System.out.println(jsonObject1.get("raw_title"));
            System.out.println(jsonObject1.get("shopName"));
            System.out.println(jsonObject1.get("view_price"));
            System.out.println(jsonObject1.get("view_sales"));
            System.out.println(jsonObject1.get("pic_url"));
            System.out.println(jsonObject1.get("detail_url"));
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
