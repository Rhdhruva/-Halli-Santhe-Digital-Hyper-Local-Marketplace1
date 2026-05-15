package com.halliSanthe.app.ui.buyer

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.halliSanthe.app.data.model.Product
import com.halliSanthe.app.databinding.ActivityMainBinding
import com.halliSanthe.app.ui.detail.ProductDetailActivity
import com.halliSanthe.app.viewmodel.ProductViewModel
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Halli-Santhe"

        setupRecyclerView()
        setupSearch()
        observeProducts()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter { product -> openDetail(product) }
        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = this@MainActivity.adapter
            setHasFixedSize(true)
        }
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true.also {
                viewModel.setSearchQuery(query.orEmpty())
            }
            override fun onQueryTextChange(newText: String?) = true.also {
                viewModel.setSearchQuery(newText.orEmpty())
            }
        })

        binding.searchView.setOnCloseListener {
            viewModel.clearSearch()
            false
        }
    }

    private fun observeProducts() {
        viewModel.products.observe(this) { products ->
            adapter.submitList(products)
            binding.emptyState.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE
            binding.rvProducts.visibility = if (products.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun openDetail(product: Product) {
        val intent = Intent(this, ProductDetailActivity::class.java)
            .putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.id)
        startActivity(intent)
    }
}
