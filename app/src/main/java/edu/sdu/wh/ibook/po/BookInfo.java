package edu.sdu.wh.ibook.po;

/**
 *搜索结果的书籍信息
 */

public class BookInfo {
    private String name_code;
    private String type;
    private String author_publisher;
    private String stored_available_Num;
    private String link;


    public void setName_code(String name_code) {
        this.name_code = name_code;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAuthor_publisher(String author_publisher) {
        this.author_publisher = author_publisher;
    }

    public void setStored_available_Num(String stored_available_Num) {
        this.stored_available_Num = stored_available_Num;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName_code() {

        return name_code;
    }

    public String getType() {
        return type;
    }

    public String getAuthor_publisher() {
        return author_publisher;
    }

    public String getStored_available_Num() {
        return stored_available_Num;
    }

    public String getLink() {

        return link;
    }
}
