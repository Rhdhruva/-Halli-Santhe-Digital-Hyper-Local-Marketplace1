package com.halliSanthe.app.ui.artisan

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.halliSanthe.app.R
import com.halliSanthe.app.data.model.Product
import com.halliSanthe.app.databinding.ActivityAddProductBinding
import com.halliSanthe.app.viewmodel.ProductViewModel

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private val viewModel: ProductViewModel by viewModels()
    private var selectedImageUri: Uri? = null

    private val categories = listOf(
        "Handicraft", "Pottery", "Textile", "Jewellery",
        "Woodwork", "Paintings", "Food & Spices", "Other"
    )

    // ── Image picker ──────────────────────────────────────────────────────────
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                Glide.with(this).load(it).centerCrop().into(binding.ivProductPreview)
            }
        }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) imagePickerLauncher.launch("image/*")
            else Snackbar.make(binding.root, "Permission needed to pick images", Snackbar.LENGTH_SHORT).show()
        }

    // ─────────────────────────────────────────────────────────────────────────

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Add New Product"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupCategoryDropdown()
        setupClickListeners()
        observeResults()
    }

    private fun setupCategoryDropdown() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        binding.actvCategory.setAdapter(adapter)
    }

    private fun setupClickListeners() {
        binding.ivProductPreview.setOnClickListener { pickImage() }
        binding.btnPickImage.setOnClickListener { pickImage() }
        binding.btnSubmit.setOnClickListener { submitProduct() }
    }

    private fun pickImage() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            imagePickerLauncher.launch("image/*")
        } else {
            permissionLauncher.launch(permission)
        }
    }

    private fun submitProduct() {
        val name = binding.etProductName.text.toString().trim()
        val priceStr = binding.etPrice.text.toString().trim()
        val category = binding.actvCategory.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val location = binding.etLocation.text.toString().trim()
        val artisanName = binding.etArtisanName.text.toString().trim()

        // Validation
        if (name.isEmpty()) { binding.tilProductName.error = "Required"; return }
        if (priceStr.isEmpty()) { binding.tilPrice.error = "Required"; return }
        if (category.isEmpty()) { binding.tilCategory.error = "Select a category"; return }
        if (location.isEmpty()) { binding.tilLocation.error = "Required"; return }
        if (artisanName.isEmpty()) { binding.tilArtisanName.error = "Required"; return }

        val price = priceStr.toDoubleOrNull()
        if (price == null || price <= 0) {
            binding.tilPrice.error = "Enter a valid price"
            return
        }

        // Clear errors
        listOf(binding.tilProductName, binding.tilPrice, binding.tilCategory,
            binding.tilLocation, binding.tilArtisanName).forEach { it.error = null }

        val product = Product(
            name = name,
            price = price,
            category = category,
            description = description,
            location = location,
            artisanName = artisanName,
            imageUri = selectedImageUri?.toString() ?: ""
        )

        binding.btnSubmit.isEnabled = false
        viewModel.insertProduct(product)
    }

    private fun observeResults() {
        viewModel.operationResult.observe(this) { result ->
            when (result) {
                is ProductViewModel.OperationResult.Success -> {
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                    finish()
                }
                is ProductViewModel.OperationResult.Error -> {
                    binding.btnSubmit.isEnabled = true
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
