package com.example.myshot.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myshot.R
import com.example.myshot.databinding.FragmentPhotographDetailBinding


class PhotoDetailFragmnet : Fragment()  {

    private lateinit var binging: FragmentPhotographDetailBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binging= FragmentPhotographDetailBinding.inflate(inflater,container,false)
        return binging.root
    }


}

