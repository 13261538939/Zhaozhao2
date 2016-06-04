package com.szl.zhaozhao2.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.szl.zhaozhao2.view.LoadMoreView;

/**
 * Created by itsdon on 16/5/15.
 */
public class BaseListFragment extends BaseFragment {
    private LoadMoreView loadMoreView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadMoreView = createLoadMoreView();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private LoadMoreView createLoadMoreView() {
        loadMoreView = new LoadMoreView(getActivity());
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        loadMoreView.setLayoutParams(params);
        return loadMoreView;
    }

    protected LoadMoreView getLoadMoreView() {
        if (loadMoreView == null) {
            createLoadMoreView();
        }
        return loadMoreView;
    }

}
