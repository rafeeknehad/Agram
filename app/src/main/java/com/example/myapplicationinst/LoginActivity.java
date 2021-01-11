package com.example.myapplicationinst;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int LOGIN_SIGNIN = 1;
    //ui
    private TextInputLayout mEmailInputLayout;
    private TextInputLayout mPassInputLayout;
    private LoginButton mFacebookLogin;
    private Button mLoginBtn;
    private Button mCreateAccountBtn;
    private TextView mForgetPassword;
    private CheckBox mRemmenberMe;

    //facebook
    private CallbackManager mCallbackManager;
    private ProfileTracker mProfileTracker;
    private AccessTokenTracker mAccessTokenTracker;

    //firebase
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    //sharedReferance
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        initComponenet();

        mCallbackManager = CallbackManager.Factory.create();
        mFacebookLogin.setReadPermissions("user_friends");
        mFacebookLogin.setReadPermissions("public_profile");
        mFacebookLogin.setReadPermissions("email");
        mFacebookLogin.setReadPermissions("user_birthday");

        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

            }
        };
        mAccessTokenTracker.startTracking();
        mProfileTracker.startTracking();
        mFacebookLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: ssss ");
                getFaceBookDetails(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ssss");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: ssss " + error.getMessage());
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFun();
            }
        });

        mCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccountFun();
            }
        });

        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPasswordFun();
            }
        });

        if (mRemmenberMe.isChecked()) {
            if (!mEmailInputLayout.getEditText().getText().toString().trim().equals("")) {
                remmenberMeFun();
            }
        }
    }


    private void initComponenet() {
        sharedPreferences = getSharedPreferences("remmenberMe", MODE_PRIVATE);
        mEmailInputLayout = findViewById(R.id.activity_login_instgram_emailAddress);
        mPassInputLayout = findViewById(R.id.activity_login_instgram_password);
        mFacebookLogin = findViewById(R.id.activity_login_facebook);
        mLoginBtn = findViewById(R.id.activity_login_login_btn);
        mCreateAccountBtn = findViewById(R.id.activity_login_create_account);
        mForgetPassword = findViewById(R.id.activity_login_forget_pass);
        mRemmenberMe = findViewById(R.id.activity_login_remmeber_me);
    }

    private void getFaceBookDetails(LoginResult loginResult) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
        Profile profile = Profile.getCurrentProfile();
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String email = object.getString("email");
                        } catch (JSONException e) {
                            Log.d(TAG, "onCompleted: ****** ");
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void loginFun() {
        String emailStr = mEmailInputLayout.getEditText().getText().toString().trim();
        String passStr = mPassInputLayout.getEditText().getText().toString().trim();
        firebaseAuth.signInWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                        }
                    }
                });
    }

    private void createAccountFun() {
        Intent intent = new Intent(LoginActivity.this, SigninActivity.class);
        startActivityForResult(intent, LOGIN_SIGNIN);
    }

    private void forgetPasswordFun() {
        String emailStr = mEmailInputLayout.getEditText().getText().toString().trim();
        if (!mEmailInputLayout.getEditText().getText().toString().trim().equals("")) {
            firebaseAuth.sendPasswordResetEmail(emailStr)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "onComplete: **** forgetpassword");
                        }
                    });
        }
    }

    private void remmenberMeFun() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", mEmailInputLayout.getEditText().getText().toString().trim());
        editor.putString("password", mPassInputLayout.getEditText().getText().toString().trim());
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }
}