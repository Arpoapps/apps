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

        print("Count = " + count);
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
        ListAvatar p1 = new ListAvatar();
        p1.setName(mainPacket.getClientName());
        p1.setPushUpTaken("0");
        p1.setPushUPTimeTaken("00:00:000");
        p1.setTakenChallenge(false);
        p1.setSelected(false);
        p1.setRemoteMachine(true);
        p1.setUniqueId(list.size());
        list.add(p1);
        print("unique id of " + p1.getName() + " is set as " + p1.getUniqueId());
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                avatarAdapter.notifyDataSetChanged();

            }
        });

    }

    private void updateResult(ArpoPacket packet)
    {
        mainPacket = packet;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListAvatar p1 = list.get(mainPacket.getUniqueplayerID());
                p1.setPushUpTaken(mainPacket.getResultCount());
                p1.setPushUPTimeTaken(mainPacket.getResultTime());
                avatarAdapter.notifyDataSetChanged();
                p1.setTakenChallenge(true);
                print("Player "+p1.getName()+" took "+p1.getPushUpCount()+" pushups in "+p1.getPushUpTimeTaken()+" seconds");
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

    public void sendResultToClient(String Strcount, String time, int id)
    {
        ArpoPacket packet = new ArpoPacket();
        packet.setMessage("Client Result");
        print("send client Result to server");

        SharedPreferences prefs = activity.getSharedPreferences("AVATAR_INFO", activity.MODE_PRIVATE);
        String restoredText = prefs.getString("name", null);

        packet.setClientName(restoredText);
        packet.setMessageType(ArpoPacket.ARPO_PACKET_CHALLENGER_RESULT);
        packet.setResultCount(Strcount);
        packet.setResultTime(time);
        packet.setUniqueplayerID(id);

        for(int i = 0; i < count;i++)
        {
            packet.setMessage("PushUp Result: " + clientsSockInfo[i].clientNumber + " MESSAGE NUMBER=" + msgCount++);
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

    public void sendChallengerInfo()
    {
        ArpoPacket packet = new ArpoPacket();
        packet.setMessageType(ArpoPacket.ARPO_PACKET_REPOSNSE_CHALLENGERS);
        packet.setChallengerList(list);

        for (ListAvatar item:list
             ) {
            print("ChallangerInfo is sent to client : challenger:"+item.getName());

        }

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
                if(clientsSockInfo[i].serverSocket != null)
                clientsSockInfo[i].serverSocket.close();
            }
            catch (Exception ex)
            {

            }
        }
    }

    private boolean isGameOver()
    {
        boolean result = true;
        for (ListAvatar item: list
                ) {
            if(item.isTakenChallenge() == false) {
                result = false;
                break;
            }

        }

        return result;
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
                        case ArpoPacket.ARPO_PACKET_CLIENT_RESULT:
                            print("got Result from client "+packet.getClientName() +" unique id="+ packet.getUniqueplayerID());
                            updateResult(packet);

                            if(isGameOver())
                            {
                                //showResults();
                                print("GAME OVER");
                            }
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