package com.example.blogp.collapsingavatar


import android.R.attr.x
import android.R.attr.y
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.TextView


class Demo1Activity : AppCompatActivity() {
    private lateinit var ivUserAvatar: TextView
    private var EXPAND_AVATAR_SIZE: Float = 0F
    private var COLLAPSE_IMAGE_SIZE: Float = 0F
    private var horizontalToolbarAvatarMargin: Float = 0F
    private lateinit var toolbar: Toolbar
    private lateinit var appBarLayout: AppBarLayout
    private var cashCollapseState: Pair<Int, Int>? = null
    private lateinit var titleToolbarText: AppCompatTextView
    private lateinit var titleToolbarTextSingle: AppCompatTextView
    private lateinit var invisibleTextViewWorkAround: AppCompatTextView
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var background: FrameLayout
    private lateinit var viewstartsmall: View

    /**/
    private var avatarAnimateStartPointY: Float = 0F
    private var avatarCollapseAnimationChangeWeight: Float = 0F
    private var isCalculated = false
    private var verticalToolbarAvatarMargin = 0F
    private var appBarHeight = 0F
    private var endY = 0F
    private var startY = 0F


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
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar)
        viewstartsmall = findViewById(R.id.view_start_small)

        ivUserAvatar.viewTreeObserver.addOnGlobalLayoutListener( object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val location = IntArray(2)
                ivUserAvatar.getLocationOnScreen(location)
                endY = location[1].toFloat()
                ivUserAvatar.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })

        viewstartsmall.viewTreeObserver.addOnGlobalLayoutListener( object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val location = IntArray(2)
                viewstartsmall.getLocationOnScreen(location)
//                startY = location[1].toFloat()
                viewstartsmall.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })

        toolbar.viewTreeObserver.addOnGlobalLayoutListener( object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val location = IntArray(2)
                toolbar.getLocationOnScreen(location)
                startY = location[1].toFloat()
                toolbar.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })

        (toolbar.height - COLLAPSE_IMAGE_SIZE) * 2
        /**/
        appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
                if (isCalculated.not()) {
                    avatarAnimateStartPointY =
                        Math.abs((appBarLayout.height - (EXPAND_AVATAR_SIZE + horizontalToolbarAvatarMargin)) / appBarLayout.totalScrollRange)
                    avatarCollapseAnimationChangeWeight = 1 / (1 - avatarAnimateStartPointY)
                    verticalToolbarAvatarMargin = (toolbar.height - COLLAPSE_IMAGE_SIZE) * 2
//                        verticalToolbarAvatarMargin = (toolbar.height/2).toFloat()

                    isCalculated = true
                }
                Log.d("qwe appBarLayout.height", appBarLayout.height.toString())
                Log.d("qwe i = ", i.toString())
                Log.d("qwe totalScrollRange", appBarLayout.totalScrollRange.toString())
                Log.d("qwe strartY", avatarAnimateStartPointY.toString())
                Log.d("qwe collapse ", collapsingToolbarLayout.height.toString())
                val center = appBarLayout.totalScrollRange.toFloat() / 2 + i.toFloat()
                val offset = i / appBarLayout.totalScrollRange.toFloat()
                Log.d("qwe offset = = ", offset.toString())


                /**/
                updateViews(Math.abs(i / appBarLayout.totalScrollRange.toFloat()), i)
            })
    }

    fun calculatePercent(distance: Float) : Int {
       return (distance * 100).toInt()
    }

    fun calculateY(currentPercentPath: Int) : Float {

        val xz = appBarLayout.height - (EXPAND_AVATAR_SIZE + horizontalToolbarAvatarMargin)
        val endImg = endY//appBarLayout.height - (EXPAND_AVATAR_SIZE + horizontalToolbarAvatarMargin)
        val startImg = startY //((toolbar.height) / 2).toFloat()

//        if(currentPercentPath < 10) {
//            return endImg
//        }

        val y = (endImg - startImg) * currentPercentPath / 100
        return y
    }

    private fun updateViews(offset: Float, center: Int) {
        /* apply levels changes*/
        when (offset) {
            in 0.15F..1F -> {
                titleToolbarText.apply {
                    if (visibility != View.VISIBLE) visibility = View.VISIBLE
                    alpha = (1 - offset) * 0.35F
                }
            }

            in 0F..0.15F -> {
                titleToolbarText.alpha = (1f)
                ivUserAvatar.alpha = 1f
            }
        }

        /** collapse - expand switch*/
        when {
            offset < SWITCH_BOUND -> Pair(TO_EXPANDED, cashCollapseState?.second ?: WAIT_FOR_SWITCH)
            else -> Pair(TO_COLLAPSED, cashCollapseState?.second ?: WAIT_FOR_SWITCH)
        }.apply {
            when {
                cashCollapseState != null && cashCollapseState != this -> {
                    when (first) {
                        TO_EXPANDED -> {
                            /* set avatar on start position (center of parent frame layout)*/
//                            ivUserAvatar.translationX = 0F
//                            ivUserAvatar.translationY = Math.abs((appBarLayout.height - (EXPAND_AVATAR_SIZE + horizontalToolbarAvatarMargin)) / appBarLayout.totalScrollRange)
                            /**/
                            background.setBackgroundColor(
                                ContextCompat.getColor(
                                    this@Demo1Activity,
                                    R.color.color_transparent
                                )
                            )
                            /* hide top titles on toolbar*/
                            titleToolbarTextSingle.visibility = View.INVISIBLE
                        }
                        TO_COLLAPSED -> background.apply {
                            alpha = 0F
                            setBackgroundColor(
                                ContextCompat.getColor(
                                    this@Demo1Activity,
                                    R.color.colorPrimary
                                )
                            )
                            animate().setDuration(250).alpha(1.0F)

                            /* show titles on toolbar with animation*/
                            titleToolbarTextSingle.apply {
                                visibility = View.VISIBLE
                                alpha = 0F
                                animate().setDuration(500).alpha(1.0f)
                            }
                        }
                    }
                    cashCollapseState = Pair(first, SWITCHED)
                }
                else -> {
                    cashCollapseState = Pair(first, WAIT_FOR_SWITCH)
                }
            }
            Log.d("qwe > avatar = ", (offset > avatarAnimateStartPointY).toString())

            /* Collapse avatar img*/
            ivUserAvatar.apply {
                when {
                    offset > avatarAnimateStartPointY -> {
                        //размер аватарки в свернутом тулбаре
                        val avatarCollapseAnimateOffset =
                            (offset - avatarAnimateStartPointY) * avatarCollapseAnimationChangeWeight
                        val avatarSize =
                            EXPAND_AVATAR_SIZE - (EXPAND_AVATAR_SIZE - COLLAPSE_IMAGE_SIZE) * avatarCollapseAnimateOffset
//                        this.layoutParams.also {
//                            it.height = Math.round(avatarSize)
//                            it.width = Math.round(avatarSize)
//                        }
                        invisibleTextViewWorkAround.setTextSize(TypedValue.COMPLEX_UNIT_PX, offset)

                        translationY = calculateY(calculatePercent(offset))
//                        this.translationX = 0f
//                        this.translationY = ((toolbar.height) / 2).toFloat()
                        this.textSize = 14f
                    }
                    else -> this.layoutParams.also {
                        if (it.height != EXPAND_AVATAR_SIZE.toInt()) {
                            it.height = EXPAND_AVATAR_SIZE.toInt()
                            it.width = EXPAND_AVATAR_SIZE.toInt()
                            this.layoutParams = it
                        }
                        Log.d("qwe yy ==", calculateY(calculatePercent(offset)).toString())
                        translationY = calculateY(calculatePercent(offset))
//                        translationX = 0f
                    }
                }
            }
        }
    }

    companion object {
        const val SWITCH_BOUND = 0.8f
        const val TO_EXPANDED = 0
        const val TO_COLLAPSED = 1
        const val WAIT_FOR_SWITCH = 0
        const val SWITCHED = 1
    }
}