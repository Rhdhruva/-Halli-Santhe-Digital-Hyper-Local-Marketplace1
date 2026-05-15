package com.halliSanthe.app.data.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.halliSanthe.app.data.model.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM products ORDER BY createdAt DESC")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Long): Product?

    @Query("""
        SELECT * FROM products
        WHERE name LIKE '%' || :query || '%'
        OR category LIKE '%' || :query || '%'
        ORDER BY createdAt DESC
    """)
    fun searchProducts(query: String): LiveData<List<Product>>

    @Query("SELECT DISTINCT category FROM products ORDER BY category ASC")
    fun getAllCategories(): LiveData<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProductById(id: Long)
}
