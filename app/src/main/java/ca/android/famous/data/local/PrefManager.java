package ca.android.famous.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    Context context;
    public static final String USERNAME = "username";
    public static PrefManager instance = null;
    public static  SharedPreferences sharedPreferences;

    public static PrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new PrefManager(context);
            sharedPreferences=context.getSharedPreferences("LoginDetails",Context.MODE_PRIVATE);
        }
        return instance;
    }


    private PrefManager(Context context) {
        this.context = context;
    }

    public void saveLoginDetails(String email) {

       sharedPreferences.edit().putString(USERNAME, email).commit();
    }

    public String getUserName() {
        return context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE).getString(USERNAME, "");
    }

    public boolean isUserLogedOut() {
        return context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE).getString(USERNAME, "").isEmpty();
    }

    public void removeData() {

        sharedPreferences.edit().remove(USERNAME).commit();
    }
}
