package com.arpo.mychallenge;

import java.io.Serializable;

/**
 * Created by jithin on 11/12/16.
 */
public class ArpoPacket implements Serializable {
    static  final int ARPO_PACKET_JUNK_MESSAGE = -1;
    public static final int ARPO_PACKET_WELCOME = 0;
    static final int ARPO_PACKET_REQUEST = 1;
    static final int ARPO_PACKET_RESPONSE = 2;
    static final int ARPO_PACKET_RESPONSE_CLIENT_INFO = 0;

    private int type;
    private String msg;

    private String serverName;

    private String clientName;

    ArpoPacket()
    {
        type = ARPO_PACKET_WELCOME;
        msg = "Hello from arpo server, you are connected ";
    }

    public String getMessage()
    {
        return msg;
    }

    public void setMessage(String str)
    {
        msg = str;
    }

    public void appendMessage(String str)
    {
        msg = msg+ " " +str;
    }

    public String getServerName()
    {
        return serverName;
    }

    public void setServerName(String str)
    {
        serverName = str;
    }

    public String getClientName()
    {
        return clientName;
    }

    public void setClientName(String str)
    {
        clientName = str;
    }

    public int getMessageType()
    {
        return type;
    }

    public void setMessageType(int msgType)
    {
        type = msgType;
    }


}
