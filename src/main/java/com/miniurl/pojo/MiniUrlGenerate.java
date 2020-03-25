package com.miniurl.pojo;

import java.util.Random;

public class MiniUrlGenerate {
    public static String MiniUrlGenerate(int range) {
        int length=(int)(Math.random()*range)+1;
        Random random = new Random();
        StringBuffer valSb = new StringBuffer();
        String charStr = "0123456789abcdefghijklmnopqrstuvwxyz";
        int charLength = charStr.length();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charLength);
            valSb.append(charStr.charAt(index));
        }
        return valSb.toString();
    }
    public static String MiniUrlGenerate(){
        return MiniUrlGenerate(5);
    }
}
