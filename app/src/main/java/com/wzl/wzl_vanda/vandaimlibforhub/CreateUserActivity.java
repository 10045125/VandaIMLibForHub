package com.wzl.wzl_vanda.vandaimlibforhub;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.Conversation;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.wzl.wzl_vanda.vandaimlibforhub.data.DBHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.data.GlobalData;
import com.wzl.wzl_vanda.vandaimlibforhub.model.ConversationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateUserActivity extends ActionBarActivity {

    @Bind(R.id.id_et_clientId)
    EditText mIdClientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_user, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.id_btn_create)
    public void onCreateUserBtnClick(View view){
        final String clientId = this.mIdClientId.getText().toString();
        Log.i("IM", "onCreateUserBtnClick, clientId:" + clientId);

        AVIMClient curClient = GlobalData.curClient;
        if (curClient != null && !curClient.getClientId().equalsIgnoreCase(clientId)) {
            Log.i("IM", "onCreateUserBtnClick, closing AVIMClient:" + curClient.getClientId());
            curClient.close(null);
            GlobalData.curClient = null;
        }

        AVIMClient client = AVIMClient.getInstance(clientId);
        Log.i("IM", "AVIMClient.getInstance, clientId:" + clientId);
        client.open(new AVIMClientCallback() {

            @Override
            public void done(AVIMClient avimClient, AVException e) {
                Log.i("IM", "AVIMClient.open done.");

                if (e != null) {
                    Toast.makeText(CreateUserActivity.this, "instance AVIMClient failed!", Toast.LENGTH_SHORT).show();
                    Log.e("IM", "get AVIMClient instance failed, clientId:" + clientId, e);
                    return;
                }

                DBHelper.init(CreateUserActivity.this, clientId);
                initConversation(avimClient);
            }
        });

    }

    private void initConversation(final AVIMClient client) {
        AVIMConversationQuery query = client.getQuery();
        query.whereEqualTo(ConversationType.ATTR_TYPE_KEY, ConversationType.Group.getValue());
        query.orderByAscending("createdAt");
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> conversations, AVException e) {
                if (e != null) {
                    Toast.makeText(CreateUserActivity.this, "query conversations failed!", Toast.LENGTH_SHORT).show();
                    Log.e("IM", "query conversations failed", e);
                    return;
                }

                int convSize = conversations.size();
                Toast.makeText(CreateUserActivity.this, "found conversations.size:" + convSize, Toast.LENGTH_SHORT).show();

                if (convSize > 0) {
                    try2JoinConversation(client, conversations.get(0));
                    return;
                }

                Map<String, Object> attrs = new HashMap<>();
                attrs.put(ConversationType.TYPE_KEY, ConversationType.Group.getValue());

                List<String> members = new ArrayList<>();
                members.add(client.getClientId());
                client.createConversation(members, attrs, new CreateConvCallback(client));

            }
        });
    }

    class CreateConvCallback extends AVIMConversationCreatedCallback {
        public AVIMClient avimClient;

        public CreateConvCallback(AVIMClient client) {
            this.avimClient = client;
        }

        public void done(AVIMConversation conv, AVException e) {
            if (e != null) {
                Toast.makeText(CreateUserActivity.this, "create conversation failed!", Toast.LENGTH_SHORT).show();
                Log.e("IM", "create conversation failed", e);
                return;
            }

            Toast.makeText(CreateUserActivity.this, "create conversation DONE!", Toast.LENGTH_SHORT).show();
            startListeningMsg(avimClient, conv);
        }
    }

    private void try2JoinConversation(final AVIMClient client, final AVIMConversation targetConv) {
        List<String> members = new ArrayList<>();
        members.add(client.getClientId());
        targetConv.addMembers(members, new AVIMConversationCallback() {

            @Override
            public void done(AVException e) {
                if (e != null) {
                    Toast.makeText(CreateUserActivity.this, "join conversation failed!", Toast.LENGTH_SHORT).show();
                    Log.e("IM", "join conversation failed", e);
                    return;
                }

                Toast.makeText(CreateUserActivity.this, "join conversation DONE!", Toast.LENGTH_SHORT).show();
                startListeningMsg(client, targetConv);
            }
        });
    }

    private void startListeningMsg(AVIMClient client, AVIMConversation conv) {
        Log.i("IM", "startListeningMsg, convId:" + conv.getConversationId());

        GlobalData.curClient = client;
        GlobalData.curConv = conv;
        ChatRoomActivity.startActivity(this);
    }
}
