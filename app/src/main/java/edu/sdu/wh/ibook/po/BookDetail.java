package edu.sdu.wh.ibook.po;

/**
 * 书籍详细信息类
 */
public class BookDetail {
    private String barcode;
    private String year;
    private String school_place;
    private String status;

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setSchool_place(String school_place) {
        this.school_place = school_place;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getYear() {
        return year;
    }

    public String getSchool_place() {
        return school_place;
    }

    public String getStatus() {
        return status;
    }

}
