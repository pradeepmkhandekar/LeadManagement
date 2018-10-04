package com.pb.leadmanagement.reports.health_report

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.chemistlead.core.APIResponse
import com.pb.leadmanagement.R
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.controller.loan.LoanLeadController
import com.pb.leadmanagement.core.controller.report.ReportController
import com.pb.leadmanagement.core.facade.UserFacade

import kotlinx.android.synthetic.main.activity_health_report.*
import java.text.SimpleDateFormat

class HealthReportActivity : AppCompatActivity() , View.OnClickListener, IResponseSubcriber {


    internal var simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
    lateinit var dialog: AlertDialog
    lateinit var dialogView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_report)
        setSupportActionBar(toolbar)
         initDialog()
       showLoading("Fetching Reports..")
      //  LoanLeadController(this@LoanActivity).addLoanLead(loanRequestEntity, this)
        ReportController(this@HealthReportActivity).getHealthReport("1538049676000","1538308876000", (UserFacade(this@HealthReportActivity).getUserID()).toString(),this);
        //
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

    override fun onClick(p0: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun OnSuccess(response: APIResponse?, message: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        dismissDialog();
        Toast.makeText(this,"Done..",Toast.LENGTH_SHORT).show()
    }

    override fun OnFailure(error: String?) {
        dismissDialog()
        Toast.makeText(this,error!!,Toast.LENGTH_SHORT).show()
    }


}
