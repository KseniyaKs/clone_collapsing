package com.example.blogp.collapsingavatar

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

class Demo3Activity: AppCompatActivity()  {
    private lateinit var ivUserAvatar: ImageView
    private var EXPAND_AVATAR_SIZE: Float = 0F
    private var COLLAPSE_IMAGE_SIZE: Float = 0F
    private var horizontalToolbarAvatarMargin: Float = 0F
    private lateinit var toolbar: Toolbar
    private lateinit var appBarLayout: AppBarLayout
    private var cashCollapseState: Pair<Int, Int>? = null
    private lateinit var titleToolbarText: AppCompatTextView
    private lateinit var titleToolbarTextSingle: AppCompatTextView
    private lateinit var invisibleTextViewWorkAround: AppCompatTextView
    private lateinit var background: FrameLayout
    /**/
    private var avatarAnimateStartPointY: Float = 0F
    private var avatarCollapseAnimationChangeWeight: Float = 0F
    private var isCalculated = false
    private var verticalToolbarAvatarMargin =0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_1)
        /**/
        EXPAND_AVATAR_SIZE = resources.getDimension(R.dimen.default_expanded_image_size)
        COLLAPSE_IMAGE_SIZE = resources.getDimension(R.dimen.default_collapsed_image_size)
        horizontalToolbarAvatarMargin = resources.getDimension(R.dimen.activity_margin)
        /* collapsingAvatarContainer = findViewById(R.id.stuff_container)*/
        appBarLayout = findViewById(R.id.app_bar_layout)
        toolbar = findViewById(R.id.anim_toolbar)
        ivUserAvatar = findViewById(R.id.imgb_avatar_wrap)
        titleToolbarText = findViewById(R.id.tv_profile_name)
        titleToolbarTextSingle = findViewById(R.id.tv_profile_name_single)
        background = findViewById(R.id.fl_background)
        invisibleTextViewWorkAround = findViewById(R.id.tv_workaround)

//        (toolbar.height - COLLAPSE_IMAGE_SIZE) * 2
        /**/
        appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
                if (isCalculated.not()) {

                }
            })
    }
}