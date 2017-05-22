package com.arpo.mychallenge;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_description.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_description#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_description extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    Databasepushup db;
    TextView tv_step1;
    TextView tv_heading;

    int position;
    String fid;
    String des = "";

    public Fragment_description() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_description.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_description newInstance(String param1, String param2) {
        Fragment_description fragment = new Fragment_description();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    void print(String str) {
        Log.d("JKS", str);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private void getDescription(String id) {
        db = new Databasepushup(getContext());
        db.openConnection();
        String se = "select Steps from tb_description where Pushup_id=" + id;
        Cursor c = db.selectData(se);
        int i = 1;
        print("Got " + c.getCount());
        if (c != null) {
            while (c.moveToNext()) {
                String s1 = c.getString(0);
                des = des + "Step " + i + ":\n";//+s1+"\n";
                String[] s2 = s1.split(".:");
                for (int j = 0; j < s2.length; j++) {
                    des = des + s2[j] + ".\n";
                }
                i++;
            }
            tv_step1.setText(des);


        }
        db.closeConnection();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View description_page = inflater.inflate(R.layout.fragment_fragment_description, container, false);
        tv_step1 = (TextView) description_page.findViewById(R.id.tv_des);
        tv_heading = (TextView) description_page.findViewById(R.id.tv_headline);
        /*getDescription();
        fid = getIntent().getStringExtra("fid");
        position = getIntent().getIntExtra("position", -1);
        Log.d("JKS", "Intent received on details page pos= " + position + " fid = " + fid);
        getpushupdescription();
*/

        Bundle bundle = this.getArguments();
        String name = bundle.getString("NAME");
        String id = bundle.getString("ID");
        tv_heading.setText(name);
        getDescription(id);
        print("GOT PUSH UP = " + name + "WITH ID=" + id);


        String challengeType;
        challengeType = "pushup";
        String challengeCount = "0";
        challengeCount = "20";

        String apName = String.format("%s/%s/%s", "EXCERCISE", challengeType, challengeCount);
        apName = "ARPO/" + apName;

        SharedPreferences.Editor editor = getContext().getSharedPreferences("GAME_INFO", getContext().MODE_PRIVATE).edit();
        editor.putString("name", apName);
        editor.commit();


        // button cclick listner
        Button challenge = (Button) description_page.findViewById(R.id.btn_description_challenge);
        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent takeChallenge = new Intent(getContext(), TakeChallenge.class);
                startActivityForResult(takeChallenge, 201);
            }
        });


        return description_page;
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        print("OnActivity result");
        if (requestCode == 201) {

            print("got the result");
            print("result =" + resultCode);
            if (resultCode != 0) {
                String count = data.getStringExtra("count");
                String time = data.getStringExtra("time");

                print("Taken = " + count + " time = " + time + " FROM Activity");

            }

        }

    }
}
