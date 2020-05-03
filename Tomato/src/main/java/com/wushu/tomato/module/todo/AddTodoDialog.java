package com.wushu.tomato.module.todo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.wushu.tomato.MainActivity;
import com.wushu.tomato.R;

/**
 * 添加任务弹框
 */
public class AddTodoDialog extends DialogFragment {

    private Dialog dlg;
    private MainActivity mainActivity;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(R.layout.dlg_todo, null, false);
        dlg = new AlertDialog.Builder(context).setTitle("添加任务").setView(view).create();
        final EditText editTextTodo = view.findViewById(R.id.inputTodo);
        final RatingBar ratingBarTomato = view.findViewById(R.id.ratingTomato);
        Button btnEnter = view.findViewById(R.id.btnEnter);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dlg.isShowing()) {
                    dlg.dismiss();
                }
                String todo = editTextTodo.getText().toString();
                float rating = ratingBarTomato.getRating();
                if (mainActivity != null) {
                    mainActivity.onAddTodo(todo, rating);
                }
            }
        });
        return dlg;
    }

    public interface Listener {
        void onAddTodo(String todoName, float rating);
    }
}
