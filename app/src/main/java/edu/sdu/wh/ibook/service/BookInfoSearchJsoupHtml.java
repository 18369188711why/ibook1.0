package edu.sdu.wh.ibook.service;

import org.jsoup.nodes.Document;

import java.util.List;

import edu.sdu.wh.ibook.po.BookInfo;

/**
 *
 */
public class BookInfoSearchJsoupHtml extends JsoupHtml{
    private Document document;
    private List<BookInfo> bookInfos;

    public BookInfoSearchJsoupHtml(String html) {
        super(html);
        this.document=super.getDocument();
    }

    public List<BookInfo> getBookInfos() {
        return bookInfos;
    }

    @Override
    public void parseHtml() {

    }

}
