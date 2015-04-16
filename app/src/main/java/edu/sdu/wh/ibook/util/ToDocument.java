package edu.sdu.wh.ibook.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 */
public class ToDocument {
    public static Document getDocument(String html) {
        return Jsoup.parse(html);
    }
}
