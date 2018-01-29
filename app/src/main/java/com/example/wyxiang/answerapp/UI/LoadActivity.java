package com.example.wyxiang.answerapp.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wyxiang.answerapp.R;
import com.example.wyxiang.answerapp.Database.Users;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoadActivity extends AppCompatActivity {

    private Button Load;

    private Button Register;

    private EditText Id;

    private EditText Password;

    private String id;

    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        Bmob.initialize(this,"03ed672534583aab5914232995118da3");

        initView();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoadActivity.this,
                        RegisterActivity.class);
                //哇,好尴尬,竟然冲突了,,,,
                startActivity(intent);
            }
        });

        Load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = Id.getText().toString();
                password = Password.getText().toString();
                final NiftyDialogBuilder dialog = NiftyDialogBuilder.getInstance(LoadActivity.this);

                if(id.equals("")){
                    dialog.withTitle("提示")
                            .withDialogColor("#2894FF")
                            .withMessage("学号不能为空")
                            .withButton1Text("确定")
                            .setButton1Click(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.hide();
                                }
                            }).show();
                } else if (password.equals("")){
                    dialog.withTitle("提示")
                            .withDialogColor("#2894FF")
                            .withMessage("密码不能为空")
                            .withButton1Text("确定")
                            .setButton1Click(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.hide();
                                }
                            }).show();
                }else{

                    Users user = new Users();
                    user.setUsername(id);
                    user.setPassword(password);
                    user.login(new SaveListener<Users>() {

                        @Override
                        public void done(Users users, BmobException e) {
                            if (e==null){
                                //验证成功,登录
                                Intent intent = new Intent(LoadActivity.this,Menu.class);
                                intent.putExtra("id",id);
                                startActivity(intent);
                            }else {

                                dialog.withTitle("提示")
                                        .withDialogColor("#2894FF")
                                        .withMessage("学号或密码错误")
                                        .withButton1Text("确定")
                                        .setButton1Click(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.hide();
                                            }
                                        }).show();

                            }

                        }
                    });
                }

            }
        });

    }

    private void initView(){

        Load= (Button) findViewById(R.id.ld_load);

        Register = (Button) findViewById(R.id.ld_register);

        Id = (EditText) findViewById(R.id.ld_et_id);

        Password = (EditText) findViewById(R.id.ld_et_password);

    }


}
