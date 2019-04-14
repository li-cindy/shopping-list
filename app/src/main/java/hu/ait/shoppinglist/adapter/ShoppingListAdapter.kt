package hu.ait.shoppinglist.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hu.ait.shoppinglist.R
import hu.ait.shoppinglist.data.ShoppingItem
import hu.ait.shoppinglist.touch.ShoppingListTouchHelperCallback
import kotlinx.android.synthetic.main.list_row.view.*
import java.util.*

class ShoppingListAdapter : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>, ShoppingListTouchHelperCallback {

    var shoppingItems = mutableListOf<ShoppingItem>(
        ShoppingItem("Food", "bananas", "yellow", 0.65.toFloat(), true),
        ShoppingItem("Electronics", "TV", "yeah a whole tv", 1000.toFloat(), false)
    )

    private val context: Context
    constructor(context: Context) : super() {
        this.context = context
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ShoppingListAdapter.ViewHolder {
        val itemRowView = LayoutInflater.from(context).inflate(
            R.layout.list_row, viewGroup, false
        )

        return ViewHolder(itemRowView)
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    override fun onBindViewHolder(viewHolder: ShoppingListAdapter.ViewHolder, position: Int) {
        val item = shoppingItems.get(position)

        viewHolder.tvCategory.text = item.category
        viewHolder.tvName.text = item.name
        viewHolder.tvDescription.text = item.description
        viewHolder.tvPrice.text = String.format("$%.2f", item.price)
        viewHolder.cbBought.isChecked = item.status
    }

    fun addItem(item: ShoppingItem) {
        shoppingItems.add(0, item)
        notifyItemInserted(0)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCategory = itemView.tvCategory
        var tvName = itemView.tvName
        var tvDescription = itemView.tvDescription
        var tvPrice = itemView.tvPrice
        var cbBought = itemView.cbBought
    }

    override fun onDismissed(position: Int) {
        shoppingItems.removeAt(position)
        notifyItemRemoved(position)    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(shoppingItems, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)    }


}