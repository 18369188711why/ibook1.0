package edu.sdu.wh.ibook.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/**
 * 解析Html文档，返回对应数据
 */
public abstract class JsoupHtml{
    private Document document;

    public JsoupHtml(String html){
        document= Jsoup.parse(html);
    }

    public Document getDocument() {
        return document;
    }

    public abstract void parseHtml();
}
