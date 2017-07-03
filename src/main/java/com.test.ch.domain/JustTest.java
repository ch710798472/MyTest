package com.test.ch.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by banmo.ch on 17/3/17.
 *
 * @author banmo.ch
 * @date 2017/03/17
 */
public class JustTest {
    public static void main(String[] args) {
        String url = "abc";
        String nullString = null;
        System.out.println(url + nullString);
        String dateString = "20991230";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = sdf.parse(dateString);
            Date now = new Date();
            System.out.println(now.compareTo(date));
            System.out.println(now);
            System.out.println(date);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        String pic
            = "sfs-FACE||/certifyPicture/T1mFtgXmpXXXXXXXXX?xsig=6261862fd4c0e857e271aeb1c3d2b787##sfs-VERSO"
            + "||/certifyPicture/T1EWlgXX4XXXXXXXXX?xsig=cd780d6a1f6156fe355004eed7253cdb##sfs-FEATURE_FACE"
            + "||/ossbisface/1a9410bb-8282-4ce9-91dd-19e9f89b48b3.jpg##A-VERSO1||ASDASD\"";
        String[] pics = pic.split("##");
        System.out.println(Arrays.asList(pics));
        System.out.println(
            pics[0].indexOf("-FACE") + "\r\n" + pics[1].indexOf("-VERSO") + "\r\n" + pics[2].indexOf("-FACE"));
        System.out.println(
            pics[0].indexOf("-FACE") + "\r\n" + pics[1].indexOf("-VERSO") + "\r\n" + pics[2].indexOf("-FACE"));
        String pictureUrl = pic;
        String[] pictures = pictureUrl.split("##");
        //至少需要正反面2张图片，有可能有侧脸但是只取FACE和VERSO
        if (pictures.length > 1) {
            for (String pic1 : pictures) {
                if (pic1.indexOf("-FACE") > 0) {
                    System.out.println(pic1);
                }
                if (pic1.indexOf("-VERSO") > 0) {
                    System.out.println(pic1);
                }
            }
        }
    }
}
