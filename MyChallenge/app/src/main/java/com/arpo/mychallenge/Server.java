package com.arpo.mychallenge;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
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

    int count = -1;
    int msgCount = 0;

    boolean exitFlag = true;

    private class sockClass {
        public  Socket serverSocket;
        public int clientNumber;
        //PrintWriter serverOut;
        ObjectOutputStream serverOutObj;
    }
    sockClass [] clientsSockInfo  = new sockClass[16];

    public Server(MainActivity activity) {
        this.activity = activity;
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public boolean isConnected()    {
        boolean result = false;

        print("Count = "+count);
        if(count > 0 )
            result = true;

        return result;
    }

    public void sendDummy()
    {
        ArpoPacket packet = new ArpoPacket();

        for(int i = 0; i < count;i++)
        {
            packet.setMessage("JKS Hello from server to client " + clientsSockInfo[i].clientNumber + " MESSAGE NUMBER=" + msgCount++);
            if(clientsSockInfo[i].serverOutObj == null)
            {
                print("print servet is null");
            }
            else {

                try {
                    clientsSockInfo[i].serverOutObj.writeObject(packet);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                //clientsSockInfo[i].serverOut.println(");
            }
        }
    }
    public Server() {


        count = 0;
        for(int i = 0; i <MAX_DEVICES;i++)
        {
            clientsSockInfo[i] = new sockClass();
            clientsSockInfo[i].clientNumber = i;
        }
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public void closeConnection()
    {
        exitFlag = false;
        for(int i = 0; i <MAX_DEVICES;i++)
        {
            try {
                clientsSockInfo[i].serverSocket.close();
            }
            catch (Exception ex)
            {

            }
        }
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
                print("server ip ="+getIpAddress());
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

                    clientsSockInfo[count].serverOutObj =new ObjectOutputStream(clientsSockInfo[count].serverSocket.getOutputStream());

                    if(clientsSockInfo[count].serverOutObj == null)
                    {
                        print("Couldnt get serverOut it is null");
                    }

                    SocketServerReplyThread socketServerReplyThread =
                            new SocketServerReplyThread(clientsSockInfo[count].serverSocket , count , clientsSockInfo[count].serverOutObj);
                    socketServerReplyThread.start();
                    count++;
                    print("INCREMENTED count to " + count);



                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        private ObjectOutputStream outObj;
        int cnt;

        SocketServerReplyThread(Socket socket, int c, ObjectOutputStream outSocket) {
            hostThreadSocket = socket;
            cnt = c;
            outObj = outSocket;
        }

        @Override
        public void run() {

            ArpoPacket msg = new ArpoPacket();

            msg.appendMessage("you are #" + cnt);


            try {

                ObjectInputStream objectInput = new ObjectInputStream(hostThreadSocket.getInputStream());

                outObj.writeObject(msg);

                while(exitFlag) {
                    //waiting for input
                    print("server waiting for data");
                    ArpoPacket packet = (ArpoPacket) objectInput.readObject();
                    print("Server got data = "+packet.getMessage());
                    Thread.sleep(500);
                }

            } catch (ClassNotFoundException ex)
            {
                ex.printStackTrace();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }
            catch (InterruptedException e) {
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