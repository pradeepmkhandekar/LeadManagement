package com.pb.leadmanagement.profile


import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.R
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.controller.authentication.AuthenticationController
import com.pb.leadmanagement.core.controller.master.MasterController
import com.pb.leadmanagement.core.facade.UserFacade
import com.pb.leadmanagement.core.model.LoginEntity
import com.pb.leadmanagement.core.response.LoginResponse
import com.pb.leadmanagement.core.response.PincodeResponse
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(), View.OnClickListener, IResponseSubcriber {

    var loginEntity: LoginEntity? = null
    lateinit var displayView: View
    lateinit var dialog: AlertDialog
    lateinit var dialogView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        displayView = inflater.inflate(R.layout.fragment_profile, container, false)

        initDialog()

        setListener(displayView)

        loginEntity = UserFacade(activity!!)?.getUser()

        bindUserProfile(displayView)

        return displayView
    }


    fun hideKeyBoard() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(displayView.etAddress.windowToken, 0)
    }

    override fun OnSuccess(response: APIResponse?, message: String?) {

        dismissDialog()

        if (response is PincodeResponse) {

            if (response?.MasterData != null) {
                displayView.etState.setText(response?.MasterData.state_name.toLowerCase())
                displayView.etCity.setText(response?.MasterData.cityname.toLowerCase())
                displayView.etLocation.setText(response?.MasterData.postname.toLowerCase())
                displayView.etState.isEnabled = false
                displayView.etCity.isEnabled = false
            } else {
                displayView.etState.isEnabled = true
                displayView.etCity.isEnabled = true
            }
        } else if (response is LoginResponse) {

            if (response.StatusNo == 0) {
                showMessage(etName, response.Message, "", null)

            }
        }
    }

    override fun OnFailure(error: String?) {
        dismissDialog()
        showMessage(displayView.etName, error.toString(), "", null)
    }

    private fun showMessage(view: View, message: String, action: String, onClickListener: View.OnClickListener?) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(action, onClickListener).show()
    }

    private fun setListener(view: View) {
        view.btnUpdate.setOnClickListener(this)
        view.etPincode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 6) {
                    hideKeyBoard()
                    MasterController(view.context).fetchCityState(s.toString(), this@ProfileFragment)
                }
            }
        })
    }

    private fun bindUserProfile(view: View) {

        view.etName.setText(loginEntity?.Name)
        view.etMobileNo.setText(loginEntity?.MobileNo)
        view.etEmail.setText(loginEntity?.EmailID)
        view.etAddress.setText(loginEntity?.Address)
        view.etPincode.setText(loginEntity?.Pincode)
        view.etCity.setText(loginEntity?.City)
        view.etState.setText(loginEntity?.State)
        view.etLocation.setText(loginEntity?.Location)
        view.etChainCode.setText(loginEntity?.ChainCode)

        for (item: String in loginEntity?.LeadInterest!!) {

            if (item.equals("health"))
                view.chkHealth.isChecked = true
            else if (item.equals("life"))
                view.chkLife.isChecked = true
            else if (item.equals("loan"))
                view.chkLoan.isChecked = true
            else if (item.equals("motor"))
                view.chkMotor.isChecked = true
            else if (item.equals("other"))
                view.chkOther.isChecked = true


        }
    }

    fun isValidEMail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.btnUpdate -> {

                //region validation
                if (displayView.etName.text.toString().length < 1) {
                    showMessage(etName, "Invalid full-name", "", null)
                    return
                }

                if (displayView.etMobileNo.text.toString().length < 10) {
                    showMessage(etName, "Invalid Mobile No", "", null)
                    return
                }


                if (!isValidEMail(displayView.etEmail.text.toString())) {
                    showMessage(etName, "Invalid Email-ID", "", null)
                    return
                }

                /* if (etPassword.text.toString().length < 6) {
                     showMessage(etName, "Password minimum 6 character", "", null)
                     return
                 }*/

                if (displayView.etAddress.text.toString().length < 1) {
                    showMessage(etName, "Invalid Address", "", null)
                    return
                }

                if (displayView.etPincode.text.toString().length < 6) {
                    showMessage(etName, "Invalid Pincode", "", null)
                    return
                }

                if (displayView.etCity.text.toString().length < 2) {
                    showMessage(etName, "Invalid City", "", null)
                    etCity.isEnabled = true
                    return
                }

                if (displayView.etState.text.toString().length < 2) {
                    showMessage(etName, "Invalid State", "", null)
                    etState.isEnabled = true
                    return
                }

                if (displayView.etLocation.text.toString().length < 2) {
                    showMessage(etName, "Invalid Location", "", null)
                    etLocation.isEnabled = true
                    return
                }


                if (!displayView.chkHealth.isChecked && !displayView.chkLife.isChecked && !displayView.chkLoan.isChecked
                        && !displayView.chkMotor.isChecked && !displayView.chkOther.isChecked) {
                    showMessage(displayView.etName, "Select Lead Interest", "", null)
                    return
                }

                val leadItems = mutableListOf<String>()

                if (displayView.chkHealth.isChecked)
                    leadItems.add("health")

                if (displayView.chkLife.isChecked)
                    leadItems.add("life")

                if (displayView.chkLoan.isChecked)
                    leadItems.add("loan")

                if (displayView.chkMotor.isChecked)
                    leadItems.add("motor")

                if (displayView.chkOther.isChecked)
                    leadItems.add("other")


                //var chainID = etName.text.toString().take(3) + etMobileNo.text.toString().takeLast(four)

                var loginEntity = LoginEntity(
                        displayView.etAddress.text.toString(),
                        displayView.etChainCode.text.toString(),
                        this.loginEntity!!.ChainID,
                        displayView.etCity.text.toString(),
                        displayView.etEmail.text.toString(),
                        displayView.spFieldManager.selectedItem.toString(),
                        0,
                        leadItems,
                        displayView.etLocation.text.toString(),
                        displayView.etMobileNo.text.toString(),
                        displayView.etName.text.toString(),
                        "",
                        displayView.etPincode.text.toString(),
                        displayView.etState.text.toString()

                )


                //endregion
                showLoading("Profile updating...")
                AuthenticationController(activity!!).updateProfile(loginEntity, this)
            }
        }
    }

    //region Progress Dialog

    private fun initDialog() {
        val builder = AlertDialog.Builder(activity!!)
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
}
