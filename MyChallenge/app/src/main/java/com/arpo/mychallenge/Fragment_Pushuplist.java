package com.arpo.mychallenge;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Pushuplist.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Pushuplist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Pushuplist extends Fragment implements AdapterView.OnItemClickListener {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private GridView gridv;
    List<pushupc> list1;
    Databasepushup db;
    Adapterpushuplist adapterlist;


    public Fragment_Pushuplist() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Pushuplist.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Pushuplist newInstance(String param1, String param2) {
        Fragment_Pushuplist fragment = new Fragment_Pushuplist();
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

    private void getpushuplist() {
        list1 = new ArrayList<>();

        db = new Databasepushup(getContext());
        db.openConnection();

        {

            String se = "select * from tb_pushupdetails";

            Cursor c = db.selectData(se);
            if (c != null) {
                while (c.moveToNext()) {
                    String id = c.getString(0);
                    String n = c.getString(1);
                    int img = c.getInt(2);
                    pushupc p1 = new pushupc();
                    p1.setName(n);
                    p1.setId(id);
                    p1.setImage(img);
                    p1.setNextbest(c.getString(4));
                    p1.setBesttime(c.getString(5));
                    p1.setTopbest(c.getString(3));
                    p1.setTaken(c.getString(6));
                    list1.add(p1);
                    //Log.d("JKS", "Adding " + n);

                }
            }
            adapterlist = new Adapterpushuplist(getContext(), list1);
            gridv.setAdapter(adapterlist);
            /*int count = c.getCount();*/
            gridv.setNumColumns(2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_fragment__pushuplist, container, false);
        gridv = (GridView) rootView.findViewById(R.id.gv);
        gridv.setNumColumns(2);

        getpushuplist();
        Log.d("pss", "layoutdone");

        gridv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Log.d("JKS","CLicked in postion "+position);
                try {

                    String name = list1.get(position).getName();
                    String pushupid = list1.get(position).getId();
                    Log.d("JKS","got to next fragment with "+name + " id="+pushupid);
                    Bundle bundle=new Bundle();
                    bundle.putString("NAME",name);
                    bundle.putString("ID",pushupid);
                    bundle.putString("NEXTSET",list1.get(position).getNextbest());
                    // save all data in bundle
                    Fragment nextFrag = Fragment_description.class.newInstance();
                    nextFrag.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent, nextFrag, "description")
                            .addToBackStack(null)
                            .commit();

                }
                catch (Exception e)
                {
                    Log.d("JKS","Exception");
                    e.printStackTrace();
                }
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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