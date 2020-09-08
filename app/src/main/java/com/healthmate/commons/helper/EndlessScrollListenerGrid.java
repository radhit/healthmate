package com.healthmate.commons.helper;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessScrollListenerGrid extends RecyclerView.OnScrollListener {
    private int mPreviousTotal = 0;
    public boolean mLoading = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        Log.i("endless","dx: "+dx+" |dy: "+dy);
//        try{
//            if(dy > 0){ // only when scrolling up
//                final int visibleThreshold = 2;
//                GridLayoutManager layoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
//                int lastItem  = layoutManager.findLastCompletelyVisibleItemPosition();
//                int currentTotalCount = layoutManager.getItemCount();
//                Log.i("endless","currentTotalCount: "+currentTotalCount+" |lastItem: "+lastItem+" |visibleThreshold: "+visibleThreshold);
//                if(!mLoading && currentTotalCount <= lastItem + visibleThreshold){
//                    //show your loading view
//                    // load content in background
//                    onLoadMore();
//                    mLoading = true;
//                }
//            }
//
//        }catch (IndexOutOfBoundsException e){
//            e.printStackTrace();
//        }

        try{
            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = recyclerView.getLayoutManager().getItemCount();
            int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
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
