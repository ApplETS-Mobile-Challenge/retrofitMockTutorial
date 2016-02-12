package com.saiimons.mockapi;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity implements Action1<SwimmingPoolList> {

    private PoolAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = ((RecyclerView) findViewById(R.id.results));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new PoolAdapter();
        recyclerView.setAdapter(adapter);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        Observable<Observable<SwimmingPoolList>> swipes = RxSwipeRefreshLayout
                .refreshes(refreshLayout)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(nil -> new SwimmingPoolService(this).getApi().getSwimmingPools());

        Observable
                .switchOnNext(swipes)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        swimmingPoolList -> {
                            refreshLayout.setRefreshing(false);
                            adapter.update(swimmingPoolList);
                        },
                        throwable -> {
                            refreshLayout.setRefreshing(false);
                            Snackbar.make(refreshLayout, throwable.getMessage(), Snackbar.LENGTH_SHORT);
                        }
                );

    }

    @Override
    public void call(SwimmingPoolList swimmingPoolList) {

    }

    private static class PoolHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView address;

        public PoolHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.address);
        }
    }

    private class PoolAdapter extends RecyclerView.Adapter<PoolHolder> {
        private SwimmingPoolList list;

        @Override
        public PoolHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PoolHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item_pool, parent, false));
        }

        @Override
        public void onBindViewHolder(PoolHolder holder, int position) {
            SwimmingPool pool = list.swimmingPools.get(position);
            holder.name.setText(pool.name);
            holder.address.setText(pool.address);
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.swimmingPools.size();
        }

        public void update(SwimmingPoolList list) {
            this.list = list;
            notifyDataSetChanged();
        }
    }
}
