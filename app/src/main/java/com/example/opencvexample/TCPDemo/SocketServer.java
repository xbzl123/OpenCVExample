package com.example.opencvexample.TCPDemo;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    @Test
    public void main(){
        try {
            ServerSocket socketServer = new ServerSocket(9090);

            while (true){
                Socket socket = socketServer.accept();
                new Thread(new Task(socket)).start();
            }
//            socketServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String getMessage(Socket socket) {
        byte[] bytes = new byte[1024];
        int len = 0;
        try {
            InputStream inputStream = socket.getInputStream();
            len = inputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = new String(bytes, 0, len);
        return s; }

    private class Task implements Runnable {
        private Socket socket;
        public Task(Socket socketServer) {
            this.socket = socketServer;
        }

        @Override
        public void run() {
            try{
            String message1 = getMessage(socket);
            System.out.println("来自客户端：" + message1);

            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("收到，建立连接了,200".getBytes());

            String message = getMessage(socket);
            System.out.println("来自客户端：" + message);

            if (message.equals("打开图片")) {
                File file = new File("D:\\OpenCV-android-sdk\\OpenCVExample\\app\\src\\main\\res\\drawable\\timg.jpg");
                InputStream fileInputStream = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                while (fileInputStream.read(bytes)!= -1){
                    outputStream.write(bytes);
                }
//                int len = 0;
//                while ((len = fileInputStream.read(bytes))!= -1){
//                    outputStream.write(bytes,0,len);
//                }
                System.out.println("发送图片");
            }
            socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}