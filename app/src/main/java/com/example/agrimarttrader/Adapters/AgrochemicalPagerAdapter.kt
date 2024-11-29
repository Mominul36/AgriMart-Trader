package com.example.agrimarttrader.Adapters


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.agrimarttrader.Fragments.FertilizerFragment
import com.example.agrimarttrader.Fragments.PesticideFragment

class AgrochemicalPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FertilizerFragment()
            1 -> PesticideFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}
