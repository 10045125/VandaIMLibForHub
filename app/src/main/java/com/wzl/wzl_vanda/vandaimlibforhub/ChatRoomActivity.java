package com.wzl.wzl_vanda.vandaimlibforhub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wzl.wzl_vanda.vandaimlibforhub.data.GlobalData;
import com.wzl.wzl_vanda.vandaimlibforhub.fragment.ChatRoomFragment;

import butterknife.ButterKnife;

public class ChatRoomActivity extends AppCompatActivity {
    public static void startActivity(Activity fromActivity) {
        Intent intent = new Intent(fromActivity, ChatRoomActivity.class);
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ChatRoomFragment(GlobalData.curClient, GlobalData.curConv))
                    .commit();
        }
    }

}
