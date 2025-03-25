package com.example.foodie_finder.data.remote

import android.graphics.Bitmap
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.cloudinary.android.policy.GlobalUploadPolicy
import com.cloudinary.android.policy.UploadPolicy
import com.example.foodie_finder.BuildConfig
import com.example.foodie_finder.base.MyApplication
import com.example.foodie_finder.utils.extensions.toFile

class CloudinaryModel private constructor() {

    init {
        val config = mapOf(
            "cloud_name" to BuildConfig.CLOUDINARY_CLOUD_NAME,
            "api_key" to BuildConfig.CLOUDINARY_API_KEY,
            "api_secret" to BuildConfig.CLOUDINARY_API_SECRET,
        )

        MyApplication.Globals.context?.let {
            MediaManager.init(it, config)
            MediaManager.get().globalUploadPolicy = GlobalUploadPolicy.Builder()
                .maxConcurrentRequests(3)
                .networkPolicy(UploadPolicy.NetworkType.UNMETERED)
                .build()
        }
    }

    companion object {
        @Volatile
        private var instance: CloudinaryModel? = null

        fun getInstance(): CloudinaryModel {
            return instance ?: synchronized(this) {
                instance ?: CloudinaryModel().also { instance = it }
            }
        }
    }

    fun uploadImageToCloudinary(
        image: Bitmap,
        name: String,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) {
        val context = MyApplication.Globals.context ?: return
        val file = image.toFile(context, name)

        MediaManager.get().upload(file.path)
            .option(
                "folder",
                "images"
            ) // Optional: Specify a folder in your Cloudinary account
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    // Called when upload starts
                    Log.d("TAG", "start")

                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    // Called during upload progress
                    Log.d("TAG", "progress")

                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    Log.d("TAG", "success")
                    val publicUrl = resultData["secure_url"] as? String ?: ""
                    onSuccess(publicUrl)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Log.d("TAG", "error")

                    onError(error?.description ?: "Unknown error")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                    Log.d("TAG", "reschedule")

                    // Called during reschedule

                }

            })
            .dispatch()
    }
}