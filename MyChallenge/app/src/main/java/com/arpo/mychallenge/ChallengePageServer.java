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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChallengePageServer.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChallengePageServer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChallengePageServer extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Server mServer;

    private GridView gv_avatar;
    AdapterChallengers avatarAdapter;
    List<ListAvatar> list;

    private ArpoWifi wifiModule;

    public ChallengePageServer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChallengePageServer.
     */
    // TODO: Rename and change types and number of parameters
    public static ChallengePageServer newInstance(String param1, String param2) {
        ChallengePageServer fragment = new ChallengePageServer();
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

    public void print(String str)
    {
        Log.d("JKS", str);
    }

    private void fillAvatarInfo()
    {
        list = new ArrayList<>();
        ListAvatar p1 = new ListAvatar();
        // adding self information
        {

            SharedPreferences prefs = getContext().getSharedPreferences("AVATAR_INFO", getContext().MODE_PRIVATE);
            String restoredText = prefs.getString("name", null);
            p1.setName(restoredText);
            p1.setPushUpTaken("0");
            p1.setPushUPTimeTaken("00:00:000");
            list.add(p1);
        }

        avatarAdapter = new AdapterChallengers(getContext(), list);
        gv_avatar.setAdapter(avatarAdapter);
        //gv_avatar.setOnItemClickListener(this);


    }

    public void addChallengerToGridView() {

        ListAvatar p1 = new ListAvatar();
        p1.setName("Challenger");
        p1.setPushUpTaken("0");
        p1.setPushUPTimeTaken("00:00:000");
        list.add(p1);
        avatarAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_challenge_page_server, container, false);


        gv_avatar = (GridView)rootView.findViewById(R.id.grdViewServer);
        gv_avatar.setNumColumns(2);
        fillAvatarInfo();

        new Thread(new Runnable() {
            public void run(){
                wifiModule = new ArpoWifi(getContext());
                try {

                    while(wifiModule.isIpAddressPresent() == false)
                    {
                        Thread.sleep(500);
                    }
                    print("GOT IP ADDRESS");
                    Thread.sleep(500);
                }catch (Exception e)
                {

                }
                print("Start server");
                mServer = new Server(list, avatarAdapter, getActivity());

                //wifiModule.startServer();
            }
        }).start();


        final Button btn_start = (Button) rootView.findViewById(R.id.btn_startMyChallenge);
        final Button btn_takeChallenge = (Button) rootView.findViewById(R.id.btn_take_challenge);
        Button btn_addChallenger = (Button) rootView.findViewById(R.id.btn_addChallenger);

        btn_takeChallenge.setEnabled(false);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServer.sendStartChallenge();
                btn_start.setEnabled(false);
                btn_takeChallenge.setEnabled(true);
            }
        });

        btn_addChallenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print("Add a player");
            }
        });

        btn_takeChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print("Take Challenge");
            }
        });
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
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mServer.closeConnection();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name

        void onFragmentInteraction(Uri uri);
    }
}
