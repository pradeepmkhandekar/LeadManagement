package com.pb.leadmanagement.upload

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.LinearLayout
import com.pb.leadmanagement.R
import kotlinx.android.synthetic.main.content_upload_image.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class UploadImageActivity : AppCompatActivity(), View.OnClickListener {

    private val TAKE_PHOTO_CAMERA: Int = 1111
    private val TAKE_PHOTO_GALLERY: Int = 2222

    lateinit var imageFilePath: String
    private var mCurrentPhotoPath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_upload_image)
        setSupportActionBar(toolbar)
        setListener()

    }

    private fun setListener() {
        imgRC.setOnClickListener(this)
        imgPolicy.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgRC -> {
                //validatePermissions()
            }

            R.id.imgPolicy -> {
                // validatePermissions()
            }
        }
    }

    private fun showCameraGalleryPopUp() {
        val builder = AlertDialog.Builder(this, R.style.CustomDialog)

        val lyCamera: LinearLayout
        val lyGallery: LinearLayout
        val inflater = this.layoutInflater

        val dialogView = inflater.inflate(R.layout.layout_cam_gallery, null)

        builder.setView(dialogView)
        val alertDialog = builder.create()
        // set the custom dialog components - text, image and button
        lyCamera = dialogView.findViewById(R.id.lyCamera)
        lyGallery = dialogView.findViewById(R.id.lyGallery)

        lyCamera.setOnClickListener {
            //  launchCamera()
            alertDialog.dismiss()
        }

        lyGallery.setOnClickListener {
            //  pickFromGallery()
            alertDialog.dismiss()
        }
        alertDialog.setCancelable(true)
        alertDialog.show()

    }


    private fun showMessage(view: View, message: String, action: String, onClickListener: View.OnClickListener?) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(action, onClickListener).show()
    }
}
