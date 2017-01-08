package com.arpo.mychallenge;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by jithin on 1/12/16.
 */
public class Client extends AsyncTask<Void, Void, Void> {

    String dstAddress;
    int dstPort;
    String response = "";
    TextView textResponse;
    boolean exitFlag = true;
    Socket socket = null;
    ObjectOutputStream clientOutObj;
    Activity uiActitivy;

    Button takeChallenge;
    List<ListAvatar> list;
    AdapterChallengers avatarAdapter;

    ArpoPacket mainPct;

    int Pcount = 0;

    void print(String str)
    {
        Log.d("JKS", str);
    }

    void printToScreen( String str) {
        final String printScr = str;
        uiActitivy.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textResponse.setText(printScr);
            }
        });
    }
    public void sendMsg()
    {
        ArpoPacket packet = new ArpoPacket();
        packet.setMessageType(ArpoPacket.ARPO_PACKET_JUNK_MESSAGE);
        packet.setMessage("HELLO FROM CLIENT");
        print("send messge from client");

        try {
            clientOutObj.writeObject(packet);
        }
        catch (Exception ex)
        {

        }
    }

    public void sendMyInfo()
    {
        ArpoPacket packet = new ArpoPacket();
        packet.setMessage("Client information");
        print("send client info to server");

        SharedPreferences prefs = uiActitivy.getSharedPreferences("AVATAR_INFO", uiActitivy.MODE_PRIVATE);
        String restoredText = prefs.getString("name", null);
        packet.setClientName(restoredText);
        packet.setMessageType(ArpoPacket.ARPO_PACKET_RESPONSE_CLIENT_INFO);

        try {
            clientOutObj.writeObject(packet);
        }
        catch (Exception ex)
        {

        }
    }

    public void sendMyResults(String count, String time, int uniqueId)
    {
        ArpoPacket packet = new ArpoPacket();
        packet.setMessage("Client Result");
        print("send client Result to server");

        SharedPreferences prefs = uiActitivy.getSharedPreferences("AVATAR_INFO", uiActitivy.MODE_PRIVATE);
        String restoredText = prefs.getString("name", null);

        packet.setClientName(restoredText);
        packet.setMessageType(ArpoPacket.ARPO_PACKET_CLIENT_RESULT);
        packet.setResultCount(count);
        packet.setResultTime(time);
        packet.setUniqueplayerID(uniqueId);


        try {
            clientOutObj.writeObject(packet);
        }
        catch (Exception ex)
        {

        }
    }

    private void updatePushUpResult(ArpoPacket pct)
    {

        print("got result for player with player id"+pct.getUniqueplayerID());
        ListAvatar item = list.get(pct.getUniqueplayerID());
        item.setPushUpTaken(pct.getResultCount());
        item.setPushUPTimeTaken(pct.getResultTime());
        print("update : "+item.getName()+" took"+item.getPushUpCount()+" pushups in"+item.getPushUpTimeTaken()+" seconds");
        uiActitivy.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                avatarAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateChallengerList(ArpoPacket packet)
    {

        SharedPreferences prefs = uiActitivy.getSharedPreferences("AVATAR_INFO", uiActitivy.MODE_PRIVATE);
        String restoredText = prefs.getString("name", null);

        list.clear();

        List <ListAvatar> tmp = packet.getChallengerList();
        for (ListAvatar item: tmp) {
            print(" IN OTHER THREAD name = " + item.getName());
            print(" name = " + item.getName() + " uniqueid=" + item.getUniqueId());
            item.setRemoteMachine(true);
            item.setSelected(false);
            if(item.getName().equals(restoredText))
            {
                item.setRemoteMachine(false);
                item.setSelected(true);
            }
            list.add(item);
        }

        mainPct = packet;

        print("UPDATE CHALLENGER LIST");
        printToScreen("UPDATE CHALLENGER LIST WITH LIST FROM SERVER");
        uiActitivy.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                avatarAdapter.notifyDataSetChanged();
            }
        });
    }

    public void updateServerName(ArpoPacket packet)
    {
        mainPct = packet;
        uiActitivy.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListAvatar p1 = new ListAvatar();
                p1.setName(mainPct.getServerName());
                p1.setPushUpTaken("0");
                p1.setPushUPTimeTaken("00:00:000");
                list.add(p1);
                avatarAdapter.notifyDataSetChanged();
            }
        });
    }

    Client(String addr, int port, TextView textResponse) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;

        Thread socketClientThread = new Thread(new SocketClientThread());
        socketClientThread.start();
    }

    Client(String addr, int port, TextView textResponse, Activity uiAct, List<ListAvatar> lst, AdapterChallengers adapter, Button takeChallenge) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
        this.uiActitivy = uiAct;
        this.takeChallenge = takeChallenge;
        list = lst;
        avatarAdapter = adapter;


        Thread socketClientThread = new Thread(new SocketClientThread());
        socketClientThread.start();
    }
    public void closeConnection()
    {
        exitFlag = false;

        try {
            socket.close();
        }
        catch (Exception e)
        {

        }
    }
    private class SocketClientThread extends Thread {


        @Override
        public void run() {

            try {
                Log.d("JKS","ip = "+getIpAddress());
                printToScreen("trying to connect");
                socket = new Socket(dstAddress, dstPort);

                printToScreen("CONNECTED");

                clientOutObj = new ObjectOutputStream(socket.getOutputStream());

                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = socket.getInputStream();

                ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());

         /*
          * notice: inputStream.read() will block if no data return
          */

                while (exitFlag) {

                    print("try to read");

                    //bytesRead = inputStream.read(buffer);
                    ArpoPacket packet = (ArpoPacket)objectInput.readObject();
                    print("Received packet");

                    response = packet.getMessage();
                    print("response   =" + response);
                    uiActitivy.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textResponse.setText(response);
                        }
                    });

                    int type = packet.getMessageType();
                    print(("TYPE=" + type));

                    switch (type)
                    {
                        case ArpoPacket.ARPO_PACKET_WELCOME:
                            print("STATE 1");
                            sendMyInfo();
                            //updateServerName(packet);
                            break;
                        case ArpoPacket.ARPO_PACKET_REPOSNSE_CHALLENGERS:
                            printToScreen("Received challenger information; updating");
                            List <ListAvatar> tmp = packet.getChallengerList();
                            for (ListAvatar item: tmp) {
                                print("received name = " + item.getName());
                            }
                            updateChallengerList(packet);
                            break;
                        case ArpoPacket.ARPO_PACKET_START_CHALLENGE:
                            printToScreen("Start Challenge");
                            uiActitivy.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    takeChallenge.setEnabled(true);
                                }
                            });

                            break;
                        case ArpoPacket.ARPO_PACKET_CHALLENGER_RESULT:
                            updatePushUpResult(packet);
                            break;
                    }


                    /*else
                    {
                        final String error = "Read = "+bytesRead;

                        //Thread.sleep(1000);
                        uiActitivy.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                textResponse.setText(error);
                        //        print("not reading againg");
                            }
                        });
                      //  print("not reading again");
                    }*/
                    //inputStream.close();
                   // else print("not getting any data");
                }


            }
            catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            }
            catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }
            //catch (InterruptedException ex) {
            //}

            /* finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }*/

        }
    }
    @Override
    protected Void doInBackground(Void... arg0) {
/*
        Socket socket = null;

        try {
            Log.d("JKS","ip = "+getIpAddress());
            socket = new Socket(d   stAddress, dstPort);


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                    1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            InputStream inputStream = socket.getInputStream();

         *//*
          * notice: inputStream.read() will block if no data return
          *//*
            boolean exitFlag = true;
            while (exitFlag) {
                print(" read data");
                bytesRead = inputStream.read(buffer);
               // print("read "+buffer);
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                response += byteArrayOutputStream.toString("UTF-8");
                print("response   ="+response);
            }
            print("Not reading again");

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }*/
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Log.d("JKS", "Response ="+response);
        textResponse.setText(response);
        super.onPostExecute(result);
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