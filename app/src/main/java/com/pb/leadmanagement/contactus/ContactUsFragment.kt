package com.pb.leadmanagement.contactus


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.pb.leadmanagement.R
import com.pb.leadmanagement.home.NavigationActivity


/**
 * A simple [Fragment] subclass.
 */
class ContactUsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view.context as NavigationActivity).setTitle("Contact Us")
    }

}// Required empty public constructor
