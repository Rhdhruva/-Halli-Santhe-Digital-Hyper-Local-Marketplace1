package com.halliSanthe.app.ui.artisan

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.halliSanthe.app.R
import com.halliSanthe.app.data.model.Product
import com.halliSanthe.app.databinding.ItemArtisanProductBinding

class ArtisanProductAdapter(
    private val onDelete: (Product) -> Unit
) : ListAdapter<Product, ArtisanProductAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArtisanProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ViewHolder(
        private val binding: ItemArtisanProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvName.text = product.name
            binding.tvPrice.text = "₹${String.format("%.2f", product.price)}"
            binding.tvCategory.text = product.category
            binding.tvLocation.text = product.location

            Glide.with(binding.ivThumbnail)
                .load(if (product.imageUri.isNotEmpty()) Uri.parse(product.imageUri) else null)
                .placeholder(R.drawable.ic_product_placeholder)
                .error(R.drawable.ic_product_placeholder)
                .centerCrop()
                .into(binding.ivThumbnail)

            binding.btnDelete.setOnClickListener { onDelete(product) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product) =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Product, newItem: Product) =
                oldItem == newItem
        }
    }
}
