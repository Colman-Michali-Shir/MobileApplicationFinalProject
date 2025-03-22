package com.example.foodie_finder

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.foodie_finder.databinding.FragmentProfileBinding
import com.example.foodie_finder.model.Model
import com.example.foodie_finder.model.User
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {
    private var binding: FragmentProfileBinding? = null
    private var cameraLauncher: ActivityResultLauncher<Void?>? = null
    private var galleryLauncher: ActivityResultLauncher<String>? = null
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        loadUserData()
        setupImagePickers()

        binding?.addPhotoImageButton?.setOnClickListener {
            showImageSourceChooser()
        }
        binding?.saveButton?.setOnClickListener {
            saveUserData()
        }

        return binding?.root
    }

    private fun loadUserData() {
        Model.shared.getUser { user ->
            user?.let {
                this.user = it
                binding?.apply {
                    firstNameEditText.setText(it.firstName)
                    lastNameEditText.setText(it.lastName)
                    it.avatarUrl?.takeIf { avatarUrl ->
                        avatarUrl.isNotBlank()
                    }?.let { url ->
                        Picasso.get()
                            .load(url)
                            .placeholder(R.drawable.baseline_person_24)
                            .into(binding?.userImageView)
                    }
                }
            } ?: showToast("User not found")
        }
    }

    private fun setupImagePickers() {
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                bitmap?.let { binding?.userImageView?.setImageBitmap(it) }
            }

        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let { binding?.userImageView?.setImageURI(it) }
            }
    }

    private fun saveUserData() {
        user?.let { currentUser ->
            val updatedUser = currentUser.copy(
                firstName = binding?.firstNameEditText?.text.toString(),
                lastName = binding?.lastNameEditText?.text.toString()
            )

            val bitmap = extractBitmapFromImageView()

            Model.shared.updateUser(updatedUser, bitmap) { success ->
                if (success) {
                    showToast("User is updated")
                    this.user = updatedUser
                } else {
                    showToast("Failed to update user")
                }
            }
        }
    }

    private fun extractBitmapFromImageView(): Bitmap? {
        val view = binding?.userImageView ?: return null

        return if (view.drawable is BitmapDrawable) {
            (view.drawable as BitmapDrawable).bitmap
        } else {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            bitmap
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun showImageSourceChooser() {
        val options = arrayOf("Take Photo", "Choose from Gallery")

        AlertDialog.Builder(requireContext())
            .setTitle("Select Image Source")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> cameraLauncher?.launch(null)
                    1 -> galleryLauncher?.launch("image/*")
                }
            }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}