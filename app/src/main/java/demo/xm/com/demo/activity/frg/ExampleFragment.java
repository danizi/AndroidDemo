package demo.xm.com.demo.activity.frg;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import demo.xm.com.demo.R;

public class ExampleFragment extends Fragment {
    ExampleActivity exampleActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        exampleActivity = (ExampleActivity) context;
        show("onAttach");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        show("onCreateView");
        return inflater.inflate(R.layout.frg_example,container,false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        show("onDestroy");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        show("setUserVisibleHint :" + isVisibleToUser);
    }

    @Override
    public void onStart() {
        super.onStart();
        show("onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        show("onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        show("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        show("onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        show("onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        show("onDetach");
    }

    private void show(String msg) {
        Log.d(ExampleFragment.class.getSimpleName(), msg);
    }

    public void request(){
        exampleActivity.onResult(16);
    }

    interface ExampleListener{
        void onResult(int result);
    }
}
