package com.pb.leadmanagement.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.R
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.controller.authentication.AuthenticationController
import com.pb.leadmanagement.core.requestentity.LoginRequestEntity
import com.pb.leadmanagement.core.response.LoginResponse
import com.pb.leadmanagement.forgot.ForgotActivity
import com.pb.leadmanagement.home.NavigationActivity
import com.pb.leadmanagement.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_login.*

class LoginActivity : AppCompatActivity(), OnClickListener, IResponseSubcriber {

    lateinit var dialog: AlertDialog
    lateinit var dialogView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
        txtSignUp.text = Html.fromHtml(resources.getString(R.string.sign_up))

        listeners()
        initDialog()

    }

    private fun hideKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etMobileNo.windowToken, 0)
    }

    override fun OnSuccess(response: APIResponse?, message: String?) {
        dismissDialog()
        if (response is LoginResponse) {


            showMessage(etMobileNo, response.Message, "", null)

            Handler().postDelayed(Runnable {
                finish()
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
            }, 2000)


        }

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

    override fun OnFailure(error: String?) {
        dismissDialog()
        showMessage(etMobileNo, error!!, "", null)
        //Toast.makeText(applicationContext, errorStatus, Toast.LENGTH_SHORT).show()
    }

    private fun listeners() {
        txtForgotPassword.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        txtSignUp.setOnClickListener(this)
    }

    private fun showMessage(view: View, message: String, action: String, onClickListener: View.OnClickListener?) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(action, onClickListener).show()
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.txtSignUp -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            R.id.txtForgotPassword -> {
                val intent = Intent(this, ForgotActivity::class.java)
                startActivity(intent)
            }

            R.id.btnLogin -> {
                if (etMobileNo.text.length < 10) {
                    showMessage(etMobileNo, "Invalid Mobile No.", "", null)
                    //Toast.makeText(applicationContext, "Invalid Mobile No.", Toast.LENGTH_SHORT).show()
                    return
                }


                if (etPassword.text.length < 6) {
                    showMessage(etMobileNo, "Invalid Password", "", null)
                    // Toast.makeText(applicationContext, "Invalid Password", Toast.LENGTH_SHORT).show()
                    return
                }

                hideKeyBoard()

                var loginRequestEntity = LoginRequestEntity(etMobileNo.text.toString(),
                        etPassword.text.toString())

                showLoading("Loading..")
                AuthenticationController(this).login(loginRequestEntity, this)


            }
        }
    }
}
