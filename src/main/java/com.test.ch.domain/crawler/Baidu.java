package com.test.ch.domain.crawler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by banmo.ch on 17/6/14.
 */
public class Baidu {
    private Logger log = LoggerFactory.getLogger(Baidu.class);

    //public void startIndex() {
    //    无限循环
        //while (true) {
        //    从数据库获取可用uk,可用首先从某个粉丝超多的用户入手,获取他粉丝的uk,存入数据库
            //Avaiuk avaiuk = Avaiuk.dao.findFirst("select * from avaiuk where flag=0  limit 1");
            //更新数据库,标记该uk已经被用户爬过
            //avaiuk.set("flag", 1).update();
            //getFllow(avaiuk.getLong("uk"), 0);
        //}
    //}
    //
    //static String url
    //    = "http://yun.baidu.com/pcloud/friend/getfollowlist?query_uk=%s&limit=24&start=%s&bdstoken"
    //    + "=e6f1efec456b92778e70c55ba5d81c3d&channel=chunlei&clienttype=0&web=1&logid"
    //    + "=MTQ3NDA3NDg5NzU4NDAuMzQxNDQyMDY2MjA5NDA4NjU=";
    //static Map map = new HashMap();
    //
    //static {
    //    map.put("User-Agent",
    //        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 "
    //            + "Safari/537.36");
    //    map.put("X-Requested-With", "XMLHttpRequest");
    //    map.put("Accept", "application/json, text/javascript, */*; q=0.01");
    //    map.put("Referer", "https://yun.baidu.com/share/home?uk=325913312#category/type=0");
    //    map.put("Accept-Language", "zh-CN");
    //}
    //
    //获取订阅用户
    //public void getFllow(long uk, int start, boolean index) {
    //    log.info("进来getFllow,uk:{},start:{}", uk, start);
    //    boolean exitUK = false;
    //    try {
    //        exitUK = Redis.use().exists(uk);
    //    } catch (Exception e) {
    //        exitUK = true;
    //    }
    //    if (!exitUK) {
    //        Redis.use().set(uk, "");
    //        if (index) {
    //            indexResource(uk);
    //        }
    //        recFollow(uk, start, true);
    //    } else {
    //        if (start > 0) {//分页订阅
    //            recFollow(uk, start, false);
    //        } else {
    //            log.warn("uk is index:{}", uk);
    //        }
    //    }
    //
    //}
    //
    //public void recFollow(long uk, int start, boolean goPage) {
    //    try {
    //        Thread.sleep(4000);
    //    } catch (InterruptedException e) {
    //        e.printStackTrace();
    //    }
    //    String real_url = String.format(url, uk, start);
    //    ResponseBody body = OkhttpUtil.syncGet(real_url, Headers.of(map));
    //    if (body != null) {
    //        try {
    //            Follow follow = JSON.parseObject(body.string(), Follow.class);
    //
    //            if (follow.getErrno() == 0) {
    //                List<Follow.FollowListBean> followListBeen = follow.getFollow_list();
    //                if (followListBeen != null && followListBeen.size() > 0) {
    //                    log.info("不为空:{}", follow);
    //                    for (Follow.FollowListBean bean : followListBeen) {
    //                        int follow_count = bean.getFollow_count();
    //                        int shareCount = bean.getPubshare_count();
    //                        if (follow_count > 0) {
    //                            if (shareCount > 0) {
    //                                getFllow(bean.getFollow_uk(), 0, true);
    //                            } else {
    //                                getFllow(bean.getFollow_uk(), 0, false);
    //                            }
    //                        }
    //                    }
    //                    if (goPage) {
    //                        int total_count = follow.getTotal_count();
    //                        log.warn("分页页数：{}",total_count);
                            //分页
                            //int total_page = (total_count - 1) / 24 + 1;
                            //
                            //for (int i = 1; i < total_page; i++) {
                            //    getFllow(uk, i * 24, false);
                            //}
                        //
                        //}
                    //} else {
                    //    log.info("为空:{}", follow);
                    //}
                //}
            //
            //} catch (IOException e) {
            //    e.printStackTrace();
            //}
        //}
    //}
    //
    //long uinfoId = 0;
    //long nullStart = System.currentTimeMillis();
    //
    //public void indexResource(long uk) {
    //    while (true) {
    //        String url = "http://pan.baidu.com/wap/share/home?uk=%s&start=%s&adapt=pc&fr=ftw";
    //        String real_url = String.format(url, uk, 0);
    //
    //        YunData yunData = DataUtil.getData(real_url);
    //
    //        if (yunData != null) {
    //            log.info("{}", yunData.toString());
    //            int share_count = yunData.getUinfo().getPubshare_count();
    //            if (share_count > 0) {
    //                Uinfo uinfo = new Uinfo();
    //                uinfo.set("uname", yunData.getUinfo().getUname()).set("avatar_url",
    //                    yunData.getUinfo().getAvatar_url()).set("uk", uk).set("incache", 1).save();
    //                uinfoId = uinfo.getLong("id");
    //                List<Records> recordses = yunData.getFeedata().getRecords();
    //                for (Records record : recordses) {
    //                    new ShareData().set("title", record.getTitle()).set("shareid", record.getShareid()).set(
    //                        "uinfo_id", uinfoId).save();
    //                }
    //
    //            }
    //            int totalPage = (share_count - 1) / 20 + 1;
    //
    //            int start = 0;
    //            if (totalPage > 1) {
    //                for (int i = 1; i < totalPage; i++) {
    //                    start = i * 20;
    //                    real_url = String.format(url, uk, start);
    //                    yunData = DataUtil.getData(real_url);
    //                    if (yunData != null) {
    //                        log.info("{}", yunData.toString());
    //                        List<Records> recordses = yunData.getFeedata().getRecords();
    //                        for (Records record : recordses) {
    //                            用户分享的数据存入数据库
                                //new ShareData().set("title", record.getTitle()).set("shareid", record.getShareid()).set(
                                //    "uinfo_id", uinfoId).save();
                            //}
                        //} else {
                        //    i--;
                        //    log.warn("uk:{},msg:{}", uk, yunData);
                        //    long temp = nullStart;
                        //    nullStart = System.currentTimeMillis();
                        //    if ((nullStart - temp) < 1500) {
                        //        try {
                        //            Thread.sleep(60000);
                        //        } catch (InterruptedException e) {
                        //            e.printStackTrace();
                        //        }
                        //    }
                        //
                        //}
                    //
                    //}
                //
                //}
                //break;
            //} else {
            //    log.warn("uk:{},msg:{}", uk, yunData);
            //    long temp = nullStart;
            //    nullStart = System.currentTimeMillis();
            //    在1500毫秒内2次请求到的数据都为null时,此时可能被百度限制了,休眠一段时间就可以恢复
                //if ((nullStart - temp) < 1500) {
                //    try {
                //        Thread.sleep(60000);
                //    } catch (InterruptedException e) {
                //        e.printStackTrace();
                //    }
                //}
            //}
        //}
    //
    //}
}
