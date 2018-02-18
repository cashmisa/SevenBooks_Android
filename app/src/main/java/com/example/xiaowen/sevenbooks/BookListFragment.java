package com.example.xiaowen.sevenbooks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;

public class BookListFragment extends Fragment {
    private static final String SEARCH_TERM = "searchTerm";
    private static final String CATEGORY = "category";
    private static final String ALLBOOKS = "allBook";

    private String critieria, searchTermValue, categoryValue;

    private MainActivity mActivity;

    public BookListFragment() {}

    //有时候getActivity会返回null，所以在onAttach的时候就绑定Activity比较保险
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //根据bundle，看按照什么条件加载booklist
        if (getArguments() != null) {
            //如果穿过来的bundle里面的key是SEARCH_TERM那么就是搜索
            if((searchTermValue = getArguments().getString(SEARCH_TERM)) != null){
                critieria = SEARCH_TERM;
            }
            //如果穿过来的bundle里面的key是CATEGORY那么就是显示分类的书目
            else if((categoryValue = getArguments().getString(CATEGORY)) !=null){
                critieria = CATEGORY;
            }
        }

        //如果没有过来bundle那么就是没有限制条件，就是全部书目
        else{
            critieria = ALLBOOKS;
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 先加载这个fragment对应的XML，在这里是含有ListView的那个
        final View v = inflater.inflate(R.layout.fragment_book_list, container, false);

        //找到要加载的ListView
        final ListView BooklistView = v.findViewById(R.id.listview_book);

        //execute的时候pass之前拿到的criteria，根据这个看我们需要call哪个找书的方法（）
        new AsyncTask<String, Void, List<Book>>() {

            @Override
            protected List<Book> doInBackground(String... params) {
                //这个params就是下面.execute的时候传过来的argument，用这个找到对应书单
                switch (params[0]){
                    case SEARCH_TERM:
                        return Book.findBookByTitile(searchTermValue);
                    case CATEGORY:
                        return Book.findBookByCategory(categoryValue);
                    default:
                        return Book.findAllBooks();
                }
            }
            //拿到书单之后，用adapter加载到ListView里面
            @Override
            protected void onPostExecute(List<Book> result) {
                BooklistView.setAdapter (new SimpleAdapter(
                        mActivity, //getActivity(),
                        result,
                        R.layout.booklist_row,
                        new String[]{"title", "author"},
                        new int[]{R.id.row_textView_title, R.id.row_textView_author})
                );
            }
        }.execute(critieria);


        //给这个bookListView加个点击事件，根据点击事件告诉hosting activity选的哪个书
        //如果implement OnItemClickListener()则可以直接@override这个方法，然后pass this

        BooklistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //因为之前加载的是List<Book>所以这里选的就是Book Type
                Book book = (Book) parent.getAdapter().getItem(position);

                mActivity.loadDetailFragment("id", book.get("id"));
            }
        });

        return v;
    }
}
