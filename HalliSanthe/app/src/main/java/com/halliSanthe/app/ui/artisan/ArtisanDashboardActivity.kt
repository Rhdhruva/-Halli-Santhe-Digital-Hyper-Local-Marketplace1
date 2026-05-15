package com.halliSanthe.app.ui.artisan

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.halliSanthe.app.data.model.Product
import com.halliSanthe.app.databinding.ActivityArtisanDashboardBinding
import com.halliSanthe.app.viewmodel.ProductViewModel

class ArtisanDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtisanDashboardBinding
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var adapter: ArtisanProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtisanDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "My Listings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
        observeProducts()
        observeOperationResults()

        binding.fabAddProduct.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        adapter = ArtisanProductAdapter(
            onDelete = { product -> confirmDelete(product) }
        )
        binding.rvMyProducts.apply {
            layoutManager = LinearLayoutManager(this@ArtisanDashboardActivity)
            adapter = this@ArtisanDashboardActivity.adapter
        }
    }

    private fun observeProducts() {
        viewModel.products.observe(this) { products ->
            adapter.submitList(products)
            binding.emptyState.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE
            binding.rvMyProducts.visibility = if (products.isEmpty()) View.GONE else View.VISIBLE
            binding.tvListingCount.text = "${products.size} listing${if (products.size != 1) "s" else ""}"
        }
    }

    private fun observeOperationResults() {
        viewModel.operationResult.observe(this) { result ->
            when (result) {
                is ProductViewModel.OperationResult.Success ->
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                is ProductViewModel.OperationResult.Error ->
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun confirmDelete(product: Product) {
        AlertDialog.Builder(this)
            .setTitle("Remove Listing")
            .setMessage("Are you sure you want to remove \"${product.name}\"?")
            .setPositiveButton("Remove") { _, _ -> viewModel.deleteProduct(product) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
