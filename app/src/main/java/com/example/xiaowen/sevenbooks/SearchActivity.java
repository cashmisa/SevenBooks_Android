package com.example.xiaowen.sevenbooks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    EditText editText;
    ListView categoryListView;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        button = findViewById(R.id.button_search);
        editText = findViewById(R.id.editText_search);

        //button点击事件，获取在editText里面输入的searchTerm然后传到Main
        button.setOnClickListener(this);

        //加载view by category的category列表
        categoryListView = findViewById(R.id.listview_categories);
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                return Book.findAllCategories();
            }
            @Override
            protected void onPostExecute(List<String> result) {
                categoryListView.setAdapter(
                        new ArrayAdapter<String>(
                                SearchActivity.this,
                                R.layout.categorylist_row,
                                R.id.row_textview_category,
                                result));
            }
        }.execute();

        //选择了category之后跳到MainActivity，并且pass选择的category string
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = (String) parent.getAdapter().getItem(position);
                Intent intent = new Intent
                        (getApplicationContext(), MainActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }});
    }

    @Override
    public void onClick(View v) {
        String searchTerm = editText.getText().toString();
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        intent.putExtra("searchTerm", searchTerm);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String website = "http://172.16.173.128/SevenBooksApplication/";
        switch (item.getItemId()) {
            case R.id.menu_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.menu_search:
                recreate();
                return true;
            case R.id.open_in_browser:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(website)));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
