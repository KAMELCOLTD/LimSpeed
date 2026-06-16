package com.kamel.limspeed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kamel.limspeed.databinding.ItemAppBinding
import com.kamel.limspeed.models.AppSpeed

class AppListAdapter(
    private val apps: List<AppSpeed>,
    private val onItemClick: (AppSpeed) -> Unit
) : RecyclerView.Adapter<AppListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(apps[position])
    }

    override fun getItemCount() = apps.size

    inner class ViewHolder(private val binding: ItemAppBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(app: AppSpeed) {
            binding.ivAppIcon.setImageDrawable(app.icon)
            binding.tvAppName.text = app.appName
            binding.tvAppSpeed.text = if (app.speedKbps > 0) {
                "${app.speedKbps} KB/s"
            } else {
                "Default"
            }
            binding.root.setOnClickListener { onItemClick(app) }
        }
    }
}
