package com.example.administrator.myapplication;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.todayoffice.main.R;
import com.todayoffice.view.navigation.NavigationTab;


public class MainActivity extends AppCompatActivity implements NavigationTab.OnSelectedChangedListener {
    private NavigationTab tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tab = NavigationTab.attachTo(this, savedInstanceState);
        tab.addFragments(new MyFragment(), "首页", new int[]{R.drawable.se1, R.drawable.se2}, new int[]{0XFFF0FF00, 0XFFFF00FF})
                .addFragments(new MyFragment2(), "消息", new int[]{R.drawable.se1, R.drawable.se2}, new int[]{0XFFF0FF00, 0XFFFF00FF})
                .addFragments(new MyFragment3(), "工作", new int[]{R.drawable.se1, R.drawable.se2}, new int[]{0XFFF0FF00, 0XFFFF00FF})
                .addFragments(new MyFragment4(), "我", new int[]{R.drawable.se1, R.drawable.se2}, new int[]{0XFFF0FF00, 0XFFFF00FF})
                .commit(getSupportFragmentManager(), R.id.fragment_container, 2, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        tab.onSaveInstanceState(outState);
    }

    @Override
    public void onSelectedChanged(int index) {
        Toast.makeText(this, String.valueOf(index), Toast.LENGTH_SHORT).show();
    }
}
