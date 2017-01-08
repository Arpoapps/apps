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
    boolean selected;
    boolean takenTest;
    boolean isRemote;
    int uniqueId;

    ListAvatar ()
    {
        selected = false;
        takenTest = false;
        isRemote = false;
    }

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

    public boolean getSelected()   {
        return selected;
    }

    public boolean isTakenChallenge() { return  takenTest;}

    public void setTakenChallenge(boolean value) { takenTest = value; }

    public void setSelected(boolean value) { selected = value;}

    public void setName(String str){
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

    public boolean isRemoteMachine() { return isRemote; }

    public void setRemoteMachine(boolean value) { isRemote = value;}

    public int getUniqueId() { return  uniqueId;}

    public void setUniqueId(int uId) { uniqueId = uId; }
}
