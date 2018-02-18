package com.example.xiaowen.sevenbooks;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment {

    public DetailsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //和这个fragment对应的layout XML联系起来：

        final View v = inflater.inflate(R.layout.fragment_details, container, false);

        //为adapter做好准备
        final String[] fields = new String[]
                {"title", "author", "isbn", "category", "stock", "price"};
        final int[] textviewsIDs = new int[]
                {R.id.details_textview_title, R.id.details_textview_author,
                R.id.details_textview_isbn, R.id.details_textview_category,
                R.id.details_textview_stock, R.id.details_textview_price};
        final ImageView image = v.findViewById(R.id.details_imageview);


        //获取invoke这个fragment的时候传入的argument：

        String bookID = getArguments().getString("id");


        //把这个argument放进.executive里面，通过找这个到Book获得Book的详情：

        new AsyncTask<String, Void, List<Object>>() {
            @Override
            protected List<Object> doInBackground(String... ids) {
                //ids就是.executive传入的参数

                Book book = Book.findBookById(ids[0]);
                List<Object> bookDetails = new ArrayList<>();
                bookDetails.add(book);
                bookDetails.add(Book.findImageById(book.get("isbn")));
                return bookDetails;
            }
            @Override
            protected void onPostExecute(List<Object> bookDetails) {

                //加载信息到view里面
                for (int i = 0; i < fields.length; i++) {
                    TextView textView = v.findViewById(textviewsIDs[i]);
                    textView.setText(((Book) bookDetails.get(0)).get(fields[i]));
                }
                image.setImageBitmap((Bitmap) bookDetails.get(1));
            }
        }.execute(bookID);

        return v;
    }
}
