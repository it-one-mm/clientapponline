package com.itonemm.clientapponline;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedRef {
    Context context;
    SharedPreferences preferences;

    public SharedRef(Context context) {
        this.context = context;
        preferences=context.getSharedPreferences("playdata",Context.MODE_PRIVATE);
    }
    public void savePosiition(int position)
    {
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("currentPosition",position);
        editor.commit();
    }
    public int loadPosition()
    {
        return  preferences.getInt("currentPosition",0);
    }


}
