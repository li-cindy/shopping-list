package hu.ait.shoppinglist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import hu.ait.shoppinglist.data.ShoppingItem
import kotlinx.android.synthetic.main.new_item_dialog.view.*
import java.lang.RuntimeException
import android.widget.TextView


class ShoppingItemDialog : DialogFragment() {

    interface ShoppingItemHandler {
        fun itemCreated(item: ShoppingItem)
        fun itemUpdated(item: ShoppingItem)
    }

    private lateinit var shoppingItemHandler: ShoppingItemHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ShoppingItemHandler) {
            shoppingItemHandler = context
        } else {
            throw RuntimeException(
                getString(R.string.does_not_implement)
            )
        }
    }

    private lateinit var spItemCategory: Spinner
    private lateinit var etItemName: EditText
    private lateinit var etItemDescription: EditText
    private lateinit var etItemPrice: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.new_item))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.new_item_dialog, null
        )

        val categoriesAdapter = ArrayAdapter.createFromResource(
            activity,
            R.array.array_categories, android.R.layout.simple_spinner_item
        )
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spItemCategory = rootView.spinnerCategory
        spItemCategory.adapter = categoriesAdapter


        etItemName = rootView.etName
        etItemDescription = rootView.etDescription
        etItemPrice = rootView.etPrice
        builder.setView(rootView)

        val arguments = this.arguments
        if (arguments != null && arguments.containsKey(
                ScrollingActivity.KEY_ITEM_TO_EDIT
            )
        ) {
            val shoppingItem = arguments.getSerializable(
                ScrollingActivity.KEY_ITEM_TO_EDIT
            ) as ShoppingItem
            when (shoppingItem.category) {
                getString(R.string.clothing_category) -> spItemCategory.setSelection(1)
                getString(R.string.electronics_category) -> spItemCategory.setSelection(2)
                getString(R.string.food_category) -> spItemCategory.setSelection(3)
                getString(R.string.household_category) -> spItemCategory.setSelection(4)
                getString(R.string.other_category) -> spItemCategory.setSelection(5)
            }
            etItemName.setText(shoppingItem.name)
            etItemDescription.setText(shoppingItem.description)
            etItemPrice.setText(shoppingItem.price.toString())

            builder.setTitle(getString(R.string.edit_item))
        }

        builder.setPositiveButton(getString(R.string.ok)) { dialog, witch ->
            // empty
        }

        builder.setNegativeButton(getString(R.string.cancel)) { dialog, witch ->
            // empty
        }
        return builder.create()
    }


    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (spItemCategory.selectedItemPosition != 0 || etItemName.text.isNotEmpty() ||
                etItemDescription.text.isNotEmpty() || etItemPrice.text.isNotEmpty()) {
                val arguments = this.arguments
                if (arguments != null && arguments.containsKey(ScrollingActivity.KEY_ITEM_TO_EDIT)) {
                    handleShoppingItemEdit()
                } else {
                    handleShoppingItemCreate()
                }

                dialog.dismiss()
            } else {
                if (spItemCategory.selectedItemPosition == 0) {
                    (spItemCategory.getChildAt(0) as TextView).error = getString(R.string.cannot_empty)
                }
                if (etItemName.text.isEmpty()) {
                    etItemName.error = getString(R.string.cannot_empty)
                }
                if (etItemDescription.text.isEmpty()) {
                    etItemDescription.error = getString(R.string.cannot_empty)
                }
                if (etItemPrice.text.isEmpty()) {
                    etItemPrice.error = getString(R.string.cannot_empty)
                }
            }
        }
    }

    private fun getCategorySelected() : String {
        var idx = spItemCategory.selectedItemPosition
        when (idx) {
            1 -> return getString(R.string.clothing_category)
            2 -> return getString(R.string.electronics_category)
            3 -> return getString(R.string.food_category)
            4 -> return getString(R.string.household_category)
            5 -> return getString(R.string.other_category)
        }
        return ""
    }

    private fun handleShoppingItemCreate() {
        shoppingItemHandler.itemCreated(
            ShoppingItem(
                null,
                getCategorySelected(),
                etItemName.text.toString(),
                etItemDescription.text.toString(),
                etItemPrice.text.toString().toFloat(),
                false
            )
        )
    }

    private fun handleShoppingItemEdit() {
        val itemToEdit = arguments?.getSerializable(
            ScrollingActivity.KEY_ITEM_TO_EDIT
        ) as ShoppingItem
        itemToEdit.category = getCategorySelected()
        itemToEdit.name = etItemName.text.toString()
        itemToEdit.description = etItemDescription.text.toString()
        itemToEdit.price = etItemPrice.text.toString().toFloat()

        shoppingItemHandler.itemUpdated(itemToEdit)
    }

}
