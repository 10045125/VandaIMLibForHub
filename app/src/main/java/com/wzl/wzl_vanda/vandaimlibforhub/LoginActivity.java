package com.wzl.wzl_vanda.vandaimlibforhub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzl_vanda on 15/7/31.
 */
public class LoginActivity extends AppCompatActivity {


    @Bind(R.id.id_username)
    EditText idUsername;
    @Bind(R.id.id_userpassword)
    EditText idUserpassword;
    @Bind(R.id.id_btn_login)
    Button idBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_mina);
        ButterKnife.bind(this);
        if (AVUser.getCurrentUser() != null){
            MainBaseActivity.goMainActivityFromActivity(LoginActivity.this);
        }
    }

    @OnClick(R.id.id_btn_login)
    public void IdButtonLogin(View view){

        if (!idUsername.getText().toString().equals("") && !idUserpassword.getText().toString().equals("")){
            AVUser.logInInBackground(idUsername.getText().toString(), idUserpassword.getText().toString(), new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    MainBaseActivity.goMainActivityFromActivity(LoginActivity.this);
                }
            });
        }else{
            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
        }
    }

}
