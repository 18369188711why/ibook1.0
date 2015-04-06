package edu.sdu.wh.ibook.util;

/**
 * 获取字符串中的两个数字
 */
public class GetInteger {
    public static String[] getInteger(String str){
        String[] integer;
        integer=new String[2];
        for(int i=1;i<str.length();i++)
        {
            if(str.charAt(i)==')')
            {
                integer[0]=str.substring(1,i);
                integer[1]=str.substring(i+3,str.length()-1);
                break;
            }
        }
     return integer;
    }
}
