package demo.xm.com.demo.view.event.slidingconflict;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import demo.xm.com.demo.R;

/**
 * 滑动冲突分为三种
 * 1 上下滑动冲突，父容器上下滑动、子容器上下滑动。
 * 2 上下左右滑动冲突，父容器左右滑动、子容器上下滑动。
 * 3 12两种嵌套滑动冲突。
 * <p>
 * 滑动冲突两种方案解决
 * 内部拦截
 * 外部拦截
 *
 * 博客例子
 * https://blog.csdn.net/nnmmbb/article/details/82997461
 */
public class SlidingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ExampleAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding);
        viewPager = findViewById(R.id.viewpager);
        fragmentList.add(new SlidingFragment());
        fragmentList.add(new SlidingFragment());
        adapter = new ExampleAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
    }

    static class ExampleAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public ExampleAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList != null ? fragmentList.size() : 0;
        }
    }
}
