package com.example.xiaowen.sevenbooks;
import android.net.Uri;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    //书目列表的fragment tag（左边）
    final static String TAG = "BOOKLIST_FRAG";

    //详情页面的fragment tag（右边）
    final static String TAG2 = "BOOKDETAILS_FRAG";

    private static final String SEARCH_TERM = "searchTerm";
    private static final String CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 加载自己的Layout file

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 检查自己是否是被别的activity启动的；

        Bundle bundle = getIntent().getExtras();
        Bundle bundleToPass = new Bundle();

        String searchTerm, category;

        // 决定BookList Fragment 要load之前放什么argument

        if (bundle != null) {

            if ((searchTerm = bundle.getString(SEARCH_TERM)) != null) {
                bundleToPass.putString(SEARCH_TERM, searchTerm);

            } else if ((category = bundle.getString(CATEGORY)) != null) {
                bundleToPass.putString(CATEGORY, category);
            }
        }
        else {
            //没有bundle代表需要load全部的书单
            bundleToPass = null;
        }
        loadBookListFragment(this, bundleToPass);
    }

    // BookList Fragment 具体的loading
    public void loadBookListFragment(AppCompatActivity activity, Bundle bundleToPass) {

        // 这个MainActivity继承AppCompatActivity which 继承了Fragment
        // Fragment可以用.getSupportFM..否则用Activity.getFragmentManager()

        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction trans = activity.getSupportFragmentManager().beginTransaction();
        BookListFragment fragment = new BookListFragment();

        //刚刚传入的bundleToPass
        if (bundleToPass != null)
            fragment.setArguments(bundleToPass);

        //检查fragment：如没有创建，用add；如果已经创建了，用replace
        if (fm.findFragmentByTag(TAG) == null) {
            //这个fragment要放入的container名字，
            // 就是frameLayout的id，这个fragment instance，还有TAG
            trans.add(R.id.frame_placeholder_booklist, fragment, TAG);
        } else
            trans.replace(R.id.frame_placeholder_booklist, fragment, TAG);
        //导入
        trans.commit();
    }

    //详情页面： 需要加载details的时候使用的方法

    public void loadDetailFragment(String key, String value){

        //如果Null则是竖屏
        if(findViewById(R.id.frame_placeholder_details) == null)
        {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(key, value);
            startActivity(intent);
        }

        else{ //横屏
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction trans = fm.beginTransaction();
            DetailsFragment fragment = new DetailsFragment();

            //要加载详情需要的argument通过bundle放进去
            Bundle args = new Bundle();
            args.putString(key, value);
            fragment.setArguments(args);

            //放好之后开始看是add还是replace
            if (fm.findFragmentByTag(TAG2) == null) {
                //frameLayout placeholder的id，这个fragment instance，还有TAG
                trans.add(R.id.frame_placeholder_details, fragment, TAG);
            } else
                trans.replace(R.id.frame_placeholder_details, fragment, TAG);

            //导入
            trans.commit();
        }
    }

    //菜单：加载这个右上角菜单的layout XML：R.menu.menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //菜单：点击事件。点击搜索菜单，打开搜索事件；

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String website = "http://172.16.173.128/SevenBooksApplication/";
        switch (item.getItemId()) {
            case R.id.menu_home:
                this.finish();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.menu_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            case R.id.open_in_browser:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(website)));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
