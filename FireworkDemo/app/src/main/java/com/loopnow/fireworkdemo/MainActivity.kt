package com.loopnow.fireworkdemo

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log


import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.loopnow.fireworkdemo.ui.main.SectionsPagerAdapter
import com.loopnow.fireworklibrary.FireworkSDK
import com.loopnow.fireworklibrary.adapters.VideoFeedAdapter
import com.loopnow.fireworklibrary.views.VideoFeedFragment
import com.loopnow.fireworklibrary.views.VideoViewFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        viewPager.offscreenPageLimit = 4
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                sectionsPagerAdapter.setCurrentPage(p0)
            }
        })

        FireworkSDK.addOnItemClickListener(object: VideoFeedAdapter.OnItemClickListener {
            override fun onItemClicked(position: Int) {
            }

            override fun onItemClicked(title: String, id: String, duration: Float) {
            }
        })

        FireworkSDK.addVideoPlaybackTracker(object: FireworkSDK.VideoPlaybackTracker {
            override fun videoWatched(title: String, id: String, duration: Float) {
            }

            override fun nowPlaying(title: String, id: String, duration: Float) {
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        FireworkSDK.addOnItemClickListener(null)

    }



/*
    // use of VideoFeedFragment is deprecated
    val attachedFragmentSet = HashSet<Fragment>()
    // We are keeping the track of all the fragments that are created in FireworkSDK
    override fun onAttachFragment(fragment: Fragment) {
        when(fragment) {
            is VideoFeedFragment, is VideoViewFragment -> {
                attachedFragmentSet.add(fragment)
            }
        }
        super.onAttachFragment(fragment)
    }


    // We are removing and detaching all the FireworkSDK related fragments
    override fun onSaveInstanceState(outState: Bundle) {
        for(fragment in attachedFragmentSet) {
            supportFragmentManager.beginTransaction().remove(fragment).detach(fragment).commitNowAllowingStateLoss()
        }
        attachedFragmentSet.clear()
        super.onSaveInstanceState(outState)
    }
    */

}