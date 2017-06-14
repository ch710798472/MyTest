package com.test.ch.domain.crawler;

/**
 * Created by banmo.ch on 17/6/14.
 */
public class DataUtil {
    /*
    public static YunData getData(String url) {
        //自己对okhttp的封装
        ResponseBody body = OkhttpUtil.syncGet(url);
        String html = null;
        if (body == null) {
            return null;
        }
        try {
            html = body.string();
        } catch (IOException e) {
            return null;
        }
        Pattern pattern = Pattern.compile("window.yunData = (.*})");
        Matcher matcher = pattern.matcher(html);
        String json = null;
        while (matcher.find()) {
            json = matcher.group(1);
        }
        if (json == null) {
            return null;
        }
        //fastjson
        YunData yunData = JSON.parseObject(json, YunData.class);
        return yunData;
    }
    */
}
