package com.example.foodie_finder.fragments.addPost

import android.app.AlertDialog
import android.graphics.Bitmap
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
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.foodie_finder.R
import com.example.foodie_finder.data.entities.FirebasePost
import com.example.foodie_finder.data.model.UserModel
import com.example.foodie_finder.databinding.FragmentNewPostBinding
import com.example.foodie_finder.utils.extensions.createTextWatcher
import com.example.foodie_finder.utils.extensions.isNotEmpty
import com.example.foodie_finder.utils.extensions.validateForm
import com.google.firebase.Timestamp
import java.util.UUID

class AddPostFragment : Fragment() {
    private var binding: FragmentNewPostBinding? = null

    private var didSetPostImage = false
    private var cameraLauncher: ActivityResultLauncher<Void?>? = null
    private var galleryLauncher: ActivityResultLauncher<String>? = null

    private val viewModel: AddPostViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)

        binding?.cancelButton?.setOnClickListener(::onCancelClicked)
        binding?.postButton?.setOnClickListener(::onPostClicked)


        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                if (bitmap != null) {
                    binding?.postInputForm?.postImageView?.setImageBitmap(bitmap)
                    didSetPostImage = true
                }
            }

        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    binding?.postInputForm?.postImageView?.setImageURI(it)
                    didSetPostImage = true
                }
            }

        binding?.postInputForm?.uploadImageButton?.setOnClickListener {
            showImageSourceChooser()
        }

        binding?.postInputForm?.restaurantNameTextField?.addTextChangedListener(createTextWatcher { validateAddPostForm() })
        binding?.postInputForm?.reviewTextField?.addTextChangedListener(createTextWatcher { validateAddPostForm() })
        binding?.postInputForm?.ratingBar?.setOnRatingBarChangeListener { _, _, _ -> validateAddPostForm() }


        return binding?.root

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

    private fun validateAddPostForm() {
        validateForm(
            binding?.postButton,
            null,
            Triple(
                binding?.postInputForm?.restaurantNameTextField,
                ::isNotEmpty,
                "Restaurant name cannot be empty"
            ),
            Triple(binding?.postInputForm?.reviewTextField, ::isNotEmpty, "Review cannot be empty")
        )
    }

    private fun onPostClicked(view: View) {
        val userRef = UserModel.shared.getConnectedUserRef() ?: return

        val title = binding?.postInputForm?.restaurantNameTextField?.text.toString().trim()
        val content = binding?.postInputForm?.reviewTextField?.text.toString().trim()
        val rating = binding?.postInputForm?.ratingBar?.rating?.toInt() ?: 0

        var bitmap: Bitmap? = null

        if (didSetPostImage) {
            binding?.postInputForm?.postImageView?.isDrawingCacheEnabled = true
            binding?.postInputForm?.postImageView?.buildDrawingCache()
            bitmap = (binding?.postInputForm?.postImageView?.drawable as BitmapDrawable).bitmap
        }

        val post = FirebasePost(
            id = UUID.randomUUID().toString(),
            postedBy = userRef,
            title = title,
            content = content,
            rating = rating,
            lastUpdateTime = Timestamp.now().toDate().time,
            creationTime = Timestamp.now().toDate().time
        )

        binding?.progressBar?.visibility = View.VISIBLE

        viewModel.createPost(post, bitmap) { (isSuccessful, errorMessage) ->
            if (isSuccessful) {
                showToast("Post uploaded")
                view.findNavController().popBackStack()
                resetForm()
            } else {
                showToast(errorMessage ?: "There was an error uploading the post")
            }
            binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun resetForm() {
        binding?.postInputForm?.restaurantNameTextField?.text?.clear()
        binding?.postInputForm?.reviewTextField?.text?.clear()
        binding?.postInputForm?.ratingBar?.rating = 0f
        resetImageView()
    }

    private fun resetImageView() {
        binding?.postInputForm?.postImageView?.setImageResource(R.drawable.placeholderimg)
        didSetPostImage = false
    }


    private fun onCancelClicked(view: View) {
        view.findNavController().popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}