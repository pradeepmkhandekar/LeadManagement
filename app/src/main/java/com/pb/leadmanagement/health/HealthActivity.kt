package com.pb.leadmanagement.health

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
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
    var acInsurer: AutoCompleteTextView? = null
    var adapterCity: CityAdapter? = null

    lateinit var dialog: AlertDialog
    lateinit var dialogView: View

    var CityID: Int = 0

    var adapterInsurance: InsuranceCompanyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        acCity = findViewById<AutoCompleteTextView>(R.id.etCity)
        acCity?.threshold = 2


        acInsurer = findViewById<AutoCompleteTextView>(R.id.etInsurer)
        acInsurer?.threshold = 1

        var insurerList = UserFacade(this@HealthActivity).getInsuranceList()
        adapterInsurance = InsuranceCompanyAdapter(this@HealthActivity, R.layout.activity_health, R.id.lbl_name, insurerList);
        acInsurer?.setAdapter(adapterInsurance)

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
        builder.setTitle("SAVED!")

        // Display a message on alert dialog
        builder.setMessage("Do you want to upload any document?")

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
        builder.setNegativeButton("No") { dialog, which ->
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

    private var datePickerDialog: View.OnClickListener = View.OnClickListener { view ->
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
                    showMessage(etName, response.Message, "", null)
                    Handler().postDelayed(Runnable { uploadImageDialog(response.Result.LeadID) }, 2000)

                } else {
                    showMessage(etName, response.Message, "", null)
                    Handler().postDelayed(Runnable { this!!.finish() }, 2000)
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
        // etCity.setOnFocusChangeListener(acCityFocusListner)

        acCity?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            val selectedCity = adapterCity!!.getItem(position) as CityMasterEntity
            CityID = selectedCity.CityID
            acCity?.setError(null)
            hideKeyBoard()
        }

        acCity?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                val str = acCity?.getText().toString()

                val listAdapter = acCity?.getAdapter()

                Log.d("CITY", "" + listAdapter?.count)

                if (listAdapter != null) {
                    for (i in 0 until listAdapter.getCount()) {
                        val temp = listAdapter.getItem(i).toString()
                        if (str.compareTo(temp) == 0) {
                            acCity?.setError(null)
                            return
                        }
                    }

                    acCity?.setError("Invalid City")
                    CityID = 0
                    acCity?.setFocusable(true)
                }

            }

        })

        etInsurer?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            val makeData = adapterInsurance!!.getItem(position) as InsuranceCompanyMasterEntity
            insurerID = makeData.InsCompanyID
            hideKeyBoard()
        }


        spExistingDisease?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 4) {
                    ilOtherDisease.visibility = View.VISIBLE
                } else {
                    ilOtherDisease.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spPolicyIs?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    etPolicyExpiry.visibility = View.GONE
                    etInsurer.visibility = View.GONE
                } else {
                    etPolicyExpiry.visibility = View.VISIBLE
                    etInsurer.visibility = View.VISIBLE
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

    private fun isValidEMail(email: String): Boolean {
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

                if (etCity.text.toString().length < 2 || CityID == 0) {
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
                        UserFacade(this@HealthActivity).getReferenceCode(),
                        UserFacade(this@HealthActivity).getUserID(),
                        etRemark.text.toString()
                )

                confirmationAlert(healthLeadRequestEntity)


            }
        }
    }

    private fun confirmationAlert(healthLeadRequestEntity: HealthLeadRequestEntity) {

        // Initialize a new instance of
        val builder = AlertDialog.Builder(this@HealthActivity)

        // Set the alert dialog title
        builder.setTitle("Create Lead")

        val healthView = LayoutInflater.from(this).inflate(R.layout.layout_health_confirm, null)

        builder.setView(healthView)

        healthView.findViewById<TextView>(R.id.txtName).setText("" + healthLeadRequestEntity.Name)
        healthView.findViewById<TextView>(R.id.txtMobile).setText("" + healthLeadRequestEntity.MobileNo)
        healthView.findViewById<TextView>(R.id.txtEmail).setText("" + healthLeadRequestEntity.EmailID)
        healthView.findViewById<TextView>(R.id.txtCity).setText("" + healthLeadRequestEntity.City)
        healthView.findViewById<TextView>(R.id.txtPolicyIs).setText("" + healthLeadRequestEntity.ProposalType)
        healthView.findViewById<TextView>(R.id.txtPolicyType).setText("" + healthLeadRequestEntity.Category)
        healthView.findViewById<TextView>(R.id.txtPolicyExpiry).setText("" + if (healthLeadRequestEntity.PolicyExpiryDate.length > 0) "${healthLeadRequestEntity.PolicyExpiryDate}" else "Not applicable")
        healthView.findViewById<TextView>(R.id.txtExistingDisease).setText("" + healthLeadRequestEntity.ExistingDisease)
        healthView.findViewById<TextView>(R.id.txtInsuranceName).setText("" + UserFacade(this).getInsuranceName(healthLeadRequestEntity.CurrentYearInsCmpID.toInt()))


        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Save") { dialog, which ->

            dialog.dismiss()
            showLoading("Loading..")
            HealthController(this@HealthActivity).addHealthLead(healthLeadRequestEntity, this)

        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("Edit") { dialog, which ->
            dialog.dismiss()

        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

}
