package edu.sdu.wh.ibook.service;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import edu.sdu.wh.ibook.po.HotBookInfo;

/**
 *
 */
public class BookInfoHotJsoupHtml extends JsoupHtml{
    private Document document;
    private List<HotBookInfo> hotBookInfos;

    public BookInfoHotJsoupHtml(String html) {
        super(html);
        document=super.getDocument();
        this.parseHtml();
    }

    @Override
    public void parseHtml() {
        hotBookInfos=new ArrayList<HotBookInfo>();
        Elements contents=document.select("div[id=\"mainbox\"]").
                select("div[id=\"container\"]").
                select("table[class=\"table_line\"]").
                select("tbody").
                select("tr");
        for (int i = 1; i <contents.size() ; i++) {
            HotBookInfo hot=new HotBookInfo();
            Elements book=contents.get(i).select("td");
            hot.setName(book.get(0).text()+"、"+book.get(1).text());
            hot.setLink(book.get(1).select("a").first().attr("href"));
            hot.setAuthor(book.get(2).text());
            hot.setPublisher(book.get(3).text());
            hot.setCode(book.get(4).text());
            hot.setPlaceInfo(book.get(5).text());
            hot.setBorrowNum(book.get(6).text());
            hot.setBorrowRate(book.get(7).text());
            hotBookInfos.add(hot);
        }
    }

    public List<HotBookInfo> getHotBookInfos() {
        return hotBookInfos;
    }
}
