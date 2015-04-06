package edu.sdu.wh.ibook.util;

/**
 *
 */
public class GetSupport {
    public static String getInteger(String str){
        for(int i=1;i<str.length();i++)
        {
            if(str.charAt(i)=='(')
            {
                str=str.substring(1,i);
                break;
            }
        }
        return str;
    }
}
