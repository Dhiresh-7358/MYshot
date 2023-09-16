package com.example.myshot.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myshot.databinding.FragmentPhotographerBinding

class PhotographerFragment : Fragment() {

    private lateinit var binging: FragmentPhotographerBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binging= FragmentPhotographerBinding.inflate(inflater,container,false)
        return binging.root
    }


}