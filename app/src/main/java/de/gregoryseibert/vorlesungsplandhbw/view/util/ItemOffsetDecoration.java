package de.gregoryseibert.vorlesungsplandhbw.view.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Gregory Seibert on 16.01.2018.
 */

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
    private int mItemOffset;
    private boolean horizontal;

    public ItemOffsetDecoration(int itemOffset, boolean horizontal) {
        mItemOffset = itemOffset;
        this.horizontal = horizontal;
    }

    public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId, boolean horizontal) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId), horizontal);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if(horizontal) {
            outRect.set(0, 0, mItemOffset, 0);
        } else {
            outRect.set(0, 0, 0, mItemOffset);
        }
    }
}
