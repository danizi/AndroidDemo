package com.wushu.tomato;

import android.annotation.SuppressLint;
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

import com.wushu.tomato.module.todo.TodoRvAdapter;
import com.wushu.tomato.module.todo.TodoTomatoBean;
import com.wushu.tomato.module.todo.AddTodoDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity implements AddTodoDialog.Listener {

    private FloatingActionButton floatingActionButton;
    private RecyclerView rv;
    private List<TodoTomatoBean> todos = new ArrayList<>();
    private TodoRvAdapter adapter;
    private AddTodoDialog addTodoDialog;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigation;

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
        navigation = findViewById(R.id.drawerNavigation);
        toolbar = findViewById(R.id.toolbar);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //toolbar设置
        setSupportActionBar(toolbar);
        toolbar.setTitle("首页");
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.share:
                        break;
                    case R.id.setting:
                        break;
                    case R.id.dress_up:
                        break;
                    case R.id.service:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "菜单打开了", Toast.LENGTH_SHORT).show();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(MainActivity.this, "菜单关闭了", Toast.LENGTH_SHORT).show();
                super.onDrawerClosed(drawerView);
            }
        };
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);

        //悬浮按钮设置监听
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTodoDialog.show(getSupportFragmentManager(), "addTodoDialog");
            }
        });
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
