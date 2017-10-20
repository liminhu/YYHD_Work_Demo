package com.gameassist.plugin.wechat.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hulimin on 2017/10/18.
 */

public class MatchersUtils {
    public static String match(String p, String str){
		/*
		 * Pattern指定为字符串的正则表达式必须首先被编译为此类的实例。
		 * 然后，可将得到的模式用于创建 Matcher 对象，依照正则表达式，该对象可以与任意字符序列匹配。
		 * 执行匹配所涉及的所有状态都驻留在匹配器中，所以多个匹配器可以共享同一模式。
		 *
		*/
        //compile()方法将给定的正则表达式编译到模式中
        Pattern pattern = Pattern.compile(p);
        //matcher()方法创建匹配给定输入与此模式的匹配器
        Matcher m = pattern.matcher(str);
        if(m.find()){
            return m.group(1);
        }
        return null;
    }
}
