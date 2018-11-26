package com.pb.leadmanagement.motor

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.TextView
import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.R
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.controller.master.MasterController
import com.pb.leadmanagement.core.controller.motor.MotorController
import com.pb.leadmanagement.core.facade.UserFacade
import com.pb.leadmanagement.core.requestentity.MotorLeadRequestEntity
import com.pb.leadmanagement.core.response.MakeX
import com.pb.leadmanagement.core.response.ModelX
import com.pb.leadmanagement.core.response.MotorLeadResponse
import com.pb.leadmanagement.core.response.Variant
import com.pb.leadmanagement.upload.UploadImageActivity
import com.pb.leadmanagement.utility.DateTimePicker
import kotlinx.android.synthetic.main.content_add_motor_lead.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class AddMotorLeadActivity : AppCompatActivity(), View.OnClickListener, IResponseSubcriber {

    internal var simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
    lateinit var dialog: AlertDialog
    lateinit var dialogView: View


    var txtMakeSearch: AutoCompleteTextView? = null
    var txtModelSearch: AutoCompleteTextView? = null
    var spVariant: Spinner? = null

    var adapterMake: MakeAdapter? = null
    var adapterModel: ModelAdapter? = null

    var mListMake: List<MakeX>? = null
    var mListModel: List<ModelX>? = null

    var vehicleTypeID: Int = 2
    var MakeID: Int = 0
    var ModelID: Int = 0
    var subModelID: Int = 0

    var currentDate = Date()

    var NCB: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_motor_lead)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        txtMakeSearch = findViewById<AutoCompleteTextView>(R.id.etMake)
        txtModelSearch = findViewById<AutoCompleteTextView>(R.id.etModel)
        spVariant = findViewById<Spinner>(R.id.spVariant)

        txtMakeSearch?.threshold = 2
        txtModelSearch?.threshold = 1


        setListener()
        initDialog()

    }

    private fun showMessage(view: View, message: String, action: String, onClickListener: View.OnClickListener?) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(action, onClickListener).show()
    }

    internal fun isValidEMail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
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

    internal var acModelFocusListner: View.OnFocusChangeListener = View.OnFocusChangeListener { view, b ->
        if (!b) {

            val str = etModel.getText().toString()

            val listAdapter = etModel.getAdapter()
            if (listAdapter != null && listAdapter.count > 0) {
                for (i in 0 until listAdapter.getCount()) {
                    val temp = listAdapter.getItem(i) as ModelX
                    if (str.compareTo(temp.Model) == 0) {
                        return@OnFocusChangeListener
                    }
                }
            }
            etModel.setText("")
            showMessage(etModel, "Invalid Model", "", null)

        } else {
            etModel.setError(null)
        }
    }

    internal var acMakeFocusListner: View.OnFocusChangeListener = View.OnFocusChangeListener { view, b ->
        if (!b) {

            val str = etMake.getText().toString()

            val listAdapter = etMake.getAdapter()
            for (i in 0 until listAdapter.getCount()) {
                val temp = listAdapter.getItem(i) as MakeX

                if (str.compareTo(temp.Make) == 0) {
                    return@OnFocusChangeListener
                }
            }
            etMake.setText("")
            showMessage(etMake, "Invalid Make", "", null)

        } else {
            etMake.setError(null)
        }
    }

    private fun uploadImageDialog(leadID: Int) {
        // Initialize a new instance of
        val builder = AlertDialog.Builder(this@AddMotorLeadActivity)

        // Set the alert dialog title
        builder.setTitle("LEAD SAVED.!")

        // Display a message on alert dialog
        builder.setMessage("Do you want to upload any document?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Upload") { dialog, which ->

            if (UserFacade(this@AddMotorLeadActivity).getUserID() != 0) {

                dialog.dismiss()
                val intent = Intent(this, UploadImageActivity::class.java)
                intent.putExtra("LEAD_ID", leadID)
                intent.putExtra("FROM", "MOTOR")
                this!!.finish()
                startActivity(intent)
            }
        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("NO") { dialog, which ->
            dialog.dismiss()
            Handler().postDelayed(Runnable { this!!.finish() }, 500)
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

    private fun confirmationAlert(motorLeadRequestEntity: MotorLeadRequestEntity) {

        // Initialize a new instance of
        val builder = AlertDialog.Builder(this@AddMotorLeadActivity)

        // Set the alert dialog title
        builder.setTitle("Create Lead")

        val healthView = LayoutInflater.from(this).inflate(R.layout.layout_motor_confirm, null)

        builder.setView(healthView)

        healthView.findViewById<TextView>(R.id.txtName).setText("" + motorLeadRequestEntity.Name)
        healthView.findViewById<TextView>(R.id.txtMobile).setText("" + motorLeadRequestEntity.MobileNo)
        healthView.findViewById<TextView>(R.id.txtEmail).setText("" + motorLeadRequestEntity.EmailID)
        healthView.findViewById<TextView>(R.id.txtRelationshipOwner).setText("" + if (motorLeadRequestEntity.RelationshipWithOwner.length > 0) "${motorLeadRequestEntity.RelationshipWithOwner}" else "Not applicable")
        healthView.findViewById<TextView>(R.id.txtRelativeName).setText("" + if (motorLeadRequestEntity.RelativeName.length > 0) "${motorLeadRequestEntity.RelativeName}" else "Not applicable")
        healthView.findViewById<TextView>(R.id.txtRelativeMobileNo).setText("" + if (motorLeadRequestEntity.RelativeMobileNo.length > 0) "${motorLeadRequestEntity.RelativeMobileNo}" else "Not applicable")
        healthView.findViewById<TextView>(R.id.txtRegNo).setText("" + motorLeadRequestEntity.RegistrationNo);//if (healthLeadRequestEntity.PolicyExpiryDate.length > 0) "${healthLeadRequestEntity.PolicyExpiryDate}" else "Not applicable")

        var motorName = UserFacade(this@AddMotorLeadActivity).getVehicleName(vehicleTypeID, motorLeadRequestEntity.MakeID, motorLeadRequestEntity.ModelID, motorLeadRequestEntity.SubModelID)

        healthView.findViewById<TextView>(R.id.txtVehicleName).setText("" + motorName)
        healthView.findViewById<TextView>(R.id.txtMfgDate).setText("" + motorLeadRequestEntity.ManufacturingDate)
        healthView.findViewById<TextView>(R.id.txtPolicyExpiryDate).setText("" + if (motorLeadRequestEntity.PolicyExpiryDate.length > 0) "${motorLeadRequestEntity.PolicyExpiryDate}" else "Not applicable")
        healthView.findViewById<TextView>(R.id.txtNCB).setText("" + if (motorLeadRequestEntity.NCB > 0) "${motorLeadRequestEntity.NCB}" else "Not applicable")


        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Save") { dialog, which ->

            dialog.dismiss()
            showLoading("Loading..")
            MotorController(this@AddMotorLeadActivity).addMotorLead(motorLeadRequestEntity, this)

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


    override fun OnSuccess(response: APIResponse?, message: String?) {

        dismissDialog()
        if (response is MotorLeadResponse) {
            if (response.StatusNo == 0) {
                showMessage(etName, response.Message, "", null)
                Handler().postDelayed(Runnable { uploadImageDialog(response.Result.LeadID) }, 2000)

            }
        }
    }

    override fun OnFailure(error: String?) {
        dismissDialog()
        showMessage(etName, error!!, "", null)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> {

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

                if (etVehicleNo.text.toString().length < 5) {
                    showMessage(etName, "Invalid Vehicle no", "", null)
                    return
                }


                var regxVehicle = "^[A-Z]{2}[0-9]{1,2}[A-Z]{1,2}[0-9]{4}\$".toRegex()


                //var regxVehicle = "^[A-Z]{two}[0-9]{one,two}(?: [A-Z])?(?: [A-Z]*)? [0-9]{three}\$".toRegex()

                if (!etVehicleNo.text.toString().matches(regxVehicle)) {
                    showMessage(etName, "Invalid Vehicle no", "", null)
                    return
                }


                if (spRelation.selectedItemPosition > 0) {
                    if (etRelationName.text.toString().length < 1) {
                        showMessage(etName, "Invalid Relation Name", "", null)
                        return
                    }

                    if (etRelationMobile.text.toString().length < 10) {
                        showMessage(etName, "Invalid Relation Mobile No", "", null)
                        return
                    }
                }

                if (etMake.text.toString().length < 2 || MakeID == 0) {
                    showMessage(etName, "Invalid Make", "", null)
                    return
                }

                if (etModel.text.toString().length < 2 || ModelID == 0) {
                    showMessage(etName, "Invalid Model", "", null)
                    return
                }

                if (etMfgDate.text.toString().length < 4) {
                    showMessage(etName, "Invalid Manufacture Date", "", null)
                    return
                }

                if (etPolicyExpiry.text.toString().length < 4) {
                    showMessage(etName, "Invalid Policy Expiry Date", "", null)
                    return
                }

                if (NCB == 100) {
                    showMessage(spNCB, "Choose NCB", "", null)
                    return
                }

                var motorRequestEntity = MotorLeadRequestEntity(etName.text.toString(),
                        etMobileNo.text.toString(),
                        etEmail.text.toString(),
                        spRelation.selectedItem.toString(),
                        etRelationName.text.toString(),
                        etRelationMobile.text.toString(),
                        vehicleTypeID,
                        etVehicleNo.text.toString(),
                        MakeID,
                        ModelID,
                        subModelID,
                        etMfgDate.text.toString(),
                        etPolicyExpiry.text.toString(),
                        NCB,
                        if (UserFacade(this@AddMotorLeadActivity).getReferenceCode() != null) UserFacade(this@AddMotorLeadActivity).getReferenceCode() else "",
                        etRemark.text.toString(),
                        UserFacade(this@AddMotorLeadActivity).getUserID()

                )

                confirmationAlert(motorRequestEntity)


                //endregion
            }
        }
    }

    private fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etName.windowToken, 0)
    }

    protected var datePickerDialog: View.OnClickListener = View.OnClickListener { view ->
        hideKeyBoard()

        //region regdate
        if (view.id == R.id.etMfgDate) {


            DateTimePicker.mfgYearMonthValidation(view.context, currentDate, DatePickerDialog.OnDateSetListener { view1, year, monthOfYear, dayOfMonth ->
                if (view1.isShown) {

                    val calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val currentDay = simpleDateFormat.format(calendar.time)
                    etMfgDate.setText(currentDay)

                }
            })
        } else if (view.id == R.id.etPolicyExpiry) {

            DateTimePicker.policyExpValidation(view.context, currentDate,
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

    private fun setListener() {

        btnAdd.setOnClickListener(this)
        etVehicleNo.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS

        etMfgDate.setOnClickListener(datePickerDialog)
        etPolicyExpiry.setOnClickListener(datePickerDialog)

        // etMake.setOnFocusChangeListener(acMakeFocusListner)
        //etModel.setOnFocusChangeListener(acModelFocusListner)

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

        spRelation?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                if (position > 0) {
                    trRelation.visibility = View.VISIBLE
                } else {
                    trRelation.visibility = View.GONE
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spVariant?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (spVariant?.adapter != null) {
                    var variant = spVariant?.adapter?.getItem(position) as Variant
                    subModelID = variant.VariantID
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        spVehicleType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                txtMakeSearch?.setText("")
                txtModelSearch?.setText("")
                spVariant?.adapter = null

                //fetch all new data again
                mListMake = emptyList()
                mListModel = emptyList()


                if (position == 0) {
                    vehicleTypeID = 2
                    mListMake = UserFacade(this@AddMotorLeadActivity).getFourWheelerMaster(2) //four wheeler
                } else if (position == 1) {
                    vehicleTypeID = 4
                    mListMake = UserFacade(this@AddMotorLeadActivity).getTwoWheelerMaster(4)//two wheeler
                }

                if (mListMake!!.isNotEmpty()) {
                    changeMakeAdapter()
                } else {
                   // showMessage("")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        txtMakeSearch?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            val makeData = adapterMake!!.getItem(position) as MakeX
            MakeID = makeData.MakeID
            mListModel = makeData.Model
            txtMakeSearch?.setError(null)
            txtModelSearch?.setText("")
            changeModelAdapter()
            hideKeyBoard()
        }

        txtModelSearch?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            if (adapterModel!!.getItem(position) != null) {
                ModelID = adapterModel!!.getItem(position)!!.ModelID
                etModel.setError(null)
            }

            if (adapterModel!!.getItem(position)?.Variant != null) {

                val variantList = adapterModel!!.getItem(position)!!.Variant as List<Variant>
                if (variantList != null) {
                    hideKeyBoard()
                    var spinnerAdapter = VariantAdapter(this!!, variantList)
                    spVariant?.adapter = spinnerAdapter
                }
            } else {
                spVariant?.visibility = View.GONE
            }
        }

        swNCB?.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                NCB = 0
                spNCB.visibility = View.GONE
            } else {
                spNCB.visibility = View.VISIBLE
            }
        })

        spNCB?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (spNCB?.adapter != null) {

                    when (position) {
                        0 -> {
                            NCB = 100
                            //showMessage(spNCB, "Invalid NCB", "", null)
                        }
                        1 -> {
                            NCB = 0
                        }
                        2 -> {
                            NCB = 20
                        }
                        3 -> {
                            NCB = 25
                        }
                        4 -> {
                            NCB = 35
                        }
                        5 -> {
                            NCB = 45
                        }
                        6 -> {
                            NCB = 50
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


    }

    private fun changeModelAdapter() {
        adapterModel = ModelAdapter(this@AddMotorLeadActivity,
                R.layout.activity_add_motor_lead, R.id.lbl_name, mListModel)
        txtModelSearch?.setAdapter(adapterModel)
    }

    private fun changeMakeAdapter() {
        if (mListMake != null) {
            adapterMake = MakeAdapter(this@AddMotorLeadActivity,
                    R.layout.activity_add_motor_lead, R.id.lbl_name, mListMake)
            txtMakeSearch?.setAdapter(adapterMake)
        }
    }

}
