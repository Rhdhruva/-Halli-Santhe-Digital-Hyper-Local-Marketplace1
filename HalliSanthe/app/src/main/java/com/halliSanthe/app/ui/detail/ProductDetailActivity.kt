package com.halliSanthe.app.ui.detail

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.halliSanthe.app.R
import com.halliSanthe.app.data.model.Product
import com.halliSanthe.app.databinding.ActivityProductDetailBinding
import com.halliSanthe.app.databinding.DialogInquiryBinding
import com.halliSanthe.app.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private val viewModel: ProductViewModel by viewModels()

    companion object {
        const val EXTRA_PRODUCT_ID = "extra_product_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val productId = intent.getLongExtra(EXTRA_PRODUCT_ID, -1L)
        if (productId == -1L) { finish(); return }

        lifecycleScope.launch {
            val product = viewModel.getProductById(productId) ?: run { finish(); return@launch }
            populateUI(product)
        }

        observeResults()
    }

    private fun populateUI(product: Product) {
        supportActionBar?.title = product.name
        binding.tvProductName.text = product.name
        binding.tvPrice.text = "₹${String.format("%.2f", product.price)}"
        binding.tvCategory.text = product.category
        binding.tvDescription.text = product.description.ifEmpty { getString(R.string.no_description) }
        binding.tvArtisanName.text = product.artisanName
        binding.tvLocation.text = product.location

        if (product.imageUri.isNotEmpty()) {
            Glide.with(this).load(Uri.parse(product.imageUri))
                .placeholder(R.drawable.ic_product_placeholder)
                .error(R.drawable.ic_product_placeholder)
                .into(binding.ivProductImage)
        }

        binding.btnInquire.setOnClickListener { showInquiryDialog(product) }
    }

    private fun showInquiryDialog(product: Product) {
        val dialog = BottomSheetDialog(this)
        val dialogBinding = DialogInquiryBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.tvProductName.text = product.name

        dialogBinding.btnSendInquiry.setOnClickListener {
            val name = dialogBinding.etBuyerName.text.toString().trim()
            val message = dialogBinding.etMessage.text.toString().trim()
            if (name.isEmpty()) { dialogBinding.tilBuyerName.error = "Enter your name"; return@setOnClickListener }
            if (message.isEmpty()) { dialogBinding.tilMessage.error = "Enter a message"; return@setOnClickListener }
            viewModel.sendInquiry(product.id, name, message)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun observeResults() {
        viewModel.operationResult.observe(this) { result ->
            when (result) {
                is ProductViewModel.OperationResult.Success ->
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                is ProductViewModel.OperationResult.Error ->
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
