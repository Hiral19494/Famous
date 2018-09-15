package ca.android.famous;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.android.famous.data.remote.APIClient;
import ca.android.famous.model.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.link_signup)
    TextView txvLink;
    CallbackManager callbackManager;

    private static final String EMAIL = "email";

    @BindView(R.id.login_button)
    LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("email",loginResult.toString());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }
    @OnClick(R.id.btn_login)
    public void submitDetails() {

        if (edtEmail.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
            // tilEmail.requestFocus();
        } else if (edtPassword.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
            tilPassword.requestFocus();
        } else {
       /* else{
            tilPassword.setErrorEnabled(false);
        }*/
            // new AsynLogin().execute(edtEmail.getText().toString(), edtPassword.getText().toString());
            apiLogincall();

        }
    }

    private void apiLogincall() {
        Call<UserInfo> userInfoCall = APIClient.getApiInstanceLogin().userLogin(edtEmail.getText().toString(), edtPassword.getText().toString());

        userInfoCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                Log.d("token", String.valueOf(response.body().getToken()));

                prefManager.saveLoginDetails(response.body().getToken());
                Intent homeScreenIntent = new Intent(LoginActivity.this, ProductActivity.class);
                startActivity(homeScreenIntent);
                finish();
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @OnClick(R.id.link_signup)
    public void onClickLink() {

       // Intent signupIntent = new Intent(LoginActivity.this,SignUpActivity.class);
        //startActivity(signupIntent);

    }


}

