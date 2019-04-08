package com.pb.leadmanagement

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.pb.leadmanagement.core.controller.BaseController
import com.pb.leadmanagement.core.controller.authentication.AuthenticationController
import java.util.*

open class BaseActivity : AppCompatActivity() {

    lateinit var dialog: AlertDialog
    lateinit var dialogView: View
    var basecontroller: BaseController? = null
    private val objectContainer = WeakHashMap<Int, Objects>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDialog()
    }

    fun getService(serviceProvider: ServiceProvider): BaseController? {

        var obj = objectContainer.get(serviceProvider.ordinal)

        when (serviceProvider) {

            ServiceProvider.HEALTH -> {
                basecontroller = AuthenticationController(this)
            }
        }
        return basecontroller
    }

    //region Progress Dialog

    private fun initDialog() {
        val builder = AlertDialog.Builder(this)
        dialogView = layoutInflater.inflate(R.layout.layout_progress_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
    }

    fun showLoading(message: CharSequence) {
        val msg = dialogView.findViewById<TextView>(R.id.txtProgressTitle)
        msg.text = message
        dialog.show()
    }

    fun dismissDialog() {
        if (dialog != null && dialog.isShowing) {
            dialog.dismiss()
        }
    }

    //endregion

    enum class ServiceProvider {
        REGISTRATION,
        MOTOR,
        HEALTH,
        LIFE,
        LEAD
    }
}