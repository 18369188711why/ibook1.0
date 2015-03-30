package edu.sdu.wh.ibook.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import edu.sdu.wh.ibook.po.Mydocument;

/**
 *
 */
public class MyDocumentJsoupHtml extends JsoupHtml{
    private Document document;
    private List<Mydocument> mydocuments;

    public MyDocumentJsoupHtml(String html) {
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

        System.out.println(attitudes);
        mydocuments=new ArrayList<Mydocument>();
        for(int i=0;i<attitudes.size();i++)
        {
            Element element=attitudes.get(i);
            System.out.println(element);
            Elements documentContent= element.select("div").select("p");
            System.out.println(documentContent);
//            Mydocument mydocument=new Mydocument();
//            mydocument.setDocumentNum(Integer.toString(i));
            System.out.println(Integer.toString(i));
//            mydocument.setBookName(documentContent.get(0).select("a").text());
            System.out.println(documentContent.get(0).select("a"));
//            mydocument.setAuthor(documentContent.get(0).text());
            System.out.println(documentContent.get(0).text());
//            mydocument.setContent(documentContent.get(1).text());
            System.out.println(documentContent.get(1).text());
//            mydocument.setSupportNum(documentContent.get(2).select("img").get(0).text());
            System.out.println(documentContent.get(2).select("img").get(0).text());
//            mydocument.setAgainstNum(documentContent.get(2).select("img").get(1).text());
            System.out.println(documentContent.get(2).select("img").get(1).text());
//            mydocuments.add(mydocument);
        }

    }

    public List<Mydocument> getMydocuments() {
        return mydocuments;
    }
}
