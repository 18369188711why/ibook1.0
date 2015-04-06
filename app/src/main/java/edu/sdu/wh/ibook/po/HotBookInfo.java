package edu.sdu.wh.ibook.po;

/**
 * 热门检索图书信息
 */
public class HotBookInfo {
    private String name;
    private String author;
    private String publisher;
    private String code;
    private String placeInfo;
    private String borrowNum;
    private String borrowRate;
    private String link;

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPlaceInfo(String placeInfo) {
        this.placeInfo = placeInfo;
    }

    public void setBorrowNum(String borrowNum) {
        this.borrowNum = borrowNum;
    }

    public void setBorrowRate(String borrowRate) {
        this.borrowRate = borrowRate;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {

        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getCode() {
        return code;
    }

    public String getPlaceInfo() {
        return placeInfo;
    }

    public String getBorrowNum() {
        return borrowNum;
    }

    public String getBorrowRate() {
        return borrowRate;
    }

    public String getLink() {
        return link;
    }
}
