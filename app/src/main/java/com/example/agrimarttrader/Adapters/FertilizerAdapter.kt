package com.example.agrimarttrader.Adapters


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agrimarttrader.Model.Fertilizer
import com.example.agrimarttrader.R
import com.google.firebase.database.FirebaseDatabase


class FertilizerAdapter(
    private val context: Context,
    private val fertilizerList: MutableList<Fertilizer>
) : RecyclerView.Adapter<FertilizerAdapter.FertilizerViewHolder>() {

    class FertilizerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var pic: ImageView = itemView.findViewById(R.id.pic)
        var id: TextView = itemView.findViewById(R.id.id)
        var name: TextView = itemView.findViewById(R.id.name)
        var rate: TextView = itemView.findViewById(R.id.rate)
        var rating: TextView = itemView.findViewById(R.id.rating)
        var delete: ImageView = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FertilizerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fertilizer, parent, false)
        return FertilizerViewHolder(view)
    }

    override fun onBindViewHolder(holder: FertilizerViewHolder, position: Int) {
        val fertilizer = fertilizerList[position]

        holder.id.text = "F-Id: ${fertilizer.id ?: "N/A"}"
        holder.name.text = fertilizer.name ?: "Unknown"
        holder.rate.text = "${fertilizer.rate ?: "0"} taka/${fertilizer.unit ?: "unit"}"
        holder.rating.text = "Rating: ${fertilizer.rating ?: "0.0"}"

        Glide.with(context).load(fertilizer.pic).into(holder.pic)

        holder.delete.setOnClickListener {
            deleteFertilizer(position, fertilizer.id)
        }
    }

    override fun getItemCount() = fertilizerList.size

    private fun deleteFertilizer(position: Int, fertilizerId: String?) {
        fertilizerList.removeAt(position)
        notifyItemRemoved(position)

        fertilizerId?.let {
            FirebaseDatabase.getInstance().getReference("Fertilizer")
                .child(it)
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(context, "Fertilizer deleted successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to delete fertilizer", Toast.LENGTH_SHORT).show()
                }
        }
    }
}

