package edu.sdu.wh.ibook.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import edu.sdu.wh.ibook.po.BookInfo;
import edu.sdu.wh.ibook.util.GetContent;

/**
 *
 */
public class BookInfoSearchJsoupHtml extends JsoupHtml{
    private Document document;
    private List<BookInfo> bookInfos;
    private static String URL_BASIC="http://202.194.40.71:8080/opac/";

    public BookInfoSearchJsoupHtml(String html) {
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

            String type_name_ISBN=contents.get(i).select("h3").text();
            String type=contents.get(i).select("h3").select("span").text();
            String name_ISBN=type_name_ISBN.substring(type.length(),type_name_ISBN.length());

            Element a=contents.get(i).select("a").first();
            String link=a.attr("href");
            //书的链接
            bookInfo.setLink(URL_BASIC+link);

            //书名和ISBN
            bookInfo.setName_code(name_ISBN);

            //书种类
            bookInfo.setType(type);

            String author_publish_number=contents.get(i).select("p").text();
            String number=contents.get(i).select("p").select("span").text();
            String author_publisher= GetContent.getContent(author_publish_number.substring(number.length(),author_publish_number.length()));

            //馆藏&可借
            bookInfo.setStored_available_Num(number);
            //作者&出版社
            bookInfo.setAuthor_publisher(author_publisher);
            bookInfos.add(bookInfo);
        }
    }

    public List<BookInfo> getBookInfos() {
        return bookInfos;
    }

}
