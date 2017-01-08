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
import android.widget.TextView;
import android.widget.Toast;

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

    Button btn_takeChallenge;

    int selectedPlayer = 0;

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

        gv_avatarCl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ListAvatar gridItem = list.get(position);

                if (selectedPlayer == position) {
                    print("Aleady selected");
                    return;
                }

                ListAvatar selectedGrid = list.get(selectedPlayer);

                if(gridItem.isRemoteMachine())
                {
                    print("Clicking on remote machine");
                    return;
                }
                selectedGrid.setSelected(false);


                gridItem.setSelected(true);

                selectedPlayer = position;
                avatarAdapter.notifyDataSetChanged();


            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_challenge_page_client, container, false);
        final TextView msg = (TextView) rootView.findViewById(R.id.txt_msg);

        gv_avatarCl = (GridView)rootView.findViewById(R.id.gv_client);
        gv_avatarCl.setNumColumns(2);
        btn_takeChallenge = (Button) rootView.findViewById(R.id.btn_take_challenge);
        btn_takeChallenge.setEnabled(false);
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
                    arpoClient = new Client("192.168.43.1", 8080, msg,getActivity(),list,avatarAdapter,btn_takeChallenge);

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        print("OnActivity result");

        if (requestCode == 201)
        {

            print("got the result");
            print("result =" + resultCode);
            if (resultCode != 0) {
                String count = data.getStringExtra("count");
                String time = data.getStringExtra("time");
                int uId = 0;


                print("Taken = " + count + " time = " + time + " FROM Activity");

                for (ListAvatar item: list
                     ) {

                    if(item.getSelected())
                    {
                        item.setPushUpTaken(count);
                        item.setPushUPTimeTaken(time);
                        item.setTakenChallenge(true);
                        uId = item.getUniqueId();
                        break;

                    }
                }

                /*
                ListAvatar p1 = list.get(selectedPlayer);
                p1.setPushUpTaken(count);
                p1.setPushUPTimeTaken(time);
                p1.setTakenChallenge(true);
                uId = p1.getUniqueId();
                */

                avatarAdapter.notifyDataSetChanged();

                arpoClient.sendMyResults(count,time,uId);
               /* if(isGameOver())
                {
                    showResults();
                }*/

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
