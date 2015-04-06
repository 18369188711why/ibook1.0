package edu.sdu.wh.ibook.service;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import edu.sdu.wh.ibook.po.BookDetail;

/**
 *
 */
public class BookDetailJsoupHtml extends JsoupHtml{
    List<BookDetail> bookDetails;
    Document document;
    public BookDetailJsoupHtml(String html) {
        super(html);
        document=super.getDocument();
        this.parseHtml();
    }
    @Override
    public void parseHtml() {

        bookDetails =new ArrayList<BookDetail>();
        Elements elements=document.select("div[id=\"mainbox\"]").
                select("div[id=\"container\"]").
                select("div[id=\"content_item\"]").
                select("div[id=\"tabs2\"]").
                select("div[id=\"tab_item\"]").
                select("table[id=\"item\"]").
                select("tbody").
                select("tr[class=\"whitetext\"]");

        for(int i=0;i<elements.size();i++)
        {
            BookDetail bookDetail=new BookDetail();
            Elements contents=elements.get(i).select("td");
            bookDetail.setBarcode(contents.get(1).text());
            bookDetail.setYear(contents.get(2).text());
            bookDetail.setSchool_place(contents.get(3).text());
            bookDetail.setStatus(contents.get(4).text());
            bookDetails.add(bookDetail);
        }
    }

    public List<BookDetail> getBookDetails() {
        return bookDetails;
    }
}
