package com.example.foodie_finder.ui.view

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
import androidx.navigation.findNavController
import com.example.foodie_finder.R
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.data.model.PostModel
import com.example.foodie_finder.databinding.FragmentEditPostBinding
import com.example.foodie_finder.utils.extensions.createTextWatcher
import com.example.foodie_finder.utils.extensions.isNotEmpty
import com.example.foodie_finder.utils.extensions.validateForm
import com.squareup.picasso.Picasso

class EditPostFragment : Fragment() {
    private var binding: FragmentEditPostBinding? = null

    private var post: Post? = null

    private var didSetPostImage = false
    private var cameraLauncher: ActivityResultLauncher<Void?>? = null
    private var galleryLauncher: ActivityResultLauncher<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        post = arguments?.let {
            EditPostFragmentArgs.fromBundle(it).post
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPostBinding.inflate(inflater, container, false)

        binding?.cancelButton?.setOnClickListener(::onCancelClicked)
        binding?.saveButton?.setOnClickListener(::onSaveClicked)
//        binding?.deleteButton?.setOnClickListener(::onDeleteClicked)

        binding?.postInputForm?.restaurantNameTextField?.setText(post?.title)
        binding?.postInputForm?.reviewTextField?.setText(post?.content)
        binding?.postInputForm?.ratingBar?.rating = post?.rating?.toFloat() ?: 0f

        post?.imgUrl?.let {
            if (it.isNotBlank()) {
                Picasso.get()
                    .load(it)
                    .placeholder(R.drawable.placeholderimg)
                    .into(binding?.postInputForm?.postImageView)
            }
        }

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
            binding?.saveButton,
            null,
            Triple(
                binding?.postInputForm?.restaurantNameTextField,
                ::isNotEmpty,
                "Restaurant name cannot be empty"
            ),
            Triple(binding?.postInputForm?.reviewTextField, ::isNotEmpty, "Review cannot be empty")
        )
    }

    private fun onSaveClicked(view: View) {
        val editedPost = post?.copy(
            title = binding?.postInputForm?.restaurantNameTextField?.text.toString().trim(),
            content = binding?.postInputForm?.reviewTextField?.text.toString().trim(),
            rating = binding?.postInputForm?.ratingBar?.rating?.toInt() ?: 0
        )

        var bitmap: Bitmap? = null

        if (didSetPostImage) {
            binding?.postInputForm?.postImageView?.isDrawingCacheEnabled = true
            binding?.postInputForm?.postImageView?.buildDrawingCache()
            bitmap = (binding?.postInputForm?.postImageView?.drawable as BitmapDrawable).bitmap
        }

        binding?.progressBar?.visibility = View.VISIBLE

        editedPost?.let {
            PostModel.shared.updatePost(it, bitmap) { (isSuccessful, errorMessage) ->
                if (isSuccessful) {
                    showToast("Post uploaded")
                    view.findNavController().popBackStack()
                } else {
                    showToast(errorMessage ?: "There was an error uploading the post")
                }
                binding?.progressBar?.visibility = View.GONE
            }
        }
    }


    private fun onCancelClicked(view: View) {
        view.findNavController().popBackStack()
    }

    private fun onDeleteClicked(view: View) {


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}