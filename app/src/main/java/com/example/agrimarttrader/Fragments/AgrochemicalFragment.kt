package com.example.agrimarttrader.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.agrimarttrader.Adapters.AgrochemicalPagerAdapter
import com.example.agrimarttrader.databinding.FragmentAgrochemicalBinding
import com.google.android.material.tabs.TabLayoutMediator

class AgrochemicalFragment : Fragment() {
    private lateinit var binding: FragmentAgrochemicalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAgrochemicalBinding.inflate(inflater, container, false)

        val adapter = AgrochemicalPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Fertilizer"
                1 -> "Pesticide"
                else -> "Tab ${position + 1}"
            }
        }.attach()

        return binding.root
    }
}
