package com.test.ch.domain.systemInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ClassLoadInfo {
    private static Logger logger = LoggerFactory.getLogger(ClassLoadInfo.class);

    public static void main(String[] args) {
        String logFileDir = "/Users/banmo/logs/tomcat_console.log";
        String libFileDir = "/Users/banmo/work/repo/taxcenter/taxcenter-web/target/taxcenter/WEB-INF/lib";
        try {
            File tomcatStdoutLog = new File(logFileDir);
            File jarDir = new File(libFileDir);
            String[] fileNames = jarDir.list();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(tomcatStdoutLog));
            String line = null;

            Set<String> sysJars = new HashSet<>();
            Set<String> lines = new HashSet<>();
            List<String> noUsedJars = new LinkedList<>();
            while ((line = bufferedReader.readLine()) != null) {

                // System jar包
                if (line.startsWith("[Loaded") && line.contains("from")
                    && line.contains("taobao-hsf.sar") && line.contains("/opt/taobao/install")) {

                    int index = line.lastIndexOf("]");
                    String sysJar = line.substring(0, index);
                    index = sysJar.lastIndexOf(" ");
                    sysJar = sysJar.substring(index + 1);

                    if (sysJar.endsWith("!/") && sysJar.length() > 2) {
                        sysJar = sysJar.substring(0, sysJar.length() - 2);
                    }
                    index = sysJar.lastIndexOf("/");
                    sysJar = sysJar.substring(index + 1);
                    sysJars.add(sysJar);
                }

                if (line.startsWith("[Loaded") &&
                    line.contains("from") &&
                    !line.contains("taobao-hsf.sar") &&
                    !line.contains("/opt/taobao/install")) {

                    int startIndex = line.lastIndexOf("/");
                    int endIndex = line.indexOf(".jar]");
                    if (startIndex >= 0 && endIndex > 0 && endIndex + 4 <= line.length()) {
                        line = line.substring(startIndex + 1, endIndex + 4);
                    }
                    lines.add(line);
                }
            }
            for (String fileName : fileNames) {
                if (!lines.contains(fileName) && !sysJars.contains(fileName)) {
                    noUsedJars.add(fileName);
                }
            }

            double totalLen = 0.0;
            for (String fileName : noUsedJars) {
                File file = new File(libFileDir + fileName);
                String strLen = null;
                double tmpLen = 0.0;
                long len = file.length() >> 10;
                if (len > 1024) {
                    tmpLen = (len * 1.0 / 1024);
                    strLen = tmpLen + "MB";
                } else {
                    tmpLen = len;
                    strLen = tmpLen + "kB";
                }
                totalLen += len;
                logger.warn("fileName=:{}, size={}", fileName, strLen);
            }
            logger.warn("total size={}MB", totalLen / 1024);
        } catch (Exception e) {
            logger.warn("[CheckNoUsedJarsJob] error e={}", e);
        }
    }
}
