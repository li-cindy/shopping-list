package hu.ait.shoppinglist.data

data class ShoppingItem(
    var category: String, var name: String, var description: String, var price: Float, var status: Boolean
)