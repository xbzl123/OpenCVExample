package com.example.opencvexample.TCPDemo;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpServer {
    URL uri;
    @Test
    public void test() {
        try {
//            HttpClient client = new DefaultHttpClient();
//            HttpGet httpGet = new HttpGet();
//            httpGet.setURI(new URI(urlStr));
//            HttpResponse response = client.execute(httpGet);
//            HttpEntity entity  = response.getEntity();
//            fileSize = entity.getContentLength();
//            client.getConnectionManager().shutdown();
            uri = new URL("https://time.geekbang.org/column/intro/100039001");
            HttpURLConnection httpServer = (HttpURLConnection) uri.openConnection();
            httpServer.connect();
            int responseCode = httpServer.getResponseCode();
            System.out.println("返回结果是："+responseCode+",请求消息类型是："+httpServer.getContentType()+",大小是："+httpServer.getInputStream().available());
            if(responseCode == 200){
                //获取接收到是数据
                InputStream inputStream = httpServer.getInputStream();
                byte[] bytes = new byte[1024];
                File file = new File("D:\\test1.html");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while (inputStream.read(bytes)!=-1){
//                    System.out.println(new String(bytes));
                    fileOutputStream.write(bytes);
                }
                //发送输出去的数据
//                OutputStream outputStream = httpServer.getOutputStream();
//                outputStream.write(bytes);
                fileOutputStream.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
@Test
    public void test1(){
        String tmp1 = "111";
        String tmp2 = "112";
        System.out.println(tmp1.compareTo(tmp2));
        System.out.println(tmp1.equals(tmp2));
}


}
