package com.example.answerapp.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.answerapp.R;
import com.example.answerapp.database.Users;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SignUpActivity extends AppCompatActivity implements TextWatcher {

    private String TAG = getClass().getName();

    private EditText et_name;
    private EditText et_phone;
    private EditText et_password;
    private EditText et_repassword;
    private EditText et_mail;

    private Button signup;

    private String name;
    private String phone;
    private String password;
    private String repassword;
    private String mail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("注册");

        Bmob.initialize(this,"03ed672534583aab5914232995118da3");

        initView();

        signup = findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Users user = new Users();
                user.setUsername(name);
                user.setPassword(password);
                user.setEmail(mail);
                user.setMobilePhoneNumber(phone);
                user.signUp(new SaveListener<Users>() {

                    @Override
                    public void done(Users users, BmobException e) {
                        if (e==null){
                            Toast.makeText(SignUpActivity.this,"注册成功，请查看邮箱验证",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "done: 注册成功");
                            finish();
                        }else {
                            Log.e(TAG, "error:" + String.valueOf(e));
                        }
                    }
                });
            }
        });

    }

    private void initView() {
        et_name = findViewById(R.id.reg_et_name);
        et_phone = findViewById(R.id.reg_et_phone);
        et_password = findViewById(R.id.reg_et_password);
        et_repassword = findViewById(R.id.reg_et_repassword);
        et_mail = findViewById(R.id.reg_et_email);

        et_name.addTextChangedListener(this);
        et_phone.addTextChangedListener(this);
        et_repassword.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_mail.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        name = et_name.getText().toString();
        phone = et_phone.getText().toString();
        password = et_password.getText().toString();
        repassword = et_repassword.getText().toString();
        mail = et_mail.getText().toString();

        if (!name.isEmpty() && !phone.isEmpty() && !mail.isEmpty() && !password.isEmpty() && !repassword.isEmpty()){
            if (password.equals(repassword)) {
                signup.setEnabled(true);
            }else {
                signup.setEnabled(false);
            }
        }

    }

    //TODO 加入邮箱格式检测
    public static boolean checkEmail(String email) {// 验证邮箱的正则表达式
        String format = "\\p{Alpha}\\w{2,15}[@][a-z0-9]{3,}[.]\\p{Lower}{2,}";
        //p{Alpha}:内容是必选的，和字母字符[\p{Lower}\p{Upper}]等价。如：200896@163.com不是合法的。
        //w{2,15}: 2~15个[a-zA-Z_0-9]字符；w{}内容是必选的。 如：dyh@152.com是合法的。
        //[a-z0-9]{3,}：至少三个[a-z0-9]字符,[]内的是必选的；如：dyh200896@16.com是不合法的。
        //[.]:'.'号时必选的； 如：dyh200896@163com是不合法的。
        //p{Lower}{2,}小写字母，两个以上。如：dyh200896@163.c是不合法的。
        if (email.matches(format)) {
            return true;// 邮箱名合法，返回true
        }
        else {
            return false;// 邮箱名不合法，返回false
        }
    }
}
