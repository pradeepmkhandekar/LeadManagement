package com.pb.leadmanagement.life

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.R
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.controller.life.LifeController
import com.pb.leadmanagement.core.facade.UserFacade
import com.pb.leadmanagement.core.model.CityMasterEntity
import com.pb.leadmanagement.core.requestentity.LifeLeadRequestEntity
import com.pb.leadmanagement.core.response.MotorLeadResponse
import com.pb.leadmanagement.utility.CityAdapter
import com.pb.leadmanagement.utility.DateTimePicker
import kotlinx.android.synthetic.main.content_life.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class LifeActivity : AppCompatActivity(), View.OnClickListener, IResponseSubcriber {

    internal var simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")

    var acCity: AutoCompleteTextView? = null
    var adapterCity: CityAdapter? = null
    lateinit var dialog: AlertDialog
    lateinit var dialogView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        acCity = findViewById<AutoCompleteTextView>(R.id.etCity)
        acCity?.threshold = 2

        setListener()
        initDialog()

        if (UserFacade(this@LifeActivity).getCity() != null) {
            adapterCity = CityAdapter(this@LifeActivity,
                    R.layout.activity_life, R.id.lbl_name, UserFacade(this@LifeActivity).getCity())
            acCity?.setAdapter(adapterCity)
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

    private fun setListener() {

        etCity.setOnFocusChangeListener(acCityFocusListner)
        btnAddLife.setOnClickListener(this)
        etDOB.setOnClickListener(datePickerDialog)

    }

    protected var datePickerDialog: View.OnClickListener = View.OnClickListener { view ->
        hideKeyBoard()

        //region regdate
        if (view.id == R.id.etDOB) {


            DateTimePicker.showDatePickerDialog(view.context, DatePickerDialog.OnDateSetListener { view1, year, monthOfYear, dayOfMonth ->
                if (view1.isShown) {

                    val calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val currentDay = simpleDateFormat.format(calendar.time)
                    etDOB.setText(currentDay)

                }
            })
        }
    }


    override fun OnSuccess(response: APIResponse?, message: String?) {

        dismissDialog()
        if (response is MotorLeadResponse) {
            if (response.StatusNo == 0) {
                showMessage(etName, response.Message, "", null)
                Handler().postDelayed(Runnable { this!!.finish() }, 1000)
            }
        }
    }

    override fun OnFailure(error: String?) {
        dismissDialog()
        showMessage(etName, error!!, "", null)
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnAddLife -> {

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

                if (etDOB.text.toString().length < 4) {
                    showMessage(etName, "Invalid Date of Birth", "", null)
                    return
                }



                if (etCity.text.toString().length < 2) {
                    showMessage(etName, "Invalid City", "", null)
                    return
                }

                var lifelead = LifeLeadRequestEntity(etName.text.toString(),
                        etMobileNo.text.toString(),
                        etEmail.text.toString(),
                        etDOB.text.toString(),
                        acCity?.text.toString(),
                        spInvestment.selectedItem.toString(),
                        UserFacade(this!!).getChainID(),
                        UserFacade(this!!).getUserID())

                showLoading("Loading...")
                LifeController(this@LifeActivity).addLifeLead(lifelead, this)
            }
        }
    }

    private fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etName.windowToken, 0)
    }

    private fun showMessage(view: View, message: String, action: String, onClickListener: View.OnClickListener?) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(action, onClickListener).show()
    }

    fun isValidEMail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    internal var acCityFocusListner: View.OnFocusChangeListener = View.OnFocusChangeListener { view, b ->
        if (!b) {

            val str = acCity?.getText().toString()

            val listAdapter = acCity?.getAdapter()
            for (i in 0 until listAdapter!!.getCount()) {
                val temp = listAdapter.getItem(i) as CityMasterEntity

                if (str.compareTo(temp.CityName) == 0) {
                    return@OnFocusChangeListener
                }
            }
            acCity?.setText("")
            showMessage(etName, "Invalid City", "", null)

        } else {
            acCity?.setError(null)
        }
    }
}
