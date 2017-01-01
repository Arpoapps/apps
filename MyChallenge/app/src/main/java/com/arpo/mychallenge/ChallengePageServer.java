package com.arpo.mychallenge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


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

    int playerCount = 0;
    int selectedPlayer = 0;

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
            p1.setSelected(true);
            p1.setTakenChallenge(false);
            selectedPlayer = 0;
            list.add(p1);
        }

        avatarAdapter = new AdapterChallengers(getContext(), list);

        gv_avatar.setAdapter(avatarAdapter);


        gv_avatar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // DO something

                if (selectedPlayer == position) return;

                ListAvatar selectedGrid = list.get(selectedPlayer);
                selectedGrid.setSelected(false);

                ListAvatar gridItem = list.get(position);
                gridItem.setSelected(true);

                selectedPlayer = position;
                avatarAdapter.notifyDataSetChanged();

            }
        });


    }

    public void addChallengerToGridView() {

        ListAvatar p1 = new ListAvatar();
        p1.setName("Challenger");
        p1.setPushUpTaken("0");
        p1.setPushUPTimeTaken("00:00:000");
        p1.setSelected(false);
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

                Intent addPlayer = new Intent(getContext(), AddPlayerPopUpActivity.class);
                startActivityForResult(addPlayer, 101);
            }
        });

        btn_takeChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print("Take Challenge");
                ListAvatar p1 = list.get(selectedPlayer);
                if (p1.isTakenChallenge()) {
                    Toast.makeText(getContext(), "Already taken challenge", Toast.LENGTH_SHORT).show();
                } else {
                    Intent takeChallenge = new Intent(getContext(), TakeChallenge.class);
                    startActivityForResult(takeChallenge, 201);
                }
            }
        });
        return rootView;
    }

    private boolean isGameOver()
    {
        boolean result = true;
        for (ListAvatar item: list
             ) {
            if(item.isTakenChallenge() == false) {
                result = false;
                break;
            }

        }

        return result;
    }

    private ListAvatar getListFirst()
    {
        ListAvatar first = list.get(0);

        int challengeType = -1;
        SharedPreferences prefs = getContext().getSharedPreferences("GAME_INFO", getContext().MODE_PRIVATE);
        String challengeName = prefs.getString("name", null);

        String[] tokens = challengeName.split("/");

        if(tokens[2].equals("time")) {
            challengeType = 0;
        }
        else if(tokens[2].equals("pushup")) {
            challengeType=1;
        }

        for (ListAvatar item:list
             ) {
            if(challengeType == 0) {
                if (Integer.parseInt(first.getPushUpCount()) < Integer.parseInt(item.getPushUpCount())) {
                    first = item;
                }
            }
            else if(challengeType == 1)
            {
                DateFormat df = new SimpleDateFormat("mm:ss.S");
                try {
                    Date firstDate = df.parse(first.getPushUpTimeTaken());
                    Date curDate = df.parse(item.getPushUpTimeTaken());
                    long firstMillis = firstDate.getTime()+ TimeZone.getDefault().getRawOffset();;
                    long itemMillis = curDate.getTime()+TimeZone.getDefault().getRawOffset();;

                    if(firstMillis > itemMillis)
                        first = item;

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }
        return  first;
    }
    private ListAvatar getListSecond()
    {
        ListAvatar second= null;
        return second;
    }
    private ListAvatar getListThird()
    {
        ListAvatar third= null;
        return third;
    }

    private void showResults()
    {
        Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                ListAvatar first = getListFirst();
                ListAvatar second = getListSecond();
                ListAvatar third = getListThird();

                Intent challengeResult = new Intent(getContext(), Winner.class);

                challengeResult.putExtra("firstName",first.getName());
                challengeResult.putExtra("firstPushUpCount",first.getPushUpCount());
                challengeResult.putExtra("firstTime",first.getPushUpTimeTaken());

                startActivityForResult(challengeResult, 401);
            }
        };
        thread.start();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        print("OnActivity result");
        if(requestCode == 101)
        {
            print("got the result");
            print( "result ="+resultCode);
            if(resultCode != 0) {
                String name = data.getStringExtra("name");
                String age = data.getStringExtra("age");

                print("Name = " + name + " Age = " + age + " FROM Activity");
                ListAvatar p1 = new ListAvatar();
                p1.setName(name);
                p1.setPushUpTaken("0");
                p1.setPushUPTimeTaken("00:00:000");
                p1.setSelected(false);
                p1.setTakenChallenge(false);
                list.add(p1);
                avatarAdapter.notifyDataSetChanged();
                playerCount++;
            }
        }
        else if (requestCode == 201)
        {

            print("got the result");
            print("result =" + resultCode);
            if (resultCode != 0) {
                String count = data.getStringExtra("count");
                String time = data.getStringExtra("time");

                print("Taken = " + count + " time = " + time + " FROM Activity");
                ListAvatar p1 = list.get(selectedPlayer);
                p1.setPushUpTaken(count);
                p1.setPushUPTimeTaken(time);
                p1.setTakenChallenge(true);
                avatarAdapter.notifyDataSetChanged();
                if(isGameOver())
                {
                    showResults();
                }

            }

        }
        else if (requestCode == 401)
        {
            try
            {

            Fragment nextFrag = Fragment_ChallengeMain.class.newInstance();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, nextFrag, "ChallengeMain")
                    .addToBackStack(null)
                    .commit();

            }
            catch (Exception e)
            {

            }
        }

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
