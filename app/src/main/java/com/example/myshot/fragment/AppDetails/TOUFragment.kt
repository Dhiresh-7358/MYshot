package com.example.myshot.fragment.AppDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myshot.R
import com.example.myshot.databinding.FragmentTOUBinding


class TOUFragment : Fragment() {

    private lateinit var binding:FragmentTOUBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentTOUBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tOUBack.setOnClickListener {
            requireFragmentManager().popBackStack()
        }
    }

    companion object {

        fun newInstance() =
            TOUFragment()
    }
}