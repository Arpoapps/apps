package com.arpo.mychallenge;

import java.io.Serializable;

/**
 * Created by jithin on 11/12/16.
 */
public class ArpoPacket implements Serializable {
    static final int ARPO_PACKET_WELCOME = 0;
    static final int ARPO_PACKET_REQUEST = 1;
    static final int ARPO_PACKET_RESPONSE = 2;
    private int type;
    private String msg;

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
}
