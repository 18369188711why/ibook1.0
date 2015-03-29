package edu.sdu.wh.ibook.service;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import edu.sdu.wh.ibook.po.BookInfo;

/**
 *
 */
public class SearchJsoupHtml extends JsoupHtml{
    private Document document;
    private List<BookInfo> bookInfos;

    public SearchJsoupHtml(String html) {
        super(html);
        document=super.getDocument();
        this.parseHtml();
    }

    @Override
    public void parseHtml() {
        Elements contents=document.select("div[id=\"mainbox\"]").
                select("div[id=\"container\"]").
                select("div[id=\"content\"]").
                select("div[class=\"book_article\"]").
                get(3).
                select("ol[id=\"search_book_list\"]").
                select("li[class=\"book_list_info\"]");
        bookInfos=new ArrayList<BookInfo>();
        for (int i = 0; i < contents.size(); i++) {
            BookInfo bookInfo=new BookInfo();
            bookInfo.setName(contents.get(i).select("h3").select("a").text());
            bookInfo.setISBN(contents.get(i).select("h3").text());
            bookInfo.setType(contents.get(i).select("h3").select("span").text());
            bookInfo.setAuthor(contents.get(i).select("p").text());
            bookInfo.setPublisher(contents.get(i).select("p").text());
            bookInfo.setStoredNum(contents.get(i).select("p").select("span").text());
            bookInfo.setAvaiableNum(contents.get(i).select("p").text());
            bookInfos.add(bookInfo);
        }
    }

    public List<BookInfo> getBookInfos() {
        return bookInfos;
    }

}
