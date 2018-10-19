package com.pb.leadmanagement.upload

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.android.chemistlead.core.APIResponse
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.pb.leadmanagement.R
import com.pb.leadmanagement.core.IResponseSubcriber
import com.pb.leadmanagement.core.controller.motor.MotorController
import com.pb.leadmanagement.core.requestentity.UploadDocRequestEntity
import com.pb.leadmanagement.core.response.MotorLeadResponse
import kotlinx.android.synthetic.main.content_upload_image.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UploadImageActivity : AppCompatActivity(), View.OnClickListener, IResponseSubcriber {

    val TAKE_PHOTO_CAMERA: Int = 1111
    val TAKE_PHOTO_GALLERY: Int = 2222


    lateinit var imageFilePath: String
    var isPolicy: Boolean = false

    var leadID: Int = 0
    var productID: Int = 0
    var documentName: String = ""
    lateinit var dialogView: View
    lateinit var dialog: AlertDialog


    companion object {
        private val IMAGE_DIRECTORY = "/LeadManagement"
    }

    open fun encodeTobase64(image: Bitmap): String {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)
        setSupportActionBar(toolbar)

        initDialog()

        if (intent.getStringExtra("FROM") != null) {
            if (intent.getStringExtra("FROM").equals("MOTOR")) {
                cardMotor.visibility = View.VISIBLE
                cardRC.visibility = View.VISIBLE
                productID = 2;
                leadID = intent.getIntExtra("LEAD_ID", 0)
            } else if (intent.getStringExtra("FROM").equals("HEALTH")) {
                cardMotor.visibility = View.VISIBLE
                cardRC.visibility = View.GONE
                productID = 10
                leadID = intent.getIntExtra("LEAD_ID", 0)
            }
        }

        setListener()

    }

    private fun setListener() {
        imgRC.setOnClickListener(this)
        imgPolicy.setOnClickListener(this)
        txtUploadPolicy.setOnClickListener(this)
        txtUploadRC.setOnClickListener(this)
    }

    private fun pickFromGallery() {
        var intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, TAKE_PHOTO_GALLERY)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName: String = "JPEG_" + timeStamp + "_"
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES + IMAGE_DIRECTORY)
        if (!storageDir.exists()) storageDir.mkdirs()
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        imageFilePath = imageFile.absolutePath
        return imageFile
    }

    private fun launchCamera() {

        try {
            val imageFile = createImageFile()
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(packageManager) != null) {
                val authorities = packageName + ".fileprovider"
                val imageUri = FileProvider.getUriForFile(this, authorities, imageFile)
                callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(callCameraIntent, TAKE_PHOTO_CAMERA)
            }
        } catch (e: IOException) {
            Toast.makeText(this, "Could not create file!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun OnSuccess(response: APIResponse?, message: String?) {

        dismissDialog()
        if (response is MotorLeadResponse) {
            showMessage(imgPolicy, response.Message, "", null)
        }
    }

    override fun OnFailure(error: String?) {
        dismissDialog()
        showMessage(imgPolicy, error!!, "", null)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgRC -> {
                validatePermissions()
                isPolicy = false
                documentName = "RC"
            }

            R.id.imgPolicy -> {
                validatePermissions()
                isPolicy = true
                documentName = "Policy"
            }

            R.id.txtUploadPolicy -> {
                baseAsync(this, imgPolicy.getTag(R.id.imgPolicy) as Bitmap, documentName, leadID, productID).execute()
            }

            R.id.txtUploadRC -> {
                baseAsync(this, imgRC.getTag(R.id.imgRC) as Bitmap, documentName, leadID, productID).execute()
            }
        }
    }

    fun uploadImageToServer(uploadDocRequestEntity: UploadDocRequestEntity) {
        showLoading("Uploading document..")
        MotorController(this@UploadImageActivity).uploadDocuments(uploadDocRequestEntity, this)

    }

    class baseAsync(var activity: UploadImageActivity?, var bitmap: Bitmap, var docName: String, var leadID: Int, var pID: Int)
        : AsyncTask<Void, Void, String>() {


        override fun onPreExecute() {
            super.onPreExecute()
            activity?.showLoading("Preparing document to upload")
        }

        override fun doInBackground(vararg params: Void?): String {
            return activity?.encodeTobase64(bitmap)!!
        }

        override fun onPostExecute(result: String?) {

            var uploadDocRequestEntity = UploadDocRequestEntity(pID,
                    docName,
                    leadID,
                    result!!)
            activity?.uploadImageToServer(uploadDocRequestEntity)

            super.onPostExecute(result)

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


    //endregion


    private fun validatePermissions() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {


                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        AlertDialog.Builder(this@UploadImageActivity)
                                .setTitle(
                                        "Required Permission")
                                .setMessage(
                                        "Required all permission to process further.")
                                .setNegativeButton(
                                        android.R.string.cancel,
                                        { dialog, _ ->
                                            dialog.dismiss()
                                            token?.cancelPermissionRequest()
                                        })
                                .setPositiveButton(android.R.string.ok,
                                        { dialog, _ ->
                                            dialog.dismiss()
                                            token?.continuePermissionRequest()
                                        })
                                .setOnDismissListener({
                                    token?.cancelPermissionRequest()
                                })

                                .show()
                    }

                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                        if (report!!.areAllPermissionsGranted()) {
                            showCameraGalleryPopUp()
                        }

                        if (report!!.isAnyPermissionPermanentlyDenied) {
                            AlertDialog.Builder(this@UploadImageActivity)
                                    .setTitle("Grant Permission")
                                    .setMessage("Allow all permission from settings.")
                                    .setPositiveButton(android.R.string.ok,
                                            { dialog, _ ->
                                                dialog.dismiss()
                                                finish()
                                            })
                                    .show()
                        }
                    }

                })
                .onSameThread()
                .check()

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
            launchCamera()
            alertDialog.dismiss()
        }

        lyGallery.setOnClickListener {
            pickFromGallery()
            alertDialog.dismiss()
        }
        alertDialog.setCancelable(true)
        alertDialog.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == TAKE_PHOTO_GALLERY && data != null && resultCode == Activity.RESULT_OK) {
            val selectedImage = data.data
            val filepath = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage, filepath, null, null, null)
            cursor.moveToFirst()
            val Index = cursor.getColumnIndex(filepath[0])
            val Picture = cursor.getString(Index)
            cursor.close()
            var bitmap = BitmapFactory.decodeFile(Picture)
            if (isPolicy) {
                imgPolicy.setImageBitmap(bitmap)
                imgPolicy.setTag(R.id.imgPolicy, bitmap)
            } else {
                imgRC.setImageBitmap(bitmap)
                imgRC.setTag(R.id.imgRC, bitmap)
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == TAKE_PHOTO_CAMERA) {

            if (isPolicy) {
                var bitmap = setScaledBitmap(imgPolicy)
                imgPolicy.setImageBitmap(bitmap)
                imgPolicy.setTag(R.id.imgPolicy, bitmap)
            } else {
                var bitmap = setScaledBitmap(imgRC)
                imgRC.setImageBitmap(bitmap)
                imgRC.setTag(R.id.imgRC, bitmap)
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setScaledBitmap(imageView: ImageView): Bitmap {
        val imageViewWidth = imageView.width
        val imageViewHeight = imageView.height

        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePath, bmOptions)
        val bitmapWidth = bmOptions.outWidth
        val bitmapHeight = bmOptions.outHeight

        val scaleFactor = Math.min(bitmapWidth / imageViewWidth, bitmapHeight / imageViewHeight)

        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor

        return BitmapFactory.decodeFile(imageFilePath, bmOptions)

    }

    private fun showMessage(view: View, message: String, action: String, onClickListener: View.OnClickListener?) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(action, onClickListener).show()
    }
}
