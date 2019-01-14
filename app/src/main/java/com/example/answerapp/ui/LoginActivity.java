package com.example.answerapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.answerapp.R;
import com.example.answerapp.database.Users;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends AppCompatActivity implements TextWatcher {

    private Button login, signup;

    private EditText et_id, et_password;

    private String id, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bmob.initialize(this,"03ed672534583aab5914232995118da3");

        initView();
        autoLogin();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        SignUpActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = et_id.getText().toString();
                password = et_password.getText().toString();
                //登录
                Users user = new Users();
                user.setUsername(id);
                user.setPassword(password);
                user.login(new SaveListener<Users>() {

                    @Override
                    public void done(Users users, BmobException e) {
                        if (e==null){
                            //在此存入账号信息，用于自动登录
                            SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                            editor.putString("userMail",id);
                            editor.putString("userPassword",password);
                            editor.apply();

                            //验证成功,登录
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this,"邮箱或密码错误",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

    }

    /**
     * 自动登录
     */
    private void autoLogin() {
        // 获取本地登录状态
        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        id = preferences.getString("userMail","");
        password = preferences.getString("userPassword","");
        if (!id.equals("") && !password.equals("")){
            //验证成功,登录
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initView(){

        login = findViewById(R.id.ld_load);

        signup = findViewById(R.id.ld_register);

        et_id = findViewById(R.id.ld_et_id);

        et_password = findViewById(R.id.ld_et_password);

        et_id.addTextChangedListener(this);

        et_password.addTextChangedListener(this);

    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        id = et_id.getText().toString();
        password = et_password.getText().toString();
        if (!id.equals("") && !password.equals("")){
            login.setEnabled(true);
        }else {
            login.setEnabled(false);
        }
    }
}
