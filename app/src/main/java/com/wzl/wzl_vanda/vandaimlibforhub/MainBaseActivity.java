package com.wzl.wzl_vanda.vandaimlibforhub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.avos.avoscloud.AVUser;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.fragment.MessageFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by wzl_vanda on 15/7/29.
 */
public class MainBaseActivity extends AppCompatActivity {


    @Bind(R.id.container)
    FrameLayout container;

    public static void goMainActivityFromActivity(Activity fromActivity) {

        ChatManager chatManager = ChatManager.getInstance();
        chatManager.setupDatabaseWithSelfId(AVUser.getCurrentUser().getObjectId());
        chatManager.openClientWithSelfId(AVUser.getCurrentUser().getObjectId(), null);
        Intent intent = new Intent(fromActivity, MainBaseActivity.class);
        fromActivity.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus eventBus = EventBus.getDefault();
//        ChatManager chatManager = ChatManager.getInstance();
//        if (AVUser.getCurrentUser() != null) {
//            chatManager.setupDatabaseWithSelfId(AVUser.getCurrentUser().getObjectId());//Utils.getLocalMacAddress(this));
//            chatManager.openClientWithSelfId(AVUser.getCurrentUser().getObjectId(), null);
//        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MessageFragment.newInstance())
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatManager.getInstance().getImClient().close(null);
    }
}
