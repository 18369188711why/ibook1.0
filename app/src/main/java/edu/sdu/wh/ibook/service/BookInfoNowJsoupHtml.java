package edu.sdu.wh.ibook.service;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
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

        bookInfos=new ArrayList<NowBookInfo>();

        if(contents.size()==1)
        {
            bookInfos.add(null);
        }else{
            for(int i=1;i<contents.size();i++)
            {
                NowBookInfo bookInfo = new NowBookInfo();
                Elements book=contents.get(i).select("td");
                bookInfo.setBarcode(book.get(0).text().trim());
                bookInfo.setName_author(book.get(1).text().trim());
                bookInfo.setLink(book.get(1).select("a").first().attr("href"));
                bookInfo.setBorrowDate(book.get(2).text().trim());
                bookInfo.setReturnDate(book.get(3).text().trim());
                bookInfo.setRenewNum(book.get(4).text().trim());
                bookInfo.setPlace(book.get(5).text().trim());
                bookInfos.add(bookInfo);
            }
        }
    }

    public List<NowBookInfo> getBookInfos(){
        return bookInfos;
    }
}
