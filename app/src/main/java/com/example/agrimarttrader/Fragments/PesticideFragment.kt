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
import com.example.agrimarttrader.Adapters.PesticideAdapter
import com.example.agrimarttrader.AddFertilizerActivity
import com.example.agrimarttrader.AddPesticideActivity
import com.example.agrimarttrader.Class.MyClass
import com.example.agrimarttrader.Model.Fertilizer
import com.example.agrimarttrader.Model.Pesticide
import com.example.agrimarttrader.R
import com.example.agrimarttrader.databinding.FragmentFertilizerBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PesticideFragment : Fragment() {
    private lateinit var binding: FragmentFertilizerBinding
    lateinit var pesticideList : MutableList<Pesticide>
    lateinit var adapter: PesticideAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFertilizerBinding.inflate(inflater, container, false)

        pesticideList = mutableListOf()
        adapter = PesticideAdapter(requireContext(),pesticideList)
        binding.recyclerViewFertilizers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFertilizers.adapter = adapter

        binding.recyclerViewFertilizers.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE



        fetchFertilizer()


        binding.fabAddFertilizer.setOnClickListener {

            startActivity(Intent(requireContext(), AddPesticideActivity::class.java))


        }

        return binding.root
    }

    private fun fetchFertilizer() {

        binding.recyclerViewFertilizers.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        MyClass().getCurrentTrader { trader->
            if(trader!=null){
                var traderId = trader.id.toString()


                var database = FirebaseDatabase.getInstance().getReference("Pesticide")

                database.orderByChild("traderId").equalTo(traderId).addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            pesticideList.clear()
                            for(childSnapshot in snapshot.children){
                                var pesticide = childSnapshot.getValue(Pesticide::class.java)
                                if(pesticide!=null){
                                    pesticideList.add(pesticide)
                                }

                            }



                            binding.recyclerViewFertilizers.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
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
