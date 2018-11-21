package com.pb.leadmanagement.forgot

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.R
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.controller.authentication.AuthenticationController
import com.pb.leadmanagement.core.response.MotorLeadResponse
import com.pb.leadmanagement.core.response.OTPResponse
import kotlinx.android.synthetic.main.content_forgot.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class ForgotActivity : AppCompatActivity(), View.OnClickListener, IResponseSubcriber {

    lateinit var dialog: AlertDialog
    lateinit var dialogView: View
    lateinit var btnForgot: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initDialog()
        btnForgot = findViewById(R.id.btnForgotPassword)
        btnForgot.setOnClickListener(this)

    }

    private fun hideKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etMobileNo.windowToken, 0)
    }

    private fun showMessage(view: View, message: String, action: String, onClickListener: View.OnClickListener?) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(action, onClickListener).show()
    }

    //region Progress Dialog

    private fun initDialog() {
        val builder = AlertDialog.Builder(this)
        dialogView = layoutInflater.inflate(R.layout.layout_progress_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
    }

    private fun showLoading(message: CharSequence) {
        val msg = dialogView.findViewById<TextView>(R.id.txtProgressTitle)
        msg.text = message


        dialog.show()
    }

    private fun dismissDialog() {

        if (dialog != null && dialog.isShowing) {
            dialog.dismiss()
        }
    }

    //endregion

    private fun otpVerifyDialog(strOTP: String) {

        // Initialize a new instance of
        val builder = AlertDialog.Builder(this@ForgotActivity)

        // Set the alert dialog title
        //builder.setTitle("Verify OTP")

        val healthView = LayoutInflater.from(this).inflate(R.layout.layout_verify_otp, null)

        builder.setView(healthView)

        builder.setCancelable(false)

        var etOTP = healthView.findViewById<TextInputEditText>(R.id.etOTP)

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()


        healthView.findViewById<Button>(R.id.btnVerifyCancel).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dialog.dismiss()
            }
        })
        healthView.findViewById<Button>(R.id.btnVerify).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                if (etOTP.text.toString().contentEquals(strOTP)) {
                    dialog.dismiss()
                    showMessage(healthView, "Verified successfully, Enter new password", "", null)
                    ilPassword.visibility = View.VISIBLE
                    btnForgot.text = "SUBMIT"

                } else {
                    showMessage(healthView, "Invalid OTP", "", null)
                }
            }
        })

        // Display the alert dialog on app interface
        dialog.show()
    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnForgotPassword -> {
                hideKeyBoard()
                if (btnForgot.text.toString().contentEquals("Verify OTP")) {
                    if (etMobileNo.text.toString().length < 10) {
                        showMessage(etMobileNo, "Invalid Mobile No", "", null)
                        return
                    }

                    showLoading("Verify mobile number")
                    AuthenticationController(this@ForgotActivity).verifyOTP(etMobileNo.text.toString(),
                            "1", this@ForgotActivity)


                } else if (btnForgot.text.toString().contentEquals("SUBMIT")) {

                    if (etMobileNo.text.toString().length < 10) {
                        showMessage(etMobileNo, "Invalid Mobile No", "", null)
                        return
                    }


                    showLoading("")
                    AuthenticationController(this@ForgotActivity).forgotPassword(
                            etMobileNo.text.toString(), etPassword.text.toString(), this@ForgotActivity)

                }
            }
        }
    }

    override fun OnSuccess(response: APIResponse?, message: String?) {
        dismissDialog()
        if (response is OTPResponse) {

            if (response?.StatusNo == 0) {
                otpVerifyDialog(response?.Result.OTP)

            }
        } else if (response is MotorLeadResponse) {

            if (response?.StatusNo == 0) {
                showMessage(etMobileNo, response?.Message, "", null)
                finish()
            }
        }
    }

    override fun OnFailure(error: String?) {
        dismissDialog()
        showMessage(etMobileNo, error!!, "", null)
    }
}
