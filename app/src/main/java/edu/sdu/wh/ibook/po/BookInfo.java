package edu.sdu.wh.ibook.po;

/**
 *当前借阅和借阅历史<div id="mylib_content" >
 */

public class BookInfo {
    private String name;
    private String ISBN;
    private String type;
    private String author;
    private String storedNum;
    private String publisher;
    private String avaiableNum;

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAvaiableNum(String avaiableNum) {
        this.avaiableNum = avaiableNum;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setStoredNum(String storedNum) {
        this.storedNum = storedNum;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getAuthor() {
        return author;
    }

    public String getAvaiableNum() {
        return avaiableNum;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getStoredNum() {
        return storedNum;
    }
}
