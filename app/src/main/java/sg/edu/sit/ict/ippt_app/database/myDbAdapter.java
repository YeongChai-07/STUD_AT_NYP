package sg.edu.sit.ict.ippt_app.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
/**
 * Created by User on 23/2/2015.
 */
public class myDbAdapter {

    private static final String DATABASE_NAME = "Napfa.db";

	/* YC Database Attributes */

    private static final String YC_NAPFA_RESULT_TABLE ="NapfaTestResult";
    private static final String YC_NAPFA_RESULT_CHART_TABLE = "NapfaResultChart";

    private static final String AGE_GRP = "ageGrp";

    private static final String ADM_NO = "adminNo";
    private static final String TEST_DATE = "testDate";


    //Ryan's Login Attributes
    private static final String RYAN_DATABASE_TABLE = "myTestDb";
    public static final String KEY_ID = "_id";
    public static final int COLUMN_KEY_ID = 0;
    public static final String ENTRY_NAME = "entry_name";
    public static final int COLUMN_NAME_ID = 1;
    public static final String ENTRY_PWD = "entry_password";
    public static final int COLUMN_TEL_ID = 2;
    public static final String ENTRY_ADMIN = "entry_admin";
    public static final int COLUMN_ADMIN_ID = 3;
    public static final String ENTRY_AGE = "entry_age";
    public static final int COLUMN_AGE_ID = 4;

    //Test Stations
    public static final String SIT_UP = "sitUp";
    public static final String SBJ = "standingBoardJump";
    public static final String SNR = "sitAndReach";
    public static final String PULL_UP = "pullUp";
    public static final String SHUTTLE_RUN = "shuttleRun";
    public static final String KM_Run ="KMRun";

    //Achievements
    private static final String IPPT_STD = "achievedIPPTStd";
    private static final String AWARD = "awardAchieved";

    //Others
    private static final String PERF_GRD = "perfGrade";
    private static final String PTS = "pts";

    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase _db = null;
    private final Context context;

    //Create Table statement
    protected static final String YC_CREATE_NAPFA_TABLE = "create table " + YC_NAPFA_RESULT_TABLE + " (" + ADM_NO + " Text, " + TEST_DATE + " Text, " + SIT_UP + " Integer, " +
            SBJ + " Integer, " + SNR + " Integer, " + PULL_UP + " Integer, " + SHUTTLE_RUN + " Real, " + KM_Run + " Integer, " + IPPT_STD + " Text, " +
            AWARD + " Text);";
    protected static final String RYAN_DATABASE_CREATE = "create table " +
            RYAN_DATABASE_TABLE + " ("+ KEY_ID +" integer primary key autoincrement, "+ ENTRY_NAME + " text, "+ENTRY_PWD +" text, " + ENTRY_ADMIN+" text, "+ENTRY_AGE+" integer);";

	/*protected static final String YC_CREATE_NAPFA_RESULT_CHART = "create table " + YC_NAPFA_RESULT_CHART_TABLE + " (" + AGE_GRP + " Integer, "  + PERF_GRD + " Text, " +
			PTS + " Integer, " + SIT_UP + " Integer, " + SBJ + " Integer, " + SNR + " Integer, " + PULL_UP + " Integer, " + SHUTTLE_RUN + " Real, " + KM_Run + " Integer);";*/

    // YC's Cursor
    private Cursor ycCur = null;


    private String MYDBADAPTER_LOG_CAT = "MY_LOG";

    class myDbOpenHelper extends SQLiteOpenHelper {

        public myDbOpenHelper(Context context, String name,
                              CursorFactory factory, int version) {
            super(context, name, factory, version);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            // Create Ryan's Table
            db.execSQL(RYAN_DATABASE_CREATE);
            Log.w(MYDBADAPTER_LOG_CAT,"Helper : DB " + RYAN_DATABASE_CREATE + " Created!!");

            //Create YC Table
            db.execSQL(YC_CREATE_NAPFA_TABLE);
            Log.w(MYDBADAPTER_LOG_CAT,"Helper : DB " + YC_NAPFA_RESULT_TABLE + " Created!!");
			/*db.execSQL(YC_CREATE_NAPFA_RESULT_CHART);
			Log.w(MYDBADAPTER_LOG_CAT,"Helper : DB " + YC_CREATE_NAPFA_RESULT_CHART + " Created!!");*/

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }

    }

    private myDbOpenHelper dbHelper;

    public myDbAdapter(Context _context)
    {
        this.context = _context;
        dbHelper = new myDbOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void close()
    {
        _db.close();
        Log.w(MYDBADAPTER_LOG_CAT,"DB Closed");
    }

    public void open() throws SQLiteException
    {
        try
        {
            _db = dbHelper.getWritableDatabase();
            Log.w(MYDBADAPTER_LOG_CAT, "DB was opened as a writable database");
        }
        catch (SQLiteException ex)
        {
            _db = dbHelper.getReadableDatabase();
            Log.w(MYDBADAPTER_LOG_CAT, "DB was opened as a readable database");
        }
    }

    /* YC Retrieve Methods */
    public Cursor retrieveSelectedAdminNoNapfa_Result(String adminNo)
    {

        ycCur = null;

        try
        {
            ycCur = _db.rawQuery("SELECT " + TEST_DATE + ", " + SIT_UP + ", " + SBJ + ", " + SNR + ", " + PULL_UP + ", " +
                    SHUTTLE_RUN + ", " + KM_Run + ", " + IPPT_STD + ", " + AWARD  + " FROM " + YC_NAPFA_RESULT_TABLE +
                    " WHERE " + ADM_NO + " = ?;", new String [] {adminNo});
        }
        catch (SQLException sle)
        {
            Log.w(MYDBADAPTER_LOG_CAT, "Retrieve Fails !");
        }

        return ycCur;
    }

    public Cursor retrieveSelectedTestStationNapfaResult(String testStation, String adminNo)
    {
        ycCur = null;

        Log.w("Retrieve Test Station Napfa Result", "SELECT " + TEST_DATE + ", " +  testStation + " FROM " + YC_NAPFA_RESULT_TABLE +
                " WHERE " + ADM_NO + " = '" + adminNo  + "';" );

        try
        {
            ycCur = _db.rawQuery("SELECT " + TEST_DATE + ", " +  testStation + " FROM " + YC_NAPFA_RESULT_TABLE +
                    " WHERE " + ADM_NO + " = ?;", new String[]{adminNo});
            Log.w("Retrieve Test Station Napfa Result", "Retrieve Suceed!" );
        }
        catch (SQLException sle)
        {
            Log.w("Retrieve Test Station Napfa Result", "Retrieve Fails !");
        }

        return ycCur;
    }


    public byte retrieveExistTestDate(String testDate, String adminNo)
    {
        ycCur = null;

        try
        {
            ycCur = _db.rawQuery("SELECT COUNT(" + TEST_DATE + ") FROM " + YC_NAPFA_RESULT_TABLE +
                    " WHERE " + TEST_DATE + " = ? AND " + ADM_NO + " =?;", new String[] {testDate, adminNo});
        }
        catch (SQLException sle)
        {
            Log.w(MYDBADAPTER_LOG_CAT, "Retrieve Fails !");
        }

        ycCur.moveToFirst();


        return (byte)ycCur.getInt(0);
    }

    public int countNoRowsForSelectedTestStation (String adminNo)
    {
        ycCur = null;

        try
        {
            ycCur = _db.rawQuery("SELECT COUNT(*) FROM " + YC_NAPFA_RESULT_TABLE +
                    " WHERE " + ADM_NO + " = ?;", new String[]{adminNo});
        }
        catch (SQLException sle)
        {
            Log.w(MYDBADAPTER_LOG_CAT, "Retrieve Fails !");
        }

        ycCur.moveToFirst();

        return ycCur.getInt(0);

    }

    public Cursor arrangeSelectedTestStationOrder(String testStation, String adminNo, String orderType)
    {
        ycCur = null;

        Log.w("Arrange Test Station", "SELECT " + TEST_DATE + ", " +  testStation + " FROM " + YC_NAPFA_RESULT_TABLE +
                " WHERE " + ADM_NO + " = '" + adminNo  + "' ORDER BY " + testStation + " " + orderType + ";" );

        try
        {
            ycCur = _db.rawQuery("SELECT " + TEST_DATE + ", " +  testStation + " FROM " + YC_NAPFA_RESULT_TABLE +
                    " WHERE " + ADM_NO + " = ?" + " ORDER BY " + testStation + " " + orderType + ";", new String[]{adminNo});
            Log.w("Arrange Test Station", "Retrieve Suceed!" );
        }
        catch (SQLException sle)
        {
            Log.w("Arrange Test Station", "Retrieve Fails !");
        }

        return ycCur;
    }

    //Retrieve data from NapfaResultChartTable
    public Cursor retrieveSelectedTestStationIntRange(String colName, int rangeValue)
    {
        ycCur = null;

        try
        {
            ycCur = _db.rawQuery("SELECT " + PERF_GRD + ", " +  colName + " FROM " + YC_NAPFA_RESULT_CHART_TABLE +
                    " WHERE " + colName + " >= ?;", new String[]{"" + rangeValue});
        }
        catch (SQLException sle)
        {
            Log.w(MYDBADAPTER_LOG_CAT, "Retrieve Fails !");
        }

        return ycCur;
    }

    public Cursor retrieveSelectedTestStationFloatRange(String colName, float rangeValue)
    {
        ycCur = null;

        try
        {
            ycCur = _db.rawQuery("SELECT " + PERF_GRD + ", " +  colName + " FROM " + YC_NAPFA_RESULT_CHART_TABLE +
                    " WHERE " + colName + " >= ?;", new String[]{"" + rangeValue});
        }
        catch (SQLException sle)
        {
            Log.w(MYDBADAPTER_LOG_CAT, "Retrieve Fails !");
        }

        return ycCur;
    }


    /*public long insertEntry(String entryName, String entryTel)
    {
        //Insert a new task
        ContentValues newEntryValues = new ContentValues();
        //Assign values for each row
        newEntryValues.put(ENTRY_NAME, entryName);
        newEntryValues.put("ENTRY_TELEPHONE", entryTel);

        //Insert the row.
        Log.w("MYDBADAPTER_LOG_CAT", "Inserted EntryName = " + entryName + "EntryTel = " + entryTel + " into the "
                + DATABASE_TABLE + " table.");

        return _db.insert(DATABASE_TABLE, null, newEntryValues);
    }*/
    public long insertTestdData(ContentValues cv)
    {
        //Insert a new task
        ContentValues newEntryValues = new ContentValues(cv);

        //newEntryValues.putAll(cv);


        //Insert the row.
		/*Log.w("MYDBADAPTER_LOG_CAT", "Inserted EntryName = " + entryName + "EntryTel = " + entryTel + " into the "
				+ DATABASE_TABLE + " table.");*/

        return _db.insert(YC_NAPFA_RESULT_TABLE, null, newEntryValues);
    }

    public long insertNapfaResult(ContentValues cv)
    {
        ContentValues newEntryValues = new ContentValues(cv);

        return _db.insert(YC_NAPFA_RESULT_TABLE, null, newEntryValues);
    }


    // Ryan

    public Cursor retrieveName() {
        // TODO Auto-generated method stub
        Cursor c =null ;
        try{
            c = _db.query(RYAN_DATABASE_TABLE,
                    new String[] {ENTRY_NAME},
                    null, null, null, null, null);
        }
        catch(SQLException sle)
        {
            Log.w(MYDBADAPTER_LOG_CAT, "Retrieve fail! ");
        }
        return c;
    }
    public long insertName(String entryName)
    {
        //Insert a new task
        ContentValues newEntryValues = new ContentValues();
        //Assign values for each row.
        newEntryValues.put(ENTRY_NAME, entryName);
        //Insert the row.
        Log.w(MYDBADAPTER_LOG_CAT, "Inserted entry_name = "+
                entryName + "in to table "+
                RYAN_DATABASE_TABLE);
        return _db.insert(RYAN_DATABASE_TABLE, null, newEntryValues);
    }
    public void updateName(String entryName)
    {
        String query = null;
        try {

            query="update user set entry_name = '" + entryName +"' ";
            _db.execSQL(query);
        } catch (SQLException sle) {
            Log.e(MYDBADAPTER_LOG_CAT, "Update fail! ");
            Log.e(MYDBADAPTER_LOG_CAT, sle.toString());
        }
    }

    public long insertEntry(String entryName, String entryPwd,String entryAdmin,int addAgeInt)
    {
        //Insert a new task
        ContentValues newEntryValues = new ContentValues();
        //Assign values for each row.
        newEntryValues.put(ENTRY_NAME, entryName);
        newEntryValues.put(ENTRY_PWD, entryPwd);
        newEntryValues.put(ENTRY_ADMIN, entryAdmin);
        newEntryValues.put(ENTRY_AGE, addAgeInt);
        //Insert the row.
        Log.w(MYDBADAPTER_LOG_CAT, "Inserted EntryName = "+
                entryName + "EntryPwd = "+ entryPwd +"EntryAdmin"+entryAdmin+"EntryAge"+addAgeInt+ "in to table "+
                RYAN_DATABASE_TABLE);
        return _db.insert(RYAN_DATABASE_TABLE, null, newEntryValues);
    }

    public boolean removeEntry(long _rowIndex){
        if(_db.delete(RYAN_DATABASE_TABLE,
                KEY_ID + " = " + _rowIndex, null)<=0)
        {
            Log.w(MYDBADAPTER_LOG_CAT,
                    "Removing Entrying where id = " + _rowIndex + " Failed");
            return false;
        }
        Log.w(MYDBADAPTER_LOG_CAT,
                "Removing Entrying where id = " + _rowIndex + " Success");
        return true;
    }
    public Cursor retrieveAllEntriesCursor(String username, String pwd) {
        Cursor c =null ;
        try{
            c=_db.rawQuery("SELECT * FROM myTestDb WHERE " + ENTRY_NAME +" = ? AND " + ENTRY_PWD + " = ? ",new String[] {username,pwd});
            //c=_db.query( DATABASE_TABLE, null , "ENTRY_NAME = ? AND ENTRY_PWD = ?" , new String [] {username,pwd}, null, null, null);


        }
        catch(SQLException sle)
        {
            Log.w(MYDBADAPTER_LOG_CAT, "Retrieve fail! " + sle);
        }
        return c;
    }


}
