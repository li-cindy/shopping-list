package hu.ait.shoppinglist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import hu.ait.shoppinglist.adapter.ShoppingListAdapter
import hu.ait.shoppinglist.data.ShoppingItem
import hu.ait.shoppinglist.touch.ShoppingListTouchCallback
import kotlinx.android.synthetic.main.activity_scrolling.*
import hu.ait.shoppinglist.data.AppDatabase


class ScrollingActivity : AppCompatActivity(), ShoppingItemDialog.ShoppingItemHandler {

    companion object {
        val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
    }

    lateinit var shoppingListAdapter: ShoppingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            showAddItemDialog()
        }

        deleteAll.setOnClickListener {
            Thread {
                AppDatabase.getInstance(this@ScrollingActivity).shoppingItemDao().deleteAllShoppingItems()
                runOnUiThread {
                    shoppingListAdapter.deleteAllShoppingItems()
                }
            }.start()        }
        initRecyclerViewFromDB()
    }

    private fun initRecyclerViewFromDB() {
        Thread {
            var listShoppingItems =
                AppDatabase.getInstance(this@ScrollingActivity).shoppingItemDao().getAllShoppingItems()

            runOnUiThread {
                // UI code here
                shoppingListAdapter = ShoppingListAdapter(this, listShoppingItems)

                recyclerShoppingList.layoutManager = LinearLayoutManager(this)

                recyclerShoppingList.adapter = shoppingListAdapter

                val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
                recyclerShoppingList.addItemDecoration(itemDecoration)

                val callback = ShoppingListTouchCallback(shoppingListAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerShoppingList)
            }

        }.start()
    }


    private fun showAddItemDialog() {
        ShoppingItemDialog().show(supportFragmentManager, "TAG_ITEM_DIALOG")

    }

    var editIndex: Int = -1

    public fun showEditItemDialog(itemToEdit: ShoppingItem, idx: Int) {
        editIndex = idx
        val editItemDialog = ShoppingItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_TO_EDIT, itemToEdit)
        editItemDialog.arguments = bundle

        editItemDialog.show(supportFragmentManager,
            "EDITITEMDIALOG")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun itemCreated(item: ShoppingItem) {
        Thread {
            var newId = AppDatabase.getInstance(this).shoppingItemDao().insertShoppingItem(item)

            item.itemId = newId

            runOnUiThread {
                shoppingListAdapter.addItem(item)
            }
        }.start()
    }

    override fun itemUpdated(item: ShoppingItem) {
        Thread {
            AppDatabase.getInstance(this).shoppingItemDao().updateShoppingItem(item)

            runOnUiThread {
                shoppingListAdapter.updateShoppingItem(item, editIndex)
            }
        }.start()
    }

}
