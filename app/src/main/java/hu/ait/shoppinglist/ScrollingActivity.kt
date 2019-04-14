package hu.ait.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import hu.ait.shoppinglist.adapter.ShoppingListAdapter
import hu.ait.shoppinglist.data.ShoppingItem
import hu.ait.shoppinglist.touch.ShoppingListTouchCallback
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.new_item_dialog.*
import kotlinx.android.synthetic.main.new_item_dialog.view.*
import android.widget.RadioButton


class ScrollingActivity : AppCompatActivity() {
    lateinit var shoppingListAdapter: ShoppingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            //            var newItemIntent = Intent()
//            newItemIntent.setClass(this@ScrollingActivity,
//                NewItemActivity::class.java)
//
//            startActivity(newItemIntent)
            showAddItemDialog()
        }


        shoppingListAdapter = ShoppingListAdapter(this)
        recyclerToDo.layoutManager = LinearLayoutManager(this)
        recyclerToDo.adapter = shoppingListAdapter


        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerToDo.addItemDecoration(itemDecoration)

        val callback = ShoppingListTouchCallback(shoppingListAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerToDo)

    }

    private fun showAddItemDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("New Item")


        val dialogView = layoutInflater.inflate(
            R.layout.new_item_dialog,
            null, false
        )
        val inputName = dialogView.etName
        val inputDescription = dialogView.etDescription
        val inputPrice = dialogView.etPrice
        var inputCategory = ""


        if (dialogView.rgCategory != null) {
            dialogView.rgCategory.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId != -1) {
                    inputCategory = (findViewById<View>(checkedId) as? RadioButton)?.text.toString()
                } else {
                    inputCategory = "wrong"
                    // TODO: don't allow them to submit
                }
            }
        }



        dialogBuilder.setView(dialogView)

        dialogBuilder.setNegativeButton("Cancel") { dialog, button ->
            dialog.dismiss()
        }
        dialogBuilder.setPositiveButton("Add") { dialog, button ->
            val item = ShoppingItem(
                inputCategory.toString(),
                inputName.text.toString(),
                inputDescription.text.toString(),
                inputPrice.text.toString().toFloat(),
                false
            )

            shoppingListAdapter.addItem(item)
        }
        dialogBuilder.show()
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
}
