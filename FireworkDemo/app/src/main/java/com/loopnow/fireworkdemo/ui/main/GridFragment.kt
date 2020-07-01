package com.loopnow.fireworkdemo.ui.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loopnow.fireworkdemo.R
import com.loopnow.fireworklibrary.FireworkSDK

class GridFragment  : IntegratedFragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_grid, container, false)
    }


    override fun setViewModel() {

    }


    override fun setRecyclerView(view: View ) {
    }

    companion object {

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(): GridFragment {
            return GridFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FireworkSDK.debug("Grid onDestroy")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        FireworkSDK.debug("Grid onDestroyView")
    }
}