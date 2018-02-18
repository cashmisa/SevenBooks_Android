package com.example.xiaowen.sevenbooks;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String bookID = getIntent().getExtras().getString("id");
        display(bookID);
    }

    public void display(String id) {

        final String TAG = "DETAILS_FRAG";

        Bundle args = new Bundle();
        args.putString("id", id);
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().add(R.id.detailsactivity_details_frame,fragment,TAG).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;

            case R.id.menu_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
