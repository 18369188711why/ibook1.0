package edu.sdu.wh.ibook.service;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import edu.sdu.wh.ibook.po.User;

/**
 *
 */
public class UserJsoupHtml extends JsoupHtml{
    private User user;
    private Document document;

    public UserJsoupHtml(String html) {
        super(html);
        document=super.getDocument();
        user=new User();
    }

    public User getUser() {
        return user;
    }

    public void parseHtml() {
        Elements contents=document.select("div[id=\"mainbox\"]").
                select("div[id=\"container\"]").
                select("div[id=\"mylib_content\"]").
                select("div[id=\"mylib_info\"]").
                select("table").
                select("tbody").
                select("tr");
        String userNameCon=contents.get(0).select("td").get(1).text();
        String[] userName=userNameCon.split("：");
        user.setUsername(userName[1]);

        String userNumCon=contents.get(0).select("td").get(2).text();
        String[] userNum=userNumCon.split("：");
        user.setUsernumber(userNum[1]);

        String userUnitCon=contents.get(6).select("td").get(0).text();
        String[] userUnit=userUnitCon.split("：");
        user.setUserunit(userUnit[1]);

        String userGenderCon=contents.get(6).select("td").get(3).text();
        String[] userGender=userGenderCon.split("：");
        user.setUsergender(userGender[1]);
    }
//    public String getUserName(){
//        return user.getUsername();
//    }

}
