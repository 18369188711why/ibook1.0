package edu.sdu.wh.ibook.service;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
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
        bookInfos=new ArrayList<HisBookInfo>();
        if(contents.size()==1)
        {
            bookInfos.add(null);
            return;
        }
        for(int i=1;i<contents.size();i++)
        {
            HisBookInfo bookInfo = new HisBookInfo();
            Elements book=contents.get(i).select("td");

            bookInfo.setBarcode(book.get(1).text().trim());
            bookInfo.setName(book.get(2).text().trim());
            bookInfo.setLink(book.get(2).select("a").first().attr("href"));
            bookInfo.setAuthor(book.get(3).text().trim());
            bookInfo.setBorrowDate(book.get(4).text().trim());
            bookInfo.setReturnDate(book.get(5).text().trim());
            bookInfo.setPlace(book.get(6).text().trim());
            bookInfos.add(bookInfo);
        }
    }
}
