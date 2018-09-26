package com.pb.leadmanagement

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.controller.master.MasterController
import com.pb.leadmanagement.core.facade.UserFacade
import com.pb.leadmanagement.home.NavigationActivity
import com.pb.leadmanagement.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        // supportActionBar?.elevation = 6.0f


        //Make Model Variant master service call
        //TYPE 2 : FOUR WHEELER
        if (!UserFacade(this@SplashScreenActivity).isMotorMasterSuccess(2))
            MasterController(this@SplashScreenActivity).getMotorAllMaster("2")

        //TYPE 2 : TWO WHEELER
        if (!UserFacade(this@SplashScreenActivity).isMotorMasterSuccess(4))
            MasterController(this@SplashScreenActivity).getMotorAllMaster("4")


        if (!UserFacade(this@SplashScreenActivity).isInsurerMasterSuccess())
            MasterController(this@SplashScreenActivity).getInsuranceMaster()

        if (!UserFacade(this@SplashScreenActivity).isCityMasterSuccess())
            MasterController(this@SplashScreenActivity).getCityMaster()


        //Initialize the Handler
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            this.finish()

            UserFacade(this).getUser()?.let {
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
            } ?: run {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

        }

    }
}
