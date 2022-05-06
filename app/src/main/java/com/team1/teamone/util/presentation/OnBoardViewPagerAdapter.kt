package com.team1.teamone.util.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.team1.teamone.R
import com.team1.teamone.util.model.OnBoardingData

class OnBoardViewPagerAdapter(private var context : Context, private var onBoardingDataList : List<OnBoardingData>) : PagerAdapter(){
    override fun getCount(): Int {
        return onBoardingDataList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.onboarding_screen_layout, null)

        val imageView : ImageView = view.findViewById(R.id.image_onboard)
        val title : TextView = view.findViewById(R.id.tv_title_onboard)
        val desc : TextView = view.findViewById(R.id.tv_desc_onboard)
        val subtitle : TextView = view.findViewById(R.id.tv_subtitle_onboard)

        imageView.setImageResource(onBoardingDataList[position].imageUrl)
        title.text = onBoardingDataList[position].title
        desc.text = onBoardingDataList[position].desc
        subtitle.text = onBoardingDataList[position].subtitle

        container.addView(view)
        return view
    }
}