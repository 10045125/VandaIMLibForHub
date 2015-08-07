package com.wzl.wzl_vanda.vandaimlibforhub.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.wangjie.androidbucket.support.recyclerview.layoutmanager.ABaseLinearLayoutManager;
import com.wangjie.androidbucket.support.recyclerview.listener.OnRecyclerViewScrollLocationListener;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.SampleEnumCursorMapAdapter;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.SampleEnumMapAdapter;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageAgent;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.model.ForMeConversationInfo;
import com.wzl.wzl_vanda.vandaimlibforhub.model.MessageEvent;
import com.wzl.wzl_vanda.vandaimlibforhub.model.MessageItem;
import com.wzl.wzl_vanda.vandaimlibforhub.service.CacheService;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.PathUtils;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.ProviderPathUtils;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.Utils;
import com.wzl.wzl_vanda.vandaimlibforhub.view.RecordButton;
import com.wzl.wzl_vanda.viewtypelibrary.bean.DemoItem;
import com.wzl.wzl_vanda.viewtypelibrary.db.ItemsDataHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by wzl_vanda on 15/7/28.
 */
public class ChatFragment extends Fragment implements TextView.OnEditorActionListener{

    private static final int TAKE_CAMERA_REQUEST = 2;
    private static final int GALLERY_REQUEST = 0;
    private static final int GALLERY_KITKAT_REQUEST = 3;
    protected String localCameraPath = PathUtils.getPicturePathByCurrentTime();

    @Bind(R.id.id_recyclerview_main)
    RecyclerView recyclerviewMain;
    @Bind(R.id.id_chat_textedit)
    EmojiconEditText idChatTextedit;
    @Bind(R.id.id_chatTextLayout)
    LinearLayout idChatTextLayout;
    @Bind(R.id.id_bottomLayout)
    LinearLayout idBottomLayout;

    protected EventBus eventBus;
    @Bind(R.id.id_showVoiceBtn)
    Button idShowVoiceBtn;
    @Bind(R.id.id_chatBottomLeftLayout)
    LinearLayout idChatBottomLeftLayout;
    @Bind(R.id.id_sendBtn)
    Button idSendBtn;
    @Bind(R.id.id_showEmotionBtn)
    Button idShowEmotionBtn;
    @Bind(R.id.id_showAddBtn)
    Button idShowAddBtn;
    @Bind(R.id.id_chatBottomRightLayout)
    LinearLayout idChatBottomRightLayout;
    @Bind(R.id.id_recordBtn)
    RecordButton idRecordBtn;
    @Bind(R.id.id_turnToTextBtn)
    Button idTurnToTextBtn;
//    @Bind(R.id.id_emotionPager)
//    ViewPager idEmotionPager;
    @Bind(R.id.id_chatEmotionLayout)
    LinearLayout idChatEmotionLayout;
    @Bind(R.id.id_addImageBtn)
    TextView idAddImageBtn;
    @Bind(R.id.id_addCameraBtn)
    TextView idAddCameraBtn;
    @Bind(R.id.id_addLocationBtn)
    TextView idAddLocationBtn;
    @Bind(R.id.id_chatAddLayout)
    LinearLayout idChatAddLayout;
    @Bind(R.id.id_chatMoreLayout)
    LinearLayout idChatMoreLayout;
    @Bind(R.id.id_fl_emojicons_content)
    FrameLayout idFlEmojiconsContent;

    private ItemsDataHelper mDataHelper;
    private SampleEnumCursorMapAdapter mSampleEnumCursorMapAdapter;
    private SampleEnumMapAdapter mSampleEnumMapAdapter;
    private ABaseLinearLayoutManager mLinearLayoutManager;
    private AVIMConversation conversation;
    private String convid;
    private ForMeConversationInfo forMeConversationInfo;

    protected MessageAgent messageAgent;
    protected MessageAgent.SendCallback defaultSendCallback = new DefaultSendCallback();

    class DefaultSendCallback implements MessageAgent.SendCallback {

        @Override
        public void onError(AVIMTypedMessage message, Exception e) {
            Utils.log("fail");
            addMessageAndScroll(message);
        }

        @Override
        public void onSuccess(AVIMTypedMessage message) {
            Utils.log("success");
            addMessageAndScroll(message);
        }
    }

    private void sendText(String content) {
        if (!TextUtils.isEmpty(content)) {
            messageAgent.sendText(content);
        }
    }

    private void addMessageAndScroll(AVIMTypedMessage message) {
        mSampleEnumMapAdapter.add(new MessageItem(message));//0,
        mSampleEnumMapAdapter.notifyDataSetChanged();
        recyclerviewMain.scrollToPosition(mSampleEnumMapAdapter.getItemCount() - 1);
    }

    public abstract class CacheMessagesTask extends AsyncTask<Void, Void, Void> {
        private List<AVIMTypedMessage> messages;
        private Exception e;

        public CacheMessagesTask(Context context, List<AVIMTypedMessage> messages) {
            this.messages = messages;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Set<String> userIds = new HashSet<String>();
                for (AVIMTypedMessage msg : messages) {
                    AVIMReservedMessageType type = AVIMReservedMessageType.getAVIMReservedMessageType(msg.getMessageType());
                    if (type == AVIMReservedMessageType.AudioMessageType) {
                        File file = new File(MessageHelper.getFilePath(msg));
                        if (!file.exists()) {
                            AVIMAudioMessage audioMsg = (AVIMAudioMessage) msg;
                            String url = audioMsg.getFileUrl();
                            Utils.downloadFileIfNotExists(url, file);
                        }
                    }
                    userIds.add(msg.getFrom());
                }
                if (ChatManager.getInstance().getChatManagerAdapter() == null) {
                    throw new NullPointerException("chat user factory is null");
                }
                ChatManager.getInstance().getChatManagerAdapter().cacheUserInfoByIdsInBackground(new ArrayList<String>(userIds));
            } catch (Exception e) {
                this.e = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (filterException(e)) {
                onSucceed(messages);
            }
        }

        abstract void onSucceed(List<AVIMTypedMessage> messages);
    }


    protected boolean filterException(Exception e) {
        if (e != null) {
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void onEvent(MessageEvent messageEvent) {
        final AVIMTypedMessage message = messageEvent.getMessage();
        if (message.getConversationId().equals(conversation
                .getConversationId())) {
            if (messageEvent.getType() == MessageEvent.Type.Come) {
                new CacheMessagesTask(getActivity(), Arrays.asList(message)) {
                    @Override
                    void onSucceed(List<AVIMTypedMessage> messages) {
                        addMessageAndScroll(message);
                    }
                }.execute();
            } else if (messageEvent.getType() == MessageEvent.Type.Receipt) {
                Utils.log("receipt");
//                AVIMTypedMessage originMessage = findMessage(message.getMessageId());
//                if (originMessage != null) {
//                    originMessage.setMessageStatus(message.getMessageStatus());
//                    originMessage.setReceiptTimestamp(message.getReceiptTimestamp());
//                    adapter.notifyDataSetChanged();
//                }
            }
        }
    }


    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        mDataHelper = new ItemsDataHelper(getActivity());
        mDataHelper.clearAll();
        convid = getActivity().getIntent().getStringExtra("ConvId");
        conversation = ChatManager.getInstance().lookUpConversationById(convid);
        forMeConversationInfo = CacheService.getMeConversationObjectId(convid);
        messageAgent = new MessageAgent(conversation, forMeConversationInfo != null ? forMeConversationInfo.objectId : "");
        messageAgent.setSendCallback(defaultSendCallback);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_layout_main, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initRecordBtn();
    }


    private void initData() {
        setEmojiconFragment(false);
        mSampleEnumMapAdapter = new SampleEnumMapAdapter(getActivity(), convid);
        mSampleEnumMapAdapter.faceUrlForMe = forMeConversationInfo != null ? forMeConversationInfo.url : "http://";
        mSampleEnumMapAdapter.nickNameForMe = forMeConversationInfo != null ? forMeConversationInfo.nickname : "...";
        recyclerviewMain.setHasFixedSize(true);
        recyclerviewMain.setItemAnimator(null);//new DefaultItemAnimator());
        recyclerviewMain.setAdapter(mSampleEnumMapAdapter);
        mLinearLayoutManager = new ABaseLinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 1200;
            }
        };
//        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setOnRecyclerViewScrollLocationListener(recyclerviewMain, new OnRecyclerViewScrollLocationListener() {
            @Override
            public void onTopWhenScrollIdle(RecyclerView recyclerView) {

            }

            @Override
            public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                onRefresh();
            }
        });

        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerviewMain.setLayoutManager(mLinearLayoutManager);
        onRefresh();
//        recyclerviewMain.scrollToPosition(mSampleEnumMapAdapter.getItemCount() - 1);
//        idChatTextedit.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI|EditorInfo.IME_ACTION_SEND);
        idChatTextedit.setOnEditorActionListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void setEmojiconFragment(boolean useSystemDefault) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.id_fl_emojicons_content,MyEmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }


    public void onEmojiconClicked(Emojicon emojicon) {
        MyEmojiconsFragment.input(idChatTextedit, emojicon);
    }

    public void onEmojiconBackspaceClicked(View v) {
        MyEmojiconsFragment.backspace(idChatTextedit);
    }

    private int offset = 0;
    private int limit = 12;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private int mFirstCompletelyVisibleItemPosition = 0;


    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {

                mSampleEnumMapAdapter.addAll(query());
//                mSampleEnumMapAdapter.addAllTop(query());
//                List<DemoItem> list = query();
//                if (list != null)
//                    for (int i = 0; i < list.size(); i++) {
//                        mSampleEnumMapAdapter.notifyItemInserted(0);
//                        mSampleEnumMapAdapter.add(0, list.get(i));
//                        mSampleEnumMapAdapter.notifyItemRangeChanged(0, mSampleEnumMapAdapter.getItemCount());
//                    }
            }
        });
    }

    public List<DemoItem> query() {
        DemoItem item = null;
        List<DemoItem> list = new ArrayList<>();
        Cursor cursor;
        cursor = mDataHelper.queryoffsetLimit(offset, limit, null, null,
                null, null);
        while (cursor.moveToNext()) {
            item = DemoItem.fromCursor(cursor);
            list.add(item);
        }
        offset += limit;
        cursor.close();
        return list;
    }


    private void hideBottomLayoutAndScrollToLast() {
        hideBottomLayout();
//        scrollToLast();
    }

    protected void hideBottomLayout() {
        hideAddLayout();
        idChatEmotionLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        switch (actionId) {
            case EditorInfo.IME_NULL:
                System.out.println("Done_content: " + v.getText());
                break;
            case EditorInfo.IME_ACTION_SEND:
                Log.e("IME_ACTION_SEND", "" + v.getText().toString());
                sendText(v.getText().toString());
                v.setText("");
                break;
            case EditorInfo.IME_ACTION_DONE:
                System.out.println("action done for number_content: " + v.getText());
                break;
        }

        return true;
    }

//    private void sendMessage(String message){
//        AVIMMessage mAVIMMessage = new AVIMMessage();
//        mAVIMMessage.setContent(message);
//        conversation.sendMessage(mAVIMMessage, new AVIMConversationCallback() {
//            @Override
//            public void done(AVException e) {
//
//            }
//        });
//    }

    public void initRecordBtn() {
        idRecordBtn.setSavePath(PathUtils.getRecordPathByCurrentTime());
        idRecordBtn.setRecordEventListener(new RecordButton.RecordEventListener() {
            @Override
            public void onFinishedRecord(final String audioPath, int secs) {
//        LogUtils.d("audioPath = ", audioPath);
                messageAgent.sendAudio(audioPath);
            }

            @Override
            public void onStartRecord() {

            }
        });
    }


    @OnClick(R.id.id_showVoiceBtn)
    protected void onShowAudioLayout() {
//        idChatTextLayout.setVisibility(View.GONE);
//        idChatRecordLayout.setVisibility(View.VISIBLE);
//        idChatEmotionLayout.setVisibility(View.GONE);
        idShowVoiceBtn.setVisibility(View.GONE);
        idChatTextedit.setVisibility(View.GONE);

        idTurnToTextBtn.setVisibility(View.VISIBLE);
        idRecordBtn.setVisibility(View.VISIBLE);

        hideSoftInputView();
    }

    @OnClick(R.id.id_turnToTextBtn)
    protected void onShowTextLayout() {
        idShowVoiceBtn.setVisibility(View.VISIBLE);
        idChatTextedit.setVisibility(View.VISIBLE);

        idTurnToTextBtn.setVisibility(View.GONE);
        idRecordBtn.setVisibility(View.GONE);
    }


    @OnClick(R.id.id_showAddBtn)
    protected void onBottomAddLayout() {
        if (idChatAddLayout.getVisibility() == View.VISIBLE) {
            hideAddLayout();
        } else {
            idChatEmotionLayout.setVisibility(View.GONE);
            hideSoftInputView();
            showAddLayout();
        }
    }

    @OnClick(R.id.id_addImageBtn)
    protected void onAddImageBtn() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.chat_activity_select_picture)),
                    GALLERY_REQUEST);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_KITKAT_REQUEST);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST:
                case GALLERY_KITKAT_REQUEST:
                    if (intent == null) {
                        Toast.makeText(getActivity(), "data lose!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Uri uri;
                    if (requestCode == GALLERY_REQUEST) {
                        uri = intent.getData();
                    } else {
                        //for Android 4.4
                        uri = intent.getData();
                        final int takeFlags = intent.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        getActivity().getContentResolver().takePersistableUriPermission(uri, takeFlags);
                    }
                    String localSelectPath = ProviderPathUtils.getPath(getActivity(), uri);
                    messageAgent.sendImage(localSelectPath);
                    hideBottomLayout();
                    break;
                case TAKE_CAMERA_REQUEST:
                    messageAgent.sendImage(localCameraPath);
                    hideBottomLayout();
                    break;
            }
        }
    }


    @OnClick(R.id.id_showEmotionBtn)
    protected void onShowEmotionLayout() {
        if (idChatEmotionLayout.getVisibility() == View.VISIBLE) {
            idChatEmotionLayout.setVisibility(View.GONE);
        } else {
            idChatEmotionLayout.setVisibility(View.VISIBLE);
            hideAddLayout();
            showTextLayout();
            hideSoftInputView();
        }
    }


    private void hideAddLayout() {
        idChatAddLayout.setVisibility(View.GONE);
    }

    private void showAddLayout() {
        idChatAddLayout.setVisibility(View.VISIBLE);
    }

    private void showTextLayout() {
        idChatTextLayout.setVisibility(View.VISIBLE);
    }

    protected void hideSoftInputView() {
        if (getActivity().getWindow().getAttributes().softInputMode !=
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            View currentFocus = getActivity().getCurrentFocus();
            if (currentFocus != null) {
                manager.hideSoftInputFromWindow(currentFocus.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }
}
