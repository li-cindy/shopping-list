package hu.ait.shoppinglist.touch

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper


class ShoppingListTouchCallback(private val shoppingListTouchHelperAdapter: ShoppingListTouchHelperCallback)
    : ItemTouchHelper.Callback() {

//    override fun isLongPressDragEnabled(): Boolean {
//        return true
//    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        shoppingListTouchHelperAdapter.onItemMoved(
            viewHolder.adapterPosition,
            target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        shoppingListTouchHelperAdapter.onDismissed(viewHolder.adapterPosition)
    }
}