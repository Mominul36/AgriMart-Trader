package com.example.agrimarttrader.Adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agrimarttrader.Model.Pesticide
import com.example.agrimarttrader.R
import com.google.firebase.database.FirebaseDatabase


class PesticideAdapter(
    private val context: Context,
    private val pesticideList: MutableList<Pesticide>
) : RecyclerView.Adapter<PesticideAdapter.PesticideViewHolder>() {

    class PesticideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var pic: ImageView = itemView.findViewById(R.id.pic)
        var id: TextView = itemView.findViewById(R.id.id)
        var name: TextView = itemView.findViewById(R.id.name)
        var rate: TextView = itemView.findViewById(R.id.rate)
        var rating: TextView = itemView.findViewById(R.id.rating)
        var delete: ImageView = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesticideViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fertilizer, parent, false)
        return PesticideViewHolder(view)
    }

    override fun onBindViewHolder(holder: PesticideViewHolder, position: Int) {
        val pesticide = pesticideList[position]

        holder.id.text = "F-Id: ${pesticide.id ?: "N/A"}"
        holder.name.text = pesticide.name ?: "Unknown"
        holder.rate.text = "${pesticide.rate ?: "0"} taka/${pesticide.unit ?: "unit"}"
        holder.rating.text = "Rating: ${pesticide.rating ?: "0.0"}"

        Glide.with(context).load(pesticide.pic).into(holder.pic)

        holder.delete.setOnClickListener {
            deletePesticide(position, pesticide.id)
        }
    }

    override fun getItemCount() = pesticideList.size

    private fun deletePesticide(position: Int, pesticideId: String?) {
        pesticideList.removeAt(position)
        notifyItemRemoved(position)

        pesticideId?.let {
            FirebaseDatabase.getInstance().getReference("Pesticide")
                .child(it)
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(context, "Pesticide deleted successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to delete Pesticide", Toast.LENGTH_SHORT).show()
                }
        }
    }
}

