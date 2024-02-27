package com.example.myshot.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myshot.R
import com.example.myshot.adapter.AllPhotographer
import com.example.myshot.adapter.PageAdapter
import com.example.myshot.dataClass.TopPhotoData
import com.example.myshot.databinding.FragmentIdeasBinding
import com.example.myshot.databinding.FragmentProfilBinding
import com.example.myshot.databinding.FragmentWishlistBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class WishlistFragment : Fragment() {

    private lateinit var adapter: PageAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var binding: FragmentWishlistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishlistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        setAdapterViewPager2()


    }

    private fun setAdapterViewPager2() {
        adapter = PageAdapter(childFragmentManager, lifecycle)

        val tab1View = LayoutInflater.from(context).inflate(R.layout.text_for_tabs, null)
        val tab1TextView = tab1View.findViewById<TextView>(R.id.tabTextView)
        val tab1LineView = tab1View.findViewById<View>(R.id.tabLineView)
        tab1TextView.text = "Photos"
        tabLayout.addTab(tabLayout.newTab().setCustomView(tab1View))

        val tab2View = LayoutInflater.from(context).inflate(R.layout.text_for_tabs, null)
        val tab2TextView = tab2View.findViewById<TextView>(R.id.tabTextView)
        val tab2LineView = tab2View.findViewById<View>(R.id.tabLineView)
        tab2TextView.text = "Ideas"

        tabLayout.addTab(tabLayout.newTab().setCustomView(tab2View))


        tab1LineView.visibility = View.VISIBLE

        tab2TextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))

        viewPager2.adapter = adapter


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<View>(R.id.tabLineView)?.visibility = View.VISIBLE
                tab?.customView?.findViewById<TextView>(R.id.tabTextView)?.setTextColor(resources.getColor(R.color.pink))
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<View>(R.id.tabLineView)?.visibility = View.INVISIBLE
                tab?.customView?.findViewById<TextView>(R.id.tabTextView)?.setTextColor(resources.getColor(R.color.grey))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }

    private fun init() {
        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager2
    }


    companion object {

        fun newInstance() = WishlistFragment()
    }
}