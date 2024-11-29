package com.example.agrimarttrader.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agrimarttrader.Adapters.FertilizerAdapter
import com.example.agrimarttrader.AddFertilizerActivity
import com.example.agrimarttrader.Class.MyClass
import com.example.agrimarttrader.Model.Fertilizer
import com.example.agrimarttrader.R
import com.example.agrimarttrader.databinding.FragmentFertilizerBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FertilizerFragment : Fragment() {
    private lateinit var binding: FragmentFertilizerBinding
    lateinit var fertilizerList : MutableList<Fertilizer>



     lateinit var adapter: FertilizerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFertilizerBinding.inflate(inflater, container, false)

        fertilizerList = mutableListOf()
        adapter = FertilizerAdapter(requireContext(),fertilizerList)
        binding.recyclerViewFertilizers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFertilizers.adapter = adapter

        fetchFertilizer()


        binding.fabAddFertilizer.setOnClickListener {

            startActivity(Intent(requireContext(),AddFertilizerActivity::class.java))


        }

        return binding.root
    }

    private fun fetchFertilizer() {

        MyClass().getCurrentTrader { trader->
            if(trader!=null){
                var traderId = trader.id.toString()


               var database = FirebaseDatabase.getInstance().getReference("Fertilizer")

                database.orderByChild("traderId").equalTo(traderId).addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if(snapshot.exists()){
                            fertilizerList.clear()

                            for(childSnapshot in snapshot.children){
                                var fertilizer = childSnapshot.getValue(Fertilizer::class.java)
                                if(fertilizer!=null){
                                    fertilizerList.add(fertilizer)
                                }

                            }

                           adapter.notifyDataSetChanged()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })







            }

        }




    }
}
