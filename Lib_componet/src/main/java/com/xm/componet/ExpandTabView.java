package com.xm.componet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

/**
 * 下拉菜单
 */
public class ExpandTabView extends LinearLayout {
    private final String TAG = this.getClass().getSimpleName();
    private float heightPercent = 0.7f;
    private static final long ANI_INTO_DURATION = 350;
    private static final long ANI_OUT_DURATION = 350;
    private LinearLayout tabContainer;
    private View shadowView;
    private FrameLayout menuView;
    private Adapter adapter;
    private int currentPos = -1;
    private int menuContainerTranslationY;
    private boolean animationExecute = false;
    private int shadowViewBg;

    public ExpandTabView(Context context) {
        this(context, null);
    }

    public ExpandTabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
    }

    private void initLayout() {

        /**
         * 自定义下拉式二级列表菜单，点击tab弹出菜单内容 再次点击tab或者阴影部分收起弹出菜单
         *  1 创建布局 tabContainer容器 + menuContainer容器 (shadowView + menuView)
         *  2 动画 菜单内容打开/关闭
         *  3 测试
         *
         * 出现的问题列表
         *  1 重复点击tabView 动画会不停执行，加一个标志位
         *  2 点击menuContainer，也会执行动画
         *  3 点击指定tabView位置没有变色，应该放在外面让用户进行修改,快速点击会同时显示颜色
         */
        shadowViewBg = Color.parseColor("#e0000000");
        setOrientation(VERTICAL);
        // 添加tabContainer容器，用來存放TabView
        tabContainer = new LinearLayout(getContext());
        LayoutParams tabViewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabContainer.setOrientation(HORIZONTAL);
        tabContainer.setLayoutParams(tabViewParams);
        addView(tabContainer);

        // 添加menuContainer容器，用来存放菜单menuView + 阴影shadowView
        FrameLayout menuContainer = new FrameLayout(getContext());
        FrameLayout.LayoutParams menuContainerParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        menuContainer.setLayoutParams(menuContainerParams);
        addView(menuContainer);

        shadowView = new View(getContext());
        shadowView.setBackgroundColor(shadowViewBg);
        menuContainer.addView(shadowView);

        menuView = new FrameLayout(getContext());
        FrameLayout.LayoutParams menuContentViewParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        menuView.setLayoutParams(menuContentViewParams);
        menuView.setBackgroundColor(Color.WHITE);
        menuContainer.addView(menuView);

        menuView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        shadowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenuContentView(menuView);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        // 添加该条件是为了防止多次执行
        if (menuContainerTranslationY == 0 && height > 0) {
            menuContainerTranslationY = (int) (height * heightPercent);
            menuView.getLayoutParams().height = menuContainerTranslationY;
            menuView.setTranslationY(-menuContainerTranslationY);
            shadowView.setAlpha(0);
            Log.d(TAG, "onMeasure()");
        }
    }

    public void setAdapter(final Adapter adapter) {
        if (null == adapter) {
            throw new IllegalArgumentException("adapter is null");
        }
        this.adapter = adapter;

        for (int pos = 0; pos < adapter.getCount(); pos++) {
            //添加tabView
            TextView tabView = (TextView) adapter.getTabView(pos, this);
            LayoutParams tabViewParams = (LayoutParams) tabView.getLayoutParams();
            tabViewParams.weight = 1;
            tabView.setText(adapter.getTabViewDes(pos));
            tabContainer.addView(tabView);
            //为每个tabView设置监听打开、关闭菜单内容
            final int finalPos = pos;
            tabView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentPos == -1) {
                        openMenuContentView(finalPos, ExpandTabView.this.menuView);
                    } else {
                        if (currentPos == finalPos) {
                            closeMenuContentView(menuView);
                        } else {
                            if (!animationExecute) {
                                adapter.closeMenu(currentPos, tabContainer.getChildAt(currentPos));
                                menuView.getChildAt(currentPos).setVisibility(GONE);
                                currentPos = finalPos;
                                adapter.openMenu(currentPos, tabContainer.getChildAt(currentPos));
                                menuView.getChildAt(currentPos).setVisibility(VISIBLE);
                            }
                        }
                    }
                }
            });

            //添加menuView
            final TextView menuView = (TextView) adapter.getMenuContentView(pos, this);
            menuView.setText(adapter.getTabViewDes(pos));
            menuView.setVisibility(GONE);
            this.menuView.addView(menuView);
        }
    }

    private void closeMenuContentView(final ViewGroup menuContentView) {
        if (animationExecute) {
            return;
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(menuContentView, "translationY", 0f, -menuContainerTranslationY);
        objectAnimator.setDuration(ANI_OUT_DURATION);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                animationExecute = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animationExecute = false;
                shadowView.setVisibility(GONE);
                adapter.closeMenu(currentPos, tabContainer.getChildAt(currentPos));
                menuContentView.getChildAt(currentPos).setVisibility(INVISIBLE);
                currentPos = -1;
            }
        });
        objectAnimator.start();
        ObjectAnimator menuShadowViewAnimator = ObjectAnimator.ofFloat(shadowView, "alpha", 1f, 0f);
        menuShadowViewAnimator.setDuration(ANI_OUT_DURATION);
        menuShadowViewAnimator.start();
    }

    private void openMenuContentView(final int position, final ViewGroup menuContentView) {
        if (animationExecute) {
            return;
        }
        ObjectAnimator menuContainerAnimator = ObjectAnimator.ofFloat(menuContentView, "translationY", -menuContainerTranslationY, 0f);
        menuContainerAnimator.setDuration(ANI_INTO_DURATION);
        menuContainerAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                animationExecute = true;
                menuContentView.getChildAt(position).setVisibility(VISIBLE);
                adapter.openMenu(position, tabContainer.getChildAt(position));
                currentPos = position;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animationExecute = false;
            }
        });
        menuContainerAnimator.start();

        shadowView.setVisibility(VISIBLE);
        ObjectAnimator menuShadowViewAnimator = ObjectAnimator.ofFloat(shadowView, "alpha", 0f, 1f);
        menuShadowViewAnimator.setDuration(ANI_INTO_DURATION);
        menuShadowViewAnimator.start();
    }

    /**
     * 适配器
     */
    public abstract static class Adapter {
        public abstract int getCount();

        public abstract View getTabView(int position, ViewGroup parentView);

        public abstract String getTabViewDes(int position);

        public abstract View getMenuContentView(int position, ViewGroup parentView);

        public abstract void openMenu(int position, View tabView);

        public abstract void closeMenu(int position, View tabView);
    }
}
