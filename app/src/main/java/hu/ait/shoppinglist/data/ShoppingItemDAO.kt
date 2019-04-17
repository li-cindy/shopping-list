package hu.ait.shoppinglist.data

import android.arch.persistence.room.*

@Dao
interface ShoppingItemDAO {
    @Query("SELECT * FROM shoppinglist")
    fun getAllShoppingItems(): List<ShoppingItem>

    @Insert
    fun insertShoppingItem(item: ShoppingItem) : Long

    @Delete
    fun deleteShoppingItem(item: ShoppingItem)

    @Update
    fun updateShoppingItem(item: ShoppingItem)

    @Query("DELETE FROM shoppinglist")
    fun deleteAllShoppingItems()
}