package com.example.chii.flickr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.example.chii.flickr.Adapter.MyAdapter;
import com.example.chii.flickr.Bean.WallpaperBean;
import com.example.chii.flickr.R;
import com.example.chii.flickr.connes.HttpService;
import com.example.chii.flickr.connes.RetrofitClient;
import com.example.chii.flickr.view.GridSpacingItemDecoration;
import com.example.chii.flickr.view.MyPhotoView;
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
    private HttpService service;

    private int mStart = 1;
    private static final int mSizes = 10;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<WallpaperBean.Wallpaper> mData = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RefreshLayout mSmartRefreshLayout;
    private Subscription mLoadNetScription;
    private boolean LoadingMore = false;
    private boolean Refreshing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        initData();
        initView();
        getDate(1);
        Madapter_click();

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

//        if (id == R.id.nav_camera) {
            // Handle the camera action
//        } else
            if (id == R.id.nav_gallery) {

//        } else if (id == R.id.nav_slideshow) {

//        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

//        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Madapter_click() {
        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "正在点开" + mData.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MyPhotoView.class);
                Bundle bundle = new Bundle();
                bundle.putInt("pos", position);

                List<String> list = new ArrayList<String>();
                for (int i = 0; i < mData.size(); i++) {
                    Log.i("mData", "getLink(): " + mData.get(i).getLink());
                    list.add(mData.get(i).getLink());
                }
                String[] strArr = new String[list.size()];
                list.toArray(strArr);

                bundle.putStringArray("url", strArr);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
//                Toast.makeText(MainActivity.this, "long click " + position + " item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        // 初始化布局管理器
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
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
                mAdapter.notifyItemRangeRemoved(0, mData.size());
                mData.clear();
                mStart = 1;
                getDate(1);
                mSmartRefreshLayout.finishRefresh();
            }
        });
        //上拉加载
        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout mSmartRefreshLayout) {
                if (LoadingMore) {
                    Log.i("mSmartRefreshLayout", "上拉加载 ");
                    getDate(mStart);
                    mSmartRefreshLayout.finishLoadMore(1000);
                }

            }
        });

        // 设置adapter
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 10, true));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MyAdapter(MainActivity.this, mData);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void getDate(int Start) {
//        service = RetrofitClient.getInstance().create(HttpService.class);
//        service.getJson(Start, mSizes)
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<WallpaperBean>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//                    @Override
//                    public void onError(Throwable e) {
////                        mSwipeRefreshLayout.setRefreshing(false);
//                    }
//                    @Override
//                    public void onNext(WallpaperBean wallpaperBean) {
//                        mData.addAll(wallpaperBean.getwallpaper());
////                        for (WallpaperBean.Wallpaper next : mData) {
////                            Log.i("RetrofitLog", "getLink = " + next.getLink());
////                        }
//                        mAdapter.notifyDataSetChanged();
//                    }
//                });
        service = RetrofitClient.getInstance().create(HttpService.class);
        if (mLoadNetScription != null && mLoadNetScription.isUnsubscribed()) {
            mLoadNetScription.unsubscribe();
        }
        mLoadNetScription = service.getJson(1 + mSizes * (mStart - 1), mSizes)
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

//                mAdapter.disableLoadMore();
            }
//            LoadingMore = false;
//            mAdapter.setLoadingMore(false);
//            mRecyclerView.setRefreshing(false);
            return rx.Observable.from(WallpaperBean.getwallpaper());
        }
    }
}
