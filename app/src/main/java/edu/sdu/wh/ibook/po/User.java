package edu.sdu.wh.ibook.po;

/**
 * <div id="mylib_info" >
 */
public class User {
    private String username;
    private String usernumber;
    private String userunit;
    private String usergender;
    private String msg;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {

        return msg;
    }

    public String getUsergender() {
        return usergender;
    }

    public void setUsergender(String usergender) {
        this.usergender = usergender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }

    public String getUserunit() {
        return userunit;
    }

    public void setUserunit(String userunit) {
        this.userunit = userunit;
    }

}
