package com.arpo.mychallenge;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
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
        packet.setMessage("HELLO FROM CLIENT");
        print("send messge from client");

        try {
            clientOutObj.writeObject(packet);
        }
        catch (Exception ex)
        {

        }


    }

    Client(String addr, int port, TextView textResponse) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;

        Thread socketClientThread = new Thread(new SocketClientThread());
        socketClientThread.start();
    }

    Client(String addr, int port, TextView textResponse, Activity uiAct) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
        this.uiActitivy = uiAct;


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
                    print("read to object");
                    //bytesRead = inputStream.read(buffer);
                    ArpoPacket packet = (ArpoPacket)objectInput.readObject();
                    print("Received packet");
                    int state = 1;

                    switch (state)
                    {
                        case 1: print("STATE 1");
                            break;
                        case 2: print("STATE 2");
                            break;
                        case 3: print("STATE 3");
                            break;
                        case 4: print("STATE 4");
                            break;
                        case 5: print("STATE 5");
                            break;
                        case 6: print("STATE 7");
                            break;
                    }

                    print("Read");
                  //  if(bytesRead != -1) {
                    {
                     //   ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                      //  byteArrayOutputStream.write(buffer, 0, bytesRead);

                        //response = byteArrayOutputStream.toString("UTF-8");
                        response = packet.getMessage();
                        print("response   =" + response);
                        uiActitivy.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textResponse.setText(response);
                            }
                        });
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