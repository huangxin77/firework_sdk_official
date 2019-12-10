package com.loopnow.fireworkdemo.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.loopnow.fireworkdemo.R
import com.loopnow.fireworkdemo.adapters.PlaybackViewAdapter
import com.loopnow.fireworklibrary.FeedResult
import com.loopnow.fireworklibrary.FireworkInitStatusListener
import com.loopnow.fireworklibrary.FireworkSDK
import kotlinx.android.synthetic.main.fragment_videoview.*

class VideoViewFragment  : Fragment() {
    lateinit var fireworkSDK: FireworkSDK

    val appid = "TIjq0YITcyqaz_zicjWpkx95gz_HAkzl"
    val bundle_id = "com.loopnow.fireworkdemo"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fireworkSDK =
            FireworkSDK.initialize(context!!,
                appid,
                "feed",
                bundle_id,
                View.generateViewId(),
                object : FireworkInitStatusListener {
                    override fun onInitializing() {
                        // Initializing the Firework SDK.
                    }

                    override fun onInitCompleted() {
                        // Firework SDK initialization completed.
                        setUI()
                        getFeed()
                    }

                    override fun onInitFailed(error: String) {
                        // Firework SDK initialization failed.
                        FireworkSDK.debug("init failed $error ")
                    }
                })

        return  inflater.inflate(R.layout.fragment_videoview, container, false)
    }


    companion object {

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(): VideoViewFragment {
            return VideoViewFragment()
        }
    }

    val adapter by lazy {
        PlaybackViewAdapter(context!!, fireworkSDK)
    }

    private fun setUI() {
        videoview_viewpager.adapter = adapter
        videoview_viewpager.offscreenPageLimit = 2

        videoview_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                fireworkSDK.nowPlayingVideo(p0, adapter.videoList[p0])
                adapter.currentVideo = p0
            }

        })
    }

    private fun getFeed() {
        // Get video feeds by a specific keyword and it will return a list with 10 videos.
        fireworkSDK.getVideoFeed().observe(this, Observer {
            it?.let { result ->
                when (result) {
                    is FeedResult.Loading -> {
                        // Loading data from the server.
                    }
                    is FeedResult.Error -> {
                        // Failed to get data from the server.
                    }
                    is FeedResult.Videos -> {
                        // Get data by the result.
                        //val playableList = fireworkSDK.updateLista(oldList,newList,adapter.currentVideo)
                        if (adapter.videoList.size > 0) {
                            fireworkSDK.preparePlayableList(adapter.videoList, result.videos, adapter.currentVideo, videoview_viewpager.offscreenPageLimit)
                        } else {

                            if (result.videos.isNotEmpty() && adapter.currentVideo == -1) {
                                adapter.setData(result.videos)
                                Handler().postDelayed({
                                    videoview_viewpager.setCurrentItem(0, false)
                                    fireworkSDK.nowPlayingVideo(0, adapter.videoList[0])
                                }, 16)
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}