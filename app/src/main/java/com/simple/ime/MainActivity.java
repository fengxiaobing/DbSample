

package com.simple.ime;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.simple.ime.bean.User;
import com.simple.ime.sqlite.BaseDaoFactory;

/**
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }


    public void init(View view) {

        BaseDaoFactory.getInstance().getBaseDao(User.class);
        Toast.makeText(this, "执行成功", Toast.LENGTH_SHORT).show();

    }

    public void insert(View view) {
        User user = new User(12,"bing","123");
        BaseDaoFactory.getInstance().getBaseDao(User.class).insert(user);
    }
}