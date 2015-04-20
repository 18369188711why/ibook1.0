package edu.sdu.wh.ibook.po;

/**
 *预约信息
 */
public class Preg {
    private String code;
    private String name_author;
    private String place;
    private String pregDate;
    private String deadDate;
    private String getPlace;
    private String status;
    private String link;

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName_author(String name_author) {
        this.name_author = name_author;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setPregDate(String pregDate) {
        this.pregDate = pregDate;
    }

    public void setDeadDate(String deadDate) {
        this.deadDate = deadDate;
    }

    public void setGetPlace(String getPlace) {
        this.getPlace = getPlace;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {

        return code;
    }

    public String getName_author() {
        return name_author;
    }

    public String getPlace() {
        return place;
    }

    public String getPregDate() {
        return pregDate;
    }

    public String getDeadDate() {
        return deadDate;
    }

    public String getGetPlace() {
        return getPlace;
    }

    public String getStatus() {
        return status;
    }
}
