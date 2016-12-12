package com.arpo.mychallenge;

import java.io.Serializable;

/**
 * Created by jithin on 11/12/16.
 */
public class ListAvatar implements Serializable {

    String name;
    String pushUpTaken;
    String pushUpTimeTaken;
    int img;

    public String getName()
    {
        return name;
    }

    public String getPushUpCount()
    {
        return pushUpTaken;
    }

    public String getPushUpTimeTaken()
    {
        return pushUpTimeTaken;
    }

    public void setName(String str)
    {
        name = str;
    }

    public void setPushUPTimeTaken(String str)
    {
        pushUpTimeTaken = str;
    }

    public void setPushUpTaken(String str)
    {
        pushUpTaken = str;
    }
}
