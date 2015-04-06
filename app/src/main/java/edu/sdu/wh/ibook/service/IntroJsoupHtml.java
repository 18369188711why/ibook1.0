package edu.sdu.wh.ibook.service;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import edu.sdu.wh.ibook.po.Comment;

/**
 *
 */
public class IntroJsoupHtml extends JsoupHtml{
    private Document document;
    private String intro;
    public IntroJsoupHtml(String html) {
        super(html);
        document=super.getDocument();
        this.parseHtml();
    }

    @Override
    public void parseHtml() {
        Elements elements=document.
                select("div[id=\"comment\"]").
                select("div[id=\"tabs1\"]").
                select("div[id=\"tabs-1\"]").
                select("div[class=\"book_article\"]").
                select("div[id=\"book_info\"]").
                select("div[class=\"book_article\"]").
                select("div[id=\"item_detail\"]").
                select("dl[class=\"booklist\"]");

        for(int i=3;i<elements.size()-2;i++)
        {
            intro+=elements.get(i).text()+"\n";
        }
        intro=intro.replace("null",null);
        System.out.println(elements.text().substring(13));
    }

    public String getIntro() {
        return intro;
    }
}
