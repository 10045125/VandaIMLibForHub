package com.wzl.wzl_vanda.vandaimlibforhub.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wangjie.androidbucket.support.recyclerview.layoutmanager.ABaseLinearLayoutManager;
import com.wangjie.androidbucket.support.recyclerview.listener.OnRecyclerViewScrollLocationListener;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.SampleEnumCursorMapAdapter;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.SampleEnumMapAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.bean.DemoItem;
import com.wzl.wzl_vanda.viewtypelibrary.db.ItemsDataHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wzl_vanda on 15/7/28.
 */
public class ChatFragment extends Fragment {


    @Bind(R.id.recyclerview_main)
    RecyclerView recyclerviewMain;

    private ItemsDataHelper mDataHelper;
    private SampleEnumCursorMapAdapter mSampleEnumCursorMapAdapter;
    private SampleEnumMapAdapter mSampleEnumMapAdapter;
    private ABaseLinearLayoutManager mLinearLayoutManager;

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataHelper = new ItemsDataHelper(getActivity());
        mDataHelper.clearAll();
        loadItems();
    }

    private void loadItems() {
        ArrayList<DemoItem> demoItems = new ArrayList<>();
        int count = 0;
        for (int i = 0; i <= 119; i++) {
            count++;
            demoItems.add(new DemoItem(i, "me " + i, count, "http://img3.fengniao.com/forum/attachpics/855/31/34166186.jpg"));
            if (count == 6) {
                count = 0;
            }
        }
        mDataHelper.bulkInsert(demoItems);
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
        mSampleEnumMapAdapter = new SampleEnumMapAdapter(getActivity());
        recyclerviewMain.setHasFixedSize(true);
        recyclerviewMain.setItemAnimator(null);//new DefaultItemAnimator());
        recyclerviewMain.setAdapter(mSampleEnumMapAdapter);
        mLinearLayoutManager = new ABaseLinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 1200;
            }
        };
        mLinearLayoutManager.setReverseLayout(true);
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
        recyclerviewMain.scrollToPosition(mSampleEnumMapAdapter.getItemCount()-1);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
//        chatEmotionLayout.setVisibility(View.GONE);
    }

    private void hideAddLayout() {
//        chatAddLayout.setVisibility(View.GONE);
    }
}
