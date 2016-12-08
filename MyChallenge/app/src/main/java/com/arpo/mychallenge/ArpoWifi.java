package com.arpo.mychallenge;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by jithin suresh on 30-11-2016.
 */
public class ArpoWifi {

    private Context mContext;
    private WifiManager mWifiManager;

    ArpoWifi(Context C)
    {
        mContext = C;
        mWifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
    }

    public boolean api_interface()
    {
        boolean result = true;

        return  result;
    }

    public boolean forgetConnectedWifi()
    {
        int networkId = mWifiManager.getConnectionInfo().getNetworkId();
        mWifiManager.removeNetwork(networkId);
        mWifiManager.saveConfiguration();
        return true;
    }
    public List<ScanResult> getWifiScanResults()
    {
       return mWifiManager.getScanResults();
    }
    public boolean startWifiScanning()
    {
        boolean result = true;
        mWifiManager.startScan();
        return  result;
    }

    public boolean isWifiConnected()
    {
        boolean result = true;

        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            result = true;
        }
        else
        {
            result = false;
        }
        return  result;
    }

    public boolean isWifi_Enabled()
    {
        boolean result = true;
        result = mWifiManager.isWifiEnabled();
        return  result;
    }
    public boolean turnOnWifi()
    {
        boolean result = true;
        mWifiManager.setWifiEnabled(true);
        return  result;
    }
    public boolean turnOffWifi()
    {
        boolean result = true;
        mWifiManager.setWifiEnabled(false);
        return  result;
    }
    public boolean turnoff_hotspot()
    {
        WifiConfiguration netConfig = new WifiConfiguration();

        try {
            Method setWifiApMethod = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            boolean apstatus = (Boolean) setWifiApMethod.invoke(mWifiManager, netConfig, false);
        }
        catch (Exception e)
        {

        }
        return  true;
    }

    public boolean connectTo_wifi(String ssid)
    {
        boolean result = true;

        WifiConfiguration wifiConfig = new WifiConfiguration();
        Log.d("JKS","Connect to "+ssid);
        wifiConfig.SSID = String.format("\"%s\"", ssid);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        // wifiConfig.preSharedKey = String.format("\"%s\"", "mostwaNted");

        int netId = mWifiManager.addNetwork(wifiConfig);
        Log.d("JKS","disconnect");
        mWifiManager.disconnect();
        Log.d("JKS","Enable network");
        mWifiManager.enableNetwork(netId, true);
        mWifiManager.reconnect();
        Log.d("JKS","Done");

        return result;
    }

    public boolean turnOn_hotspot(String ssid)
    {
        boolean result = true;

        WifiConfiguration netConfig = new WifiConfiguration();

        if(mWifiManager.isWifiEnabled())
        {
            mWifiManager.setWifiEnabled(false);
        }
        netConfig.SSID = ssid;
        netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        try {
            Method setWifiApMethod = mWifiManager.getClass().getMethod("setWifiApEnabled",  WifiConfiguration.class, boolean.class);
            boolean apstatus=(Boolean) setWifiApMethod.invoke(mWifiManager, netConfig,true);

            Method isWifiApEnabledmethod = mWifiManager.getClass().getMethod("isWifiApEnabled");
            while(!(Boolean)isWifiApEnabledmethod.invoke(mWifiManager)){};

            Method getWifiApStateMethod = mWifiManager.getClass().getMethod("getWifiApState");
            int apstate = (Integer)getWifiApStateMethod.invoke(mWifiManager);

            Method getWifiApConfigurationMethod = mWifiManager.getClass().getMethod("getWifiApConfiguration");
            netConfig = (WifiConfiguration)getWifiApConfigurationMethod.invoke(mWifiManager);

            Log.e("CLIENT", "\nSSID:"+netConfig.SSID+"\nPassword:"+netConfig.preSharedKey+"\n");
        } catch (Exception e) {
            Log.e(this.getClass().toString(), "", e);
        }

        return  result;
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

    public boolean isIpAddressPresent()
    {
        boolean result = false;

        String ip = getIpAddress();
        if(ip.equals(""))
        {
            Log.d("JKS","Didnt get ipAddress");
            result = false;
        }
        else
        {
            Log.d("JKS","Got ip address as "+ip);
            result = true;
        }
        return result;
    }
}
