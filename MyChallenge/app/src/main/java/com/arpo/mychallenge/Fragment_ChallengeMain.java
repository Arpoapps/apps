package com.arpo.mychallenge;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_ChallengeMain.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_ChallengeMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_ChallengeMain extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private static final int REQUEST_FINE_LOCATION=0;

    BroadcastReceiver bcWifiList;

    String key = "Bar12345Bar12345"; // 128 bit key

    ArpoWifi mArpoWifiModule;
    public Fragment_ChallengeMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_ChallengeMain.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_ChallengeMain newInstance(String param1, String param2) {
        Fragment_ChallengeMain fragment = new Fragment_ChallengeMain();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void createSharedPreferences()
    {
        //public static final String MY_PREFS_NAME = "MyPrefsFile";
        SharedPreferences prefs = getContext().getSharedPreferences("AVATAR_INFO", getContext().MODE_PRIVATE);
        String restoredText = prefs.getString("name", null);
        if (restoredText != null) {
            String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
            int age = prefs.getInt("age", 0); //0 is the default value.
        }
        else {
            SharedPreferences.Editor editor = getContext().getSharedPreferences("AVATAR_INFO", getContext().MODE_PRIVATE).edit();
            editor.putString("name", "PushUpChallenger");
            editor.putInt("age", 25);
            editor.putInt("avatar", 1);
            editor.commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_fragment__challenge_main, container, false);

        mArpoWifiModule = new ArpoWifi(getContext());

        final ListView lv_challengeList = (ListView)rootView.findViewById(R.id.list_challengeList);

        createSharedPreferences();

        mayRequestLocation();

        if(mArpoWifiModule.isWifiConnected()) {
            Log.d("JKS","Wifi is connected");
        }
        else {
            Log.d("JKS","Wifi is not connected");
        }

        if(mArpoWifiModule.isWifi_Enabled() == false) {
            Toast.makeText(getContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            mArpoWifiModule.turnOnWifi();
        }

        bcWifiList = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub

                List<ScanResult> mScanResults = mArpoWifiModule.getWifiScanResults();
                List<ScanResult> challengesList = new ArrayList<>();
                for(ScanResult results : mScanResults)
                {
                    if(results.SSID.startsWith("ARPO")) {
                        String[] tokens = results.SSID.split("/");
             /*           for (String t : tokens)
                            Log.d("JKS", "s=" + t);
                        Log.d("JKS", "NAME:" + tokens[1] + " TYPE=" + tokens[2] + " COUNT=" + tokens[3]);*/
                        if (tokens[0].equals("ARPO")) {
                            challengesList.add(results);
                        }
                    }
                }

               // Log.d("JKS","Result Count="+mScanResults.size());
                ChallengesAdapter adapChallenge = new ChallengesAdapter(getContext(),challengesList);
                lv_challengeList.setAdapter(adapChallenge);
                lv_challengeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("JKS","Clicked on position="+position);
                        ScanResult wifiNode = (ScanResult)parent.getAdapter().getItem(position);
                        Log.d("JKS","Connect to "+wifiNode.SSID);

                        SharedPreferences.Editor editor = getContext().getSharedPreferences("GAME_INFO", getContext().MODE_PRIVATE).edit();
                        editor.putString("name", wifiNode.SSID);
                        editor.commit();


                        mArpoWifiModule.connectTo_wifi(wifiNode.SSID);

                        try {

                        Fragment nextFrag = ChallengePageClient.class.newInstance();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.flContent, nextFrag, "ChallengePageClient")
                                .addToBackStack(null)
                                .commit();

                        }catch(Exception ex)
                        {

                        }

                    }
                });
            }
        };


        getContext().registerReceiver(bcWifiList, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        Log.d("JKS", "Start scanning");

        mArpoWifiModule.startWifiScanning();

        Button createAvatar = (Button)rootView.findViewById(R.id.btn_create_avatar);
        createAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Fragment nextFrag = CreateAvatar.class.newInstance();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent, nextFrag, "create avatar")
                            .addToBackStack(null)
                            .commit();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        Button startMyChallenge = (Button)rootView.findViewById(R.id.btn_start_new_challenge);
        startMyChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("JKS","start popup");
                Intent popUpChallenge = new Intent(getContext(),CreateChallengePopUpActivity.class);
                startActivity(popUpChallenge);
            }
        });
        return  rootView;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // The requested permission is granted.
                } else {
                    // The user disallowed the requested permission.
                }
                return;
            }

        }
    }
    private boolean mayRequestLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("JKS","Return true may request permission");
            return true;
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.d("JKS","Cond 1");
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
            Log.d("JKS","Cond 2");
        }
        return false;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        getContext().unregisterReceiver(bcWifiList);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
