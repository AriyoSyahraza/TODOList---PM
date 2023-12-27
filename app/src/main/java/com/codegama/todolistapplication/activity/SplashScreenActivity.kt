package com.codegama.todolistapplication.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.codegama.todolistapplication.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        setAnimation()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        val SPLASH_TIME_OUT = 6000
        Handler().postDelayed({
            val i = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }

    private fun setAnimation() {
        val scaleXAnimation =
            ObjectAnimator.ofFloat(findViewById(R.id.tvBottomText), "scaleX", 5.0f, 1.0f)
        scaleXAnimation.interpolator = AccelerateDecelerateInterpolator()
        scaleXAnimation.duration = 2000
        val scaleYAnimation =
            ObjectAnimator.ofFloat(findViewById(R.id.tvBottomText), "scaleY", 5.0f, 1.0f)
        scaleYAnimation.interpolator = AccelerateDecelerateInterpolator()
        scaleYAnimation.duration = 2000
        val alphaAnimation =
            ObjectAnimator.ofFloat(findViewById(R.id.tvBottomText), "alpha", 0.0f, 1.0f)
        alphaAnimation.interpolator = AccelerateDecelerateInterpolator()
        alphaAnimation.duration = 2000
        val animatorSet = AnimatorSet()
        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation)
        animatorSet.startDelay = 2000
        animatorSet.start()
        findViewById<View>(R.id.ivImageLogo).alpha = 1.0f
        val anim = AnimationUtils.loadAnimation(this, R.anim.fade)
        findViewById<View>(R.id.ivImageLogo).startAnimation(anim)
    }
}