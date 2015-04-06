package edu.sdu.wh.ibook.util;

/**
 *
 */
public class GetContent {
    public static String getContent(String str){
        for(int i=1;i<str.length();i++)
        {
            if(str.charAt(i)=='(')
            {
                str=str.substring(0,i);
                break;
            }
        }
        return str;
    }
}
