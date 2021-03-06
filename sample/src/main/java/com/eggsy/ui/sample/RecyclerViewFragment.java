package com.eggsy.ui.sample;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eggsy.ui.PullToRefreshView;
import com.eggsy.ui.sample.view.LightRefreshView;
import com.eggsy.ui.sample.view.MoreRefreshView;

import java.util.Map;

/**
 * Created by Oleksii Shliama.
 */
public class RecyclerViewFragment extends BaseRefreshFragment {

    private PullToRefreshView mPullToRefreshView;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recycler_view, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(new SampleAdapter());

        mPullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);

        mPullToRefreshView.setTopRefreshView(new LightRefreshView(getActivity(), mPullToRefreshView));

        MoreRefreshView moreRefreshView = new MoreRefreshView(getActivity(),
                (AnimationDrawable) getResources().getDrawable(R.drawable.list_bottom_load_more));
        mPullToRefreshView.setBottomRefreshView(moreRefreshView);

        mPullToRefreshView.setOnTopRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setTopRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });

        mPullToRefreshView.setOnBottomRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setBottomRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });

        return rootView;
    }

    private class SampleAdapter extends RecyclerView.Adapter<SampleHolder> {

        @Override
        public SampleHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            return new SampleHolder(view);
        }

        @Override
        public void onBindViewHolder(SampleHolder holder, int pos) {
            Map<String, Integer> data = mSampleList.get(pos);
            holder.bindData(data);
        }

        @Override
        public int getItemCount() {
            return mSampleList.size();
        }
    }

    private class SampleHolder extends RecyclerView.ViewHolder {

        private View mRootView;
        private ImageView mImageViewIcon;

        private Map<String, Integer> mData;

        public SampleHolder(View itemView) {
            super(itemView);

            mRootView = itemView;
            mImageViewIcon = (ImageView) itemView.findViewById(R.id.image_view_icon);
        }

        public void bindData(Map<String, Integer> data) {
            mData = data;

            mRootView.setBackgroundResource(mData.get(KEY_COLOR));
            mImageViewIcon.setImageResource(mData.get(KEY_ICON));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*
        mPullToRefreshView.setTopRefreshing(true);
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToRefreshView.setTopRefreshing(false);
            }
        }, REFRESH_DELAY);
        */
    }

}
