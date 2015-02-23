package sg.edu.sit.ict.ippt_app.extras;

import java.text.DecimalFormat;
import java.util.Scanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import sg.edu.sit.ict.ippt_app.database.myDbAdapter;

/**
 * Created by User on 23/2/2015.
 */
public class yc_BL {

    private Context c = null;
    private myDbAdapter myDB;

    public yc_BL(Context c)
    {
        this.c = c;
        myDB = new myDbAdapter(c);
    }

    public String[][] processRetrieveNapfaResult(String adminNo)
    {
        Cursor myCur = null;
        String[][] napfaResult;

        myDB.open();
        myCur = myDB.retrieveSelectedAdminNoNapfa_Result(adminNo);
        napfaResult = new String[myCur.getCount()][myCur.getColumnCount() - 2];

        myCur.moveToFirst();

        for(int row=0; row<napfaResult.length;row++)
        {
            for(byte col=0;col<7;col++)
            {
                String tempStr = "";

                if( ( (col >= 1) && (col <=4) ) )
                {
                    tempStr = "" + myCur.getInt(col);
                }
                else if ((col == 0))
                {
                    tempStr = "" + myCur.getString(col);
                }
                else if (col == 6)
                {
                    //int secs = myCur.getInt(col);
                    tempStr = convertToMinsAndSecs(myCur.getInt(col));
                }
                else
                {
                    tempStr = ""+ myCur.getFloat(col);
                }


                if((col == 2) || (col == 3))
                {
                    tempStr += " cm";
                }
                else if (col == 5)
                {
                    tempStr += " secs";
                }

                napfaResult[row][col] = tempStr;
            }//end col for-loop

            myCur.moveToNext();
        }//end row for-loop
        myDB.close();
        myCur.close();

        return napfaResult;

    }

    public byte[][] processRetrieveTestStnPts(String adminNo, byte age)
    {
        String [][] napfaResult = processRetrieveNapfaResult(adminNo);
        int totalRows = napfaResult.length;
        byte totalCols = (byte) napfaResult[0].length;

        byte [][] napfaResultPts = new byte[totalRows][totalCols - 1];

        for(int rowNo=0;rowNo<totalRows;rowNo++)
        {
            for(byte colNo=1;colNo<totalCols;colNo++)
            {
                if (age == 19)
                {
                    napfaResultPts[rowNo][colNo -1] = processNapfaResultPts_19YrsOld(age,colNo,napfaResult[rowNo][colNo]);
                }//end if
                else
                {
                    napfaResultPts[rowNo][colNo -1] = processNapfaResultPts_20YrsOld(age,colNo,napfaResult[rowNo][colNo]);
                }//end else
            }
        }

        return napfaResultPts;
    }

    public byte processNapfaResultPts_19YrsOld(byte age, byte testStnIndex, String testStnResultStr)
    {
        byte pts = 0;

        int testStnResult = 0;

		/*if (testStnResult > -1)
		{*/
        if(testStnIndex == 6)
        {
            Scanner sc = new Scanner(testStnResultStr);
            sc.useDelimiter(":");
            //mins + secs
            testStnResult = ( (sc.nextInt()) * 60) + sc.nextInt();
            sc.close();
        }
		/*else if (testStnIndex == 4)
		{
			testStnResult = Integer.parseInt(testStnResultStr.replace(" secs", "") );
		}*/
        else if ((testStnIndex > 1) && (testStnIndex <=3))
        {
            testStnResult = Integer.parseInt(testStnResultStr.replace(" cm", "") );
        }
        else if ((testStnIndex == 1) || (testStnIndex == 4))
        {
            testStnResult = Integer.parseInt(testStnResultStr);
        }

        switch(testStnIndex)
        {
            case 1:
                pts = getSitUpPtsFor19YrsOld(testStnResult);
                break;

            case 2:
                pts = getSBJPtsFor19YrsOld(testStnResult);
                break;

            case 3:
                pts = getSNRPtsFor19YrsOld(testStnResult);
                break;

            case 4:
                pts = getPullUpPts(testStnResult);
                break;

            case 6:
                pts = get2pt4For19YrsOld(testStnResult);
                break;

            default:
                //Do Nothing
                pts = getShuttleRunPtsFor19YrsOld(Float.parseFloat(testStnResultStr.replace(" secs", "")) );
                break;

        } //end of switch statement

        return pts;
    }

    private byte getSitUpPtsFor19YrsOld(int testStnResult)
    {
        byte pts = 0;

        if(testStnResult >=43)
        {
            pts = 5;
        }
        else if((testStnResult >=40) && (testStnResult < 43))
        {
            pts = 4;
        }
        else if ((testStnResult >=37) && (testStnResult < 40))
        {
            pts = 3;
        }
        else if ((testStnResult >=34) && (testStnResult < 37))
        {
            pts = 2;
        }
        else if ((testStnResult >=31) && (testStnResult < 34))
        {
            pts = 1;
        }
        else
        {
            pts = 0;
        }

        return pts;
    }

    private byte getSBJPtsFor19YrsOld(int testStnResult)
    {
        byte pts = 0;

        if(testStnResult >= 252)
        {
            pts = 5;
        }
        else if((testStnResult >=242) && (testStnResult < 252))
        {
            pts = 4;
        }
        else if((testStnResult >=232) && (testStnResult < 242))
        {
            pts = 3;
        }
        else if((testStnResult >=222) && (testStnResult < 232))
        {
            pts = 2;
        }
        else if ((testStnResult >=212) && (testStnResult < 222))
        {
            pts = 1;
        }
        else
        {
            pts = 0;
        }

        return pts;
    }

    private byte getSNRPtsFor19YrsOld(int testStnResult)
    {
        byte pts = 0;

        if(testStnResult >= 49)
        {
            pts = 5;
        }
        else if((testStnResult >=45) && (testStnResult < 49))
        {
            pts = 4;
        }
        else if((testStnResult >=41) && (testStnResult < 45))
        {
            pts = 3;
        }
        else if((testStnResult >=37) && (testStnResult < 41))
        {
            pts = 2;
        }
        else if ((testStnResult >=32) && (testStnResult < 37))
        {
            pts = 1;
        }
        else
        {
            pts = 0;
        }

        return pts;
    }

    private byte get2pt4For19YrsOld(int testStnResult)
    {
        byte pts = 0;

        if(testStnResult <= 620)
        {
            pts = 5;
        }
        else if((testStnResult <=660) && (testStnResult > 620))
        {
            pts = 4;
        }
        else if((testStnResult <=700) && (testStnResult > 660))
        {
            pts = 3;
        }
        else if((testStnResult <=750) && (testStnResult > 700))
        {
            pts = 2;
        }
        else if ((testStnResult <=800) && (testStnResult > 750))
        {
            pts = 1;
        }
        else
        {
            pts = 0;
        }

        return pts;
    }

    private byte getShuttleRunPtsFor19YrsOld(float testStnResult)
    {
        byte pts = 0;

        if (testStnResult <= 10.1f)
        {
            pts = 5;
        }
        else if ((testStnResult <=10.3f) && (testStnResult > 10.1f))
        {
            pts = 4;
        }
        else if ((testStnResult <=10.5f) && (testStnResult > 10.3f))
        {
            pts = 3;
        }
        else if((testStnResult <=10.7f) && (testStnResult > 10.5f))
        {
            pts = 2;
        }
        else if((testStnResult <=10.9f)&& (testStnResult > 10.7f))
        {
            pts = 1;
        }
        else
        {
            pts = 0;
        }

        return pts;
    }

    public byte processNapfaResultPts_20YrsOld(byte age, byte testStnIndex, String testStnResultStr)
    {
        byte pts = 0;

        int testStnResult = 0;

		/*if (testStnResult > -1)
		{*/
        if(testStnIndex == 6)
        {
            Scanner sc = new Scanner(testStnResultStr);
            sc.useDelimiter(":");
            //mins + secs
            testStnResult = ( (sc.nextInt()) * 60) + sc.nextInt();
            sc.close();
        }
		/*else if (testStnIndex == 4)
		{
			testStnResult = Integer.parseInt(testStnResultStr.replace(" secs", "") );
		}*/
        else if ((testStnIndex > 1) && (testStnIndex <=3))
        {
            testStnResult = Integer.parseInt(testStnResultStr.replace(" cm", "") );
        }
        else if ((testStnIndex == 1) || (testStnIndex == 4))
        {
            testStnResult = Integer.parseInt(testStnResultStr);
        }

        switch(testStnIndex)
        {
            case 1:
                pts = getSitUpPtsFor20YrsOld(testStnResult);
                break;

            case 2:
                pts = getSBJPtsFor20YrsOld(testStnResult);
                break;

            case 3:
                pts = getSNRPtsFor20YrsOld(testStnResult);
                break;

            case 4:
                pts = getPullUpPts(testStnResult);
                break;

            case 6:
                pts = get2pt4PtsFor20YrsOld(testStnResult);
                break;

            default:
                //Do Nothing
                pts = getShuttleRunPtsFor20YrsOld(Float.parseFloat(testStnResultStr.replace(" secs", "")) );
                break;

        } //end of switch statement

        return pts;
    }

    private byte getSitUpPtsFor20YrsOld(int testStnResult)
    {
        byte pts = 0;

        if(testStnResult >=40)
        {
            pts = 5;
        }
        else if((testStnResult >=37) && (testStnResult < 40))
        {
            pts = 4;
        }
        else if ((testStnResult >=34) && (testStnResult < 37))
        {
            pts = 3;
        }
        else if ((testStnResult >=31) && (testStnResult < 34))
        {
            pts = 2;
        }
        else if ((testStnResult >=28) && (testStnResult < 31))
        {
            pts = 1;
        }
        else
        {
            pts = 0;
        }

        return pts;
    }

    private byte getSBJPtsFor20YrsOld(int testStnResult)
    {
        byte pts = 0;

        if(testStnResult >= 243)
        {
            pts = 5;
        }
        else if((testStnResult >=234) && (testStnResult < 243))
        {
            pts = 4;
        }
        else if((testStnResult >=225) && (testStnResult < 234))
        {
            pts = 3;
        }
        else if((testStnResult >=216) && (testStnResult < 225))
        {
            pts = 2;
        }
        else if ((testStnResult >=207) && (testStnResult < 216))
        {
            pts = 1;
        }
        else
        {
            pts = 0;
        }

        return pts;
    }

    private byte getSNRPtsFor20YrsOld(int testStnResult)
    {
        byte pts = 0;

        if(testStnResult >= 48)
        {
            pts = 5;
        }
        else if((testStnResult >=44) && (testStnResult < 48))
        {
            pts = 4;
        }
        else if((testStnResult >=40) && (testStnResult < 44))
        {
            pts = 3;
        }
        else if((testStnResult >=36) && (testStnResult < 40))
        {
            pts = 2;
        }
        else if ((testStnResult >=32) && (testStnResult < 36))
        {
            pts = 1;
        }
        else
        {
            pts = 0;
        }

        return pts;
    }

    private byte getPullUpPts(int testStnResult)
    {
        byte pts = 0;

        if(testStnResult >= 11)
        {
            pts = 5;
        }
        else if((testStnResult >=9) && (testStnResult < 11))
        {
            pts = 4;
        }
        else if((testStnResult >=7) && (testStnResult < 9))
        {
            pts = 3;
        }
        else if((testStnResult >=5) && (testStnResult < 7))
        {
            pts = 2;
        }
        else if ((testStnResult >=3) && (testStnResult < 5))
        {
            pts = 1;
        }
        else
        {
            pts = 0;
        }

        return pts;
    }

    private byte getShuttleRunPtsFor20YrsOld(float testStnResult)
    {

        byte pts = 0;

        if (testStnResult <= 10.3f)
        {
            pts = 5;
        }
        else if ((testStnResult <=10.5f) && (testStnResult > 10.3f))
        {
            pts = 4;
        }
        else if ((testStnResult <=10.7f) && (testStnResult > 10.5f))
        {
            pts = 3;
        }
        else if((testStnResult <=10.9f) && (testStnResult > 10.7f))
        {
            pts = 2;
        }
        else if((testStnResult <=11.1f)&& (testStnResult > 10.9f))
        {
            pts = 1;
        }
        else
        {
            pts = 0;
        }

        return pts;
    }

    private byte get2pt4PtsFor20YrsOld(int testStnResult)
    {
        byte pts = 0;

        if(testStnResult <= 620)
        {
            pts = 5;
        }
        else if((testStnResult <=660) && (testStnResult > 620))
        {
            pts = 4;
        }
        else if((testStnResult <=700) && (testStnResult > 660))
        {
            pts = 3;
        }
        else if((testStnResult <=740) && (testStnResult > 700))
        {
            pts = 2;
        }
        else if ((testStnResult <=780) && (testStnResult > 740))
        {
            pts = 1;
        }
        else
        {
            pts = 0;
        }

        return pts;
    }



    public int processRetrieveNoOfRows_SelectedTestStation(String adminNo)
    {
        int rowCount = 0;

        myDB.open();
        rowCount = myDB.countNoRowsForSelectedTestStation(adminNo);
        myDB.close();

        return rowCount;

    }

    //Perform check whether the selected testing station should be arranged DESC or ASC
    public String[][] processArrangeSelectedTestStationOrder(String testStation, String adminNo, String orderType, int noOfResults,
                                                             boolean isGetFloat, String unitType)
    {
        String[][] testStnArr;
        Cursor myCur = null;

        int totalRows = 0;
        byte totalCols = 0;

        myDB.open();
        myCur = myDB.arrangeSelectedTestStationOrder(testStation, adminNo, orderType);
        myCur.moveToFirst();

        if(noOfResults == 0)
        {
            totalRows = myCur.getCount();
        }
        else
        {
            totalRows = noOfResults;
        }
        //totalRows = noOfResults;
        totalCols = (byte) myCur.getColumnCount();
        testStnArr = new String[totalRows][totalCols];

        //Rows
        for(int rowCount=0;rowCount<totalRows;rowCount++)
        {
            for(byte colCount=0;colCount<totalCols;colCount++)
            {
                if (colCount == 0)
                {
                    testStnArr[rowCount][colCount] = myCur.getString(colCount);
                }
                else
                {
                    if(isGetFloat)
                    {
                        testStnArr[rowCount][colCount] = "" + myCur.getFloat(colCount) + unitType;
                    }
                    else
                    {
                        if(testStation.equals(myDbAdapter.KM_Run))
                        {
                            testStnArr[rowCount][colCount] = convertToMinsAndSecs(myCur.getInt(colCount)) + unitType;
                        }
                        else
                        {
                            testStnArr[rowCount][colCount] = "" + myCur.getInt(colCount) + unitType;
                        }

                    }
                }//end outer else

            }//end cols for-loop

            myCur.moveToNext();
        }//end rows for-loop

        myDB.close();
        myCur.close();

        return testStnArr;
    }

    public String[][] processRetrieveSelectedTestStationNapfaResult(String testStation, String adminNo, int noOfResults,
                                                                    boolean isGetFloat, String unitType)
    {
        String[][] testStnArr;
        Cursor myCur = null;

        int totalRows = 0;
        byte totalCols = 0;

        myDB.open();
        myCur = myDB.retrieveSelectedTestStationNapfaResult(testStation, adminNo);
        myCur.moveToFirst();

        if(noOfResults == 0)
        {
            totalRows = myCur.getCount();
        }
        else
        {
            totalRows = noOfResults;
        }
        //totalRows = noOfResults;
        totalCols = (byte) myCur.getColumnCount();
        testStnArr = new String[totalRows][totalCols];

        //Rows
        for(int rowCount=0;rowCount<totalRows;rowCount++)
        {
            for(byte colCount=0;colCount<totalCols;colCount++)
            {
                if (colCount == 0)
                {
                    testStnArr[rowCount][colCount] = myCur.getString(colCount);
                }
                else
                {
                    if(isGetFloat)
                    {
                        testStnArr[rowCount][colCount] = "" + myCur.getFloat(colCount) + unitType;
                    }
                    else
                    {
                        if(testStation.equals(myDbAdapter.KM_Run))
                        {
                            testStnArr[rowCount][colCount] = convertToMinsAndSecs(myCur.getInt(colCount)) + unitType;
                        }
                        else
                        {
                            testStnArr[rowCount][colCount] = "" + myCur.getInt(colCount) + unitType;
                        }

                    }
                }//end outer else

            }//end cols for-loop

            myCur.moveToNext();
        }//end rows for-loop

        myDB.close();
        myCur.close();

        return testStnArr;
    }

    public String processResultAchievement_Status(int age, int testStnIndex, String testStnResultStr)
    {
        String msg = "";

        if (age == 19)
        {
            msg = getAnotherGradeTargetFor19YrsOld(testStnIndex, testStnResultStr);
        }
        else
        {
            msg = getAnotherGradeTargetFor20YrsOld(testStnIndex, testStnResultStr);
        }

        return msg;
    }

    public String processShuttleRunResultAchievement_Status(int age, float testStnResult)
    {
        String msg = "";

        if(age==19)
        {
            msg = getShuttleRunTargetFor19YrsOld(testStnResult);
        }
        else
        {
            msg = getShuttleRunTargetFor20YrsOld(testStnResult);
        }

        return msg;
    }


    public String convertToMinsAndSecs(int secs)
    {
        return "" + (int)secs/60 + ":" + secs%60;
    }

    //if testStnResult == -1, isGetFloat is True
    private String getAnotherGradeTargetFor19YrsOld(int testStnIndex, String testStnResultStr)
    {
        String upGradeMsg = "You achieved ";
        int testStnResult = 0;

		/*if (testStnResult > -1)
		{*/
        if(testStnIndex == 5)
        {
            Scanner sc = new Scanner(testStnResultStr);
            sc.useDelimiter(":");
            //mins + secs
            testStnResult = ( (sc.nextInt()) * 60) + sc.nextInt();
            sc.close();
        }
		/*else if (testStnIndex == 4)
		{
			testStnResult = Integer.parseInt(testStnResultStr.replace(" secs", "") );
		}*/
        else if ((testStnIndex > 0) && (testStnIndex <=2))
        {
            testStnResult = Integer.parseInt(testStnResultStr.replace(" cm", "") );
        }
        else if ((testStnIndex == 0) || (testStnIndex == 3))
        {
            testStnResult = Integer.parseInt(testStnResultStr);
        }

        switch(testStnIndex)
        {
            case 0:
                if(testStnResult >=43)
                {
                    upGradeMsg += " A for Sit-Ups. Well done and keep it up!";
                }
                else if((testStnResult >=40) && (testStnResult < 43))
                {
                    upGradeMsg += " B for Sit-Ups. Keep it up! You are just " + (43 - testStnResult)
                            + " reps away from acheiving A!";
                }
                else if ((testStnResult >=37) && (testStnResult < 40))
                {
                    upGradeMsg += " C for Sit-Ups. Keep it up! You are " + (40 - testStnResult)
                            + " reps away from acheiving B!";
                }
                else if ((testStnResult >=34) && (testStnResult < 37))
                {
                    upGradeMsg = "You had just met the IPPT standard (with a D) for Sit-Up. Keep working hard! You are " + (37 - testStnResult)
                            + " reps away from acheiving C!";
                }
                else if ((testStnResult >=31) && (testStnResult < 34))
                {
                    upGradeMsg = "You did not meet the IPPT Standard (with a E) due to Sit-Ups. Will have to work Harder! You are " + (34 - testStnResult)
                            + " reps away from meeting the IPPT standard (D grade) for Sit-Ups!";
                }
                else
                {
                    upGradeMsg = "You did not qualify a pass for Sit-Ups. Will have to work even tougher! You are " + (34 - testStnResult)
                            + " reps away from meeting the IPPT standard (D grade) for Sit-Ups!";
                }

                break;

            case 1:
                if(testStnResult >= 252)
                {
                    upGradeMsg += " A for Standing Board Jump. Well done and keep it up!";
                }
                else if((testStnResult >=242) && (testStnResult < 252))
                {
                    upGradeMsg += " B for Standing Board Jump. Keep it up! You are just " + (252 - testStnResult)
                            + " cm away from acheiving A!";
                }
                else if((testStnResult >=232) && (testStnResult < 242))
                {
                    upGradeMsg += " C for Standing Board Jump. Keep it up! You are " + (242 - testStnResult)
                            + " cm away from acheiving B!";
                }
                else if((testStnResult >=222) && (testStnResult < 232))
                {
                    upGradeMsg = "You had just met the IPPT standard (with a D) for Standing Board Jump. Keep working hard! You are " + (232 - testStnResult)
                            + " cm away from acheiving C!";
                }
                else if ((testStnResult >=212) && (testStnResult < 222))
                {
                    upGradeMsg = "You did not meet the IPPT Standard (with a E) due to Standing Board Jump. Will have to work Harder! You are " + (222 - testStnResult)
                            + " cm away from meeting the IPPT standard (D grade) for Standing Broad Jump!";
                }
                else
                {
                    upGradeMsg = "You did not qualify a pass for Standing Board Jump. Will have to work even tougher! You are " + (222 - testStnResult)
                            + " cm away from meeting the IPPT standard (D grade) for Standing Broad Jump!";
                }
                break;

            case 2:
                if(testStnResult >= 49)
                {
                    upGradeMsg += " A for Sit and Reach. Well done and keep it up!";
                }
                else if((testStnResult >=45) && (testStnResult < 49))
                {
                    upGradeMsg += " B for Sit and Reach. Keep it up! You are just " + (49 - testStnResult)
                            + " cm away from acheiving A!";
                }
                else if((testStnResult >=41) && (testStnResult < 45))
                {
                    upGradeMsg += " C for Sit and Reach. Keep it up! You are " + (45 - testStnResult)
                            + " cm away from acheiving B!";
                }
                else if((testStnResult >=37) && (testStnResult < 41))
                {
                    upGradeMsg = "You had just met the IPPT standard (with a D) for Sit and Reach. Keep working hard! You are " + (41 - testStnResult)
                            + " cm away from acheiving C!";
                }
                else if ((testStnResult >=32) && (testStnResult < 37))
                {
                    upGradeMsg = "You did not meet the IPPT Standard (with a E) due to Sit and Reach. Will have to work Harder! You are " + (37 - testStnResult)
                            + " cm away from meeting the IPPT standard (D grade) for Sit and Reach!";
                }
                else
                {
                    upGradeMsg = "You did not qualify a pass for Sit and Reach. Will have to work even tougher! You are " + (37 - testStnResult)
                            + " cm away from meeting the IPPT standard (D grade) for Sit and Reach!";
                }
                break;

            case 3:
                if(testStnResult >= 11)
                {
                    upGradeMsg += " A for Pull ups. Well done and keep it up!";
                }
                else if((testStnResult >=9) && (testStnResult < 11))
                {
                    upGradeMsg += " B for Pull ups. Keep it up! You are just " + (11 - testStnResult)
                            + " reps away from acheiving A!";
                }
                else if((testStnResult >=7) && (testStnResult < 9))
                {
                    upGradeMsg += " C for Pull ups. Keep it up! You are " + (9 - testStnResult)
                            + " reps away from acheiving B!";
                }
                else if((testStnResult >=5) && (testStnResult < 7))
                {
                    upGradeMsg = "You had just met the IPPT standard (with a D) for Pull ups. Keep working hard! You are " + (7 - testStnResult)
                            + " reps away from acheiving C!";
                }
                else if ((testStnResult >=3) && (testStnResult < 5))
                {
                    upGradeMsg = "You did not meet the IPPT Standard (with a E) due to Pull ups. Will have to work Harder! You are " + (5 - testStnResult)
                            + " reps away from meeting the IPPT standard (D grade) for Pull ups!";
                }
                else
                {
                    upGradeMsg = "You did not qualify a pass for Pull ups. Will have to work even tougher! You are " + (5 - testStnResult)
                            + " reps away from meeting the IPPT standard (D grade) for Pull ups!";
                }
                break;

            case 5:
                if(testStnResult <= 620)
                {
                    upGradeMsg += " A for 2.4KM Run. Well done and keep it up!";
                }
                else if((testStnResult <=660) && (testStnResult > 620))
                {
                    upGradeMsg += " B for 2.4KM Run. Keep it up! You are just " + convertToMinsAndSecs(testStnResult - 620).replace(":", " mins ")
                            + " secs away from acheiving A!";
                }
                else if((testStnResult <=700) && (testStnResult > 660))
                {
                    upGradeMsg += " C for 2.4KM Run. Keep it up! You are " + convertToMinsAndSecs(testStnResult - 660).replace(":", " mins ")
                            + " secs away from acheiving B!";
                }
                else if((testStnResult <=750) && (testStnResult > 700))
                {
                    upGradeMsg = "You had just met the IPPT standard (with a D) for 2.4KM Run. Keep working hard! You are " + convertToMinsAndSecs(testStnResult - 700).replace(":", " mins ")
                            + " secs away from acheiving C!";
                }
                else if ((testStnResult <=800) && (testStnResult > 750))
                {
                    upGradeMsg = "You did not meet the IPPT Standard (with a E) due to 2.4KM Run. Will have to work Harder! You are " + convertToMinsAndSecs(testStnResult - 750).replace(":", " mins ")
                            + " secs away from meeting the IPPT standard (D grade) for 2.4KM Run!";
                }
                else
                {
                    upGradeMsg = "You did not qualify a pass for 2.4KM Run. Will have to work even tougher! You are " + convertToMinsAndSecs(testStnResult - 750).replace(":", " mins ")
                            + " secs away from meeting the IPPT standard (D grade) for 2.4KM Run!";
                }
                break;

            default:
                //Do Nothing
                upGradeMsg = getShuttleRunTargetFor19YrsOld(Float.parseFloat(testStnResultStr.replace(" secs", "")) );
                break;

        } //end of switch statement

		/*}//end if
		else
		{

		}*/


        return upGradeMsg;
    }

    private String getShuttleRunTargetFor19YrsOld(float testStnResult)
    {
        String upGradeMsg = "You acheived ";

        if (testStnResult <= 10.1f)
        {
            upGradeMsg += " A for Shuttle Run. Well done and keep it up!";
        }
        else if ((testStnResult <=10.3f) && (testStnResult > 10.1f))
        {
            upGradeMsg += " B for Shuttle Run. Keep it up! You are just " + new DecimalFormat("#.##").format(testStnResult - 10.1f)
                    + " secs away from acheiving A!";
        }
        else if ((testStnResult <=10.5f) && (testStnResult > 10.3f))
        {
            upGradeMsg += " C for Shuttle Run. Keep it up! You are " + new DecimalFormat("#.##").format(testStnResult - 10.3f)
                    + " secs away from acheiving B!";
        }
        else if((testStnResult <=10.7f) && (testStnResult > 10.5f))
        {
            upGradeMsg = "You had just met the IPPT standard (with a D) for Shuttle Run. Keep working hard! You are " +
                    new DecimalFormat("#.##").format(testStnResult - 10.5f) + " secs away from acheiving C!";
        }
        else if((testStnResult <=10.9f)&& (testStnResult > 10.7f))
        {
            upGradeMsg = "You did not meet the IPPT Standard (with a E) due to Shuttle Run. Will have to work Harder! You are " +
                    new DecimalFormat("#.##").format(testStnResult - 10.7f) + " secs away from meeting the IPPT standard (D grade) for Shuttle Run!";
        }
        else
        {
            upGradeMsg = "You did not qualify a pass for Shuttle Run. Will have to work even tougher! You are " +
                    new DecimalFormat("#.##").format(testStnResult - 10.7f) + " secs away from meeting the IPPT standard (D grade) for Shuttle Run!";
        }

        return upGradeMsg;
    }

    private String getAnotherGradeTargetFor20YrsOld(int testStnIndex, String testStnResultStr)
    {
        String upGradeMsg = "You achieved ";

		/*if (testStnResult > -1)
		{*/
        int testStnResult=0;

        if(testStnIndex == 5)
        {
            Scanner sc = new Scanner(testStnResultStr);
            sc.useDelimiter(":");
            //mins + secs
            testStnResult = ( (sc.nextInt()) * 60) + sc.nextInt();
            sc.close();
        }
		/*else if (testStnIndex == 4)
		{
			testStnResult = Integer.parseInt(testStnResultStr.replace(" secs", "") );
		}*/
        else if ((testStnIndex > 0) && (testStnIndex <=2))
        {
            testStnResult = Integer.parseInt(testStnResultStr.replace(" cm", "") );
        }
        else if ((testStnIndex == 0) || (testStnIndex == 3))
        {
            testStnResult = Integer.parseInt(testStnResultStr);
        }

        switch(testStnIndex)
        {
            case 0:
                if(testStnResult >=40)
                {
                    upGradeMsg += " A for Sit-Ups. Well done and keep it up!";
                }
                else if((testStnResult >=37) && (testStnResult < 40))
                {
                    upGradeMsg += " B for Sit-Ups. Keep it up! You are just " + (40 - testStnResult)
                            + " reps away from acheiving A!";
                }
                else if ((testStnResult >=34) && (testStnResult < 37))
                {
                    upGradeMsg += " C for Sit-Ups. Keep it up! You are " + (37 - testStnResult)
                            + " reps away from acheiving B!";
                }
                else if ((testStnResult >=31) && (testStnResult < 34))
                {
                    upGradeMsg = "You had just met the IPPT standard (with a D) for Sit-Up. Keep working hard! You are " + (34 - testStnResult)
                            + " reps away from acheiving C!";
                }
                else if ((testStnResult >=28) && (testStnResult < 31))
                {
                    upGradeMsg = "You did not meet the IPPT Standard (with a E) due to Sit-Ups. Will have to work Harder! You are " + (31 - testStnResult)
                            + " reps away from meeting the IPPT standard (D grade) for Sit-Ups!";
                }
                else
                {
                    upGradeMsg = "You did not qualify a pass for Sit-Ups. Will have to work even tougher! You are " + (31 - testStnResult)
                            + " reps away from meeting the IPPT standard (D grade) for Sit-Ups!";
                }

                break;

            case 1:
                if(testStnResult >= 243)
                {
                    upGradeMsg += " A for Standing Board Jump. Well done and keep it up!";
                }
                else if((testStnResult >=234) && (testStnResult < 243))
                {
                    upGradeMsg += " B for Standing Board Jump. Keep it up! You are just " + (243 - testStnResult)
                            + " cm away from acheiving A!";
                }
                else if((testStnResult >=225) && (testStnResult < 234))
                {
                    upGradeMsg += " C for Standing Board Jump. Keep it up! You are " + (234 - testStnResult)
                            + " cm away from acheiving B!";
                }
                else if((testStnResult >=216) && (testStnResult < 225))
                {
                    upGradeMsg = "You had just met the IPPT standard (with a D) for Standing Board Jump. Keep working hard! You are " + (225 - testStnResult)
                            + " cm away from acheiving C!";
                }
                else if ((testStnResult >=207) && (testStnResult < 216))
                {
                    upGradeMsg = "You did not meet the IPPT Standard (with a E) due to Standing Board Jump. Will have to work Harder! You are " + (216 - testStnResult)
                            + " cm away from meeting the IPPT standard (D grade) for Standing Broad Jump!";
                }
                else
                {
                    upGradeMsg = "You did not qualify a pass for Standing Board Jump. Will have to work even tougher! You are " + (216 - testStnResult)
                            + " cm away from meeting the IPPT standard (D grade) for Standing Broad Jump!";
                }
                break;

            case 2:
                if(testStnResult >= 48)
                {
                    upGradeMsg += " A for Sit and Reach. Well done and keep it up!";
                }
                else if((testStnResult >=44) && (testStnResult < 48))
                {
                    upGradeMsg += " B for Sit and Reach. Keep it up! You are just " + (48 - testStnResult)
                            + " cm away from acheiving A!";
                }
                else if((testStnResult >=40) && (testStnResult < 44))
                {
                    upGradeMsg += " C for Sit and Reach. Keep it up! You are " + (44 - testStnResult)
                            + " cm away from acheiving B!";
                }
                else if((testStnResult >=36) && (testStnResult < 40))
                {
                    upGradeMsg = "You had just met the IPPT standard (with a D) for Sit and Reach. Keep working hard! You are " + (40 - testStnResult)
                            + " cm away from acheiving C!";
                }
                else if ((testStnResult >=32) && (testStnResult < 36))
                {
                    upGradeMsg = "You did not meet the IPPT Standard (with a E) due to Sit and Reach. Will have to work Harder! You are " + (36 - testStnResult)
                            + " cm away from meeting the IPPT standard (D grade) for Sit and Reach!";
                }
                else
                {
                    upGradeMsg = "You did not qualify a pass for Sit and Reach. Will have to work even tougher! You are " + (36 - testStnResult)
                            + " cm away from meeting the IPPT standard (D grade) for Sit and Reach!";
                }
                break;

            case 3:
                if(testStnResult >= 11)
                {
                    upGradeMsg += " A for Pull ups. Well done and keep it up!";
                }
                else if((testStnResult >=9) && (testStnResult < 11))
                {
                    upGradeMsg += " B for Pull ups. Keep it up! You are just " + (11 - testStnResult)
                            + " reps away from acheiving A!";
                }
                else if((testStnResult >=7) && (testStnResult < 9))
                {
                    upGradeMsg += " C for Pull ups. Keep it up! You are " + (9 - testStnResult)
                            + " reps away from acheiving B!";
                }
                else if((testStnResult >=5) && (testStnResult < 7))
                {
                    upGradeMsg = "You had just met the IPPT standard (with a D) for Pull ups. Keep working hard! You are " + (7 - testStnResult)
                            + " reps away from acheiving C!";
                }
                else if ((testStnResult >=3) && (testStnResult < 5))
                {
                    upGradeMsg = "You did not meet the IPPT Standard (with a E) due to Pull ups. Will have to work Harder! You are " + (5 - testStnResult)
                            + " reps away from meeting the IPPT standard (D grade) for Pull ups!";
                }
                else
                {
                    upGradeMsg = "You did not qualify a pass for Pull ups. Will have to work even tougher! You are " + (5 - testStnResult)
                            + " reps away from meeting the IPPT standard (D grade) for Pull ups!";
                }
                break;

            case 5:
                if(testStnResult <= 620)
                {
                    upGradeMsg += " A for 2.4KM Run. Well done and keep it up!";
                }
                else if((testStnResult <=660) && (testStnResult > 620))
                {
                    upGradeMsg += " B for 2.4KM Run. Keep it up! You are just " + convertToMinsAndSecs(testStnResult - 620).replace(":", " mins ")
                            + " secs away from acheiving A!";
                }
                else if((testStnResult <=700) && (testStnResult > 660))
                {
                    upGradeMsg += " C for 2.4KM Run. Keep it up! You are " + convertToMinsAndSecs(testStnResult - 660).replace(":", " mins ")
                            + " secs away from acheiving B!";
                }
                else if((testStnResult <=740) && (testStnResult > 700))
                {
                    upGradeMsg = "You had just met the IPPT standard (with a D) for 2.4KM Run. Keep working hard! You are " + convertToMinsAndSecs(testStnResult - 700).replace(":", " mins ")
                            + " secs away from acheiving C!";
                }
                else if ((testStnResult <=780) && (testStnResult > 740))
                {
                    upGradeMsg = "You did not meet the IPPT Standard (with a E) due to 2.4KM Run. Will have to work Harder! You are " + convertToMinsAndSecs(testStnResult - 740).replace(":", " mins ")
                            + " secs away from meeting the IPPT standard (D grade) for 2.4KM Run!";
                }
                else
                {
                    upGradeMsg = "You did not qualify a pass for 2.4KM Run. Will have to work even tougher! You are " + convertToMinsAndSecs(testStnResult - 740).replace(":", " mins ")
                            + " secs away from meeting the IPPT standard (D grade) for 2.4KM Run!";
                }
                break;

            default:
                //Do Nothing
                upGradeMsg = getShuttleRunTargetFor20YrsOld(Float.parseFloat(testStnResultStr.replace(" secs", "")) );
                break;

        } //end of switch statement

		/*}//end if
		else
		{

		}*/


        return upGradeMsg;
    }

    private String getShuttleRunTargetFor20YrsOld(float testStnResult)
    {

        String upGradeMsg = "You acheived ";

        if (testStnResult <= 10.3f)
        {
            upGradeMsg += " A for Shuttle Run. Well done and keep it up!";
        }
        else if ((testStnResult <=10.5f) && (testStnResult > 10.3f))
        {
            upGradeMsg += " B for Shuttle Run. Keep it up! You are just " + new DecimalFormat("#.##").format(testStnResult - 10.3f)
                    + " secs away from acheiving A!";
        }
        else if ((testStnResult <=10.7f) && (testStnResult > 10.5f))
        {
            upGradeMsg += " C for Shuttle Run. Keep it up! You are " + new DecimalFormat("#.##").format(testStnResult - 10.5f)
                    + " secs away from acheiving B!";
        }
        else if((testStnResult <=10.9f) && (testStnResult > 10.7f))
        {
            upGradeMsg = "You had just met the IPPT standard (with a D) for Shuttle Run. Keep working hard! You are " +
                    new DecimalFormat("#.##").format(testStnResult - 10.7f) + " secs away from acheiving C!";
        }
        else if((testStnResult <=11.1f)&& (testStnResult > 10.9f))
        {
            upGradeMsg = "You did not meet the IPPT Standard (with a E) due to Shuttle Run. Will have to work Harder! You are " +
                    new DecimalFormat("#.##").format(testStnResult - 10.9f) + " secs away from meeting the IPPT standard (D grade) for Shuttle Run!";
        }
        else
        {
            upGradeMsg = "You did not qualify a pass for Shuttle Run. Will have to work even tougher! You are " +
                    new DecimalFormat("#.##").format(testStnResult - 10.9f) + " secs away from meeting the IPPT standard (D grade) for Shuttle Run!";
        }

        return upGradeMsg;


    }

    //Insert to DB
    public void processInsertTestDataNapfaResult()
    {
        ContentValues myValues = new ContentValues();
        String [] testDate = {"23/5/2011","26/5/2011","30/5/2011","3/6/2011","7/6/2011"};
        byte [] sitUp = {31,31,35,37,40};
        byte [] SR = {31,33,33,37,39};
        int [] SBJ = {206,210,214,217,222};
        byte [] PU = {2,2,4,5,5};
        float [] Shuttle = {12.0f,11.5f,11.0f,10.7f,10.5f};
        int [] KM = {805,770,755,743,739};
        String[] achievedIPPT = {"N","N","N","N","Y"};
        String[] award = {"NA","NA","NA","NA","SILVER"};

        for(int i=0;i<5;i++)
        {
            myValues.put("adminNo","096228B");
            myValues.put("testDate", testDate[i]);
            myValues.put("sitUp", sitUp[i]);
            myValues.put("standingBoardJump", SBJ[i]);
            myValues.put("sitAndReach", SR[i]);
            myValues.put("pullUp", PU[i]);
            myValues.put("shuttleRun", Shuttle[i]);
            myValues.put("KMRun", KM[i]);
            myValues.put("achievedIPPTStd", achievedIPPT[i]);
            myValues.put("awardAchieved", award[i]);

            myDB.open();
            long blah = myDB.insertTestdData(myValues);
            myDB.close();
            myValues.clear();
        }
    }

    public long processInsertNapfaResult(String adminNo, byte age, String testDate, byte sitUp, int sbj, byte sNR,
                                         byte pullUp, float shuttle, int longRun)
    {
        int totalPts = 0;
        byte[] ptsArr;

        boolean achieveGold = true;
        boolean achieveSilver = true;

        String achievedIPPTStd = "";
        String award = "";

        if(age == 19)
        {
            totalPts = getSitUpPtsFor19YrsOld(sitUp) + getSBJPtsFor19YrsOld(sbj) + getSNRPtsFor19YrsOld(sNR) +
                    getShuttleRunPtsFor19YrsOld(shuttle) + get2pt4For19YrsOld(longRun);

            byte[] tempArr = {getSitUpPtsFor19YrsOld(sitUp),getSBJPtsFor19YrsOld(sbj),getSNRPtsFor19YrsOld(sNR),
                    getPullUpPts(pullUp),getShuttleRunPtsFor19YrsOld(shuttle),get2pt4For19YrsOld(longRun)};

            ptsArr = tempArr;
        }
        else
        {
            totalPts = getSitUpPtsFor20YrsOld(sitUp) + getSBJPtsFor20YrsOld(sbj) + getSNRPtsFor20YrsOld(sNR) +
                    getShuttleRunPtsFor20YrsOld(shuttle) + get2pt4PtsFor20YrsOld(longRun);

            byte[] tempArr = {getSitUpPtsFor20YrsOld(sitUp),getSBJPtsFor20YrsOld(sbj),getSNRPtsFor20YrsOld(sNR),
                    getPullUpPts(pullUp),getShuttleRunPtsFor20YrsOld(shuttle),get2pt4PtsFor20YrsOld(longRun)};

            ptsArr = tempArr;
        }
        totalPts += getPullUpPts(pullUp);

        for(byte i=0;i<ptsArr.length;i++)
        {
            if(ptsArr[i] < 3)
            {
                achieveGold = false;

            }

            if(ptsArr[i] < 2)
            {
                achieveSilver = false;
                break;
            }
        }

        if( (achieveGold == true) && (totalPts >=21))
        {
            achievedIPPTStd = "Y";
            award="GOLD";
        }
        else if ( (achieveSilver == true) && ( (totalPts >=15)) )
        {
            achievedIPPTStd = "Y";
            award="SILVER";
        }
        else
        {
            achievedIPPTStd = "N";
            award="NA";
        }


        ContentValues myValues = new ContentValues();

        myValues.put("adminNo", adminNo);
        myValues.put("testDate", testDate);
        myValues.put("sitUp", sitUp);
        myValues.put("standingBoardJump", sbj);
        myValues.put("sitAndReach", sNR);
        myValues.put("pullUp", pullUp);
        myValues.put("shuttleRun", shuttle);
        myValues.put("KMRun", longRun);
        myValues.put("achievedIPPTStd", achievedIPPTStd);
        myValues.put("awardAchieved", award);

        myDB.open();
        long insertStatus = myDB.insertNapfaResult(myValues);
        myDB.close();

        return insertStatus;
    }

    public boolean processCheckIfTestDateExists(String todayDate, String adminNo)
    {
        boolean existsCheck = false;
        byte rowCount = 0;

        myDB.open();
        rowCount = myDB.retrieveExistTestDate(todayDate, adminNo);
        myDB.close();

        if(rowCount > 0)
        {
            existsCheck = true;
        }

        return existsCheck;
    }


}
