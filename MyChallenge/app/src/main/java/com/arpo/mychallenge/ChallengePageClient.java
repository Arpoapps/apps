package com.arpo.mychallenge;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChallengePageClient.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChallengePageClient#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChallengePageClient extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Client arpoClient;
    ArpoWifi wifiModule;
    GridView gv_avatarCl;
    List<ListAvatar> list;
    AdapterChallengers avatarAdapter;

    public void print(String str)
    {
        Log.d("JKS",str);
    }

    public ChallengePageClient() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChallengePageClient.
     */
    // TODO: Rename and change types and number of parameters
    public static ChallengePageClient newInstance(String param1, String param2) {
        ChallengePageClient fragment = new ChallengePageClient();
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

    private void fillAvatarInfo()
    {
        list = new ArrayList<>();
        ListAvatar p1 = new ListAvatar();
        // adding self information
        // not filling client information, it will come from server
        /*
        {

            SharedPreferences prefs = getContext().getSharedPreferences("AVATAR_INFO", getContext().MODE_PRIVATE);
            String restoredText = prefs.getString("name", null);
            p1.setName(restoredText);
            p1.setPushUpTaken("0");
            p1.setPushUPTimeTaken("00:00:000");
            list.add(p1);
        }
*/
        avatarAdapter = new AdapterChallengers(getContext(), list);
        gv_avatarCl.setAdapter(avatarAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_challenge_page_client, container, false);
        final TextView msg = (TextView) rootView.findViewById(R.id.txt_msg);

        gv_avatarCl = (GridView)rootView.findViewById(R.id.gv_client);
        gv_avatarCl.setNumColumns(2);
        fillAvatarInfo();


        new Thread(new Runnable() {
            public void run(){
                wifiModule = new ArpoWifi(getContext());

                try {
                    while(wifiModule.isIpAddressPresent() == false)
                    {
                        Thread.sleep(5);
                    }
                    print("GOT IP ADDRESS");
                    Thread.sleep(3000);
                    print("TRY CONNECTING");
                    arpoClient = new Client("192.168.43.1", 8080, msg,getActivity(),list,avatarAdapter);

                    //wifiModule.startClient("192.168.43.1", 8080, msg);
                    //arpoClient.execute();
                }catch (Exception e)
                {

                }
            }
        }).start();

        return rootView;
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
        print("Detach client");
        arpoClient.closeConnection();
        mListener = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        arpoClient.closeConnection();
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
