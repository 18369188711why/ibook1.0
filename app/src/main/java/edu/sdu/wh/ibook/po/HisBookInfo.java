package edu.sdu.wh.ibook.po;

/**
 *借阅历史的书籍信息
 */
public class HisBookInfo {
    private String barcode;
    private String name;
    private String author;
    private String borrowDate;
    private String returnDate;
    private String place;
    private String link;

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getPlace() {
        return place;
    }

    public String getLink() {

        return link;
    }
}
