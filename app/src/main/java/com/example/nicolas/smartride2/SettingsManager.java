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

    // Memorized Manual run
    public static final String KEY_RUNMANUAL = "IsThereManualRun";

    // Memorized Motion Capture run
    public static final String KEY_RUNMOTION = "IsThereMotionRun";


    // Constructor
    public SettingsManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.putBoolean(KEY_RUNMANUAL,false);
        editor.putBoolean(KEY_RUNMOTION,false);
        editor.commit();
    }

    /**
     * Create settings session
     * */
    public void createSettingsSession(Boolean runManualPref, Boolean runMotionPref){

        // Storing name in pref
        editor.putBoolean(KEY_RUNMANUAL, runManualPref);
        editor.putBoolean(KEY_RUNMOTION, runMotionPref);
        // commit changes
        editor.commit();
    }

    public Boolean getManualRunPref(){
        Boolean runManualPref = pref.getBoolean(KEY_RUNMANUAL,false);
        return runManualPref;
    }

    public void setManualRunPref(Boolean runManualPref){
        editor.putBoolean(KEY_RUNMANUAL,runManualPref);
        editor.commit();
    }

    public Boolean getMotionRunPref(){
        Boolean runMotionPref = pref.getBoolean(KEY_RUNMOTION,false);
        return runMotionPref;
    }

    public void setMotionRunPref(Boolean runMotionPref){
        editor.putBoolean(KEY_RUNMOTION,runMotionPref);
        editor.commit();
    }

}
