package com.example.foodie_finder.fragments.profile

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
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodie_finder.R
import com.example.foodie_finder.adapter.Post.PostsAdapter
import com.example.foodie_finder.data.entities.Post
import com.example.foodie_finder.data.model.PostModel
import com.example.foodie_finder.data.model.UserModel
import com.example.foodie_finder.databinding.FragmentProfileBinding
import com.example.foodie_finder.interfaces.OnItemClickListener
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    private var binding: FragmentProfileBinding? = null
    private var adapter: PostsAdapter? = null
    private val viewModel: ProfileViewModel by viewModels()

    private var cameraLauncher: ActivityResultLauncher<Void?>? = null
    private var galleryLauncher: ActivityResultLauncher<String>? = null
    private var isImageUpdated = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        loadUserData()
        setupImagePickers()

        binding?.userPostList?.setHasFixedSize(true)
        binding?.userPostList?.layoutManager = LinearLayoutManager(context)

        binding?.addPhotoImageButton?.setOnClickListener {
            showImageSourceChooser()
        }
        binding?.saveButton?.setOnClickListener {
            saveUserData()
        }

        adapter = PostsAdapter(
            viewModel.userPosts.value ?: emptyList(),
            viewModel.savedPosts.value ?: emptyList(),
            onSavePost = viewModel::savePost,
            onRemoveSavePost = viewModel::removeSavedPost,
        )

        viewModel.userPosts.observe(viewLifecycleOwner) { posts ->
            adapter?.updateAllPosts(posts)
            adapter?.notifyDataSetChanged()

            binding?.progressBar?.visibility = View.GONE
        }

        viewModel.savedPosts.observe(viewLifecycleOwner) { posts ->
            adapter?.updateSavedPosts(posts)
            adapter?.notifyDataSetChanged()

            binding?.progressBar?.visibility = View.GONE
        }

        binding?.swipeToRefresh?.setOnRefreshListener {
            viewModel.refreshUsersPosts()
        }

        PostModel.shared.loadingState.observe(viewLifecycleOwner) { state ->
            binding?.swipeToRefresh?.isRefreshing = state == PostModel.LoadingState.LOADING
            binding?.progressBar?.visibility =
                if (state == PostModel.LoadingState.LOADING) View.VISIBLE else View.GONE
        }

        adapter?.listener = object : OnItemClickListener {
            override fun onEditPost(post: Post?) {
                post?.let { clickedPost ->
                    val action =
                        ProfileFragmentDirections.actionProfileFragmentToEditPostFragment(
                            clickedPost
                        )
                    binding?.root?.findNavController()?.navigate(action)
                }
            }
        }

        binding?.userPostList?.adapter = adapter

        return binding?.root
    }

    private fun loadUserData() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding?.apply {
                firstNameEditText.setText(user?.firstName)
                lastNameEditText.setText(user?.lastName)
                user?.avatarUrl?.takeIf { avatarUrl ->
                    avatarUrl.isNotBlank()
                }?.let { url ->
                    Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.baseline_person_24)
                        .into(binding?.userImageView)
                }
            }
        }
    }

    private fun setupImagePickers() {
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                bitmap?.let {
                    binding?.userImageView?.setImageBitmap(it)
                    isImageUpdated = true
                }
            }

        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    binding?.userImageView?.setImageURI(it)
                    isImageUpdated = true
                }
            }
    }

    private fun saveUserData() {
        val currentUser = UserModel.shared.connectedUser ?: return

        binding?.progressBar?.visibility = View.VISIBLE

        val updatedUser = currentUser.copy(
            firstName = binding?.firstNameEditText?.text.toString(),
            lastName = binding?.lastNameEditText?.text.toString()
        )

        val newBitmap = extractBitmapFromImageView()

        if (currentUser == updatedUser && !isImageUpdated) {
            showToast("No changes detected")
            binding?.progressBar?.visibility = View.GONE
            return
        }


        viewModel.updateUser(updatedUser, newBitmap) { success ->
            if (success) {
                showToast("User is updated")
                isImageUpdated = false
            } else {
                showToast("Failed to update user")
            }
            binding?.progressBar?.visibility = View.GONE
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
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
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

    override fun onResume() {
        super.onResume()
        getUserPosts()
    }

    private fun getUserPosts() {
        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.refreshUsersPosts()
        viewModel.refreshSavedPosts()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}