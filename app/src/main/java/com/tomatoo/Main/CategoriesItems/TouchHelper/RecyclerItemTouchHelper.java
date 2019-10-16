package com.tomatoo.Main.CategoriesItems.TouchHelper;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.tomatoo.Main.CategoriesItems.ShoppingCartAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

   ItemTouchHelperInterface itemTouchHelperInterface;

  public   interface  ItemTouchHelperInterface {
        void swipped(RecyclerView.ViewHolder holder,int direction,int position);
    }

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs,ItemTouchHelperInterface mIterface ) {
        super(dragDirs, swipeDirs);
        this.itemTouchHelperInterface=mIterface;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

      itemTouchHelperInterface.swipped(viewHolder,i,viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder!=null){
            View   foreground=((ShoppingCartAdapter.Holder)viewHolder).viewForeground;
            getDefaultUIUtil().onSelected(foreground);
        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreground=((ShoppingCartAdapter.Holder)viewHolder).viewForeground;
        getDefaultUIUtil().onDrawOver(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        View foreground=((ShoppingCartAdapter.Holder)viewHolder).viewForeground;
        getDefaultUIUtil().clearView(foreground);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreground=((ShoppingCartAdapter.Holder)viewHolder).viewForeground;
        getDefaultUIUtil().onDraw(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }
}
