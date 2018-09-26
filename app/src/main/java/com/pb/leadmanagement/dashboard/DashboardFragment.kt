package com.pb.leadmanagement.dashboard


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.*
import android.view.ActionMode.Callback
import android.view.View.*
import com.pb.leadmanagement.R
import com.pb.leadmanagement.core.facade.UserFacade
import com.pb.leadmanagement.health.HealthActivity
import com.pb.leadmanagement.life.LifeActivity
import com.pb.leadmanagement.loan.LoanActivity
import com.pb.leadmanagement.motor.AddMotorLeadActivity
import com.pb.leadmanagement.other.AddOtherActivity
import com.pb.leadmanagement.test.TestActivity
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*


/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment(), OnClickListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        setListener(view)
        return view
    }

    override fun onResume() {

        if (UserFacade(activity!!).getUser()?.ID == 0) {
            showMessage(cardMotor, "User not found, Login again", "", null)
            Handler().postDelayed(Runnable { activity!!.finish() }, 2000)
        } else {
            bindLeads()
        }
        super.onResume()
    }

    private fun bindLeads() {

        var ledInterest = UserFacade(activity!!).getUser()?.LeadInterest

        for (item in ledInterest!!) {

            if (item.equals("motor")) {
                view?.cardMotor!!.visibility = VISIBLE
            } else if (item.equals("health")) {
                view?.cardHealth!!.visibility = VISIBLE
            } else if (item.equals("loan")) {
                view?.cardLoan!!.visibility = VISIBLE
            } else if (item.equals("life")) {
                view?.cardLife!!.visibility = VISIBLE
            } else if (item.equals("other")) {
                view?.cardOther!!.visibility = VISIBLE
            }
        }
    }


    private fun setListener(view: View) {
        view.cardMotor.setOnClickListener(this)
        view.cardHealth.setOnClickListener(this)
        view.cardLife.setOnClickListener(this)
        view.cardOther.setOnClickListener(this)
        view.cardLoan.setOnClickListener(this)

    }

    private fun showMessage(view: View, message: String, action: String, onClickListener: View.OnClickListener?) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(action, onClickListener).show()
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.cardMotor -> {
                val intent = Intent(activity, AddMotorLeadActivity::class.java)
                startActivity(intent)
            }

            R.id.cardHealth -> {
                // val intent = Intent(activity, TestActivity::class.java)

                val intent = Intent(activity, HealthActivity::class.java)
                startActivity(intent)
            }

            R.id.cardLife -> {
                val intent = Intent(activity, LifeActivity::class.java)
                startActivity(intent)
            }

            R.id.cardOther -> {
                val intent = Intent(activity, AddOtherActivity::class.java)
                startActivity(intent)
            }
            R.id.cardLoan -> {
                val intent = Intent(activity, LoanActivity::class.java)
                startActivity(intent)
            }
        }
    }

}// Required empty public constructor
