package com.example.opencvexample.TCPDemo;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient {
    @Test
    public void main(){
        for (int i = 0; i < 3; i++) {
            commucatiom();
        }
    }
    public void commucatiom(){
        try {

            SocketServer socketServer = new SocketServer();

            Socket socket = new Socket("127.0.0.1",9090);
            OutputStream outputStream = socket.getOutputStream();
            new SocketClient().main();
            outputStream.write(new String("你好,服务器，我要建立连接，我是"+socket.getLocalPort()).getBytes());

            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len = inputStream.read(bytes);
            String s = new String(bytes, 0, len);
            System.out.println("来自服务器端："+s);
            if(s.contains("200")){
                outputStream.write("打开图片".getBytes());
            }
            System.out.println("接收图片");

            File file = new File("D:\\test.jpg");
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            while (socket.getInputStream().read(bytes)!= -1){
                fileOutputStream.write(bytes);
            }
            System.out.println("来自服务器端的图片大小是："+file.length()+"byte");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();//27055760
        }
    }
}
