package com.pb.leadmanagement

import android.app.Application

/**
 * Created by Nilesh Birhade on 01-10-2018.
 */
class LeadApplication : Application() {

    companion object {
        lateinit var instance: com.pb.leadmanagement.LeadApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }
}