package com.wzl.wzl_vanda.vandaimlibforhub;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.fragment.MessageFragment;
import com.wzl.wzl_vanda.vandaimlibforhub.model.User;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by wzl_vanda on 15/7/29.
 */
public class MainBaseActivity extends AppCompatActivity implements OnClickListener {

    public static MainBaseActivity instance;

    public MessageFragment mMessageFragment;
    @Bind(R.id.id_main_title)
    TextView idMainTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);
        initActionBar();
        ButterKnife.bind(this);
        idMainTitle.setText("消息");
        EventBus eventBus = EventBus.getDefault();
        if (savedInstanceState == null) {
            mMessageFragment = MessageFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mMessageFragment)
                    .commit();
        }
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.message_title_center);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            tag = 1;
            createInputDialog("创建群组名称", "创建");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    EditText et;
    AlertDialog mAlertDialog;
    private int tag = 0;

    private void createInputDialog(String title, String tag) {
        et = new EditText(this);
        mAlertDialog = new AlertDialog.Builder(this).setTitle(title).setView(
                et).setPositiveButton(tag, this)
                .setNegativeButton("取消", this).show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                if (tag == 1) {
                    createGroup(et.getText().toString());
                }
                if (tag == 2) {
                    addMembers(et.getText().toString());
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:

                break;
        }
    }


    private void createGroup(String name) {
        Map<String, Object> attr = new HashMap<String, Object>();
        int ConversationType_Group = 1;
        attr.put("type", ConversationType_Group);
        List<String> clientIds = new ArrayList<>();
        ChatManager.getInstance().getImClient().createConversation(clientIds, name, attr, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation conversation_, AVException e) {
                if (null != conversation_) {
                    // 成功了！
                    conversation = conversation_;
                    Toast.makeText(MainBaseActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                    mAlertDialog.dismiss();
                    tag = 2;
                    createInputDialog("添加群组成员", "添加");
                }
            }
        });
    }

    AVIMConversation conversation;

    private void addMembers(final String name) {
        // 假设需要邀请 Alex，Ben，Chad 三人加入对话

        final List<String> clientIds = new ArrayList<>();
        AVQuery<AVUser> q = AVUser.getQuery(AVUser.class);
        q.whereContains(User.USERNAME, name);
        AVUser user = AVUser.getCurrentUser();
        Log.e("user.getObjectId() ", user.getUsername() + " " + user.getObjectId());
        clientIds.add(name);
//        clientIds.add(ChatManager.getInstance().getSelfId());
        conversation.addMembers(clientIds, new AVIMConversationCallback() {
            @Override
            public void done(AVException error) {
                if (null != error) {
                    // 加入失败，报错.
                    error.printStackTrace();
                } else {
                    // 发出邀请，此后新成员就可以看到这个对话中的所有消息了。
                    Logger.d("invited.");
                    tag = 2;
                    createInputDialog("添加群组成员", "添加");
                }
            }
        });
    }
}
