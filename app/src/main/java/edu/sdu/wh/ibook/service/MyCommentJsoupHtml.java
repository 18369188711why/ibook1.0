package edu.sdu.wh.ibook.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import edu.sdu.wh.ibook.po.MyComment;
import edu.sdu.wh.ibook.util.GetInteger;

/**
 *
 */
public class MyCommentJsoupHtml extends JsoupHtml{
    private Document document;
    private List<MyComment> mydocuments;

    public MyCommentJsoupHtml(String html) {
        super(html);
        document=super.getDocument();
        this.parseHtml();
    }

    public void parseHtml() {
        Elements attitudes=document.select("div[id=\"mainbox\"]").
                select("div[id=\"container\"]").
                select("div[id=\"mylib_content\"]").
                select("div[id=\"comment\"]").
                select("div[class=\"attitude\"]");

        mydocuments=new ArrayList<MyComment>();
        for(int i=0;i<attitudes.size();i++)
        {
            Element element=attitudes.get(i);
            Elements documentContent= element.select("div").select("p");
            MyComment mydocument=new MyComment();
            mydocument.setDocumentNum(Integer.toString(i).trim());

            //<p>0设置书名和作者
            String name_author=documentContent.get(0).text();
            String name=documentContent.get(0).select("a").text();
            mydocument.setBookName(name);
            mydocument.setAuthor(name_author.substring(name.length()));

            //<p>1设置内容
            mydocument.setContent(documentContent.get(1).text().trim());

            //<p>2设置支持和反对的人数以及时间
            String number=documentContent.get(2).text().trim();
            String[] numbers=number.split(" ");
            mydocument.setSupportNum(GetInteger.getInteger(numbers[0])[0]);
            mydocument.setAgainstNum(GetInteger.getInteger(numbers[0])[1]);
            mydocument.setTime(numbers[3]+"  "+numbers[4]);
            mydocuments.add(mydocument);
        }

    }

    public List<MyComment> getMydocuments() {
        return mydocuments;
    }
}
