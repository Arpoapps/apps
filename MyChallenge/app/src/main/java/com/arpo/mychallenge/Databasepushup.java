package com.arpo.mychallenge;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SONY on 06-09-2016.
 */
public class Databasepushup extends SQLiteOpenHelper {

    SQLiteDatabase sqldb;
    Context mContext;



    public Databasepushup(Context C)
    {
        super(C,"DB_pushup",null,1);
        mContext = C;
    }
    public void openConnection()
    {
        sqldb =  mContext.openOrCreateDatabase("PUSHUP_DB", Context.MODE_PRIVATE, null);
        createTables();
        initData();
    }

    private void createTables()
    {


        String qury = "CREATE TABLE IF NOT EXISTS tb_staminatest(Testno INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, numPushUp INTEGER NOT NULL ,Timetaken INTEGER NOT NULL )";
        sqldb.execSQL(qury);

        String qury1 = "CREATE TABLE IF NOT EXISTS tb_userdata(Userid INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR(30) NOT NULL , height INTEGER NOT NULL ,weight INTEGER NOT NULL ,Score INTEGER NOT NULL)";
        sqldb.execSQL(qury1);


        String qury2 = "CREATE TABLE IF NOT EXISTS tb_pushupdata(Data_id INTEGER PRIMARY KEY AUTOINCREMENT ,Pushup_id INTEGER NOT NULL , Date VARCHAR(500) ,Attemptno VARCHAR(30) ,Numpushup INTEGER NOT NULL,Timetaken INTEGER NOT NULL,Badge INTEGER NOT NULL )";
        sqldb.execSQL(qury2);


        String qury3 = "CREATE TABLE IF NOT EXISTS tb_pushupdetails(Pushup_id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                "excersisename VARCHAR(500),"+
                "images VARCHAR(100) ,"+
                "targetPushUp INTEGER NOT NULL,"+
                "nextset INTEGER NOT NULL,"+
                "besttime VARCHAR(100),"+
                "taken INTEGER)";
        sqldb.execSQL(qury3);


        String qury4 = "CREATE TABLE IF NOT EXISTS tb_description( Description_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,Pushup_id INTEGER , Steps VARCHAR(100) )";
        sqldb.execSQL(qury4);


        String qury5 = "CREATE TABLE IF NOT EXISTS tb_pushupimages(Image_id INTEGER PRIMARY KEY AUTOINCREMENT ,Pushup_id INTEGER  , image VARCHAR(30) )";
        sqldb.execSQL(qury5);

        // Toast.makeText(c, "created", Toast.LENGTH_SHORT).show();


    }

    private void initData()
    {

        String checkQuery = "SELECT * FROM tb_pushupdetails";
        Cursor checkData = sqldb.rawQuery(checkQuery,null);
        if(checkData.getCount() != 0)
        {
            return;
        }

// push up name and image insetion
        int[]img ={R.mipmap.pushup,
                R.mipmap.popo,
                R.mipmap.pic,
                R.mipmap.download,
                R.mipmap.pushup,
                R.mipmap.popo,
                R.mipmap.pic,
                R.mipmap.download,
                R.mipmap.pushup,
                R.mipmap.popo,
                R.mipmap.pic,
                R.mipmap.download,
                R.mipmap.pushup,
                R.mipmap.popo,
                R.mipmap.pic,
                R.mipmap.download,
                R.mipmap.pushup,
                R.mipmap.popo,
                R.mipmap.pic,
                R.mipmap.download,
                R.mipmap.pushup,
                R.mipmap.popo,
                R.mipmap.pic,
                R.mipmap.download,
                R.mipmap.pushup,
                R.mipmap.popo,
                R.mipmap.pic,
                R.mipmap.download,
                R.mipmap.pushup,
                R.mipmap.popo,
                R.mipmap.pic,
                R.mipmap.download,
                R.mipmap.pushup,
                R.mipmap.popo,
                R.mipmap.pic};

        String[] names ={"STANDARD",
                "KNEE PUSHUP",
                "SHOULDER TAP",
                "HAND TAP",
                "T PUSHUP",
                "TIGHT TAP",
                "SINGLE HAND RAISED",
                " SINGLE LEG RAISED",
                "KNUCKLE PUSHUP",
                "STAGGERED PUSHUP",
                "ALLIGATOR PUSHUP",
                "SLOW TO KNEE",
                "SPIDERMAN PUSHUP",
                "KNEE TO CHEST",
                "PSEUDO PLAINCHEE PUSHUP",
                "OUTSIDE LEG KICK",
                "GRASSHOPPER PUSHUP",
                "FOOT TAP",
                "KNEE TO OPPOSITE ",
                "CROSS SCREW PUSHUP ELBOW",
                "DIAMOND PUSHUP",
                "WIDE PUSHUP",
                "TIGER PUSHUP",
                "PIKE PUSHUP",
                "FEET ELIVATED PUSHUP",
                "SIDE ROLL PUSHUP",
                "JACK KNIFE PUSHUP",
                "YOGA PUSHUP",
                "EXPLOSIVE STAGGERED PUSHUP",
                "EXPLOSIVE JACKS PUSHUP",
                "CLAP PUSHUP",
                "SLIDER PUSHUP",
                "FEET ON WALL",
                "SUPERMAN PUSHUP",
                "FINGER TIP PUSHUP"};

        int count = 100;

        for(int i=0;i<names.length;i++) {
            String st1 = "insert into tb_pushupdetails(excersisename,images,targetPushUp,nextset,besttime,taken )values('"+names[i]+"','"+img[i]+"',"+count+",10,'nil',0)";
            //count += 5;
            sqldb.execSQL(st1);
        }

// description inserion .... only first five excersise  detais is correct

        String[] s1={"Place your hands firmly on the ground, directly under shoulders.: Ground your toes into the floor.: Tighten your abs and flatten your back so your entire body is neutral and straight.:","Keep your back flat.:\n" +
                "Inhale as you begin to lower your body until your chest touches the floor.: \n" +
                "Your body should remain in a straight line from head to toe.: \n","Keep your core engaged.:\n" +
                "Exhale as you push back to the starting position.: \n"};

        String[] s2={"Lower yourself till nearly touching the ground.\n" +
                " Push yourself up till back to starting position and tap your left hand back with Right hand.then bring it back to starting position.\n"," Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and tap your right hand back with Left hand, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s3={" Lower yourself till nearly touching the ground.\n" +
                " Push yourself up till back to starting position and tap your Right Shoulder with left Hand.then bring it back to starting position.\n"," Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and tapLeft Shoulder with right hand, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s4={"Lower yourself till nearly touching the ground.\n" +
                " Push yourself up till back to starting position and Raise your left hand.then bring it back to starting position.\n","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Hand, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s5={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s6={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s7={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s8={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s9={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s10={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s11={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s12={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s13={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s14={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s15={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s16={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s17={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s18={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s19={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s21={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s20={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s22={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s23={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s24={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s25={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s26={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s27={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s28={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s29={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s30={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s31={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s32={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s33={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"},s34={"Lower yourself till nearly touching the ground Push yourself up till back to starting position and Raise your left Leg.then bring it back to starting position.","Lower yourself till nearly touching the ground again.\n" +
                " Push yourself up till back to starting position and Raise your Right Leg, then bring it back to starting position. \n" +
                " Inhale when going up, exhale when going down.\n"};


        for(int i=0;i<s1.length;i++){
            String des1="insert into tb_description(Pushup_id,steps)values('1','"+s1[i]+"')";
            sqldb.execSQL(des1);
        }
        int[] img1={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img1.length;i++){
            String des2="insert into tb_pushupimages(Pushup_id,image)values('1','"+img1[i]+"')";
            sqldb.execSQL(des2);
        }


        for(int i=0;i<s2.length;i++){
            String des3="insert into tb_description(Pushup_id,steps)values('2','"+s2[i]+"')";
            sqldb.execSQL(des3);
        }
        int[] img2={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img2.length;i++){
            String des4="insert into tb_pushupimages(Pushup_id,image)values('2','"+img2[i]+"')";
            sqldb.execSQL(des4);
        }
        for(int i=0;i<s3.length;i++){
            String des5="insert into tb_description(Pushup_id,steps)values('3','"+s3[i]+"')";
            sqldb.execSQL(des5);
        }
        int[] img3={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img3.length;i++){
            String des6="insert into tb_pushupimages(Pushup_id,image)values('3','"+img3[i]+"')";
            sqldb.execSQL(des6);
        }
        for(int i=0;i<s4.length;i++){
            String des7="insert into tb_description(Pushup_id,steps)values('4','"+s4[i]+"')";
            sqldb.execSQL(des7);
        }
        int[] img4={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img4.length;i++){
            String des8="insert into tb_pushupimages(Pushup_id,image)values('4','"+img4[i]+"')";
            sqldb.execSQL(des8);
        }

        // Toast.makeText(c, "inserted", Toast.LENGTH_SHORT).show();

        //  database continue........

        for(int i=0;i<s5.length;i++){
            String des9="insert into tb_description(Pushup_id,steps)values('5','"+s5[i]+"')";
            sqldb.execSQL(des9);
        }
        int[] img5={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img5.length;i++){
            String des10="insert into tb_pushupimages(Pushup_id,image)values('5','"+img5[i]+"')";
            sqldb.execSQL(des10);
        }

        for(int i=0;i<s6.length;i++){
            String des11="insert into tb_description(Pushup_id,steps)values('6','"+s6[i]+"')";
            sqldb.execSQL(des11);
        }
        int[] img6={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img6.length;i++){
            String des12="insert into tb_pushupimages(Pushup_id,image)values('6','"+img6[i]+"')";
            sqldb.execSQL(des12);
        }


        for(int i=0;i<s7.length;i++){
            String des13="insert into tb_description(Pushup_id,steps)values('7','"+s7[i]+"')";
            sqldb.execSQL(des13);
        }
        int[] img7={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img7.length;i++){
            String des14="insert into tb_pushupimages(Pushup_id,image)values('7','"+img7[i]+"')";
            sqldb.execSQL(des14);
        }

        for(int i=0;i<s8.length;i++){
            String des15="insert into tb_description(Pushup_id,steps)values('8','"+s8[i]+"')";
            sqldb.execSQL(des15);
        }
        int[] img8={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img8.length;i++){
            String des16="insert into tb_pushupimages(Pushup_id,image)values('8','"+img8[i]+"')";
            sqldb.execSQL(des16);
        }

        for(int i=0;i<s9.length;i++){
            String des17="insert into tb_description(Pushup_id,steps)values('9','"+s9[i]+"')";
            sqldb.execSQL(des17);
        }
        int[] img9={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img9.length;i++){
            String des18="insert into tb_pushupimages(Pushup_id,image)values('9','"+img9[i]+"')";
            sqldb.execSQL(des18);
        }
        for(int i=0;i<s10.length;i++){
            String des19="insert into tb_description(Pushup_id,steps)values('10','"+s10[i]+"')";
            sqldb.execSQL(des19);
        }
        int[] img10={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img10.length;i++){
            String des20="insert into tb_pushupimages(Pushup_id,image)values('10','"+img10[i]+"')";
            sqldb.execSQL(des20);
        }
        for(int i=0;i<s11.length;i++){
            String des21="insert into tb_description(Pushup_id,steps)values('11','"+s11[i]+"')";
            sqldb.execSQL(des21);
        }
        int[] img11={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img11.length;i++){
            String des22="insert into tb_pushupimages(Pushup_id,image)values('11','"+img11[i]+"')";
            sqldb.execSQL(des22);
        }
        for(int i=0;i<s12.length;i++){
            String des23="insert into tb_description(Pushup_id,steps)values('12','"+s12[i]+"')";
            sqldb.execSQL(des23);
        }
        int[] img12={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img12.length;i++){
            String des24="insert into tb_pushupimages(Pushup_id,image)values('12','"+img12[i]+"')";
            sqldb.execSQL(des24);
        }
        for(int i=0;i<s13.length;i++){
            String des25="insert into tb_description(Pushup_id,steps)values('13','"+s13[i]+"')";
            sqldb.execSQL(des25);
        }
        int[] img13={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img13.length;i++){
            String des26="insert into tb_pushupimages(Pushup_id,image)values('13','"+img13[i]+"')";
            sqldb.execSQL(des26);
        }
        for(int i=0;i<s14.length;i++){
            String des27="insert into tb_description(Pushup_id,steps)values('14','"+s14[i]+"')";
            sqldb.execSQL(des27);
        }
        int[] img14={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img14.length;i++){
            String des28="insert into tb_pushupimages(Pushup_id,image)values('14','"+img14[i]+"')";
            sqldb.execSQL(des28);
        }
        for(int i=0;i<s15.length;i++){
            String des29="insert into tb_description(Pushup_id,steps)values('15','"+s15[i]+"')";
            sqldb.execSQL(des29);
        }
        int[] img15={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img15.length;i++){
            String des30="insert into tb_pushupimages(Pushup_id,image)values('15','"+img15[i]+"')";
            sqldb.execSQL(des30);
        }

        for(int i=0;i<s16.length;i++){
            String des31="insert into tb_description(Pushup_id,steps)values('16','"+s16[i]+"')";
            sqldb.execSQL(des31);
        }
        int[] img16={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img16.length;i++){
            String des32="insert into tb_pushupimages(Pushup_id,image)values('16','"+img16[i]+"')";
            sqldb.execSQL(des32);
        }
        for(int i=0;i<s17.length;i++){
            String des33="insert into tb_description(Pushup_id,steps)values('17','"+s17[i]+"')";
            sqldb.execSQL(des33);
        }
        int[] img17={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img17.length;i++){
            String des34="insert into tb_pushupimages(Pushup_id,image)values('17','"+img17[i]+"')";
            sqldb.execSQL(des34);
        }
        for(int i=0;i<s18.length;i++){
            String des35="insert into tb_description(Pushup_id,steps)values('18','"+s18[i]+"')";
            sqldb.execSQL(des35);
        }
        int[] img18={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img18.length;i++){
            String des36="insert into tb_pushupimages(Pushup_id,image)values('18','"+img18[i]+"')";
            sqldb.execSQL(des36);
        }
        for(int i=0;i<s19.length;i++){
            String des37="insert into tb_description(Pushup_id,steps)values('19','"+s19[i]+"')";
            sqldb.execSQL(des37);
        }
        int[] img19={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img19.length;i++){
            String des38="insert into tb_pushupimages(Pushup_id,image)values('19','"+img19[i]+"')";
            sqldb.execSQL(des38);
        }
        for(int i=0;i<s20.length;i++){
            String des39="insert into tb_description(Pushup_id,steps)values('20','"+s20[i]+"')";
            sqldb.execSQL(des39);
        }
        int[] img20={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img20.length;i++){
            String des40="insert into tb_pushupimages(Pushup_id,image)values('20','"+img20[i]+"')";
            sqldb.execSQL(des40);
        }
        for(int i=0;i<s21.length;i++){
            String des41="insert into tb_description(Pushup_id,steps)values('21','"+s21[i]+"')";
            sqldb.execSQL(des41);
        }
        int[] img21={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img21.length;i++){
            String des42="insert into tb_pushupimages(Pushup_id,image)values('21','"+img21[i]+"')";
            sqldb.execSQL(des42);
        }
        for(int i=0;i<s22.length;i++){
            String des43="insert into tb_description(Pushup_id,steps)values('22','"+s22[i]+"')";
            sqldb.execSQL(des43);
        }
        int[] img22={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img22.length;i++){
            String des44="insert into tb_pushupimages(Pushup_id,image)values('22','"+img22[i]+"')";
            sqldb.execSQL(des44);
        }
        for(int i=0;i<s23.length;i++){
            String des45="insert into tb_description(Pushup_id,steps)values('23','"+s23[i]+"')";
            sqldb.execSQL(des45);
        }
        int[] img23={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img23.length;i++){
            String des46="insert into tb_pushupimages(Pushup_id,image)values('23','"+img23[i]+"')";
            sqldb.execSQL(des46);
        }
        for(int i=0;i<s24.length;i++){
            String des47="insert into tb_description(Pushup_id,steps)values('24','"+s24[i]+"')";
            sqldb.execSQL(des47);
        }
        int[] img24={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img24.length;i++){
            String des48="insert into tb_pushupimages(Pushup_id,image)values('24','"+img24[i]+"')";
            sqldb.execSQL(des48);
        }

        for(int i=0;i<s25.length;i++){
            String des49="insert into tb_description(Pushup_id,steps)values('25','"+s25[i]+"')";
            sqldb.execSQL(des49);
        }
        int[] img25={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img25.length;i++){
            String des50="insert into tb_pushupimages(Pushup_id,image)values('25','"+img25[i]+"')";
            sqldb.execSQL(des50);
        }

        for(int i=0;i<s26.length;i++){
            String des51="insert into tb_description(Pushup_id,steps)values('26','"+s26[i]+"')";
            sqldb.execSQL(des51);
        }
        int[] img26={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img26.length;i++){
            String des52="insert into tb_pushupimages(Pushup_id,image)values('26','"+img26[i]+"')";
            sqldb.execSQL(des52);
        }

        for(int i=0;i<s27.length;i++){
            String des53="insert into tb_description(Pushup_id,steps)values('27','"+s27[i]+"')";
            sqldb.execSQL(des53);
        }
        int[] img27={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img27.length;i++){
            String des54="insert into tb_pushupimages(Pushup_id,image)values('27','"+img27[i]+"')";
            sqldb.execSQL(des54);
        }

        for(int i=0;i<s28.length;i++){
            String des55="insert into tb_description(Pushup_id,steps)values('28','"+s28[i]+"')";
            sqldb.execSQL(des55);
        }
        int[] img28={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img28.length;i++){
            String des56="insert into tb_pushupimages(Pushup_id,image)values('28','"+img28[i]+"')";
            sqldb.execSQL(des56);
        }

        for(int i=0;i<s29.length;i++){
            String des57="insert into tb_description(Pushup_id,steps)values('29','"+s29[i]+"')";
            sqldb.execSQL(des57);
        }
        int[] img29={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img29.length;i++){
            String des58="insert into tb_pushupimages(Pushup_id,image)values('29','"+img29[i]+"')";
            sqldb.execSQL(des58);
        }

        for(int i=0;i<s30.length;i++){
            String des59="insert into tb_description(Pushup_id,steps)values('30','"+s30[i]+"')";
            sqldb.execSQL(des59);
        }
        int[] img30={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img30.length;i++){
            String des60="insert into tb_pushupimages(Pushup_id,image)values('30','"+img30[i]+"')";
            sqldb.execSQL(des60);
        }

        for(int i=0;i<s31.length;i++){
            String des61="insert into tb_description(Pushup_id,steps)values('31','"+s31[i]+"')";
            sqldb.execSQL(des61);
        }
        int[] img31={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img31.length;i++){
            String des62="insert into tb_pushupimages(Pushup_id,image)values('31','"+img31[i]+"')";
            sqldb.execSQL(des62);
        }

        for(int i=0;i<s32.length;i++){
            String des63="insert into tb_description(Pushup_id,steps)values('32','"+s32[i]+"')";
            sqldb.execSQL(des63);
        }
        int[] img32={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img32.length;i++){
            String des64="insert into tb_pushupimages(Pushup_id,image)values('32','"+img32[i]+"')";
            sqldb.execSQL(des64);
        }

        for(int i=0;i<s33.length;i++){
            String des65="insert into tb_description(Pushup_id,steps)values('33','"+s33[i]+"')";
            sqldb.execSQL(des65);
        }
        int[] img33={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img33.length;i++){
            String des66="insert into tb_pushupimages(Pushup_id,image)values('33','"+img33[i]+"')";
            sqldb.execSQL(des66);
        }

        for(int i=0;i<s34.length;i++){
            String des67="insert into tb_description(Pushup_id,steps)values('34','"+s34[i]+"')";
            sqldb.execSQL(des67);
        }
        int[] img34={R.mipmap.download, R.mipmap.pic, R.mipmap.popo,R.mipmap.pushup};

        for(int i=0;i<img34.length;i++){
            String des68="insert into tb_pushupimages(Pushup_id,image)values('34','"+img34[i]+"')";
            sqldb.execSQL(des68);

        }
    }


    public void closeConnection()
    {
        sqldb.close();
    }
    @Override
    public void onCreate(SQLiteDatabase sqldb) {


    }

    public boolean insertData(String qury)
    {
        try {
            sqldb.execSQL(qury);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public Cursor selectData(String qury)
    {
        Cursor cr=sqldb.rawQuery(qury,null);
        return cr;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
