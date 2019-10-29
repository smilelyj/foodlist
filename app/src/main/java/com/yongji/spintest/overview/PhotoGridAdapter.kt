package com.yongji.spintest.overview

import com.yongji.spintest.databinding.GridViewItemBinding
import com.yongji.spintest.network.Food
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClick a lambda that takes the
 */
class PhotoGridAdapter( val onClickListener: OnClickListener ) :
    ListAdapter<Food, PhotoGridAdapter.FoodViewHolder>(DiffCallback) {
    /**
     * The FoodViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [Food] information.
     */
    class FoodViewHolder(private var binding: GridViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(food: Food) {
            binding.food = food
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Food]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.url == newItem.url
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): FoodViewHolder {
        return FoodViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(food)
        }
        holder.bind(food)
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Food]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Food]
     */
    class OnClickListener(val clickListener: (food:Food) -> Unit) {
        fun onClick(food:Food) = clickListener(food)
    }
}
