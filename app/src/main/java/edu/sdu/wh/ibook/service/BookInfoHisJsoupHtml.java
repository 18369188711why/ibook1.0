package edu.sdu.wh.ibook.service;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

import edu.sdu.wh.ibook.po.HisBookInfo;

/**
 *
 */
public class BookInfoHisJsoupHtml extends JsoupHtml{
    List<HisBookInfo> bookInfos;
    Document document;
    public BookInfoHisJsoupHtml(String html) {
        super(html);
        document=super.getDocument();
        this.parseHtml();
    }

    public List<HisBookInfo> getBookInfos() {
        return bookInfos;
    }

    @Override
    public void parseHtml() {
        Elements contents=document.select("div[id=\"mainbox\"]").
                select("div[id=\"container\"]").
                select("div[id=\"mylib_content\"]").
                select("table").
                select("tbody").
                select("tr");
        if(contents.size()==1)
        {
            bookInfos.add(new HisBookInfo());
            return;
        }
        for(int i=1;i<contents.size();i++)
        {
            HisBookInfo bookInfo = new HisBookInfo();
            Elements books = contents.get(i).select("td");
            bookInfo.setBarcode(books.get(1).text());
            bookInfo.setName(books.get(2).text());
            bookInfo.setAuthor(books.get(3).text());
            bookInfo.setBorrowDate(books.get(4).text());
            bookInfo.setReturnDate(books.get(5).text());
            bookInfo.setPlace(books.get(6).text());
            bookInfos.add(bookInfo);
        }
    }
}
