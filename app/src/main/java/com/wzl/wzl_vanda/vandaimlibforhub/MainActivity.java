package com.wzl.wzl_vanda.vandaimlibforhub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.wzl.wzl_vanda.vandaimlibforhub.adapter.SampleEnumCursorMapAdapter;
import com.wzl.wzl_vanda.vandaimlibforhub.fragment.ChatFragment;
import com.wzl.wzl_vanda.viewtypelibrary.bean.DemoItem;
import com.wzl.wzl_vanda.viewtypelibrary.db.ItemsDataHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private ItemsDataHelper mDataHelper;
    private SampleEnumCursorMapAdapter mSampleEnumCursorMapAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ChatFragment.newInstance())
                    .commit();
        }
    }


    private void loadItems() {
        ArrayList<DemoItem> demoItems = new ArrayList<>();
        int count = 0;
        for (int i = 1; i <= 60; i++) {
            count++;
            demoItems.add(new DemoItem(i, "me "+i,count,"http://img3.fengniao.com/forum/attachpics/855/31/34166186.jpg"));
            if (count == 6){
                count = 0;
            }
        }
        mDataHelper.bulkInsert(demoItems);
    }




    private List<SampleData> getSampleData() {
        List<SampleData> dataSet = new ArrayList<>();
        int count = 0;
        for (int i = 1; i <= 60; i++) {
            count++;
            SampleData data = new SampleData();


            setData(dataSet, data, count);

            if (count == 6) {
                count = 0;
            }
        }
        return dataSet;
    }

    private boolean isSet = false;
    private boolean isIsSet = false;

    private void setData(List<SampleData> dataSet, SampleData data, int position) {
        data.mTitle = getString(R.string.title_type2);
        data.mDrawableResId = getResources().getIdentifier(
                getString(R.string.drawable_animal_name, 1), "drawable", getPackageName());
        data.mContent = getString(R.string.content_type2, 1);
        data.url = "http://img3.fengniao.com/forum/attachpics/855/31/34166186.jpg";
        data.type = position;

        if (position == 1){
            isIsSet = !isIsSet;
            if (isIsSet){
                data.sendStatus = true;
            }
        }

        if (position == 3) {
            isSet = !isSet;
            if (isSet) {
                data.sendStatus = true;
            }
        }
        dataSet.add(data);
    }

    private List<SampleData> getSample1Data() {
        List<SampleData> dataSet = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            SampleData data = new SampleData();
            data.mTitle = getString(R.string.title_type2);
            data.mDrawableResId = getResources().getIdentifier(
                    getString(R.string.drawable_animal_name, i), "drawable", getPackageName());
            data.mContent = getString(R.string.content_type2, i);
            data.type = 1;
            dataSet.add(data);
        }

        return dataSet;
    }

    private List<SampleData> getSample3Data() {
        List<SampleData> dataSet = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            SampleData data = new SampleData();
            data.mTitle = getString(R.string.title_type2);
            data.mDrawableResId = getResources().getIdentifier(
                    getString(R.string.drawable_animal_name, i), "drawable", getPackageName());
            data.mContent = getString(R.string.content_type2, i);
            data.type = 2;
            dataSet.add(data);
        }

        return dataSet;
    }

    private List<SampleData> getSample4Data() {
        List<SampleData> dataSet = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            SampleData data = new SampleData();
            data.mTitle = getString(R.string.title_type2);
            data.mDrawableResId = getResources().getIdentifier(
                    getString(R.string.drawable_animal_name, i), "drawable", getPackageName());
            data.mContent = getString(R.string.content_type2, i);
            data.type = 3;
            dataSet.add(data);
        }

        return dataSet;
    }

    private List<SampleData> getSample5Data() {
        List<SampleData> dataSet = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            SampleData data = new SampleData();
            data.mTitle = getString(R.string.title_type2);
            data.mDrawableResId = getResources().getIdentifier(
                    getString(R.string.drawable_animal_name, i), "drawable", getPackageName());
            data.mContent = getString(R.string.content_type2, i);
            data.type = 4;
            data.url = "http://img3.fengniao.com/forum/attachpics/855/31/34166189.jpg";
            dataSet.add(data);
        }

        return dataSet;
    }

    private List<SampleData> getSample6Data() {
        List<SampleData> dataSet = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            SampleData data = new SampleData();
            data.mTitle = getString(R.string.title_type2);
            data.mDrawableResId = getResources().getIdentifier(
                    getString(R.string.drawable_animal_name, i), "drawable", getPackageName());
            data.mContent = getString(R.string.content_type2, i);
            data.type = 5;
            dataSet.add(data);
        }

        return dataSet;
    }

    private List<SampleData> getSample7Data() {
        List<SampleData> dataSet = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            SampleData data = new SampleData();
            data.mTitle = getString(R.string.title_type2);
            data.mDrawableResId = getResources().getIdentifier(
                    getString(R.string.drawable_animal_name, i), "drawable", getPackageName());
            data.mContent = getString(R.string.content_type2, i);
            data.type = 6;
            data.url = "http://img3.fengniao.com/forum/attachpics/855/31/34166186.jpg";
            dataSet.add(data);
        }

        return dataSet;
    }

    private List<SampleData> getSample8Data() {
        List<SampleData> dataSet = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            SampleData data = new SampleData();
            data.mTitle = getString(R.string.title_type2);
            data.mDrawableResId = getResources().getIdentifier(
                    getString(R.string.drawable_animal_name, i), "drawable", getPackageName());
            data.mContent = getString(R.string.content_type2, i);
            data.type = 7;
            data.url = "http://img3.fengniao.com/forum/attachpics/855/31/34166186.jpg";
            dataSet.add(data);
        }

        return dataSet;
    }

    private List<SampleData> getSample9Data() {
        List<SampleData> dataSet = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            SampleData data = new SampleData();
            data.mTitle = getString(R.string.title_type2);
            data.mDrawableResId = getResources().getIdentifier(
                    getString(R.string.drawable_animal_name, i), "drawable", getPackageName());
            data.mContent = getString(R.string.content_type2, i);
            data.type = 8;
            data.url = "http://img3.fengniao.com/forum/attachpics/855/31/34166186.jpg";
            dataSet.add(data);
        }

        return dataSet;
    }

}
