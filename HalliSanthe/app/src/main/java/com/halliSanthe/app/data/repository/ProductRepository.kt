package com.halliSanthe.app.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.halliSanthe.app.data.model.Inquiry
import com.halliSanthe.app.data.model.Product

class ProductRepository(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val productDao = db.productDao()
    private val inquiryDao = db.inquiryDao()

    // ── Products ──────────────────────────────────────────────────────────────

    val allProducts: LiveData<List<Product>> = productDao.getAllProducts()

    val allCategories: LiveData<List<String>> = productDao.getAllCategories()

    fun searchProducts(query: String): LiveData<List<Product>> =
        productDao.searchProducts(query)

    suspend fun getProductById(id: Long): Product? =
        productDao.getProductById(id)

    suspend fun insertProduct(product: Product): Long =
        productDao.insertProduct(product)

    suspend fun updateProduct(product: Product) =
        productDao.updateProduct(product)

    suspend fun deleteProduct(product: Product) =
        productDao.deleteProduct(product)

    suspend fun deleteProductById(id: Long) =
        productDao.deleteProductById(id)

    // ── Inquiries ─────────────────────────────────────────────────────────────

    fun getInquiriesForProduct(productId: Long): LiveData<List<Inquiry>> =
        inquiryDao.getInquiriesForProduct(productId)

    val unreadInquiryCount: LiveData<Int> = inquiryDao.getUnreadCount()

    suspend fun sendInquiry(inquiry: Inquiry): Long =
        inquiryDao.insertInquiry(inquiry)

    suspend fun markInquiriesRead(productId: Long) =
        inquiryDao.markAllReadForProduct(productId)
}
