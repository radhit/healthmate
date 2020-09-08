package com.healthmate.commons.helper;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private int mPreviousTotal = 0;
    public boolean mLoading = true;

    @Override
    public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        Log.i("endlessx","dx: "+dx+" |dy: "+dy);
        try{
            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = recyclerView.getLayoutManager().getItemCount();
            int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            if (mLoading) {
                if (totalItemCount != mPreviousTotal) {
                    Log.i("lol","onScrolled totalItemCount: "+totalItemCount+"| mPreviousTotal: "+mPreviousTotal);
                    mLoading = false;
                    mPreviousTotal = totalItemCount;
                }
            }
            int visibleThreshold = 2;
            if (!mLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                onLoadMore();
                mLoading = true;
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

    }

    public abstract void onLoadMore();
}
