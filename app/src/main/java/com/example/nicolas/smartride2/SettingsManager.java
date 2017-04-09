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

    // Memorized run
    public static final String KEY_RUN = "IsThereRun";

    // Constructor
    public SettingsManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create settings session
     * */
    public void createSettingsSession(Boolean run){

        // Storing name in pref
        editor.putBoolean(KEY_RUN, run);

        // commit changes
        editor.commit();
    }

    public Boolean getRunPref(){
        Boolean runPref = pref.getBoolean(KEY_RUN,false);
        return runPref;
    }

    public void setRunPref(Boolean runPref){
        editor.putBoolean(KEY_RUN,runPref);
        editor.commit();
    }

}
