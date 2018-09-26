package com.pb.leadmanagement.test

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.pb.leadmanagement.R

import kotlinx.android.synthetic.main.activity_test.*
import android.R.layout.simple_spinner_item


class TestActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var txtSearch: AutoCompleteTextView? = null
    var mList: List<People>? = null
    var adapterAuto: PeopleAdapter? = null
    var spinner: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mList = retrievePeople()
        txtSearch = findViewById<AutoCompleteTextView>(R.id.txt_search)
        spinner = findViewById<Spinner>(R.id.spinner)


        this.txtSearch?.threshold = 1
        adapterAuto = PeopleAdapter(this, R.layout.activity_test, R.id.lbl_name, mList)
        txtSearch?.setAdapter(adapterAuto)


        // val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mList)

        spinner!!.adapter = adapterAuto

        txtSearch?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            val people = adapterAuto!!.getItem(position) as People
            Toast.makeText(this, "" + people.name, Toast.LENGTH_SHORT).show()
        }


        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
             //   val people = adapterAuto!!.getItem(position) as People
            //    Toast.makeText(applicationContext, "" + people.name, Toast.LENGTH_SHORT).show()
            }

        }


    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    private fun retrievePeople(): List<People> {
        val list = ArrayList<People>()
        list.add(People("James", "Bond", 1))
        list.add(People("Jason", "Bourne", 2))
        list.add(People("Ethan", "Hunt", 3))
        list.add(People("Sherlock", "Holmes", 4))
        list.add(People("David", "Beckham", 5))
        list.add(People("Bryan", "Adams", 6))
        list.add(People("Arjen", "Robben", 7))
        list.add(People("Van", "Persie", 8))
        list.add(People("Zinedine", "Zidane", 9))
        list.add(People("Luis", "Figo", 10))
        list.add(People("John", "Watson", 11))
        return list
    }
}
