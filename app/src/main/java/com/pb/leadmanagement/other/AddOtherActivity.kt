package com.pb.leadmanagement.other

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
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.R
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.controller.other.OtherLeadController
import com.pb.leadmanagement.core.facade.UserFacade
import com.pb.leadmanagement.core.requestentity.OtherRequestEntity
import com.pb.leadmanagement.core.response.MotorLeadResponse
import com.pb.leadmanagement.utility.CountryAdapter
import com.pb.leadmanagement.utility.DateTimePicker
import kotlinx.android.synthetic.main.content_add_other.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class AddOtherActivity : AppCompatActivity(), View.OnClickListener, IResponseSubcriber {

    internal var simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")

    var catNONMOTOR_FIRE_ID: Int = 38
    var catNONMOTOR_MARINE_ID: Int = 47
    var catNONMOTOR_WORKMEN_ID: Int = 72
    var NONMOTOR_PRODUCT: Int = 3

    var TRAVEL_PRODUCT: Int = 12
    var catTRAVEL_INDIVIDUAL_ID: Int = 72
    var catTRAVEL_FLOATER_ID: Int = 72

    lateinit var dialog: AlertDialog
    lateinit var dialogView: View

    var etTravelCountry: AutoCompleteTextView? = null
    var adapterCountry: CountryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_other)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setListener()
        initDialog()
        // spInsurance.setSelection(0)
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

    internal var acMakeFocusListner: View.OnFocusChangeListener = View.OnFocusChangeListener { view, b ->
        if (!b) {

            val str = etTravelCountry?.getText().toString()

            val listAdapter = etTravelCountry?.getAdapter()
            for (i in 0 until listAdapter!!.getCount()) {
                val temp = listAdapter?.getItem(i) as String

                if (str.compareTo(temp) == 0) {
                    return@OnFocusChangeListener
                }
            }
            etTravelCountry?.setText("")
            showMessage(etTravelCountry!!, "Invalid Make", "", null)

        } else {
            etTravelCountry?.setError(null)
        }
    }

    private fun setListener() {

        var country = resources.getStringArray(R.array.array_country);

        etTravelCountry = findViewById<AutoCompleteTextView>(R.id.etTravelCountry)
        etTravelCountry?.threshold = 2
        adapterCountry = CountryAdapter(this@AddOtherActivity, R.layout.activity_add_other,
                R.id.lbl_name, country.toMutableList())
        etTravelCountry?.setAdapter(adapterCountry)


        etTravelCountry?.setOnFocusChangeListener(acMakeFocusListner)

        btnAddOther.setOnClickListener(this)
        etTravelDate.setOnClickListener(datePickerDialog)
        etInsuranceDate.setOnClickListener(datePickerDialog)

        spInsurance?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {

                    llTravel.visibility = View.VISIBLE
                    llOther.visibility = View.GONE
                } else {
                    llTravel.visibility = View.GONE
                    llOther.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spInsuranceType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {

                    etInsuranceDate.visibility = View.GONE
                    etCompanyName.visibility = View.GONE
                } else {
                    etInsuranceDate.visibility = View.VISIBLE
                    etCompanyName.visibility = View.VISIBLE
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

    private fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etName.windowToken, 0)
    }

    protected var datePickerDialog: View.OnClickListener = View.OnClickListener { view ->
        hideKeyBoard()

        //region regdate
        if (view.id == R.id.etTravelDate) {


            DateTimePicker.showTodayOnwardsDatePickerDialog(view.context, DatePickerDialog.OnDateSetListener { view1, year, monthOfYear, dayOfMonth ->
                if (view1.isShown) {

                    val calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val currentDay = simpleDateFormat.format(calendar.time)
                    etTravelDate.setText(currentDay)

                }
            })
        } else if (view.id == R.id.etInsuranceDate) {

            DateTimePicker.showTodayOnwardsDatePickerDialog(view.context,
                    DatePickerDialog.OnDateSetListener { view1, year, monthOfYear, dayOfMonth ->
                        if (view1.isShown) {
                            val calendar = Calendar.getInstance()
                            calendar.set(year, monthOfYear, dayOfMonth)
                            val currentDay = simpleDateFormat.format(calendar.time)
                            etInsuranceDate.setText(currentDay)
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
            R.id.btnAddOther -> {
                if (etName.text.toString().length < 1) {
                    showMessage(etName, "Invalid full-name", "", null)
                    return
                }

                if (etMobileNo.text.toString().length < 10) {
                    showMessage(etName, "Invalid Mobile No", "", null)
                    return
                }

                if (spInsurance.selectedItemPosition == 0) {

                    if (etTravelDate.text.toString().length < 1) {
                        showMessage(etName, "Invalid travel date", "", null)
                        return
                    }

                    if (etTravelCountry?.text.toString().length < 1) {
                        showMessage(etName, "Invalid country name", "", null)
                        return
                    }
                } else {
                    if (spInsuranceType.selectedItemPosition != 0) {
                        if (etInsuranceDate.text.toString().length < 1) {
                            showMessage(etName, "Invalid Insurance date", "", null)
                            return
                        }

                        if (etCompanyName.text.toString().length < 1) {
                            showMessage(etName, "Invalid company name", "", null)
                            return
                        }
                    }

                }

                var productID = 0
                if (spInsurance.selectedItemPosition == 0) {
                    productID = TRAVEL_PRODUCT
                } else {
                    productID = NONMOTOR_PRODUCT
                }

                var catID = 0

                if (spInsurance.selectedItemPosition == 0) {
                    if (spTravelCategory.selectedItemPosition == 0) {
                        catID = catTRAVEL_INDIVIDUAL_ID
                    } else {
                        catID = catTRAVEL_FLOATER_ID
                    }
                } else if (spInsurance.selectedItemPosition == 1) {
                    //fire
                    catID = catNONMOTOR_FIRE_ID
                } else if (spInsurance.selectedItemPosition == 2) {
                    //marine
                    catID = catNONMOTOR_MARINE_ID
                } else if (spInsurance.selectedItemPosition == 3) {
                    //workmen
                    catID = catNONMOTOR_WORKMEN_ID
                }


                var isNew: Boolean = false
                if (spInsuranceType.selectedItemPosition == 0) {
                    isNew = true
                } else {
                    isNew = false
                }


                var otherRequestEntity = OtherRequestEntity(
                        etName.text.toString(),
                        etMobileNo.text.toString(),
                        productID,
                        catID,
                        etTravelDate.text.toString(),
                        etTravelCountry?.text.toString(),
                        isNew,
                        etInsuranceDate.text.toString(),
                        etCompanyName.text.toString(),
                        etCompanyName.text.toString(),
                        UserFacade(this@AddOtherActivity).getChainID(),
                        UserFacade(this@AddOtherActivity).getUserID()
                )

                showLoading("Loading..")
                OtherLeadController(this@AddOtherActivity).addOtherLead(otherRequestEntity, this)
            }
        }
    }
}
