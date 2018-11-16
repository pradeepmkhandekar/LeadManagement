package com.pb.leadmanagement.reports


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.pb.leadmanagement.R
import com.pb.leadmanagement.core.facade.UserFacade
import com.pb.leadmanagement.health.HealthActivity
import com.pb.leadmanagement.home.NavigationActivity
import com.pb.leadmanagement.life.LifeActivity
import com.pb.leadmanagement.loan.LoanActivity
import com.pb.leadmanagement.motor.AddMotorLeadActivity
import com.pb.leadmanagement.other.AddOtherActivity
import com.pb.leadmanagement.reports.health_report.HealthReportActivity
import kotlinx.android.synthetic.main.fragment_reports.*
import kotlinx.android.synthetic.main.fragment_reports.view.*


/**
 * A simple [Fragment] subclass.
 */
class ReportsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_reports, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view.context as NavigationActivity).setTitle("Reports")
    }


}// Required empty public constructor
