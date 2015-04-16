package edu.sdu.wh.ibook.po;

/**
 *
 */
public class Fine {
    private String barcode;
    private String code;
    private String name;
    private String charge;
    private String borrDate;
    private String shouldReturnDate;
    private String place;
    private String fine;
    private String pay;
    private String status;

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public void setBorrDate(String borrDate) {
        this.borrDate = borrDate;
    }

    public void setShouldReturnDate(String shouldReturnDate) {
        this.shouldReturnDate = shouldReturnDate;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBarcode() {

        return barcode;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCharge() {
        return charge;
    }

    public String getBorrDate() {
        return borrDate;
    }

    public String getShouldReturnDate() {
        return shouldReturnDate;
    }

    public String getPlace() {
        return place;
    }

    public String getFine() {
        return fine;
    }

    public String getPay() {
        return pay;
    }

    public String getStatus() {
        return status;
    }
}
