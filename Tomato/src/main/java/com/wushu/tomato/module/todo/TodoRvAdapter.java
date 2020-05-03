package com.wushu.tomato.module.todo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wushu.tomato.MainActivity;
import com.wushu.tomato.R;
import com.wushu.tomato.utils.DateUtil;

import java.util.List;

import static com.wushu.tomato.constant.Constant.INTENT_PUT_EXTRA_TODO_TOMATO_BEAN;

/**
 * 任务适配器
 */
public class TodoRvAdapter extends RecyclerView.Adapter<TodoRvAdapter.TodoVH> {

    private final String TAG = this.getClass().getSimpleName();
    private final List<TodoTomatoBean> todos;
    private Context context;

    public TodoRvAdapter(MainActivity mainActivity, List<TodoTomatoBean> todos) {
        this.todos = todos;
    }

    @NonNull
    @Override
    public TodoRvAdapter.TodoVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder");
        context = viewGroup.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_todo, viewGroup, false);
        return new TodoRvAdapter.TodoVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoRvAdapter.TodoVH todoVH, int i) {
        final TodoTomatoBean todoTomatoBean = todos.size() > i ? todos.get(i) : null;
        if (null == todoTomatoBean) {
            throw new IllegalArgumentException("todoTomatoBean is null");
        }
        todoVH.getTvTodo().setText(todoTomatoBean.getTodoName());
        todoVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TodoActivity.class);
                todoTomatoBean.setUnit(1 * 60 * 1000);
                todoTomatoBean.setProgress(0);
                todoTomatoBean.setProgressMax(todoTomatoBean.getTomatoNum() * todoTomatoBean.getUnit());
                todoTomatoBean.setProgressDes(DateUtil.msToDate((int) todoTomatoBean.getProgress()));
                intent.putExtra(INTENT_PUT_EXTRA_TODO_TOMATO_BEAN, todoTomatoBean);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * 任务ViewHolder
     */
    public static class TodoVH extends RecyclerView.ViewHolder {

        private TextView tvTodo;
        private ImageButton btnStart;

        public TodoVH(@NonNull View itemView) {
            super(itemView);
            tvTodo = itemView.findViewById(R.id.tvTodo);
            btnStart = itemView.findViewById(R.id.btnStart);
        }

        public TextView getTvTodo() {
            return tvTodo;
        }

        public ImageButton getBtnStart() {
            return btnStart;
        }
    }
}