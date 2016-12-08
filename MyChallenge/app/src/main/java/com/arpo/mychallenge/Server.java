package com.arpo.mychallenge;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by jithin on 1/12/16.
 */
public class Server {
    MainActivity activity;
    ServerSocket serverSocket;
    String message = "";
    static final int socketServerPORT = 8080;
    static final int MAX_DEVICES = 16;

    int count = 0;

    private class sockClass {
        public  Socket serverSocket;
        public int clientNumber;
    }
    sockClass [] clientsSockInfo  = new sockClass[16];

    public Server(MainActivity activity) {
        this.activity = activity;
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public boolean isConnected()    {
        boolean result = false;

        if(count > 0 )
            result = true;

        return result;
    }

    public void sendDummy()
    {
        for(int i = 0; i < count;i++)
        {
            print("Send message to client "+ clientsSockInfo[i].clientNumber);
        }
    }
    public Server() {


        for(int i = 0; i <MAX_DEVICES;i++)
        {
            print("sock info"+ i +" created");
            clientsSockInfo[i] = new sockClass();
            print("sock info"+ i +" created");
            clientsSockInfo[i].clientNumber = i;
        }
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public int getPort() {
        return socketServerPORT;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void print(String str)
    {
        Log.d("JKS", str);
    }

    private class SocketServerThread extends Thread {


        @Override
        public void run() {
            try {
                // create ServerSocket using specified port
                Thread.sleep(20);
                print("Start server with port ="+socketServerPORT);
                Log.d("JKS","server ip ="+getIpAddress());
                serverSocket = new ServerSocket(socketServerPORT);

                while (true) {
                    // block the call until connection is created and return
                    // Socket object
                    print("Wait to accept");

                    clientsSockInfo[count].serverSocket = serverSocket.accept();

                    message += "#" + count + " from "
                            + clientsSockInfo[count].serverSocket .getInetAddress() + ":"
                            + clientsSockInfo[count].serverSocket .getPort() + "\n";

                    print("#" + count + " from "
                            + clientsSockInfo[count].serverSocket .getInetAddress() + ":"
                            + clientsSockInfo[count].serverSocket .getPort() );

                    print("Accepted conection of client "+clientsSockInfo[count].clientNumber);

                    /*activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.msg.setText(message);
                        }
                    });*/

                    SocketServerReplyThread socketServerReplyThread =
                            new SocketServerReplyThread(clientsSockInfo[count].serverSocket , count);
                    socketServerReplyThread.run();
                    count++;

                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        int cnt;

        SocketServerReplyThread(Socket socket, int c) {
            hostThreadSocket = socket;
            cnt = c;
        }

        @Override
        public void run() {
            OutputStream outputStream;
            String msgReply = "Hello from Server, you are #" + cnt;

            try {
                outputStream = hostThreadSocket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(msgReply);
                printStream.close();

                message += "replayed: " + msgReply + "\n";

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }
        }

    }

    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip = inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip ="";
        }
        return ip;
    }
}