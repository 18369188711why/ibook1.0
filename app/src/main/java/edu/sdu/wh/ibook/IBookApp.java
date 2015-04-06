package edu.sdu.wh.ibook;

import android.app.Application;


import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.net.URI;
import java.util.List;

import edu.sdu.wh.ibook.po.BookInfo;
import edu.sdu.wh.ibook.po.MyComment;
import edu.sdu.wh.ibook.po.User;

/**
 *
 */
public class IBookApp extends Application{
    private static User user;
    private String nickName;
    private List<BookInfo> bookInfos;
    private List<BookInfo> hisBookInfos;
    private List<MyComment> mydocuments;
    private int nowNumber,hisNumber;
    private static DefaultHttpClient httpClient;
    private static Cookie cookie;
    private static CookieStore cookieStore;
    private static HttpContext httpContext;
    private static int DEFAULT_SOCKET_TIMEOUT=8000,DEFAULT_HOST_CONNECTIONS=1,
            DEFAULT_MAX_CONNECTIONS=2,DEFAULT_SOCKET_BUFFER_SIZE=1000;


    public static CookieStore getCookieStore() {
        return cookieStore;
    }

    public static HttpContext getHttpContext() {
        return httpContext;
    }

    public static void setUser(User user) {
        IBookApp.user = user;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setBookInfos(List<BookInfo> bookInfos) {
        this.bookInfos = bookInfos;
    }

    public void setHisBookInfos(List<BookInfo> hisBookInfos) {
        this.hisBookInfos = hisBookInfos;
    }

    public void setMydocuments(List<MyComment> mydocuments) {
        this.mydocuments = mydocuments;
    }

    public void setNowNumber(int nowNumber) {
        this.nowNumber = nowNumber;
    }

    public void setHisNumber(int hisNumber) {
        this.hisNumber = hisNumber;
    }

    public synchronized void setHttpClient(DefaultHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public static User getUser() {
        return user;
    }

    public static void setCookieStore(CookieStore cookieStore) {
        IBookApp.cookieStore = cookieStore;
    }

    public String getNickName() {
        return nickName;
    }

    public List<BookInfo> getBookInfos() {
        return bookInfos;
    }

    public List<BookInfo> getHisBookInfos() {
        return hisBookInfos;
    }

    public List<MyComment> getMydocuments() {
        return mydocuments;
    }

    public int getNowNumber() {
        return nowNumber;
    }

    public int getHisNumber() {
        return hisNumber;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        this.createHttpClient();
    }

    public static void setCookie(Cookie cookie) {
        IBookApp.cookie = cookie;
    }

    public static Cookie getCookie() {

        return cookie;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        this.shutdownHttpClient();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        this.shutdownHttpClient();
    }
    //创建HttpClient实例
    private void createHttpClient() {
        if(httpClient == null) {
            final HttpParams httpParams = new BasicHttpParams();
            // timeout: get connections from connection pool
            ConnManagerParams.setTimeout(httpParams, 1000);
            // timeout: connect to the server
            HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
            // timeout: transfer data from server
            HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
            // set max connections per host
            ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(DEFAULT_HOST_CONNECTIONS));
            // set max total connections
            ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
            // use expect-continue handshake
            HttpProtocolParams.setUseExpectContinue(httpParams, true);
            // disable stale check
            HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
            HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
            HttpClientParams.setRedirecting(httpParams, false);
            // set user agent
            String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
            HttpProtocolParams.setUserAgent(httpParams, userAgent);
            // disable Nagle algorithm
            HttpConnectionParams.setTcpNoDelay(httpParams, true);
            HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);
            // scheme: http and https
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

            this.httpClient = new DefaultHttpClient(manager, httpParams);

            httpClient.setRedirectHandler(
                    new DefaultRedirectHandler(){
                        @Override
                        public boolean isRedirectRequested(
                                HttpResponse response,
                                HttpContext context) {
                            System.out.println("isRedirectRequested_response code:"+
                                    response.getStatusLine()
                                            .getStatusCode() + "");
                            return true;
                        }
                        @Override
                        public URI getLocationURI(
                                HttpResponse response,
                                HttpContext context)
                                throws ProtocolException {
                            // TODO Auto-generated method stub
                            return null;
                        }
                    }
            );
            httpContext=new BasicHttpContext();
            cookie=new BasicClientCookie("","");
            httpContext.setAttribute(ClientContext.COOKIE_STORE,cookie);

        }
    }
    //关闭连接管理器并释放资源
    private void shutdownHttpClient() {
          if (httpClient != null && httpClient.getConnectionManager() != null) {
               httpClient.getConnectionManager().shutdown();
          }
    }
    //对外提供HttpClient实例
    public static synchronized DefaultHttpClient getHttpClient() {
        return httpClient;
    }
}