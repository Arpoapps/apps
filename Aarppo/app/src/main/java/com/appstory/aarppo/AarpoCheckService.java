package com.appstory.aarppo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by jithin suresh on 28-09-2016.
 */
public class AarpoCheckService extends Service {

    private Looper mServiceLooper;
    long diffTime = 20;
    Handler handler;
    int cbRemoved = 0;
    int todaysGameId = -1;
    Date todaysMatchTime;
    int todaysTeam1;
    int todaysTeam2;
    boolean intentCalled;

    int syncIndex;
    int syncArray[]= {20, 30, 45, 65, 90};
    int MAX_SIZE = 5;


    private ServiceHandler mServiceHandler;
    public Runnable updateTimer = new Runnable() {
        public void run() {
            diffTime--;
        //    Log.d("ARPO","diff = " +diffTime);

            if(diffTime == 15)
            {
                Intent i = new Intent(AarpoCheckService.this, AarpoBlast.class);
                i.putExtra("timeLeft",diffTime);
                i.putExtra("gameId",todaysGameId);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }

            if(diffTime <=0 )
            {
                handler.removeCallbacks(this);
          //      Log.d("ARPO","Runnable reached less than 0 or 0");
               //cbRemoved = 1;
               // diffTime = getNewDiffTime();
            }
            handler.postDelayed(this, 1000);
        }};

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private Date getDateNow()
    {
        Date today = new Date();

        //Disabling network time
        /*try {
            if( isNetworkAvailable()) {


                today = ISLMatchPage.getNetworkTime();
                //Date netWorkTime = new Date();

            }
            else {
                Log.d("ARPO","No internet connection using local time");
                today = new Date();
            }
        }
        catch(IOException ex)
        {

        }*/

        return today;
    }
    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            Log.d("ARPO","inside service handler");

            boolean serviceLoopExitFlag = true;
            while(serviceLoopExitFlag) {
                try {
                    if(isPlayingToday())
                    {
                        if(matchGoingOn())
                        {
                            Log.d("ARPO ","Match is going on :: lets sync");
                            createDbIfnotExists();
                            SQLiteDatabase mdb = openOrCreateDatabase("aarpoDB", Context.MODE_PRIVATE, null);

                            Cursor c3 = mdb.rawQuery("SELECT * FROM tbl_AARPO WHERE sched_id="+todaysGameId, null);
                            int arpo1 = 0;
                            int arpo2 = 0;
                            int arpo3 = 0;
                            int arpo4 = 0;
                            int arpo5 = 0;
                            if (c3!= null)
                            {
                                while (c3.moveToNext())
                                {
                                    arpo1 = c3.getInt(2);
                                    arpo2 = c3.getInt(3);
                                    arpo3 = c3.getInt(4);
                                    arpo4 = c3.getInt(5);
                                    arpo5 = c3.getInt(6);
                                }
                            }
                            mdb.close();
                            Date netWorkTime = getDateNow();

                            long elapsedTime =( netWorkTime.getTime() - todaysMatchTime.getTime() ) /(1000*60);
                            Log.d("ARPO","Elapesed minutes = "+elapsedTime);
                            int realIndex = 0;
                            for(int i = 0; i <MAX_SIZE;i++) {
                                if (elapsedTime >= syncArray[i])
                                    realIndex++;
                                else
                                    break;
                            }
                            if(realIndex != syncIndex) {
                                Log.d("ARPO ", "Syncing the aarpo sync time");
                                syncIndex =realIndex;
                            }
                            if(syncIndex>=MAX_SIZE) {
                                Thread.sleep(5000);
                                continue;

                            }

                            Log.d("ARPO","Sync settings "+arpo1+":"+arpo2+":"+arpo3+":"+arpo4+":"+arpo5);
                            if(syncIndex == 0 && arpo1 ==0)
                            {
                                syncIndex++;
                            }
                            else if(syncIndex == 1 && arpo2 ==0)
                            {
                                syncIndex++;
                            }
                            else if(syncIndex == 2 && arpo3 ==0)
                            {
                                syncIndex++;
                            }
                            else if(syncIndex == 3 && arpo4 ==0)
                            {
                                syncIndex++;
                            }
                            else if(syncIndex == 4 && arpo5 ==0)
                            {
                                syncIndex++;
                            }
                            Log.d("ARPO","Sync index="+syncIndex + " value = "+syncArray[syncIndex]);
                            long syncTime = todaysMatchTime.getTime() + syncArray[syncIndex] * 60 * 1000;

                            long diffTime_Match = syncTime - netWorkTime.getTime();
                            long hours = TimeUnit.MILLISECONDS.toHours(diffTime_Match) % 24;
                            long days = TimeUnit.MILLISECONDS.toDays(diffTime_Match);
                            long mins = TimeUnit.MILLISECONDS.toMinutes(diffTime_Match) % 60;
                            long secs = TimeUnit.MILLISECONDS.toSeconds(diffTime_Match) % 60;
                            Log.d("ARPO", days + "days =" + hours + " hours = " + mins + "mins = " + secs+ "seconds to sync" );
                            if (days == 0 && hours == 0 && mins == 0 && secs <= 15) {
                                if(intentCalled == false) {
                                    Intent i = new Intent(AarpoCheckService.this, AarpoBlast.class);
                                    i.putExtra("timeLeft", diffTime);
                                    i.putExtra("gameId", todaysGameId);
                                    i.putExtra("todaysGameTime",syncTime);

                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    intentCalled = true;
                                    syncIndex++;
                                }
                                else Log.d("ARPO","not launcing activity");

                            }
                            else intentCalled = false;

                        }
                        else if(matchOver())
                        {
                            Log.d("ARPO","Blasters match is over WAIT TILL next match");
                            serviceLoopExitFlag = false;
                            break;
                        }
                        else if(whisleTimeEnabled())
                        {

                            Date netWorkTime = getDateNow();
                            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                            Log.d("ARPO", "Today " + df.format(todaysMatchTime) + " now = " + df.format(netWorkTime));
                            long diffTime_Match = todaysMatchTime.getTime() - netWorkTime.getTime();
                            diffTime_Match = diffTime_Match - (2*60*1000);
                            long hours = TimeUnit.MILLISECONDS.toHours(diffTime_Match) % 24;
                            long days = TimeUnit.MILLISECONDS.toDays(diffTime_Match);
                            long mins = TimeUnit.MILLISECONDS.toMinutes(diffTime_Match) % 60;
                            long secs = TimeUnit.MILLISECONDS.toSeconds(diffTime_Match) % 60;
                            Log.d("ARPO", days + "days =" + hours + " hours = " + mins + "mins = " + secs+ "seconds=" );
                            if (days == 0 && hours == 0 && mins == 0 && secs <= 15) {
                                if(intentCalled == false) {
                                    Intent i = new Intent(AarpoCheckService.this, AarpoBlast.class);
                                    i.putExtra("timeLeft", diffTime);
                                    i.putExtra("gameId", todaysGameId);
                                    i.putExtra("todaysGameTime",todaysMatchTime.getTime());

                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    intentCalled = true;
                                    syncIndex = 0;
                                }
                                else Log.d("ARPO","not launcing activity");
                                /*if(cbRemoved == 1) {
                                    Log.d("ARPO", "seconds left = " + secs);

                                    diffTime = secs;
                                    handler.postDelayed(updateTimer, 0);
                                    cbRemoved =0;
                                }*/

                            }
                            else intentCalled = false;
                        }
                        else
                            Log.d("ARPO","Blasters playing today, but not syncing at whislet time");
                    }
                    else
                    {
                        Log.d("ARPO","Blasters not playing today");
                        serviceLoopExitFlag = false;
                        break;
                    }
                    Thread.sleep(5000);

                } catch (InterruptedException e) {
                    // Restore interrupt status.
                    Thread.currentThread().interrupt();
                }
                // Stop the service using the startId, so that we don't stop
                // the service in the middle of handling another job
            }
            stopSelf(msg.arg1);

        }
    }

    private void createDbIfnotExists()
    {
        SQLiteDatabase mdb = openOrCreateDatabase("aarpoDB", Context.MODE_PRIVATE, null);
        mdb.execSQL("CREATE TABLE IF NOT EXISTS tbl_AARPO(sched_id INTEGER, "+
                " aarpo1 INTEGER NOT NULL ,"+
                " aarpo2 INTEGER NOT NULL," +
                " aarpo3 INTEGER NOT NULL," +
                " aarpo4 INTEGER NOT NULL," +
                " aarpo5 INTEGER NOT NULL," +
                " aarpo6 INTEGER NOT NULL)");
        Cursor c4 = mdb.rawQuery("SELECT * FROM tbl_AARPO", null);
        if(c4.getCount() == 0) {
            Log.d("ARPO ", "table is empty fill data first");
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
        mdb.close();
    }
    private  boolean whisleTimeEnabled()
    {
        createDbIfnotExists();
        SQLiteDatabase mdb = openOrCreateDatabase("aarpoDB", Context.MODE_PRIVATE, null);

        Cursor c3 = mdb.rawQuery("SELECT * FROM tbl_AARPO WHERE sched_id="+todaysGameId, null);
        int arpo1 = 0;

        if (c3!= null)
        {
            while (c3.moveToNext())
            {
                arpo1 = c3.getInt(1);
            }
        }

        mdb.close();
        if(arpo1 == 1) return true;
        else return  false;
    }

    private  boolean matchGoingOn()
    {
        Date today = getDateNow();
        long timeDifference = today.getTime() - todaysMatchTime.getTime();
       // timeDifference = timeDifference - (2*60*1000);

        if( timeDifference > -(2*60*1000) && timeDifference < 5400000)
            return  true;
        else
            return  false;

    }
    private boolean matchOver()
    {
        Date today = getDateNow();
        long timeDifference = today.getTime() - todaysMatchTime.getTime();
        if( timeDifference >  5400000)
            return  true;
        else
            return  false;

    }

    private boolean isPlayingToday()
    {
        boolean result = false;

        AarpoDb db = new AarpoDb();
        db.openConnection();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date dt = new Date();
        String todaysDate = df.format(dt);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.DATE, +1);
        Date dayAfter = cal.getTime();
        cal.add(Calendar.DATE, -2);
        Date dayBefore = cal.getTime();

       // Log.d("ARPO","Todays date = "+todaysDate +" after "+ df.format(dayAfter) + " before " +df.format(dayBefore));
        String query = "select * from tbl_schedule WHERE date_time<'"+df.format(dayAfter) + "' AND date_time>'"+df.format(dayBefore)+"' AND (team1=2 OR team2=2)" ;
        Cursor c1 = db.selectData(query);
        if(c1 != null)
        {
            while(c1.moveToNext()) {
                try {
                    todaysGameId = c1.getInt(0);
                    todaysTeam1 = c1.getInt(3);
                    todaysTeam2 = c1.getInt(4);
                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    todaysMatchTime = dateformat.parse(c1.getString(1));

                } catch (ParseException ex) {

                }
            }

        }
       // Log.d("ARPO","query ="+query +" got data="+c1.getCount());

        if(c1.getCount() > 0) result = true;
        db.closeConnection();

        return result;

    }


    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        Log.d("ARPO","onCreate serviice");

        // Get the HandlerThread's Looper and use it for our Handler

        handler = new Handler();
        cbRemoved = 1;
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.d("ARPO","Start Service");
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("ARPO","Service stopned");

    }
}
