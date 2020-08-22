package com.example.demo.utils;

import org.apache.commons.lang.StringEscapeUtils;
public class HTMLUtil {
    public static String html2text(String str) {
        str = str.replaceAll("\\<.*?\\>", "");
        str = StringEscapeUtils.unescapeHtml(str);
        return str;
    }
}
