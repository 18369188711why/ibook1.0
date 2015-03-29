package edu.sdu.wh.ibook.service;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

import edu.sdu.wh.ibook.po.BookInfo;
import edu.sdu.wh.ibook.po.HisBookInfo;
import edu.sdu.wh.ibook.po.NowBookInfo;

/**
 *
 */
public class BookInfoNowJsoupHtml extends JsoupHtml{
    private List<NowBookInfo> bookInfos;
    private Document document;
    public BookInfoNowJsoupHtml(String html) {
        super(html);
        document=super.getDocument();
        parseHtml();
    }

    public void parseHtml() {
        Elements contents=document.select("div[id=\"mainbox\"]").
                select("div[id=\"container\"]").
                select("div[id=\"mylib_content\"]").
                select("table").
                select("tbody").
                select("tr");
        if(contents.size()==1)
        {
            bookInfos.add(new NowBookInfo());
            return;
        }
        for(int i=1;i<contents.size();i++)
        {
            NowBookInfo bookInfo = new NowBookInfo();
            Elements books = contents.get(i).select("td");
            bookInfo.setBarcode(books.get(0).text());
            bookInfo.setName_author(books.get(2).text());
            bookInfo.setBorrowDate(books.get(3).text());
            bookInfo.setReturnDate(books.get(4).text());
            bookInfo.setRenewNum(books.get(5).text());
            bookInfo.setPlace(books.get(6).text());
            bookInfos.add(bookInfo);
        }

    }

    public List<NowBookInfo> getBookInfos(){
        return bookInfos;
    }
}
