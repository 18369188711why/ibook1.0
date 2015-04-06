package edu.sdu.wh.ibook.po;

/**
 *
 */
public class Comment {
    private String name;
    private String time;
    private String content;

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {

        return name;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }
}
