package com.test.ch.domain.text;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XiaoQing {
    public static void main(String[] args) {
        File srcFile = new File("/Users/banmo/Downloads/paper_author_02.txt");
        //String tmp = "B. Kleihaus (1), J. Kunz (2),D. H. Tchrakian (1) ((1) NUI Maynooth, Ireland, (2) University Oldenburg, Germany)";
        String pattern1 = "\\((\\w|\\s)*\\)";
        String pattern2 = "\\(.*?\\)";
        //System.out.println(tmp.replaceAll(pattern1,""));

        try {
            FileReader fr = new FileReader(srcFile);
            BufferedReader br = new BufferedReader(fr);
            File desFile = new File("/Users/banmo/Downloads/paper_author_02_result.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(desFile));
            String str;

            while ((str = br.readLine()) != null) {
                String tmp1 = str.replaceAll(pattern1, "");
                if (tmp1.indexOf("(") > 0 ) {
                    String tmp2 = tmp1.replaceAll(pattern2,"");
                    if (tmp2.indexOf("(") > 0) {
                        System.out.println(tmp2 +"\r\n" + tmp2.substring(0,tmp2.indexOf("(")));
                        writer.write(tmp2.substring(0,tmp2.indexOf("("))+"\r\n");
                    } else {
                        writer.write(tmp2 + "\r\n");
                    }
                } else {
                    writer.write(tmp1+"\r\n");
                }

            }
            br.close();
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
