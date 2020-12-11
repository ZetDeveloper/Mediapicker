package com.jiren.mediapicker.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import com.jiren.mediapicker.PickerConstant
import com.jiren.mediapicker.fragments.MediaFragment

/**
 * Clase base para administrar los tabs
 */
class TabAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT ) {
    override fun getCount(): Int = 4
    var t1 = MediaFragment.newInstance(PickerConstant.MEDIA_TYPE_IMAGE)
    var t2 = MediaFragment.newInstance(PickerConstant.MEDIA_TYPE_VIDEO)
    var t3 = MediaFragment.newInstance(PickerConstant.MEDIA_TYPE_MUSIC)
    var t4 = MediaFragment.newInstance(PickerConstant.MEDIA_TYPE_OTHERS)

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0-> t1
            1-> t2
            2-> t3
            3-> t4
            else -> {
                t4
            }
        }
    }
}