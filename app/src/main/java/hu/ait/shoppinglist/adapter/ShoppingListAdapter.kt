package hu.ait.shoppinglist.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hu.ait.shoppinglist.R
import hu.ait.shoppinglist.ScrollingActivity
import hu.ait.shoppinglist.data.AppDatabase
import hu.ait.shoppinglist.data.ShoppingItem
import hu.ait.shoppinglist.touch.ShoppingListTouchHelperCallback
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.list_row.view.*
import java.util.*

class ShoppingListAdapter : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>, ShoppingListTouchHelperCallback {

    var shoppingItems = mutableListOf<ShoppingItem>()

    companion object {
        public var totalCost : Float = 0.toFloat()
    }

    private val context: Context
    constructor(context: Context, listShoppingItems: List<ShoppingItem>) : super() {
        this.context = context
        shoppingItems.addAll(listShoppingItems)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ShoppingListAdapter.ViewHolder {
        val itemRowView = LayoutInflater.from(context).inflate(
            R.layout.list_row, viewGroup, false
        )
        (context as ScrollingActivity).tvTotal.text = String.format("Total: $%.2f", calculateTotalCost())

        return ViewHolder(itemRowView)
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    override fun onBindViewHolder(viewHolder: ShoppingListAdapter.ViewHolder, position: Int) {
        val item = shoppingItems[position]

        viewHolder.tvCategory.text = item.category
        viewHolder.tvName.text = item.name
        viewHolder.tvDescription.text = item.description
        viewHolder.tvPrice.text = String.format("$%.2f", item.price)
        viewHolder.cbBought.isChecked = item.status

        setCorrectIcon(viewHolder)

        viewHolder.ivEdit.setOnClickListener {
            (context as ScrollingActivity).showEditItemDialog(item,
                viewHolder.adapterPosition)
        }

        viewHolder.cbBought.setOnClickListener {
            item.status = viewHolder.cbBought.isChecked
            updateShoppingItem(item)
        }
        (context as ScrollingActivity).tvTotal.text = String.format("Total: $%.2f", totalCost)

    }

    private fun setCorrectIcon(viewHolder: ViewHolder) {
        if (viewHolder.tvCategory.text == context.getString(R.string.food_category)) {
            viewHolder.ivIcon.setImageResource(R.drawable.food_icon)
        }
        if (viewHolder.tvCategory.text == context.getString(R.string.clothing_category)) {
            viewHolder.ivIcon.setImageResource(R.drawable.clothing_icon)
        }
        if (viewHolder.tvCategory.text == context.getString(R.string.electronics_category)) {
            viewHolder.ivIcon.setImageResource(R.drawable.electronics_icon)
        }
        if (viewHolder.tvCategory.text == context.getString(R.string.household_category)) {
            viewHolder.ivIcon.setImageResource(R.drawable.household_icon)
        }
    }


    fun addItem(item: ShoppingItem) {
        shoppingItems.add(item)
        notifyDataSetChanged()
        totalCost += item.price
        (context as ScrollingActivity).tvTotal.text = String.format("Total: $%.2f", totalCost)

    }

    fun deleteShoppingItem(deletePosition: Int) {
        totalCost -= shoppingItems[deletePosition].price
        (context as ScrollingActivity).tvTotal.text = String.format("Total: $%.2f", totalCost)
        Thread {
            AppDatabase.getInstance(context).shoppingItemDao().deleteShoppingItem(
                shoppingItems.get(deletePosition))

            (context as ScrollingActivity).runOnUiThread {
                shoppingItems.removeAt(deletePosition)
                notifyItemRemoved(deletePosition)
            }
        }.start()
    }

    fun deleteAllShoppingItems() {
        shoppingItems.clear()
        notifyDataSetChanged()
        totalCost = 0.toFloat()
        (context as ScrollingActivity).tvTotal.text = String.format("Total: $%.2f", totalCost)
    }

    fun updateShoppingItem(item: ShoppingItem) {
        Thread{
            AppDatabase.getInstance(context).shoppingItemDao().updateShoppingItem(item)
        }.start()
    }

    fun updateShoppingItem(item: ShoppingItem, editIndex: Int) {
        totalCost -= shoppingItems.get(editIndex).price
        totalCost += item.price
        (context as ScrollingActivity).tvTotal.text = String.format("Total: $%.2f", totalCost)
        shoppingItems[editIndex] = item
        notifyItemChanged(editIndex)
    }

    override fun onDismissed(position: Int) {
        deleteShoppingItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(shoppingItems, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCategory = itemView.tvCategory
        var tvName = itemView.tvName
        var tvDescription = itemView.tvDescription
        var tvPrice = itemView.tvPrice
        var cbBought = itemView.cbBought
        var ivIcon = itemView.ivIcon
        var ivEdit = itemView.ivEdit
    }

    private fun calculateTotalCost() : Float {
        totalCost = 0.toFloat()
        for (item in shoppingItems) {
            totalCost += item.price
        }
        return totalCost
    }
}