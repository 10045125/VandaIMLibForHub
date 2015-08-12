package com.wzl.wzl_vanda.vandaimlibforhub;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.SampleEnumCursorMapAdapter;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.data.DBHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.fragment.ChatFragment;
import com.wzl.wzl_vanda.vandaimlibforhub.fragment.MyEmojiconsFragment;
import com.wzl.wzl_vanda.vandaimlibforhub.messagehelp.MessageHelp;
import com.wzl.wzl_vanda.vandaimlibforhub.model.Constant;
import com.wzl.wzl_vanda.vandaimlibforhub.model.User;
import com.wzl.wzl_vanda.vandaimlibforhub.service.CacheService;
import com.wzl.wzl_vanda.vandaimlibforhub.service.ConversationManager;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.Logger;
import com.wzl.wzl_vanda.viewtypelibrary.db.ItemsDataHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener, EmojiconGridFragment.OnEmojiconClickedListener, MyEmojiconsFragment.OnEmojiconBackspaceClickedListener {

    @Bind(R.id.id_main_title)
    TextView idMainTitle;
    @Bind(R.id.id_second_title)
    TextView idSecondTitle;
    @Bind(R.id.id_main_title_people)
    TextView idMainTitlePeople;

    private AVIMConversation conversation;
    private ConversationManager conversationManager;

    //用来判断是否弹出通知
    private static String currentChattingConvid;
    private ChatFragment chatFragment;


    public static String getCurrentChattingConvid() {
        return currentChattingConvid;
    }

    public static void setCurrentChattingConvid(String currentChattingConvid) {
        MainActivity.currentChattingConvid = currentChattingConvid;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
        ButterKnife.bind(this);
        initData();
        if (savedInstanceState == null) {
            chatFragment = ChatFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, chatFragment)
                    .commit();
        }
    }


    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.title_center);
    }

    private void initData() {
        conversationManager = ConversationManager.getInstance();
        String convid = getIntent().getStringExtra("ConvId");
        String title = getIntent().getStringExtra("ConvName");

        idSecondTitle.setText(title);
//        conversation = ChatManager.getInstance().lookUpConversationById(convid);
        conversation = CacheService.lookupConv(convid);
        getMebers();

        boolean isNotification = getIntent().getBooleanExtra("Notification", false);
        if (isNotification) {
            DBHelper.getInstance().resetConvUnreadCount(convid);
            if (MessageHelp.getMessageFragment() != null) {
                MessageHelp.getMessageFragment().refreshData();
            }
        }
    }

    private void getMebers() {
        new AsyncTask<Void, Void, List<AVUser>>() {
            @Override
            protected List<AVUser> doInBackground(Void... params) {
                List<AVUser> users = null;
                try {
                    users = CacheService.findUsers(conversation.getMembers());
                } catch (AVException e) {
                    e.printStackTrace();
                }
                return users;
            }

            @Override
            protected void onPostExecute(List<AVUser> users) {
//                mAVList.clear();
//                mAVList.addAll(users);
                String titlePeople = "";
                if (users != null)
                    for (int i = 0; i < users.size(); i++) {
                        if (i > 0 && i < users.size()) {
                            titlePeople += "、" + users.get(i).getUsername();
                        } else {
                            titlePeople += users.get(i).getUsername();
                        }
                    }

                if (users.size() > 0) {
                    idMainTitlePeople.setText("(" + users.size() + "人)");
                }

                idMainTitle.setText(titlePeople);
                super.onPostExecute(users);
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (conversation != null)
            setCurrentChattingConvid(conversation.getConversationId());
    }

    @Override
    protected void onPause() {
        super.onPause();
        setCurrentChattingConvid(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addm, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            tag = 2;
            createInputDialog("添加成员", "添加");
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.action_what) {
            startActivity(new Intent(this, ChatSetActivity.class));
//            if (chatFragment != null){
//                chatFragment.onRefresh();
//            }
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
                    Toast.makeText(MainActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                    mAlertDialog.dismiss();
                    tag = 2;
                    createInputDialog("添加群组成员", "添加");
                }
            }
        });
    }


    private void addMembers(final String name) {
        // 假设需要邀请 Alex，Ben，Chad 三人加入对话

        final List<String> clientIds = new ArrayList<>();
        AVQuery<AVUser> q = AVUser.getQuery(AVUser.class);
        q.whereContains(User.USERNAME, name);
        AVUser user = AVUser.getCurrentUser();
        Log.e("user.getObjectId() ", user.getUsername() + " " + user.getObjectId());
//        clientIds.add(name);


        final AVQuery<AVUser> q1 = AVUser.getQuery(AVUser.class);
        q1.whereContains(User.USERNAME, name);
//        q.limit(Constant.PAGE_SIZE);
//        q.skip(skip);
//        AVUser user = AVUser.getCurrentUser();
        List<String> friendIds = new ArrayList<String>();
        friendIds.add(user.getObjectId());
        q1.whereNotContainedIn(Constant.OBJECT_ID, friendIds);
        q1.orderByDescending(Constant.UPDATED_AT);
        q1.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);

        new AsyncTask<Void, Void, List<AVUser>>() {

            @Override
            protected List<AVUser> doInBackground(Void... params) {
                List<AVUser> users = null;
                try {
                    users = q1.find();
                } catch (AVException e) {
                    e.printStackTrace();
                }
                return users;
            }

            @Override
            protected void onPostExecute(List<AVUser> aVoid) {
                if (aVoid != null) {
                    if (aVoid.size() > 0)
                        Log.e("users ->. ", aVoid.get(0).getObjectId() + "  " + aVoid.size());
                    clientIds.add(aVoid.get(0).getObjectId());
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
                } else {
                    Log.e("users ->. ", "null");
                }
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        if (chatFragment != null) {
            chatFragment.onEmojiconBackspaceClicked(v);
        }
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {

        if (chatFragment != null) {
            chatFragment.onEmojiconClicked(emojicon);
        }
    }
}
