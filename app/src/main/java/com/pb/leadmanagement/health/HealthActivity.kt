package com.pb.leadmanagement.health

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.R
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.controller.health.HealthController
import com.pb.leadmanagement.core.facade.UserFacade
import com.pb.leadmanagement.core.model.CityMasterEntity
import com.pb.leadmanagement.core.model.InsuranceCompanyMasterEntity
import com.pb.leadmanagement.core.requestentity.HealthLeadRequestEntity
import com.pb.leadmanagement.core.response.MakeX
import com.pb.leadmanagement.core.response.MotorLeadResponse
import com.pb.leadmanagement.upload.UploadImageActivity
import com.pb.leadmanagement.utility.CityAdapter
import com.pb.leadmanagement.utility.DateTimePicker
import kotlinx.android.synthetic.main.content_health.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class HealthActivity : AppCompatActivity(), View.OnClickListener, IResponseSubcriber {

    internal var simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
    var insurerID: Int = 0
    var acCity: AutoCompleteTextView? = null
    var adapterCity: CityAdapter? = null

    lateinit var dialog: AlertDialog
    lateinit var dialogView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        acCity = findViewById<AutoCompleteTextView>(R.id.etCity)
        acCity?.threshold = 2




        setListener()
        initDialog()

        if (UserFacade(this@HealthActivity).getCity() != null) {
            adapterCity = CityAdapter(this@HealthActivity,
                    R.layout.activity_add_motor_lead, R.id.lbl_name, UserFacade(this@HealthActivity).getCity())
            acCity?.setAdapter(adapterCity)
        }


    }

    private fun uploadImageDialog(leadID: Int) {
        // Initialize a new instance of
        val builder = AlertDialog.Builder(this@HealthActivity)

        // Set the alert dialog title
        builder.setTitle("Upload Document")

        // Display a message on alert dialog
        builder.setMessage("Lead genarated successfully.! Do you want to upload documents?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Upload") { dialog, which ->

            if (UserFacade(this@HealthActivity).getUserID() != 0) {

                val intent = Intent(this, UploadImageActivity::class.java)
                intent.putExtra("LEAD_ID", leadID)
                intent.putExtra("FROM", "HEALTH")
                startActivity(intent)
            }
        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("CANCEL") { dialog, which ->
            dialog.dismiss()
            Handler().postDelayed(Runnable { this!!.finish() }, 500)
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

    private fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etName.windowToken, 0)
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
        } else if (view.id == R.id.etPolicyExpiry) {

            DateTimePicker.policyExpValidation(view.context, Date(),
                    DatePickerDialog.OnDateSetListener { view1, year, monthOfYear, dayOfMonth ->
                        if (view1.isShown) {
                            val calendar = Calendar.getInstance()
                            calendar.set(year, monthOfYear, dayOfMonth)
                            val currentDay = simpleDateFormat.format(calendar.time)
                            etPolicyExpiry.setText(currentDay)
                        }
                    })
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

    override fun OnSuccess(response: APIResponse?, message: String?) {


        dismissDialog()
        if (response is MotorLeadResponse) {
            if (response.StatusNo == 0) {

                if (spPolicyIs.selectedItemPosition > 0) {
                    uploadImageDialog(response.Result.LeadID)
                } else {
                    showMessage(etName, response.Message, "", null)
                    Handler().postDelayed(Runnable { this!!.finish() }, 1000)
                }
            }
        }
    }

    override fun OnFailure(error: String?) {
        dismissDialog()
        showMessage(etName, error!!, "", null)
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

    private fun setListener() {

        btnAddHealth.setOnClickListener(this)
        etPolicyExpiry.setOnClickListener(datePickerDialog)
        etDOB.setOnClickListener(datePickerDialog)
        etCity.setOnFocusChangeListener(acCityFocusListner)

        spInsurer?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // if (position == 0) {
                //     showMessage(spInsurer, "Invalid Insurance company", "", null)
                // } else {
                var insurer = spInsurer.adapter.getItem(position) as InsuranceCompanyMasterEntity
                insurerID = insurer.InsCompanyID
                // }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spExistingDisease?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 4) {
                    etOtherDisease.visibility = View.VISIBLE
                } else {
                    etOtherDisease.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spPolicyIs?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    etPolicyExpiry.visibility = View.GONE
                    spInsurer.visibility = View.GONE
                    txtInsurer.visibility = View.GONE
                } else {
                    etPolicyExpiry.visibility = View.VISIBLE
                    spInsurer.visibility = View.VISIBLE
                    txtInsurer.visibility = View.VISIBLE

                    var insurerList = UserFacade(this@HealthActivity).getInsuranceList()
                    var spinnerAdapter = InsurerAdapter(this@HealthActivity, insurerList!!)
                    spInsurer?.adapter = spinnerAdapter

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
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
            R.id.btnAddHealth -> {

                if (etName.text.toString().length < 1) {
                    showMessage(etName, "Invalid full-name", "", null)
                    return
                }

                if (etMobileNo.text.toString().length < 10) {
                    showMessage(etName, "Invalid Mobile No", "", null)
                    return
                }

                if (etDOB.text.toString().length < 4) {
                    showMessage(etName, "Invalid Date of Birth", "", null)
                    return
                }

                if (!isValidEMail(etEmail.text.toString())) {
                    showMessage(etName, "Invalid Email-ID", "", null)
                    return
                }

                if (etCity.text.toString().length < 2) {
                    showMessage(etName, "Invalid City", "", null)
                    return
                }



                if (spPolicyIs.selectedItemPosition == 1) {


                    if (etPolicyExpiry.text.toString().length < 4) {
                        showMessage(etName, "Required policy expiry date", "", null)
                        return
                    }
                } else {
                    insurerID = 0
                }

                if (spExistingDisease.selectedItemPosition == 4) {
                    if (etOtherDisease.text.toString().length < 1) {
                        showMessage(etName, "Required existing disease name", "", null)
                        return
                    }
                }

                var healthLeadRequestEntity = HealthLeadRequestEntity(etName.text.toString(),
                        etMobileNo.text.toString(),
                        etEmail.text.toString(),
                        etDOB.text.toString(),
                        etCity.text.toString(),
                        spPolicyIs.selectedItem.toString(),
                        spPolicyType.selectedItem.toString(),
                        etPolicyExpiry.text.toString(),
                        spExistingDisease.selectedItem.toString(),
                        etOtherDisease.text.toString(),
                        insurerID.toString(),
                        UserFacade(this@HealthActivity).getChainID(),
                        UserFacade(this@HealthActivity).getUserID()
                )

                showLoading("Loading..")
                HealthController(this@HealthActivity).addHealthLead(healthLeadRequestEntity, this)
            }
        }
    }

}
