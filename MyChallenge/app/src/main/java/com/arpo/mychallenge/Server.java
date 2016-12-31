package com.arpo.mychallenge;

import android.app.Activity;
import android.content.SharedPreferences;
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
import java.util.List;

/**
 * Created by jithin on 1/12/16.
 */
public class Server {
    Activity activity;
    ServerSocket serverSocket;
    String message = "";
    static final int socketServerPORT = 8080;
    static final int MAX_DEVICES = 16;

    int count = -1;
    int msgCount = 0;

    AdapterChallengers avatarAdapter;
    List<ListAvatar> list;
    boolean exitFlag = true;
    ArpoPacket mainPacket;

    private class sockClass {
        public  Socket serverSocket;
        public int clientNumber;
        ObjectOutputStream serverOutObj;

    }
    sockClass [] clientsSockInfo  = new sockClass[16];

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
        packet.setMessageType(ArpoPacket.ARPO_PACKET_JUNK_MESSAGE);

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

    public Server(List<ListAvatar> lst, AdapterChallengers  adapter, Activity act) {

        list = lst;
        avatarAdapter = adapter;
        activity = act;

        count = 0;
        for(int i = 0; i <MAX_DEVICES;i++)
        {
            clientsSockInfo[i] = new sockClass();
            clientsSockInfo[i].clientNumber = i;
        }
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public void updateView (ArpoPacket packet)
    {
        mainPacket = packet;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListAvatar p1 = new ListAvatar();
                p1.setName(mainPacket.getClientName());
                p1.setPushUpTaken("0");
                p1.setPushUPTimeTaken("00:00:000");
                p1.setTakenChallenge(true);
                p1.setSelected(false);
                list.add(p1);
                avatarAdapter.notifyDataSetChanged();
            }
        });

    }

    public void sendStartChallenge()
    {
        ArpoPacket packet = new ArpoPacket();
        packet.setMessageType(ArpoPacket.ARPO_PACKET_START_CHALLENGE);

        for(int i = 0; i < count;i++)
        {
            packet.setMessage("Start challenge message from server to " + clientsSockInfo[i].clientNumber + " MESSAGE NUMBER=" + msgCount++);
            if(clientsSockInfo[i].serverOutObj == null)
            {
                print("print server is null");
            }
            else {

                try {
                    clientsSockInfo[i].serverOutObj.writeObject(packet);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendChallengerInfo()
    {
        ArpoPacket packet = new ArpoPacket();
        packet.setMessageType(ArpoPacket.ARPO_PACKET_REPOSNSE_CHALLENGERS);
        packet.setChallengerList(list);

        for(int i = 0; i < count;i++)
        {
            packet.setMessage("Challenger information from server to " + clientsSockInfo[i].clientNumber + " MESSAGE NUMBER=" + msgCount++);
            if(clientsSockInfo[i].serverOutObj == null)
            {
                print("print server is null");
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
            msg.setServerName("Game server");

            SharedPreferences prefs = activity.getSharedPreferences("AVATAR_INFO", activity.MODE_PRIVATE);
            String restoredText = prefs.getString("name", null);
            msg.setServerName(restoredText);

            try {

                ObjectInputStream objectInput = new ObjectInputStream(hostThreadSocket.getInputStream());

                outObj.writeObject(msg);

                while(exitFlag) {
                    //waiting for input
                    print("server waiting for data");
                    ArpoPacket packet = (ArpoPacket) objectInput.readObject();
                    print("Server got data = "+packet.getMessage());
                    int type = packet.getMessageType();
                    switch (type)
                    {
                        case ArpoPacket.ARPO_PACKET_RESPONSE_CLIENT_INFO:
                            print("got client name as "+packet.getClientName());
                            updateView(packet);
                            sendChallengerInfo();
                            break;
                    }
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