package hu.ait.shoppinglist.touch

interface ShoppingListTouchHelperCallback {

    fun onDismissed(position : Int)
    fun onItemMoved(fromPosition: Int, toPosition : Int)
}