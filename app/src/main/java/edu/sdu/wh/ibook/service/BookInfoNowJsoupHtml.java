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
                bookInfo.setBarcode(contents.get(i).select("td").get(0).text());
                bookInfo.setName_author(contents.get(i).select("td").get(1).text());
                bookInfo.setBorrowDate(contents.get(i).select("td").get(2).text());
                bookInfo.setReturnDate(contents.get(i).select("td").get(3).text());
                bookInfo.setRenewNum(contents.get(i).select("td").get(4).text());
                bookInfo.setPlace(contents.get(i).select("td").get(5).text());
                bookInfos.add(bookInfo);
            }
        }
    }

    public List<NowBookInfo> getBookInfos(){
        return bookInfos;
    }
}
