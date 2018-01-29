package com.example.wyxiang.answerapp.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wyxiang.answerapp.R;
import com.example.wyxiang.answerapp.Database.Users;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by wyxiang on 08.07.17.
 */

public class RegisterActivity extends AppCompatActivity implements TextWatcher {


    private EditText et_name;
    private EditText et_id;
    private EditText et_password;
    private EditText et_repassword;
    private EditText et_mail;

    private Button register;

    private String name;
    private String id0;
    private String password;
    private String repassword;
    private String mail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.reg_toolbar);
        setSupportActionBar(toolbar);

        Bmob.initialize(this,"03ed672534583aab5914232995118da3");

        et_name = (EditText) findViewById(R.id.reg_et_name);
        et_id = (EditText) findViewById(R.id.reg_et_id);
        et_password = (EditText) findViewById(R.id.reg_et_password);
        et_repassword = (EditText) findViewById(R.id.reg_et_password1);
        et_mail = (EditText) findViewById(R.id.reg_et_email);

        et_name.addTextChangedListener(this);
        et_id.addTextChangedListener(this);
        et_repassword.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_mail.addTextChangedListener(this);

        register = (Button) findViewById(R.id.reg_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NiftyDialogBuilder dialog = NiftyDialogBuilder.getInstance(RegisterActivity.this);
                Users user = new Users();
                user.setUsername(id0);
                user.setPassword(password);
                user.setNickname(name);
                user.setEmail(mail);
                user.signUp(new SaveListener<Users>() {

                    @Override
                    public void done(Users users, BmobException e) {
                        if (e==null){
                            dialog.withTitle("提示")
                                    .withDialogColor("#2894FF")
                                    .withMessage("注册成功")
                                    .withButton1Text("确定")
                                    .setButton1Click(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.hide();
                                            finish();
                                        }
                                    }).show();
                        }else {
                            Log.e("TAG", String.valueOf(e));
                        }
                    }
                });
            }
        });

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
        id0 = et_id.getText().toString();
        password = et_password.getText().toString();
        repassword = et_repassword.getText().toString();
        mail = et_mail.getText().toString();

        if (!name.isEmpty() && !id0.isEmpty() && !mail.isEmpty() && !password.isEmpty() && !repassword.isEmpty()){
            if (password.equals(repassword)) {
                register.setEnabled(true);
            }else {
                register.setEnabled(false);
            }
        }

    }
}
