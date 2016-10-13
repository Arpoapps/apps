package com.appstory.aarppo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.AEADBadTagException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ISLMatchPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ISLMatchPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ISLMatchPage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private  String mId;
    TextView countDown;
    Handler handler = new Handler();
    long driftTime = 0L;
    Typeface mTypeFace;

    SQLiteDatabase mdb;
Date matchDate;


    TextView battle;
    TextView vs;
    TextView cheering_time;
    private OnFragmentInteractionListener mListener;

    public ISLMatchPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ISLMatchPage.
     */
    // TODO: Rename and change types and number of parameters
    public static ISLMatchPage newInstance(String param1, String param2) {
        ISLMatchPage fragment = new ISLMatchPage();
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
    //*************** STOP WATCH ****super*********
    public Runnable updateTimer = new Runnable() {
        public void run() {


            Date curDate = new Date();
            long diffTime = matchDate.getTime() - curDate.getTime();
            diffTime = diffTime +driftTime;
            long secs = TimeUnit.MILLISECONDS.toSeconds(diffTime) %60;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diffTime) %60;
            long hours_ = TimeUnit.MILLISECONDS.toHours(diffTime)%24;
            long days_ = TimeUnit.MILLISECONDS.toDays(diffTime);

            countDown.setText("" + String.format("%02d", days_ )+ ":" + String.format("%02d", hours_) + ":" + String.format("%02d", minutes) + ":"
                    + String.format("%02d", secs));


            if(diffTime < 0)
            {
                countDown.setTextSize(26);
                countDown.setText("Let's cheer for Blasters");

                battle.setText("              ");
                handler.removeCallbacks(updateTimer);
            }

            handler.postDelayed(this, 500);
        }};

    public static final String TIME_SERVER = "pool.ntp.org";

    public static Date getNetworkTime() throws IOException {
        NTPUDPClient timeClient = new NTPUDPClient();
        InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
        TimeInfo timeInfo = timeClient.getTime(inetAddress);
        //long returnTime = timeInfo.getReturnTime();   //local device time
        long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();   //server time

        Date time = new Date(returnTime);
        return time;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private  String getTeamName(int teamId)
    {
        String teamName = "empty";
        String query = "select teamName from tbl_teamName WHERE teamId="+teamId;
        AarpoDb db = new AarpoDb();
        db.openConnection();
        Cursor c = db.selectData(query);
        if (c != null) {
            while (c.moveToNext()) {
                teamName = c.getString(0);
            }
        }
        db.closeConnection();
        return  teamName;
    }
    private String getHomeGround(int teamId)
    {
        String teamName = "empty";

        String query = "select homeGround from tbl_teamName WHERE teamId="+teamId;
        AarpoDb db = new AarpoDb();
        db.openConnection();
        Cursor c = db.selectData(query);
        if (c != null) {
            while (c.moveToNext()) {
                teamName = c.getString(0);
            }
        }
        db.closeConnection();
        return  teamName;
    }
    public  void  cancelAllAwayArpos()
    {
        String query = "select team1, team2 from tbl_schedule WHERE sched_id="+mId;
        AarpoDb db = new AarpoDb();
        db.openConnection();
        //Log.d("JKS","Cancel all away arpos query="+query);
        Cursor crsor = db.selectData(query);
        if (crsor != null) {
            while (crsor.moveToNext()) {
               // Log.d("JKS","team1="+crsor.getInt(0)+" TEAM2 ="+crsor.getInt(1));
               if(crsor.getInt(1) == 2) {
                   query = "UPDATE tbl_AARPO SET aarpo1=0,aarpo2=0,aarpo3=0,aarpo4=0,aarpo5=0,aarpo6=0 WHERE sched_id=" + mId;
                  // Log.d("JKS","query to cancel all arpos = "+query);
                   updateCheckListData(getActivity(), query, mId);
               }

            }
        }
        db.closeConnection();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        driftTime = 0;



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        View rootView = inflater.inflate(R.layout.fragment_islmatch_page, container, false);
        mTypeFace =  Typeface.createFromAsset(rootView.getContext().getAssets(), "fonts/century-gothic.ttf");
        Log.d("ARPO","==================================");
        if(isNetworkAvailable()) {
            Log.d("ARPO","Internet connection is available");



            long diffTime = BlastersSplash.getTimeDelay();

            driftTime  = diffTime;
            long secs = TimeUnit.MILLISECONDS.toSeconds(diffTime) % 60;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diffTime) % 60;
            long hours = TimeUnit.MILLISECONDS.toHours(diffTime) % 24;
            long days = TimeUnit.MILLISECONDS.toDays(diffTime);

            Log.d("ARPO", "Time drift" + days + ":" + String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":"
                    + String.format("%02d", secs));

        }
        else
        {
            Log.d("ARPO","Internet is not present");
        }
//        Context context = getActivity();
//        SharedPreferences sharedPref = context.getSharedPreferences(
//                (Settings.System.AUTO_TIME), Context.MODE_PRIVATE);
        //android.provider.Settings.Global.putInt(getActivity().getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0);
        //Settings.System.AUTO_TIME
        //startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);

        if( android.provider.Settings.Global.getInt(getActivity().getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0)== 1)
        {
            Log.d("ARPO","Auto update time is on");
        }
        else
        {
            Log.d("ARPO","Auto update time is off");
            int requestCode = 99;
            startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), requestCode) ;
            if( android.provider.Settings.Global.getInt(getActivity().getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0)== 0)
            {
                Toast.makeText(rootView.getContext(),"We need to get network time to make it work",Toast.LENGTH_LONG).show();
            }

        }
        Log.d("ARPO","==================================");
        String strtext = getArguments().getString("id");
        Log.d("ARPO","id ="+strtext);
        mId = strtext;
        
        cancelAllAwayArpos();

        String query = "select date_time,team1, team2 from tbl_schedule WHERE sched_id="+mId;
        //Log.d("ARPO", "querry  =" +query);
        AarpoDb db = new AarpoDb();
        db.openConnection();
        Cursor crsor = db.selectData(query);
        TextView team1 = (TextView) rootView.findViewById(R.id.isl_team1);
        TextView team2 = (TextView) rootView.findViewById(R.id.isl_team2);
       // TextView ground = (TextView) rootView.findViewById(R.id.isl_ground);
        team1.setTypeface(mTypeFace);
        team2.setTypeface(mTypeFace);
       // ground.setTypeface(mTypeFace);

        if (crsor != null) {
            while (crsor.moveToNext()) {
                team1.setText(getTeamName(crsor.getInt(1)));
                team2.setText(getTeamName(crsor.getInt(2)));
              //  ground.setText(getHomeGround(crsor.getInt(1)));
                //Log.d("JKS","result "+c.getString(0)+" =  "+c.getString(1) + "team1 = "+getTeamName(c.getInt(3))+" team2="+getTeamName(c.getInt(4))) ;
                try
                {
                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    matchDate = dateformat.parse(crsor.getString(0));
                    Log.d("ARPO","Match date ="+crsor.getString(0));
                }
                catch (ParseException ex)
                {

                }
            }
        }


        handler.postDelayed(updateTimer, 0);
        countDown= (TextView) rootView.findViewById(R.id.txt_countDown);
        countDown.setText("");
        countDown.setTypeface(mTypeFace);

       // hours = (TextView) rootView.findViewById(R.id.textView4);;
      //  days = (TextView) rootView.findViewById(R.id.textView5);;
      //  mins = (TextView) rootView.findViewById(R.id.textView6);
     //   sec= (TextView) rootView.findViewById(R.id.textView7);;
        battle= (TextView) rootView.findViewById(R.id.textView);;
        vs= (TextView) rootView.findViewById(R.id.textView8);;
        cheering_time= (TextView) rootView.findViewById(R.id.textView9);;
      //  hours.setTypeface(mTypeFace);
      //  days.setTypeface(mTypeFace);
      //  mins.setTypeface(mTypeFace);
      //  sec.setTypeface(mTypeFace);
        cheering_time.setTypeface(mTypeFace);
        battle.setTypeface(mTypeFace);
        vs.setTypeface(mTypeFace);
        countDown.setTypeface(mTypeFace);




        CheckBox chk1 = (CheckBox)rootView.findViewById(R.id.chk_Aarpo1);
        CheckBox chk2 = (CheckBox)rootView.findViewById(R.id.chk_Aarpo2);
        CheckBox chk3 = (CheckBox)rootView.findViewById(R.id.chk_Aarpo3);
        CheckBox chk4 = (CheckBox)rootView.findViewById(R.id.chk_Aarpo4);
        CheckBox chk5 = (CheckBox)rootView.findViewById(R.id.chk_Aarpo5);
       // CheckBox chk6 = (CheckBox)rootView.findViewById(R.id.chk_Aarpo6);
        CheckBox chk7 = (CheckBox)rootView.findViewById(R.id.chk_Aarpo7);
       // CheckBox chk8 = (CheckBox)rootView.findViewById(R.id.chk_Aarpo8);

        chk1.setTypeface(mTypeFace);
        chk2.setTypeface(mTypeFace);
        chk3.setTypeface(mTypeFace);
        chk4.setTypeface(mTypeFace);
        chk5.setTypeface(mTypeFace);
       // chk6.setTypeface(mTypeFace);
        chk7.setTypeface(mTypeFace);
       // chk8.setTypeface(mTypeFace);


        query = "Select * from tbl_AARPO WHERE sched_id="+mId;
        //
      //  Cursor aarpo =  getChecklistDate(getActivity(),mId);

        getChecklistDate(getActivity(),mId);
        mdb = getActivity().openOrCreateDatabase("aarpoDB", Context.MODE_PRIVATE, null);
        Cursor aarpo  = mdb.rawQuery(query,null);




        if (aarpo != null) {
            while (aarpo.moveToNext()) {
                Log.d("ARPO","id:"+ aarpo.getString(0)+
                        " data: "+ aarpo.getString(1)+
                        " data: "+ aarpo.getString(2)+
                        " data: "+ aarpo.getString(3)+
                        " data: "+ aarpo.getString(4)+
                        " data: "+ aarpo.getString(5));
                if(aarpo.getInt(1) == 1)
                    chk1.setChecked(true);
                else
                    chk1.setChecked(false);
                if(aarpo.getInt(2) == 1)
                    chk2.setChecked(true);
                else
                    chk2.setChecked(false);
                if(aarpo.getInt(3) == 1)
                    chk3.setChecked(true);
                else
                    chk3.setChecked(false);
                if(aarpo.getInt(4) == 1)
                    chk4.setChecked(true);
                else
                    chk4.setChecked(false);
                if(aarpo.getInt(5) == 1)
                    chk5.setChecked(true);
                else
                    chk5.setChecked(false);
               /* if(aarpo.getInt(6) == 1)
                    chk6.setChecked(true);
                else
                    chk6.setChecked(false);*/
                if(aarpo.getInt(6) == 1)
                    chk7.setChecked(true);
                else
                    chk7.setChecked(false);
               /* if(aarpo.getInt(8) == 1)
                    chk8.setChecked(true);
                else
                    chk8.setChecked(false);*/
            }

        }

        db.closeConnection();

        mdb.close();
        chk1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String query;
                if ( ((CheckBox)v).isChecked() ) {
                    // perform logic
                    Log.d("ARPO","Checked");
                     query = "UPDATE tbl_AARPO SET aarpo1=1 WHERE sched_id="+mId;
                }
                else
                {
                    query = "UPDATE tbl_AARPO SET aarpo1=0 WHERE sched_id="+mId;
                }
                Log.d("ARPO","query="+query);
                updateCheckListData(getActivity(), query,mId);
            }
        });
        chk2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String query;
                if ( ((CheckBox)v).isChecked() ) {
                    // perform logic

                    query = "UPDATE tbl_AARPO SET aarpo2=1 WHERE sched_id="+mId;
                }
                else
                {
                    query = "UPDATE tbl_AARPO SET aarpo2=0 WHERE sched_id="+mId;
                }
                Log.d("ARPO","query="+query);
                updateCheckListData(getActivity(), query,mId);
            }
        });
        chk3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String query;
                if ( ((CheckBox)v).isChecked() ) {
                    // perform logic

                    query = "UPDATE tbl_AARPO SET aarpo3=1 WHERE sched_id="+mId;
                }
                else
                {
                    query = "UPDATE tbl_AARPO SET aarpo3=0 WHERE sched_id="+mId;
                }
                Log.d("ARPO","query="+query);
                updateCheckListData(getActivity(), query,mId);
            }
        });
        chk4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String query;
                if ( ((CheckBox)v).isChecked() ) {
                    // perform logic

                    query = "UPDATE tbl_AARPO SET aarpo4=1 WHERE sched_id="+mId;
                }
                else
                {
                    query = "UPDATE tbl_AARPO SET aarpo4=0 WHERE sched_id="+mId;
                }
                Log.d("ARPO","query="+query);
                updateCheckListData(getActivity(), query,mId);
            }
        });
        chk5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String query;
                if ( ((CheckBox)v).isChecked() ) {
                    // perform logic

                    query = "UPDATE tbl_AARPO SET aarpo5=1 WHERE sched_id="+mId;
                }
                else
                {
                    query = "UPDATE tbl_AARPO SET aarpo5=0 WHERE sched_id="+mId;
                }
                Log.d("ARPO","query="+query);
                updateCheckListData(getActivity(), query,mId);
            }
        });
      /*  chk6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String query;
                if ( ((CheckBox)v).isChecked() ) {
                    // perform logic
                    Log.d("JKS","Checked");
                    query = "UPDATE tbl_AARPO SET aarpo6=1 WHERE sched_id="+mId;
                }
                else
                {
                    query = "UPDATE tbl_AARPO SET aarpo6=0 WHERE sched_id="+mId;
                }
                Log.d("JKS","query="+query);
                updateCheckListData(getActivity(), query,mId);
            }
        });*/
        chk7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String query;
                if ( ((CheckBox)v).isChecked() ) {
                    // perform logic

                    query = "UPDATE tbl_AARPO SET aarpo6=1 WHERE sched_id="+mId;
                }
                else
                {
                    query = "UPDATE tbl_AARPO SET aarpo6=0 WHERE sched_id="+mId;
                }
                Log.d("ARPO","query="+query);
                updateCheckListData(getActivity(), query,mId);
            }
        });
      /*  chk8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String query;
                if ( ((CheckBox)v).isChecked() ) {
                    // perform logic
                    Log.d("JKS","Checked");
                    query = "UPDATE tbl_AARPO SET aarpo8=1 WHERE sched_id="+mId;
                }
                else
                {
                    query = "UPDATE tbl_AARPO SET aarpo8=0 WHERE sched_id="+mId;
                }
                Log.d("JKS","query="+query);
                updateCheckListData(getActivity(), query,mId);
            }
        });*/
        FloatingActionButton myFab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FlashScreen.class);
                getActivity().startActivity(i);
            }
        });
        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) rootView.findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent(getResources().getString(R.string.banner_ad_unit_origin)).build();
        adView.loadAd(adRequest);
        return rootView;
    }

    public Cursor  getChecklistDate(Context Ctx, String id)
    {
        //Log.d("JKS","open or create databse");
         mdb = Ctx.openOrCreateDatabase("aarpoDB", Context.MODE_PRIVATE, null);
        mdb.execSQL("CREATE TABLE IF NOT EXISTS tbl_AARPO(sched_id INTEGER, "+
                " aarpo1 INTEGER NOT NULL ,"+
                " aarpo2 INTEGER NOT NULL," +
                " aarpo3 INTEGER NOT NULL," +
                " aarpo4 INTEGER NOT NULL," +
                " aarpo5 INTEGER NOT NULL," +
                " aarpo6 INTEGER NOT NULL)");
        Cursor c3 = mdb.rawQuery("SELECT * FROM tbl_AARPO", null);
        if(c3.getCount() == 0) {
            //Log.d("JKS ", "table is empty fill data first");
            AarpoDb db =new AarpoDb();
            db.openConnection();

            String query = "SELECT sched_id FROM tbl_schedule";
            Cursor c = db.selectData(query);
            for(int i = 0; i <= c.getCount();i++) {
                query = "INSERT INTO tbl_AARPO (sched_id,aarpo1,aarpo2,aarpo3,aarpo4,aarpo5,aarpo6) values("+i+",1,1,1,1,1,1)";
                mdb.execSQL(query);
            }
            db.closeConnection();
        }
       // else Log.d("JKS","data exits");
        String query = "Select * from tbl_AARPO WHERE sched_id="+mId;
        Cursor c2 = mdb.rawQuery(query,null);

        //Log.d("JKS","Cursor size=0"+c2.getCount());
        if(c2 != null)
        {

            //Log.d("JKS","Cursor size=0"+c2.getCount());
            while(c2.moveToNext())
            {
                Log.d("ARPO","id:"+ c2.getString(0)+
                        " data: "+ c2.getString(1)+
                        " data: "+ c2.getString(2)+
                        " data: "+ c2.getString(3)+
                        " data: "+ c2.getString(4)+
                        " data: "+ c2.getString(5));
            }
        }
      //  else Log.d("ARPO","Cursor came as null");

        mdb.close();
        return  c2;
    }
    void updateCheckListData(Context Ctx, String query ,String id)
    {
         mdb = Ctx.openOrCreateDatabase("aarpoDB", Context.MODE_PRIVATE, null);
        mdb.execSQL(query);
        String query2 = "Select * from tbl_AARPO WHERE sched_id="+id;
        Cursor c2 = mdb.rawQuery(query2,null);
        if(c2 != null)
        {
            while(c2.moveToNext())
            {
                Log.d("ARPO","AFTER UPDATE id:"+ c2.getString(0)+
                        " data: "+ c2.getString(1)+
                        " data: "+ c2.getString(2)+
                        " data: "+ c2.getString(3)+
                        " data: "+ c2.getString(4)+
                        " data: "+ c2.getString(5));
            }
        }
        mdb.close();
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


}
