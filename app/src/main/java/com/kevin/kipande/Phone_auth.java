package com.kevin.kipande;

/**
 * Created by ${Koech} on 1/23/2018.
 */

import android.content.Intent;
import android.os.Bundle;
//import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
EditText phone,vcode;
TextView msg;
Button getcode,verify;
private FirebaseAuth mAuth;
private String mVerificationId;
private PhoneAuthProvider.ForceResendingToken mResendToken;
private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
@Override
protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
phone = (EditText) findViewById(R.id.phone);
        vcode = (EditText) findViewById(R.id.code);
        getcode=(Button)findViewById(R.id.getcode);
        verify=(Button)findViewById(R.id.verify);
        msg=(TextView)findViewById(R.id.msgtxt);
        mAuth = FirebaseAuth.getInstance();
mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

@Override
public void onVerificationCompleted(PhoneAuthCredential credential) {
Toast.makeText(MainActivity.this,"Verified",Toast.LENGTH_LONG).show();


vcode.setVisibility(View.GONE);
verify.setVisibility(View.GONE);
msg.setVisibility(View.VISIBLE);
signInWithPhoneAuthCredential(credential);

}

@Override
public void onVerificationFailed(FirebaseException e) {
Toast.makeText(MainActivity.this,"No internet Connection!",Toast.LENGTH_LONG).show();

if (e instanceof FirebaseAuthInvalidCredentialsException) {
// Invalid request
                                // ...
} else if (e instanceof FirebaseTooManyRequestsException) {
                                // The SMS quota for the project has been exceeded
                            }

}
@Override
public void onCodeSent(String verificationId,
PhoneAuthProvider.ForceResendingToken token) {
       mVerificationId = verificationId;
       mResendToken = token;
                    }
};
}

  public void GetCode(View view) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
        phone.getText().toString(),
       60,
        TimeUnit.SECONDS,
                this,
                mCallbacks);
phone.setVisibility(View.GONE);
        getcode.setVisibility(View.GONE);
vcode.setVisibility(View.VISIBLE);
        verify.setVisibility(View.VISIBLE);
    }

   public void verify(View view) {

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, vcode.getText().toString());
    signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
     mAuth.signInWithCredential(credential).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
     @Override
     public void onComplete(@NonNull Task<AuthResult> task) {
     if (task.isSuccessful()) {
         startActivity (new Intent(MainActivity.this, Users.class));
    finish();
    Toast.makeText(MainActivity.this, "Verification Done!", Toast.LENGTH_SHORT).show();

                 } else {
                  if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                      Toast.makeText(MainActivity.this,"Invalid code",Toast.LENGTH_LONG).show();
                  }
               }
           }
        });
    }
}
