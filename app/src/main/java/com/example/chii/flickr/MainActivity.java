package com.example.chii.flickr.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.chii.flickr.Adapter.RecyclerViewAdapter;
import com.example.chii.flickr.Bean.WallpaperBean;
import com.example.chii.flickr.Option.DividerGridItemDecoration;
import com.example.chii.flickr.R;
import com.example.chii.flickr.Option.ContentApi;
import com.example.chii.flickr.Option.RetrofitClient;
import com.scwang.smartrefresh.header.StoreHouseHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ContentApi service;

    private int mStart = 1;
    private static final int mSizes = 10;
    private RecyclerViewAdapter mAdapter;
    private List<WallpaperBean.Wallpaper> mData = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RefreshLayout mSmartRefreshLayout;
    private Subscription mLoadNetScription;
    private boolean LoadingMore = false;
    private boolean Refreshing = false;
    private String source = "哔哩哔哩";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //返回顶端
//        final ScrollView sc = (ScrollView) findViewById(R.id.sc);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                mRecyclerView.getLayoutManager().scrollToPosition(0);
//                sc.fullScroll(view.FOCUS_UP);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //getDate();//获取第一次加载数据
        initData();

        initView();

        Myphoto_click();//设置点击事件
        SharedPreferences sharedPreferences = getSharedPreferences("Info", MODE_PRIVATE);

        ShowWifi(sharedPreferences);

        ShowFirst(sharedPreferences);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_bilibili:
                newData("哔哩哔哩");
                break;
            case R.id.nav_anzhuobizhi:
                newData("安卓壁纸");
                break;
            case R.id.nav_daynight:
                int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (mode == Configuration.UI_MODE_NIGHT_YES) {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);


                } else if (mode == Configuration.UI_MODE_NIGHT_NO) {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }

                break;
            case R.id.nav_about:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Myphoto_click() {
        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                final Intent intent = new Intent(context, MyPhotoView.class);
                Bundle bundle = new Bundle();

                List<String> list = new ArrayList<String>();
                for (int i = 0; i < mData.size(); i++) {
                    Log.i("mData", "getLink(): " + mData.get(i).getLink());
                    list.add(mData.get(i).getLink());
                }

                String[] strArr = new String[list.size()];
                list.toArray(strArr);

                bundle.putInt("pos", position);
                bundle.putStringArray("url", strArr);
//                List<WallpaperBean.Wallpaper> wall = new ArrayList<>(mData);
//                Log.e("~!!!!!!!!!!!!!!!!!!!!!!!!!!!", "wall: "+wall.get(1).getLink());
//                bundle.putString("wall", mData.get(1).getAuthor());
                intent.putExtras(bundle);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
//                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, view, "sharedView").toBundle());
                    }
                }).start();

            }

            @Override
            public void onItemLongClick(final View view, final int position) {
                new AlertDialog.Builder(MainActivity.this)
                        .setItems(new String[]{"点赞","收藏"}, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface arg0, int arg1) {
                                switch (arg1) {
                                    case 0:
                                        Snackbar.make(view, "已经点赞" + mData.get(position).getAuthor()+position, Snackbar.LENGTH_LONG).show();
                                        break;
                                    case 1:
                                        Snackbar.make(view, "已经收藏" + mData.get(position).getAuthor()+position, Snackbar.LENGTH_LONG).show();
                                        break;
                                    default: break;
                                }
                                arg0.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    public void onActivityResult() {


    }

    private void initData() {
        mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return super.canScrollHorizontally();
            }
        };//定义瀑布流管理器，第一个参数是列数，第二个是方向。
        // 设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        // 设置adapter
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                layoutManager.invalidateSpanAssignments();//这行主要解决了当加载更多数据时，底部需要重绘，否则布局可能衔接不上。
//            }
//        });
        //分割线类型
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
//        mRecyclerView.addItemDecoration(new StaggeredGridSpacingItemDecoration(2,10));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        mRecyclerView.setHasFixedSize(true);
        mAdapter = new RecyclerViewAdapter(MainActivity.this, mData);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initView() {
        setTitle(getResources().getString(R.string.app_name));

        mSmartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        //设置 Header
        mSmartRefreshLayout.setRefreshHeader(new StoreHouseHeader(this).initWithString("Wait")).setHeaderHeight(100);
        //设置 Footer
        mSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        //下拉刷新
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout mSmartRefreshLayout) {
                Log.i("mSmartRefreshLayout", "下拉刷新 ");
//                mAdapter.notifyItemRangeRemoved(0, mData.size());
                mData.clear();
                mStart = 1;
                getDate();
                mAdapter.notifyDataSetChanged();
                mSmartRefreshLayout.finishRefresh();
            }
        });
        //上拉加载
        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout mSmartRefreshLayout) {
                if (LoadingMore) {
                    Log.i("mSmartRefreshLayout", "上拉加载 ");
                    getDate();

                    mSmartRefreshLayout.finishLoadMore();
                }

            }
        });

    }

    private void getDate() {
        service = RetrofitClient.getInstance().create(ContentApi.class);
        if (mLoadNetScription != null && mLoadNetScription.isUnsubscribed()) {
            mLoadNetScription.unsubscribe();
        }
        mLoadNetScription = service.getJson(source, 1 + mSizes * (mStart - 1), mSizes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<WallpaperBean, Boolean>() {
                    @Override
                    public Boolean call(WallpaperBean WallpaperBean) {
                        return !WallpaperBean.isError();
                    }
                }).flatMap(new FilterMap()).subscribe(new LoadNetSubscriber());
        mStart++;
        mAdapter.notifyDataSetChanged();

    }

    /**
     * 网络请求
     */
    class LoadNetSubscriber extends Subscriber<WallpaperBean.Wallpaper> {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
//            mAdapter.setLoadingMore(false);
//            mRecyclerView.setRefreshing(false);
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(WallpaperBean.Wallpaper wallpaper) {
            mData.add(wallpaper);
            mAdapter.notifyItemInserted(mData.size());
        }
    }

    class FilterMap implements Func1<WallpaperBean, Observable<WallpaperBean.Wallpaper>> {
        @Override
        public rx.Observable<WallpaperBean.Wallpaper> call(WallpaperBean WallpaperBean) {
            if (WallpaperBean.getwallpaper().size() >= 10) {
                LoadingMore = true;

            } else {
                LoadingMore = false;
            }

            return rx.Observable.from(WallpaperBean.getwallpaper());
        }
    }

    /**
     * 获取软件版本
     *
     * @param context
     * @return
     */
    private float getVersionCode(Context context) {
        float versionCode = 0;
        //获取AndroidManifest.xml
        try {
            versionCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    private void ShowFirst(final SharedPreferences sharedPreferences) {
        float nowVersionCode = getVersionCode(MainActivity.this);
        float spVersionCode = sharedPreferences.getFloat("spVersionCode", 0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i("nowVersionCode", String.valueOf(nowVersionCode));
        Log.i("spVersionCode", String.valueOf(spVersionCode));
        if (nowVersionCode > spVersionCode) {
            editor.putFloat("spVersionCode", nowVersionCode);
            editor.apply();
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("软件在非wifi提醒")
                    .setPositiveButton("每次", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putBoolean("isShow", true);
                            editor.apply();
                            ShowWifi(sharedPreferences);
                        }
                    })
                    .setNeutralButton("不在显示", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putBoolean("isShow", false);
                            editor.apply();
                            getDate();
                        }
                    })
                    .show();
        } else {
            getDate();
        }
    }

    private void ShowWifi(SharedPreferences sharedPreferences) {

        boolean isShow = sharedPreferences.getBoolean("isShow", false);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isShow) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("本页面非常耗费流量,请在wifi下观看")
                    .setPositiveButton("观看", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getDate();
                        }
                    })
                    .setNegativeButton("否", null)
                    .setNeutralButton("不在显示", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getDate();
                            editor.putBoolean("isShow", false);
                            editor.apply();
                        }
                    })
                    .show();
        }

    }

    private void newData(String newsource) {
        mAdapter.notifyItemRangeRemoved(0, mData.size());
        source = newsource;
        Log.i("source", "source: " + source);
        mData.clear();
        mStart = 1;
        getDate();
    }
}