package com.chii.flickr.activity;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.chii.flickr.R;
import com.chii.flickr.adapter.AboutAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AboutActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView aboutListView;
    private ArrayAdapter<String> aboutAdapter;
    private AboutAdapter mAboutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("关于");
        setContentView(R.layout.activity_about);
        initView();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 1:
                Toast.makeText(this, "github", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(this, "github", Toast.LENGTH_LONG).show();
                break;
        }
    }


    private void initView() {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        //定义并初始化保存图片id的数组
        int[] imageId=new int []{R.drawable.ic_account_circle,R.drawable.ic_github};
        //定义并初始化保存列表项文字的数组
        String[] title=new String[]{"Chii","Github"};
        for (int i = 0; i <imageId.length; i++) {
            Map<String,Object> map=new HashMap<String,Object>();//实例化map对象
            map.put("img", imageId[i]);
            map.put("name",title[i]);
            listItems.add(map);//将map对象装入List集合中
        }
        SimpleAdapter sampleAdapter = new SimpleAdapter(this
                , listItems
                , R.layout.item_about_library
                , new String[]{"img", "name"}
                , new int[]{R.id.item_about_icon, R.id.item_about_title}
        );

        aboutListView = findViewById(R.id.AboutList);
        aboutListView.setAdapter(sampleAdapter);
    }
}
