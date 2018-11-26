package com.pb.leadmanagement.loan

import android.app.DatePickerDialog
import android.content.Context
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
import com.google.gson.Gson
import com.pb.leadmanagement.R
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.controller.loan.LoanLeadController
import com.pb.leadmanagement.core.facade.UserFacade
import com.pb.leadmanagement.core.model.CityMasterEntity
import com.pb.leadmanagement.core.requestentity.LoanRequestEntity
import com.pb.leadmanagement.core.requestentity.LoanRequestModifiedEntity
import com.pb.leadmanagement.core.response.MakeX
import com.pb.leadmanagement.core.response.ModelX
import com.pb.leadmanagement.core.response.MotorLeadResponse
import com.pb.leadmanagement.motor.MakeAdapter
import com.pb.leadmanagement.motor.ModelAdapter
import com.pb.leadmanagement.utility.CityAdapter
import com.pb.leadmanagement.utility.DateTimePicker
import kotlinx.android.synthetic.main.content_loan.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class LoanActivity : AppCompatActivity(), View.OnClickListener, IResponseSubcriber {

    internal var simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
    lateinit var dialog: AlertDialog
    lateinit var dialogView: View

    var acCity: AutoCompleteTextView? = null
    var adapterCity: CityAdapter? = null
    var CityID: Int = 0

    var etMakeSearch: AutoCompleteTextView? = null
    var etModelSearch: AutoCompleteTextView? = null

    var adapterMake: MakeAdapter? = null
    var adapterModel: ModelAdapter? = null

    var MakeID: Int = 0
    var ModelID: Int = 0

    var mListMake: List<MakeX>? = null
    var mListModel: List<ModelX>? = null
    var vehicleTypeID: Int = 2 //four wheeler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        acCity = findViewById<AutoCompleteTextView>(R.id.etLoanCity)
        acCity?.threshold = 2

        etMakeSearch = findViewById<AutoCompleteTextView>(R.id.etMake)
        etModelSearch = findViewById<AutoCompleteTextView>(R.id.etModel)

        etMakeSearch?.threshold = 2
        etModelSearch?.threshold = 1

        setListener()
        initDialog()





        if (UserFacade(this@LoanActivity).getCity() != null) {
            adapterCity = CityAdapter(this@LoanActivity,
                    R.layout.activity_life, R.id.lbl_name, UserFacade(this@LoanActivity).getCity())
            acCity?.setAdapter(adapterCity)
        }


        visibleProduct(0)
    }

    private fun setListener() {
        etDOB.setOnClickListener(datePickerDialog)
        etPlEarliestDate.setOnClickListener(datePickerDialog)
        etDOBMfg.setOnClickListener(datePickerDialog)


        btnAddLoan.setOnClickListener(this)

        /* spVehicleType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
             override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                 etMakeSearch?.setText("")
                 etModelSearch?.setText("")


                 //fetch all new data again
                 mListMake = emptyList()
                 mListModel = emptyList()


                 if (position == 0) {
                     vehicleTypeID = 2
                     mListMake = UserFacade(this@LoanActivity).getFourWheelerMaster(2) //four wheeler
                 } else if (position == 1) {
                     vehicleTypeID = 4
                     mListMake = UserFacade(this@LoanActivity).getTwoWheelerMaster(4)//two wheeler
                 }
                 changeMakeAdapter()
             }

             override fun onNothingSelected(parent: AdapterView<*>?) {

             }
         }*/

        chkExistingLoan.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                tlCBLLoanAmount.visibility = View.VISIBLE
            } else {
                tlCBLLoanAmount.visibility = View.GONE
            }
        }

        spLoanProduct?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                visibleProduct(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        acCity?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            val selectedCity = adapterCity!!.getItem(position) as CityMasterEntity
            acCity?.setText(selectedCity.CityName)
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


        etMake.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                val str = etMake.getText().toString()

                val listAdapter = etMake.getAdapter()

                if (listAdapter != null) {
                    for (i in 0 until listAdapter.getCount()) {
                        val temp = listAdapter.getItem(i).toString()
                        if (str.compareTo(temp) == 0) {
                            etMake.setError(null)
                            return
                        }
                    }

                    etMake.setError("Invalid Make")
                    MakeID = 0
                    etMake.setFocusable(true)
                }
            }
        })

        etModel.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                val str = etModel.getText().toString()

                val listAdapter = etModel.getAdapter()

                if (listAdapter != null) {
                    for (i in 0 until listAdapter.getCount()) {
                        val temp = listAdapter.getItem(i).toString()
                        if (str.compareTo(temp) == 0) {
                            etModel.setError(null)
                            return
                        }
                    }

                    etModel.setError("Invalid Make")
                    ModelID = 0
                    etModel.setFocusable(true)
                }
            }
        })

        etMakeSearch?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            val makeData = adapterMake!!.getItem(position) as MakeX
            MakeID = makeData.MakeID
            mListModel = makeData.Model
            etMakeSearch?.setError(null)
            etModelSearch?.setText("")
            changeModelAdapter()
            hideKeyBoard()
        }

        etModelSearch?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            if (adapterModel!!.getItem(position) != null) {
                ModelID = adapterModel!!.getItem(position)!!.ModelID
                etModel.setError(null)
            }

            /* if (adapterModel!!.getItem(position)?.Variant != null) {

                 val variantList = adapterModel!!.getItem(position)!!.Variant as List<Variant>
                 if (variantList != null) {
                     hideKeyBoard()
                     *//*var spinnerAdapter = VariantAdapter(this!!, variantList)
                    spVariant?.adapter = spinnerAdapter*//*
                }
            } else {
                //spVariant?.visibility = View.GONE
            }*/
        }


    }

    private fun changeModelAdapter() {
        adapterModel = ModelAdapter(this@LoanActivity,
                R.layout.activity_add_motor_lead, R.id.lbl_name, mListModel)
        etModelSearch?.setAdapter(adapterModel)
    }

    private fun changeMakeAdapter() {
        if (mListMake != null) {
            adapterMake = MakeAdapter(this@LoanActivity,
                    R.layout.activity_add_motor_lead, R.id.lbl_name, mListMake)
            etMakeSearch?.setAdapter(adapterMake)
        }
    }

    fun visibleProduct(position: Int) {

        when (position) {
            0 -> {
                llBusinessLoan.visibility = View.GONE
                llHomeLAP.visibility = View.GONE
                llPersonalLoan.visibility = View.GONE
                llCarLoan.visibility = View.GONE
                llCreditcard.visibility = View.GONE
                //showMessage(spLoanProduct, "Select Loan Product", "", null)
            }

            1 -> {
                llBusinessLoan.visibility = View.VISIBLE
                llHomeLAP.visibility = View.GONE
                llPersonalLoan.visibility = View.GONE
                llCarLoan.visibility = View.GONE
                llCreditcard.visibility = View.GONE
            }

            2 -> {
                llBusinessLoan.visibility = View.GONE
                llHomeLAP.visibility = View.VISIBLE
                llPersonalLoan.visibility = View.GONE
                llCarLoan.visibility = View.GONE
                llCreditcard.visibility = View.GONE
            }

            3 -> {
                llBusinessLoan.visibility = View.GONE
                llHomeLAP.visibility = View.GONE
                llPersonalLoan.visibility = View.VISIBLE
                llCarLoan.visibility = View.GONE
                llCreditcard.visibility = View.GONE
            }
            4 -> {
                llBusinessLoan.visibility = View.GONE
                llHomeLAP.visibility = View.GONE
                llPersonalLoan.visibility = View.GONE
                llCarLoan.visibility = View.VISIBLE
                llCreditcard.visibility = View.GONE

                mListMake = UserFacade(this@LoanActivity).getFourWheelerMaster(vehicleTypeID) //four wheeler
                changeMakeAdapter()
            }

            5 -> {
                llBusinessLoan.visibility = View.GONE
                llHomeLAP.visibility = View.GONE
                llPersonalLoan.visibility = View.GONE
                llCarLoan.visibility = View.GONE
                llCreditcard.visibility = View.VISIBLE
            }


        }


    }

    private fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etName.windowToken, 0)
    }

    fun isValidEMail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showMessage(view: View, message: String, action: String, onClickListener: View.OnClickListener?) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(action, onClickListener).show()
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

    private fun validateInputs(position: Int): Boolean {

        when (position) {

        //Business loan validation
            1 -> {

                if (etBLLoanAmount.text.toString().isEmpty()) {
                    showMessage(etName, "Invalid Amount", "", null)
                    return false
                }

                if (chkExistingLoan.isChecked) {
                    if (etCurrentBLLoanAmount.text.toString().isEmpty()) {
                        showMessage(etName, "Invalid Existing Loan Amount", "", null)
                        return false
                    }
                }

            }

            2 -> {

                if (etHmLoanAmount.text.toString().length < 1) {
                    showMessage(etName, "Invalid Amount", "", null)
                    return false
                }


            }

            3 -> {

                if (etPersonalAmount.text.toString().isEmpty()) {
                    showMessage(etName, "Invalid Amount", "", null)
                    return false
                }

                if (etPlEarliestDate.text.toString().isEmpty()) {
                    showMessage(etName, "Invalid Loan Dispatch date", "", null)
                    return false
                }


            }
            4 -> {
                if (etMake.text.toString().isEmpty() || MakeID == 0) {
                    showMessage(etName, "Invalid Make", "", null)
                    return false
                }

                if (etModel.text.toString().isEmpty() || ModelID == 0) {
                    showMessage(etName, "Invalid Model", "", null)
                    return false
                }

                if (etDOBMfg.text.toString().isEmpty()) {
                    showMessage(etName, "Invalid Manufactur date", "", null)
                    return false
                }
            }

        }

        return true

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAddLoan -> {
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

                /*    if (etDOB.text.toString().length < 4) {
                        showMessage(etName, "Invalid Date of Birth", "", null)
                        return
                    }*/

                if (spLoanProduct.selectedItemPosition == 0) {
                    showMessage(etName, "Select Loan Product", "", null)
                    return
                }

                if (validateInputs(spLoanProduct.selectedItemPosition)) {


                    val loanRequestModifiedEntity = prepareLoanRequest();

                    confirmationAlert(loanRequestModifiedEntity!!)

                    //  showLoading("Loading..")
                    //  LoanLeadController(this@LoanActivity).addLoanLead(loanRequestModifiedEntity!!, this)
                }
            }
        }
    }


    private fun confirmationAlert(motorLeadRequestEntity: LoanRequestModifiedEntity) {

        // Initialize a new instance of
        val builder = AlertDialog.Builder(this@LoanActivity)

        // Set the alert dialog title
        builder.setTitle("Create Lead")

        val healthView = LayoutInflater.from(this).inflate(R.layout.layout_loan_confirm, null)

        builder.setView(healthView)

        healthView.findViewById<TextView>(R.id.txtName).setText("" + motorLeadRequestEntity.Name)
        healthView.findViewById<TextView>(R.id.txtMobile).setText("" + motorLeadRequestEntity.MobileNo)
        healthView.findViewById<TextView>(R.id.txtProduct).setText("" + motorLeadRequestEntity.Products)

        healthView.findViewById<TextView>(R.id.txtBusinessLoanAmount).setText("" + if (motorLeadRequestEntity.BusinessLoanAmount > 0) "${motorLeadRequestEntity.BusinessLoanAmount.toString()}" else "Not applicable")
        healthView.findViewById<TextView>(R.id.isExistingBusinessLoan).setText("" + if (motorLeadRequestEntity.isBusinessExistingLoan) "${motorLeadRequestEntity.isBusinessExistingLoan}" else "No")
        healthView.findViewById<TextView>(R.id.txtExBusinessLoanAmount).setText("" + if (motorLeadRequestEntity.isBusinessExistingLoan) "${motorLeadRequestEntity.ExistingBusinessLoanAmount.toString()}" else "0")

        healthView.findViewById<TextView>(R.id.txtHomeLoanAmount).setText("" + motorLeadRequestEntity.HomeLoanAmount.toString());//if (healthLeadRequestEntity.PolicyExpiryDate.length > 0) "${healthLeadRequestEntity.PolicyExpiryDate}" else "Not applicable")
        healthView.findViewById<TextView>(R.id.txtHomeLoanCity).setText("" + motorLeadRequestEntity.HomeLoanCityName)
        healthView.findViewById<TextView>(R.id.txtHomeLoanType).setText("" + motorLeadRequestEntity.HomeLoantype)



        healthView.findViewById<TextView>(R.id.txtPLAmount).setText("" + motorLeadRequestEntity.PLAmount.toString())
        healthView.findViewById<TextView>(R.id.txtPLEmployeeType).setText("" + motorLeadRequestEntity.PLEmployeeType)
        healthView.findViewById<TextView>(R.id.txtPLDispatchDate).setText("" + motorLeadRequestEntity.PLDispatchDate)


        healthView.findViewById<TextView>(R.id.txtCarType).setText("" + motorLeadRequestEntity.CarType)

        var motorName = UserFacade(this@LoanActivity).getVehicleName(vehicleTypeID, motorLeadRequestEntity.CarMakeID.toInt(), motorLeadRequestEntity.CarModelID.toInt(), 0)

        healthView.findViewById<TextView>(R.id.txtCar).setText("" + motorName)
        healthView.findViewById<TextView>(R.id.txtCarMfgDate).setText("" + motorLeadRequestEntity.CarMfgDate)


        healthView.findViewById<TextView>(R.id.txtCCEmployeeType).setText("" + motorLeadRequestEntity.CCEmployeeType)
        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Save") { dialog, which ->

            dialog.dismiss()
            showLoading("Loading..")
            LoanLeadController(this@LoanActivity).addLoanLead(motorLeadRequestEntity!!, this)

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


    private fun prepareLoanRequest(): LoanRequestModifiedEntity? {

        var name = etName.text.toString()
        var mobile = etMobileNo.text.toString()
        var product = spLoanProduct.selectedItem.toString()
        var refCode = UserFacade(this@LoanActivity).getReferenceCode()
        var userID = UserFacade(this@LoanActivity).getUserID()
        var remark = etRemark.text.toString()


        //Business inputs
        var blLoanAmount: Long = 0
        var isExistingLoan = false
        var currentBLLoanAmount: Long = 0

        //Home loan
        var hmLoanAmount: Long = 0
        var cityName = ""
        var homeType = ""

        //Personal Loan
        var personalLoanAmount: Long = 0
        var employeeType = ""
        var plDispatchDate = ""

        //car loan
        var make = ""
        var model = ""
        var mfgDate = ""
        var carType = ""

        //credit card
        var ccEmployeeType = ""



        if (spLoanProduct.selectedItemPosition == 1) { //Business Loan
            blLoanAmount = etBLLoanAmount.text.toString().toLong()
            isExistingLoan = chkExistingLoan.isChecked

            if (chkExistingLoan.isChecked) currentBLLoanAmount = etCurrentBLLoanAmount.text.toString().toLong() else currentBLLoanAmount = 0

            //others empty
            hmLoanAmount = 0
            cityName = ""
            homeType = ""
            personalLoanAmount = 0
            employeeType = ""
            plDispatchDate = ""
            make = ""
            model = ""
            mfgDate = ""
            carType = ""
            ccEmployeeType = ""

        } else if (spLoanProduct.selectedItemPosition == 2) { //Home loan

            hmLoanAmount = etHmLoanAmount.text.toString().toLong()
            cityName = etLoanCity.text.toString()
            homeType = spHmbgflat.selectedItem.toString()

            //others empty
            blLoanAmount = 0
            isExistingLoan = false
            currentBLLoanAmount = 0
            personalLoanAmount = 0
            employeeType = ""
            plDispatchDate = ""
            carType = ""
            make = ""
            model = ""
            mfgDate = ""
            ccEmployeeType = ""

        } else if (spLoanProduct.selectedItemPosition == 3) { //Personal Loan

            personalLoanAmount = etPersonalAmount.text.toString().toLong()
            employeeType = spSalaried.selectedItem.toString()
            plDispatchDate = etPlEarliestDate.text.toString()


            //others empty
            hmLoanAmount = 0
            cityName = ""
            homeType = ""
            blLoanAmount = 0
            isExistingLoan = false
            currentBLLoanAmount = 0
            make = ""
            model = ""
            mfgDate = ""
            carType = ""
            ccEmployeeType = ""

        } else if (spLoanProduct.selectedItemPosition == 4) {

            make = MakeID.toString() // etMake.text.toString()
            model = ModelID.toString() // etModel.text.toString()
            mfgDate = etDOBMfg.text.toString()
            carType = spVehicleType.selectedItem.toString()


            //others empty
            personalLoanAmount = 0
            employeeType = ""
            plDispatchDate = ""
            hmLoanAmount = 0
            cityName = ""
            homeType = ""
            blLoanAmount = 0
            isExistingLoan = false
            currentBLLoanAmount = 0
            ccEmployeeType = ""

        } else if (spLoanProduct.selectedItemPosition == 5) {
            ccEmployeeType = spCCSalaried.selectedItem.toString()


            //others empty
            carType = ""
            make = ""
            model = ""
            mfgDate = ""
            personalLoanAmount = 0
            employeeType = ""
            plDispatchDate = ""
            hmLoanAmount = 0
            cityName = ""
            homeType = ""
            blLoanAmount = 0
            isExistingLoan = false
            currentBLLoanAmount = 0
        }


        var LoanRequestModifiedEntity = LoanRequestModifiedEntity(
                name,
                mobile,
                product,
                blLoanAmount,
                isExistingLoan,
                currentBLLoanAmount,
                hmLoanAmount,
                cityName,
                homeType,
                personalLoanAmount,
                employeeType,
                plDispatchDate,
                carType,
                make,
                model,
                mfgDate,
                ccEmployeeType,
                refCode,
                userID, remark
        )



        return LoanRequestModifiedEntity
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
        } else if (view.id == R.id.etPlEarliestDate) {

            DateTimePicker.showTodayOnwardsDatePickerDialog(view.context, DatePickerDialog.OnDateSetListener { view1, year, monthOfYear, dayOfMonth ->
                if (view1.isShown) {

                    val calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val currentDay = simpleDateFormat.format(calendar.time)
                    etPlEarliestDate.setText(currentDay)

                }
            })
        } else if (view.id == R.id.etDOBMfg) {

            DateTimePicker.mfgYearMonthValidation(view.context, Date(), DatePickerDialog.OnDateSetListener { view1, year, monthOfYear, dayOfMonth ->
                if (view1.isShown) {

                    val calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val currentDay = simpleDateFormat.format(calendar.time)
                    etDOBMfg.setText(currentDay)

                }
            })

        }
    }
}
