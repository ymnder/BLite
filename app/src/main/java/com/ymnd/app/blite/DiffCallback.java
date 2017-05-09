package com.ymnd.app.blite;

import android.support.v7.util.DiffUtil;

import com.ymnd.app.blite.model.Bookmark;

import java.util.List;

/**
 * Created by yamazaki on 2017/05/07.
 */

public class DiffCallback extends DiffUtil.Callback {

    private List<Bookmark> mCurrentData;
    private List<Bookmark> mNewData;

    DiffCallback(List<Bookmark> currentData, List<Bookmark> newData) {
        this.mCurrentData = currentData;
        this.mNewData = newData;
    }

    public static DiffCallback newInstance(List<Bookmark> currentData, List<Bookmark> newData){
        return new DiffCallback(currentData, newData);
    }

    @Override
    public int getOldListSize() {
        return mCurrentData.size();
    }

    @Override
    public int getNewListSize() {
        return mNewData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mCurrentData.get(oldItemPosition).getTitle().equals(mNewData.get(newItemPosition).getTitle());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mCurrentData.get(oldItemPosition).equals(mNewData.get(newItemPosition));
    }
}