package com.pb.leadmanagement.register

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.R
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.controller.authentication.AuthenticationController
import com.pb.leadmanagement.core.controller.master.MasterController
import com.pb.leadmanagement.core.model.LoginEntity
import com.pb.leadmanagement.core.response.PincodeResponse
import com.pb.leadmanagement.core.response.RegisterResponse
import kotlinx.android.synthetic.main.content_register.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener, IResponseSubcriber {

    lateinit var dialog: AlertDialog
    lateinit var dialogView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setListener()
        initDialog()
    }

    private fun setListener() {
        btnRegister.setOnClickListener(this)
        etPincode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 6) {
                    hideKeyBoard()
                    showLoading("Fetching city..")
                    MasterController(this@RegisterActivity).fetchCityState(s.toString(), this@RegisterActivity)
                }
            }
        })
    }

    fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etAddress.windowToken, 0)
    }


    override fun OnSuccess(response: APIResponse?, message: String?) {

        dismissDialog()
        if (response is PincodeResponse) {

            if (response?.MasterData != null) {
                etState.setText(response?.MasterData.state_name.toLowerCase())
                etCity.setText(response?.MasterData.cityname.toLowerCase())
                //etLocation.setText(response?.MasterData.postname.toLowerCase())
                etState.isEnabled = false
                etCity.isEnabled = false
            } else {
                etState.isEnabled = true
                etCity.isEnabled = true
            }
        } else if (response is RegisterResponse) {

            if (response.StatusNo == 0) {
                showMessage(etName, response.Message + " , Re-login with register mobile no.", "", null)
                Handler().postDelayed(mRunnable, 2000)

            }
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

    internal val mRunnable: Runnable = Runnable {
        finish()
    }

    override fun OnFailure(error: String?) {
        dismissDialog()
        showMessage(etName, error.toString(), "", null)
    }

    private fun showMessage(view: View, message: String, action: String, onClickListener: View.OnClickListener?) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(action, onClickListener).show()
    }

    fun isValidEMail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnRegister -> {

                //region validation
                if (etName.text.toString().length < 1) {
                    showMessage(etName, "Invalid full-name", "", null)
                    return
                }

                if (etMobileNo.text.toString().length < 10) {
                    showMessage(etName, "Invalid Mobile No", "", null)
                    return
                }


                if (!isValidEMail(etEmail.text.toString())) {
                    showMessage(etName, "Invalid Email-ID", "", null)
                    return
                }

                if (etPassword.text.toString().length < 6) {
                    showMessage(etName, "Password minimum 6 character", "", null)
                    return
                }

                if (etAddress.text.toString().length < 1) {
                    showMessage(etName, "Invalid Address", "", null)
                    return
                }

                if (etPincode.text.toString().length < 6) {
                    showMessage(etName, "Invalid Pincode", "", null)
                    return
                }

                if (etCity.text.toString().length < 2) {
                    showMessage(etName, "Invalid City", "", null)
                    etCity.isEnabled = true
                    return
                }

                if (etState.text.toString().length < 2) {
                    showMessage(etName, "Invalid State", "", null)
                    etState.isEnabled = true
                    return
                }

                if (etLocation.text.toString().length < 2) {
                    showMessage(etName, "Invalid Location", "", null)
                    etLocation.isEnabled = true
                    return
                }


           /*     if (!chkHealth.isChecked && !chkLife.isChecked && !chkLoan.isChecked
                        && !chkMotor.isChecked && !chkOther.isChecked) {
                    showMessage(etName, "Select Lead Interest", "", null)
                    return
                }*/

                if (etPartnerLogin.text.toString().length < 1) {
                    showMessage(etName, "Invalid Partner Reference ID", "", null)
                    return
                }

                val leadItems = mutableListOf<String>()
                leadItems.add("health")
                leadItems.add("life")
                leadItems.add("loan")
                leadItems.add("motor")
                leadItems.add("other")

             /*   if (chkHealth.isChecked)
                    leadItems.add("health")

                if (chkLife.isChecked)
                    leadItems.add("life")

                if (chkLoan.isChecked)
                    leadItems.add("loan")

                if (chkMotor.isChecked)
                    leadItems.add("motor")

                if (chkOther.isChecked)
                    leadItems.add("other")*/


                var chainID = etName.text.toString().take(3) + etMobileNo.text.toString().takeLast(5)

                var loginEntity = LoginEntity(
                        etAddress.text.toString(),
                        etPartnerLogin.text.toString(),
                        etPartnerLogin.text.toString(),
                        chainID,
                        etCity.text.toString(),
                        etEmail.text.toString(),
                        spFieldManager.selectedItem.toString(),
                        0,
                        leadItems,
                        etLocation.text.toString(),
                        etMobileNo.text.toString(),
                        etName.text.toString(),
                        etPassword.text.toString(),
                        etPincode.text.toString(),
                        etState.text.toString()

                )


                //endregion
                //TODO : one Service hit for register
                showLoading("Loading...")
                AuthenticationController(this@RegisterActivity).register(loginEntity, this)

            }
        }
    }


}
