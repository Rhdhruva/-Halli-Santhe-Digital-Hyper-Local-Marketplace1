package com.halliSanthe.app.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.halliSanthe.app.data.model.Inquiry
import com.halliSanthe.app.data.model.Product
import com.halliSanthe.app.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProductRepository(application)

    // ── Search state ──────────────────────────────────────────────────────────
    private val _searchQuery = MutableLiveData<String>("")

    val products: LiveData<List<Product>> = _searchQuery.switchMap { query ->
        if (query.isBlank()) repository.allProducts
        else repository.searchProducts(query)
    }

    val categories: LiveData<List<String>> = repository.allCategories

    val unreadInquiryCount: LiveData<Int> = repository.unreadInquiryCount

    // ── Operation result ──────────────────────────────────────────────────────
    private val _operationResult = MutableLiveData<OperationResult>()
    val operationResult: LiveData<OperationResult> = _operationResult

    // ── Public API ────────────────────────────────────────────────────────────

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() = setSearchQuery("")

    suspend fun getProductById(id: Long): Product? =
        repository.getProductById(id)

    fun getInquiriesForProduct(productId: Long) =
        repository.getInquiriesForProduct(productId)

    fun insertProduct(product: Product) = viewModelScope.launch {
        try {
            val id = repository.insertProduct(product)
            _operationResult.postValue(OperationResult.Success("Product listed successfully!", id))
        } catch (e: Exception) {
            _operationResult.postValue(OperationResult.Error("Failed to save product: ${e.message}"))
        }
    }

    fun deleteProduct(product: Product) = viewModelScope.launch {
        try {
            repository.deleteProduct(product)
            _operationResult.postValue(OperationResult.Success("Product removed."))
        } catch (e: Exception) {
            _operationResult.postValue(OperationResult.Error("Failed to delete: ${e.message}"))
        }
    }

    fun sendInquiry(productId: Long, buyerName: String, message: String) = viewModelScope.launch {
        try {
            repository.sendInquiry(
                Inquiry(productId = productId, buyerName = buyerName, message = message)
            )
            _operationResult.postValue(OperationResult.Success("Inquiry sent to artisan!"))
        } catch (e: Exception) {
            _operationResult.postValue(OperationResult.Error("Failed to send inquiry: ${e.message}"))
        }
    }

    fun markInquiriesRead(productId: Long) = viewModelScope.launch {
        repository.markInquiriesRead(productId)
    }

    // ── Sealed result class ───────────────────────────────────────────────────
    sealed class OperationResult {
        data class Success(val message: String, val newId: Long = -1) : OperationResult()
        data class Error(val message: String) : OperationResult()
    }
}
