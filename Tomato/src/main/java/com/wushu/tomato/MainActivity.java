package com.wushu.tomato;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.wushu.tomato.constant.Constant;
import com.wushu.tomato.module.drawer.DrawerActivity;
import com.wushu.tomato.module.todo.TodoRvAdapter;
import com.wushu.tomato.module.todo.TodoTomatoBean;
import com.wushu.tomato.module.todo.AddTodoDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity implements AddTodoDialog.Listener {

    private RecyclerView rv;
    private List<TodoTomatoBean> todos = new ArrayList<>();
    private TodoRvAdapter adapter;
    private AddTodoDialog addTodoDialog;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigation = findViewById(R.id.drawerNavigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //toolbar设置
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.home);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.add:
                        intent = new Intent(MainActivity.this, DrawerActivity.class);
                        intent.putExtra(Constant.INTENT_PUT_EXTRA_GOTO, Constant.INTENT_DRAWER_VALUE_ADD);
                        startActivity(intent);
                        break;
                    case R.id.share:
                        intent = new Intent(MainActivity.this, DrawerActivity.class);
                        intent.putExtra(Constant.INTENT_PUT_EXTRA_GOTO, Constant.INTENT_DRAWER_VALUE_SHARE);
                        startActivity(intent);
                        break;
                    case R.id.setting:
                        intent = new Intent(MainActivity.this, DrawerActivity.class);
                        intent.putExtra(Constant.INTENT_PUT_EXTRA_GOTO, Constant.INTENT_DRAWER_VALUE_SETTING);
                        startActivity(intent);
                        break;
                    case R.id.study_data:
                        intent = new Intent(MainActivity.this, DrawerActivity.class);
                        intent.putExtra(Constant.INTENT_PUT_EXTRA_GOTO, Constant.INTENT_DRAWER_VALUE_STUDY_DATA);
                        startActivity(intent);
                        break;
                    case R.id.dress_up:
                        intent = new Intent(MainActivity.this, DrawerActivity.class);
                        intent.putExtra(Constant.INTENT_PUT_EXTRA_GOTO, Constant.INTENT_DRAWER_VALUE_DRESS_UP);
                        startActivity(intent);
                        break;
                    case R.id.service:
                        intent = new Intent(MainActivity.this, DrawerActivity.class);
                        intent.putExtra(Constant.INTENT_PUT_EXTRA_GOTO, Constant.INTENT_DRAWER_VALUE_SERVICE);
                        startActivity(intent);
                        break;
                    case R.id.problem:
                        intent = new Intent(MainActivity.this, DrawerActivity.class);
                        intent.putExtra(Constant.INTENT_PUT_EXTRA_GOTO, Constant.INTENT_DRAWER_VALUE_PROBLEM);
                        startActivity(intent);
                        break;
                }
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return false;
            }
        });

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.openString, R.string.closeString) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_icon, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                addTodoDialog.show(getSupportFragmentManager(), "addTodoDialog");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        addTodoDialog = new AddTodoDialog();
        adapter = new TodoRvAdapter(this, todos);
        rv.setAdapter(adapter);
    }

    @Override
    public void onAddTodo(String todoName, float rating) {
        //刷新recyclerView
        TodoTomatoBean todoTomatoBean = new TodoTomatoBean(todoName, rating);
        todos.add(todoTomatoBean);
        adapter.notifyDataSetChanged();
    }
}
