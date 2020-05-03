package demo.xm.com.demo.view.event.slidingconflict;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import demo.xm.com.demo.R;

public class SlidingFragment extends Fragment {

    private View view;
    private RecyclerView rv;
    private RvAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_sliding, container, false);
        rv = view.findViewById(R.id.rv);
        adapter = new RvAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        return view;
    }

    static class RvVH extends RecyclerView.ViewHolder {

        private TextView tv;

        public RvVH(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }

        public void setTv(String msg) {
            tv.setText(msg);
        }
    }

    static class RvAdapter extends RecyclerView.Adapter<RvVH> {

        @NonNull
        @Override
        public RvVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sliding, parent, false);
            return new RvVH(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RvVH holder, int position) {
            holder.setTv("" + position);
        }

        @Override
        public int getItemCount() {
            return 100;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }
    }
}
