package edu.sdu.wh.ibook.po;

/**
 *
 */
public class NowBookInfo {
    private String barcode;
    private String name_author;
    private String borrowDate;
    private String returnDate;
    private String renewNum;
    private String place;

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setName_author(String name_author) {
        this.name_author = name_author;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public void setRenewNum(String renewNum) {
        this.renewNum = renewNum;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getBarcode() {

        return barcode;
    }

    public String getName_author() {
        return name_author;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getRenewNum() {
        return renewNum;
    }

    public String getPlace() {
        return place;
    }
}
