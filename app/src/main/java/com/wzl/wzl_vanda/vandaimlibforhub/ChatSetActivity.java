package com.wzl.wzl_vanda.vandaimlibforhub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.ChatSetAdapter;
import com.wzl.wzl_vanda.vandaimlibforhub.view.AAGridView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wzl_vanda on 15/8/10.
 * 作者：wzl_vanda on 15/8/10 16:31
 * 邮箱：806594174@qq.com
 */
public class ChatSetActivity extends AppCompatActivity {

    public static ChatSetActivity instance;

    @Bind(R.id.id_recyclerview_main)
    AAGridView idRecyclerviewMain;
    @Bind(R.id.login_main_btn_login)
    Button loginMainBtnLogin;

    private ChatSetAdapter chatSetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_set_main);
        instance = this;
        ButterKnife.bind(this);
        initData();
    }

    private void initData(){
        idRecyclerviewMain.setAdapter(new MyAdapter());
    }


    public class MyAdapter extends BaseAdapter{


        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.circle_imageview,null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }

//            final CircleImageView circleImageView = (CircleImageView)convertView.findViewById(R.id.id_chat_set_circleimage);
//            Glide.with(ChatSetActivity.instance)
//                    .load("http://www.feizl.com/upload2007/2015_04/1504021516764612.jpg")
//                    .into(circleImageView);
            Log.e("position -> ", "" + position);
            Picasso.with(instance).load("http://www.feizl.com/upload2007/2015_04/1504021516764612.jpg").into(viewHolder.circleImageView);

            return convertView;
        }


        public class ViewHolder{


            CircleImageView circleImageView;

            public ViewHolder(View view){
                circleImageView = (CircleImageView)view.findViewById(R.id.id_chat_set_circleimage);
            }
        }
    }

}
