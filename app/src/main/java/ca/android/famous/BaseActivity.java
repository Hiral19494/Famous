package ca.android.famous;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ca.android.famous.data.local.PrefManager;

public class BaseActivity extends AppCompatActivity {
    PrefManager prefManager;
    //public static final String BASEURL = "https://jsonplaceholder.typicode.com/posts";
    public static final String LOGINURL ="https://reqres.in";
    public static final String BASEURL ="https://my-json-server.typicode.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = PrefManager.getInstance(this);
    }

}
