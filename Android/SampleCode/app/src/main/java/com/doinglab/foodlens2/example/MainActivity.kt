package com.doinglab.foodlens2.example

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.doinglab.foodlens2.example.databinding.ActivityMainBinding
import com.doinglab.foodlens2.example.listview.ListItem
import com.doinglab.foodlens2.example.listview.SampleListAdapter
import com.doinglab.foodlens2.example.util.Utils

import com.doinglab.foodlens2.sdk.FoodLens
import com.doinglab.foodlens2.sdk.RecognitionResultHandler
import com.doinglab.foodlens2.sdk.config.LanguageConfig
import com.doinglab.foodlens2.sdk.errors.BaseError
import com.doinglab.foodlens2.sdk.model.RecognitionResult
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val foodLensService by lazy {
        FoodLens.createFoodLensService(this@MainActivity)
    }

    private val listAdapter by lazy {
        SampleListAdapter()
    }

    private var originBitmap: Bitmap? = null

    private val loadingDialog by lazy {
        Dialog(this@MainActivity)
    }

    var currentPhotoUri: Uri? = null
    var currentPhotoPath = ""

    val selLaunuage = arrayOf("en", "ko")
    val selOrientation = arrayOf("true", "false")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.list.adapter = listAdapter

        foodLensService.setAutoRotate(true)
        foodLensService.setLanguage(LanguageConfig.KO)

        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingDialog.setContentView(ProgressBar(this))

        binding.tvSetLanguage.text = "Language : ${foodLensService.getLanguage()}"
        binding.tvSetRotation.text = "Rotation Auto : ${foodLensService.getAutoRotate()}"

        binding.btnRunFoodlensCamera.setOnClickListener {
            openCamera(REQ_CAMERA_PICTURE)
        }

        binding.btnRunFoodlensGallery.setOnClickListener {
            openGallery(REQ_GALLERY_PICTURE)
        }

        binding.tvSetLanguage.setOnClickListener {
            selectLanguageDialog()
        }

        binding.tvSetRotation.setOnClickListener {
            selectRotationDialog()
        }
    }

    private fun openCamera(requestCode: Int) {
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast.makeText(this, "no camera", Toast.LENGTH_SHORT).show()
            return
        }
        var imageFile = createImageFile()
        imageFile?.let {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
            mCameraForResult.launch(intent)
        }

    }

    private fun openGallery(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        mGalleryForResult.launch(intent)
    }

    private var mGalleryForResult: ActivityResultLauncher<Intent> =
        registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let {
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver?.query(it, filePathColumn, null, null, null)
                    cursor?.moveToFirst()
                    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                    val picturePath = cursor?.getString(columnIndex ?: return@let) ?: return@let
                    currentPhotoPath = picturePath
                    cursor.close()

                    originBitmap = Utils.getBitmapFromFile(picturePath)

                    loadingDialog.show()
                    val byteData = Utils.readContentIntoByteArray(File(picturePath))

                    foodLensService.predict(byteData, object : RecognitionResultHandler {
                        override fun onSuccess(result: RecognitionResult?) {
                            loadingDialog.dismiss()

                            result?.let {
                                setRecognitionResultData(result)
                            }
                        }

                        override fun onError(errorReason: BaseError?) {
                            loadingDialog.dismiss()
                            Toast.makeText(applicationContext,errorReason?.getMessage(), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }

    private var mCameraForResult: ActivityResultLauncher<Intent> =
        registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                originBitmap = Utils.getBitmapFromFile(currentPhotoPath)

                loadingDialog.show()
                val byteData = Utils.readContentIntoByteArray(File(currentPhotoPath))

                foodLensService.predict(byteData, object : RecognitionResultHandler {
                    override fun onSuccess(result: RecognitionResult?) {
                        loadingDialog.dismiss()

                        result?.let {
                            setRecognitionResultData(result)
                        }
                    }

                    override fun onError(errorReason: BaseError?) {
                        loadingDialog.dismiss()
                        Toast.makeText(applicationContext, errorReason?.getMessage(), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }


    private fun setRecognitionResultData(resultData: RecognitionResult) {
        val mutableList = mutableListOf<ListItem>()

        if(foodLensService.getAutoRotate()) {
            var orientation = Utils.getOrientationOfImage(currentPhotoPath)
            originBitmap = Utils.getRotationBitmap(originBitmap, orientation)
        }

        binding.tvTitle.text = "Result from FoodLens Service"
        resultData.foodInfoList?.forEach { foodInfo ->

            foodInfo?.let {
                val xMin = it.foodPosition?.xmin ?: 0
                val yMin = it.foodPosition?.ymin ?: 0
                val xMax = it.foodPosition?.xmax ?: originBitmap?.width ?: 0
                val yMax = it.foodPosition?.ymax ?: originBitmap?.height ?: 0

                val bitmap = Utils.cropBitmap(originBitmap, xMin, yMin, xMax,yMax) ?: null

                mutableList.add(
                    ListItem(
                        id = it.hashCode(),
                        title = "${it.name}",
                        icon = BitmapDrawable(resources, bitmap),
                        foodPosition = "${getString(R.string.food_position)} : ${xMin}, ${yMin}, ${yMax}, ${yMax}",
                        foodNutrition = "${getString(R.string.carbohydrate)} : ${it.carbohydrate}, " +
                                "${getString(R.string.protein)} : ${it.protein}, " +
                                "${getString(R.string.fat)} : ${it.fat}"
                    )
                )
            }
        }

        listAdapter.submitList(mutableList)
    }

    private fun selectLanguageDialog() {
        var selectIndex = if(foodLensService.getLanguage() == LanguageConfig.EN.launuage) 0 else 1

        AlertDialog.Builder(this@MainActivity)
            .setTitle("Select Language")
            .setSingleChoiceItems(selLaunuage, selectIndex,
                DialogInterface.OnClickListener{ _, i->
                    selectIndex = i
                })
            .setPositiveButton("Confirm",
                DialogInterface.OnClickListener{ _, _ ->
                    if(selectIndex == 0) {
                        foodLensService.setLanguage(LanguageConfig.EN)
                    }
                    else {
                        foodLensService.setLanguage(LanguageConfig.KO)
                    }
                    binding.tvSetLanguage.text = "Language : ${foodLensService.getLanguage()}"
                })
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun selectRotationDialog() {
        var selectIndex = if(foodLensService.getAutoRotate()) 0 else 1

        AlertDialog.Builder(this@MainActivity)
            .setTitle("Select Auto Rotation")
            .setSingleChoiceItems(selOrientation, selectIndex,
                DialogInterface.OnClickListener{ _, i->
                    selectIndex = i
                })
            .setPositiveButton("Confirm",
                DialogInterface.OnClickListener{ _, _ ->
                    //Log.d("bongtest", "select2 - ${i}")
                    if(selectIndex == 0) {
                        foodLensService.setAutoRotate(true)
                    }
                    else {
                        foodLensService.setAutoRotate(false)
                    }
                    binding.tvSetRotation.text = "Rotation auto : ${foodLensService.getAutoRotate()}"
                })
            .setNegativeButton("Cancel", null)
            .show()
    }


    fun createImageFile() : File {
        val imageFileName = "JPEG_Image"
        val imagePath: File? = getExternalFilesDir("images")

        val newFile: File = File.createTempFile(imageFileName, ".jpg", imagePath)

        currentPhotoPath = newFile.getAbsolutePath()

        try {
            currentPhotoUri = FileProvider.getUriForFile(
                this,
                applicationContext.packageName + ".fileprovider",
                newFile
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return newFile
    }

    companion object {
        private const val REQ_GALLERY_PICTURE = 0x02
        private const val REQ_CAMERA_PICTURE = 0x03
    }



}