package com.example.fridzip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Join extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        mAuth = FirebaseAuth.getInstance();

        EditText user_email = (EditText) findViewById(R.id.user_email);
        Button user_email_next = (Button) findViewById(R.id.user_email_next);
        Button join = (Button) findViewById(R.id.join);
        EditText user_password = (EditText) findViewById(R.id.user_password);
        EditText user_password2 = (EditText) findViewById(R.id.user_password2);
        TextView join_text1 = (TextView) findViewById(R.id.join_text1);
        TextView join_text2 = (TextView) findViewById(R.id.join_text2);
        TextView join_text3 = (TextView) findViewById(R.id.join_text3);



        user_email.addTextChangedListener(new TextWatcher() { // 이메일 유효성 확인
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String idPattern = "\\w+@\\w+\\.\\w+(\\.\\w+)?"; // 이메일 정규식
                Matcher matcher1 = Pattern.compile(idPattern).matcher(user_email.getText().toString());

                if (matcher1.find()) {
                    user_email_next.setClickable(true);
                    user_email_next.setBackground(getDrawable(R.drawable.login_button));
                    user_email_next.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    user_email_next.setClickable(false);
                    user_email_next.setBackground(getDrawable(R.drawable.login_blank_button));
                    user_email_next.setTextColor(Color.parseColor("#000000"));
                }
            }
        });



        user_email_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_email.setVisibility(View.GONE);
                user_email_next.setVisibility(View.GONE);
                // join, user_password, user_password2, join_text1, join_text2, join_text3 --> gone
                join.setVisibility(View.VISIBLE);
                user_password.setVisibility(View.VISIBLE);
                user_password2.setVisibility(View.VISIBLE);
                join_text1.setVisibility(View.VISIBLE);
                join_text2.setVisibility(View.INVISIBLE);
                join_text3.setVisibility(View.VISIBLE);
            }
        });



        user_password.addTextChangedListener(new TextWatcher() { // 비밀번호 유효성 확인
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwPattern = "^(?=.*[~`!@#$%\\^&*()-]).{8,20}$"; // 특수문자 포함, 8자 이상 20자 이하
                Matcher matcher2 = Pattern.compile(pwPattern).matcher(user_password.getText().toString());

                if(matcher2.find()) {
                    join_text1.setVisibility(View.INVISIBLE);
                } else {
                    join_text1.setVisibility(View.VISIBLE);
                    join.setClickable(false);
                }
            }
        });

        user_password2.addTextChangedListener(new TextWatcher() { // 비밀번호가 일치하는지 확인
            @Override // 입력하기 전 호출
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override // edit text에 변화가 생길 때
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override // 입력이 끝난 후
            public void afterTextChanged(Editable s) {
                if (user_password.getText().toString().equals(user_password2.getText().toString())) {
                    join_text2.setVisibility(View.INVISIBLE);
                    join.setClickable(true);
                }
                else {
                    join_text2.setVisibility(View.VISIBLE);
                    join.setClickable(false);
                }
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = user_email.getText().toString();
                final String password = user_password.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Join.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "회원 가입에 성공했습니다");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);

                            Intent intent = new Intent(Join.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("fails", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Join.this, "회원 가입에 실패했습니다", Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
            }
        });

    }

    private void updateUI(FirebaseUser user) {
        if(user != null) {
            Toast.makeText(this,"You Signed In successfully",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,MainActivity.class));

        } else {
            Toast.makeText(this,"You Didn't signed in",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }

    private void reload() {}
}