package ca.android.famous;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends BaseActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        printHashKey(this);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn==true){
            Intent mainIntent = new Intent(SplashActivity.this, ProductActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }
        else if (prefManager.isUserLogedOut() == true) {
            splashHandler();

        } else  {
            Intent mainIntent = new Intent(SplashActivity.this, ProductActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
            }
    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("Hashkey", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("error", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("error", "printHashKey()", e);
        }
    }

    private void splashHandler() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create a.n Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}

