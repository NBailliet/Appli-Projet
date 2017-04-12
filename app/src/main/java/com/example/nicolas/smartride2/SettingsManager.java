package com.example.nicolas.smartride2;

/**
 * Created by Nicolas on 07/04/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Nicolas on 07/04/2017.
 */

public class SettingsManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "SmartRideSettingsPref";

    // Memorized Started Manual run
    public static final String KEY_RUNSTARTMANUAL = "IsThereStartedManualRun";

    // Memorized Started Motion Capture run
    public static final String KEY_RUNSTARTMOTION = "IsThereStartedMotionRun";

    // Memorized Finished Manual run
    public static final String KEY_RUNFINISHMANUAL = "IsThereFinishedManualRun";

    // Memorized Finished Motion Capture run
    public static final String KEY_RUNFINISHMOTION = "IsThereFinishedMotionRun";

    // Memorized Number of Runs
    public static final String KEY_RUNNB = "RunNumber";

    // Memorized Start Pause Needed
    public static final String KEY_STARTPAUSE = "IsStartPauseNeeded";

    // Memorized Stop Pause Needed
    public static final String KEY_STOPPAUSE = "IsStopPauseNeeded";

    // Memorized GPS Auto Tracking Settings
    public static final String KEY_GPSTRACK = "GPSAutoTracking";

    // Memorized Bluetooth State
    public static final String KEY_BLUETOOTH = "BluetoothState";

    // Constructor
    public SettingsManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.putBoolean(KEY_RUNSTARTMANUAL,false);
        editor.putBoolean(KEY_RUNSTARTMOTION,false);
        editor.putBoolean(KEY_RUNFINISHMANUAL,false);
        editor.putBoolean(KEY_RUNFINISHMOTION,false);
        editor.putInt(KEY_RUNNB,0);
        editor.putBoolean(KEY_STARTPAUSE,false);
        editor.putBoolean(KEY_STOPPAUSE,false);
        editor.putBoolean(KEY_GPSTRACK,true);
        editor.putBoolean(KEY_BLUETOOTH,false);
        editor.commit();
    }

    /**
     * Create settings session
     * */
    public void createSettingsSession(Boolean runManualPref, Boolean runMotionPref){

        // Storing name in pref
        editor.putBoolean(KEY_RUNSTARTMANUAL, runManualPref);
        editor.putBoolean(KEY_RUNSTARTMOTION, runMotionPref);
        // commit changes
        editor.commit();
    }



    public Boolean getStartManualRunPref(){
        Boolean runStartManualPref = pref.getBoolean(KEY_RUNSTARTMANUAL,false);
        return runStartManualPref;
    }

    public void setStartManualRunPref(Boolean runStartManualPref){
        editor.putBoolean(KEY_RUNSTARTMANUAL,runStartManualPref);
        editor.commit();
    }

    public Boolean getStartMotionRunPref(){
        Boolean runStartMotionPref = pref.getBoolean(KEY_RUNSTARTMOTION,false);
        return runStartMotionPref;
    }

    public void setStartMotionRunPref(Boolean runStartMotionPref){
        editor.putBoolean(KEY_RUNSTARTMOTION,runStartMotionPref);
        editor.commit();
    }



    public Boolean getFinishedManualRunPref(){
        Boolean runFinishManualPref = pref.getBoolean(KEY_RUNFINISHMANUAL,false);
        return runFinishManualPref;
    }

    public void setFinishedManualRunPref(Boolean runFinishManualPref){
        editor.putBoolean(KEY_RUNFINISHMANUAL,runFinishManualPref);
        editor.commit();
    }

    public Boolean getFinishedMotionRunPref(){
        Boolean runFinishMotionPref = pref.getBoolean(KEY_RUNFINISHMOTION,false);
        return runFinishMotionPref;
    }

    public void setFinishedMotionRunPref(Boolean runFinishMotionPref){
        editor.putBoolean(KEY_RUNFINISHMOTION,runFinishMotionPref);
        editor.commit();
    }



    public int getRunNbPref(){
        int runNbPref = pref.getInt(KEY_RUNNB,0);
        return runNbPref;
    }

    public void setRunNbPref(int runNbPref){
        editor.putInt(KEY_RUNNB,runNbPref);
        editor.commit();
    }

    public void addRunNbPref() {
        editor.putInt(KEY_RUNNB,pref.getInt(KEY_RUNNB,0)+1);
        editor.commit();
    }



    public Boolean getStartPausePref(){
        Boolean runStartPausePref = pref.getBoolean(KEY_STARTPAUSE,false);
        return runStartPausePref;
    }

    public void setStartPausePref(Boolean runStartPausePref){
        editor.putBoolean(KEY_STARTPAUSE,runStartPausePref);
        editor.commit();
    }

    public Boolean getStopPausePref(){
        Boolean runStopPausePref = pref.getBoolean(KEY_STOPPAUSE,false);
        return runStopPausePref;
    }

    public void setStopPausePref(Boolean runStopPausePref){
        editor.putBoolean(KEY_STOPPAUSE,runStopPausePref);
        editor.commit();
    }

    public Boolean getGPSTrackPref(){
        Boolean runGPSTrackPref = pref.getBoolean(KEY_GPSTRACK,false);
        return runGPSTrackPref;
    }

    public void setGPSTrackPref(Boolean runGPSTrackPref){
        editor.putBoolean(KEY_GPSTRACK,runGPSTrackPref);
        editor.commit();
    }

    public Boolean getBluetoothStatePref(){
        Boolean bluetoothStatePref = pref.getBoolean(KEY_BLUETOOTH,false);
        return bluetoothStatePref;
    }

    public void setBluetoothStatePref(Boolean bluetoothStatePref){
        editor.putBoolean(KEY_BLUETOOTH,bluetoothStatePref);
        editor.commit();
    }

}
