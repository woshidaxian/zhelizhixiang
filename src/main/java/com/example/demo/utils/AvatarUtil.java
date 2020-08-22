package com.example.demo.utils;

public class AvatarUtil {
    public static String getGravatar(String email) {
        String emailMd5 = MD5Util.encode(email);
        //设置图片大小32px
        String avatar = "http://www.gravatar.com/avatar/"+emailMd5+"?s=128&d=identicon&r=PG";
        return avatar;
    }
}
