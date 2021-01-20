package com.example.myapplicationinst;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationinst.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SigninActivity extends AppCompatActivity {


    public static final int IMAGE_PICK = 1;

    //constant
    private static final String TAG = "SigninActivity";

    //ui
    private TextInputLayout mEmailInputLayout;
    private TextInputLayout mUsernameInputLayout;
    private TextInputLayout mPassInputLayout;
    private TextInputLayout mConfirmPassInputLayout;
    private TextInputLayout mAboutInputLayout;
    private Button mSinginBtn;
    private RadioGroup mGender;
    private ImageView mImageView;
    private FloatingActionButton mFloatingActionButton;

    //variale
    private Uri vImageUri;
    private String vUserId;
    private String vUserNameTxt;
    private String vEmailTxt;
    private String vPassTxt;
    private String vAboutTxt;
    private RadioButton vSelectGender;

    //firebase
    private FirebaseAuth vFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore vFirebaseFirestore = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef;
    private StorageReference firebaseStorage;
    private Uri mUserImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mStorageRef = FirebaseStorage.getInstance().getReference("User Image");
        initComponent();
        mSinginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField();
            }
        });

    }

    private void initComponent() {

        mEmailInputLayout = findViewById(R.id.activity_signin_emailAddress);
        mUsernameInputLayout = findViewById(R.id.activity_signin_username);
        mPassInputLayout = findViewById(R.id.activity_signin_password);
        mConfirmPassInputLayout = findViewById(R.id.activity_signin_confirm_password);
        mSinginBtn = findViewById(R.id.activity_signin_login_btn);
        mGender = findViewById(R.id.activity_signin_radio_group);
        mImageView = findViewById(R.id.activity_signin_profile_image);
        mFloatingActionButton = findViewById(R.id.activity_signin_floatting_button);
        mAboutInputLayout = findViewById(R.id.activity_signin_about);
    }

    private void checkField() {
        if (!validEmail() | !validUsername() | !validPassword()) {
            return;
        }
        vSelectGender = findViewById(mGender.getCheckedRadioButtonId());
        vUserNameTxt = mUsernameInputLayout.getEditText().getText().toString().trim();
        vEmailTxt = mEmailInputLayout.getEditText().getText().toString().trim();
        vPassTxt = mPassInputLayout.getEditText().getText().toString().trim();
        vAboutTxt = mAboutInputLayout.getEditText().getText().toString().trim();
        vFirebaseAuth.createUserWithEmailAndPassword(vEmailTxt, vPassTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException existEmail) {
                        Toast.makeText(SigninActivity.this, "The Mail is exist", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                    }
                } else {
                    Toast.makeText(SigninActivity.this, "User Add", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            User newUser = new User(vUserNameTxt, vPassTxt, vSelectGender.getText().toString(),
                                    mUserImageUri.toString(), vAboutTxt);
                            newUser.setUserId(vFirebaseAuth.getUid());
                            addNewUser(newUser);
                        }
                    }, 5000);
                    Intent intent = new Intent(SigninActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private Boolean validEmail() {
        String email = mEmailInputLayout.getEditText().getText().toString().trim();
        if (email.equals("")) {
            mEmailInputLayout.setErrorEnabled(true);
            mEmailInputLayout.setError("Field can't be empty");
            return false;
        }
        mEmailInputLayout.setErrorEnabled(false);
        return true;
    }

    private Boolean validUsername() {
        String userName = mUsernameInputLayout.getEditText().getText().toString().trim();
        if (userName.equals("")) {
            mUsernameInputLayout.setErrorEnabled(true);
            mUsernameInputLayout.setError("Field can't be empty");
            return false;
        }
        mUsernameInputLayout.setErrorEnabled(false);
        return true;
    }

    private Boolean validPassword() {
        String pass = mPassInputLayout.getEditText().getText().toString().trim();
        String confirmPass = mConfirmPassInputLayout.getEditText().getText().toString().trim();
        mPassInputLayout.setErrorEnabled(true);
        mConfirmPassInputLayout.setErrorEnabled(true);
        if (pass.equals("")) {
            mPassInputLayout.setError("Field can't be empty");
        }
        if (confirmPass.equals("")) {
            mConfirmPassInputLayout.setError("Field can't be empty");
        }
        if (!pass.equals(confirmPass) && !pass.equals("") && !confirmPass.equals("")) {
            mPassInputLayout.setError("Password don't match");
            mConfirmPassInputLayout.setError("Password dont't match");
            return false;
        }
        if (pass.length() < 6) {
            mPassInputLayout.setError("Password must be at least 6 characters");
            mConfirmPassInputLayout.setError("Password must be at least 6 characters");
            return false;
        }
        mPassInputLayout.setErrorEnabled(false);
        mConfirmPassInputLayout.setErrorEnabled(false);
        return true;
    }

    private void addNewUser(User user) {
        vFirebaseFirestore.collection("Users").add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                });
    }

    public void ChooseImageView(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            vImageUri = data.getData();
            firebaseStorage = mStorageRef.child("Image");
            firebaseStorage = firebaseStorage
                    .child(System.currentTimeMillis() + "");

            firebaseStorage.putFile(vImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            firebaseStorage.getDownloadUrl().
                                    addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            mUserImageUri = uri;
                                        }
                                    });
                        }
                    });
            Picasso.with(SigninActivity.this).load(vImageUri).into(mImageView);
        }
    }
}