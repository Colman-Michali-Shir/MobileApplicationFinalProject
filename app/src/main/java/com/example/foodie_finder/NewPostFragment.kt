package com.example.foodie_finder

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.example.foodie_finder.databinding.FragmentNewPostBinding
import com.example.foodie_finder.dialogs.alert.showSuccessOperationDialog
import com.example.foodie_finder.model.Model
import com.example.foodie_finder.model.Student
import com.example.foodie_finder.dialogs.pickers.showDatePickerDialog
import com.example.foodie_finder.dialogs.pickers.showTimePickerDialog
import com.example.foodie_finder.utils.DateTimeUtils
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Time

class NewPostFragment : Fragment() {
    private var binding: FragmentNewPostBinding? = null
    private var didSetProfileImage = false
    private var cameraLauncher: ActivityResultLauncher<Void?>? = null
    private var galleryLauncher: ActivityResultLauncher<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)

        binding?.cancelButton?.setOnClickListener(::onCancelClicked)
        binding?.saveButton?.setOnClickListener(::onSaveClicked)
        binding?.studentInputForm?.birthDateEditText?.let { showDatePickerDialog(it, context) }
        binding?.studentInputForm?.birthTimeEditText?.let { showTimePickerDialog(it, context) }

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                if (bitmap != null) {
                    binding?.studentInputForm?.studentImageView?.setImageBitmap(bitmap)
                    didSetProfileImage = true
                }
            }

        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    binding?.studentInputForm?.studentImageView?.setImageURI(it)
                    didSetProfileImage = true
                }
            }

        binding?.studentInputForm?.addPhotoImageButton?.setOnClickListener {
            showImageSourceChooser()
        }
        return binding?.root

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

    private fun onSaveClicked(view: View) {
        val newStudent = Student(
            name = binding?.studentInputForm?.nameEditText?.text.toString(),
            id = FirebaseFirestore.getInstance().collection("students").document().id,
            phone = binding?.studentInputForm?.phoneEditText?.text.toString(),
            address = binding?.studentInputForm?.addressEditText?.text.toString(),
            avatarUrl = null,
            isChecked = binding?.studentInputForm?.checkBox?.isChecked ?: false,
            birthDate = binding?.studentInputForm?.birthDateEditText?.text.toString()
                .takeIf { it.isNotBlank() }
                ?.let { DateTimeUtils.parseDate(it) },
            birthTime = binding?.studentInputForm?.birthTimeEditText?.text.toString()
                .takeIf { it.isNotBlank() }?.let {
                    DateTimeUtils.parseTime(it)?.let { birthTime -> Time(birthTime.time) }
                }
        )

        binding?.progressBar?.visibility = View.VISIBLE
        if (didSetProfileImage) {
            binding?.studentInputForm?.studentImageView?.let { view ->
                val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                view.draw(canvas)
            }
            val bitmap =
                (binding?.studentInputForm?.studentImageView?.drawable as BitmapDrawable).bitmap
            Model.shared.addStudent(newStudent, bitmap) {
                binding?.progressBar?.visibility = View.GONE
                Navigation.findNavController(view).popBackStack()
                context?.let {
                    showSuccessOperationDialog(it, "add")
                }
            }
        } else {
            Model.shared.addStudent(newStudent, null) {
                binding?.progressBar?.visibility = View.GONE
                Navigation.findNavController(view).popBackStack()
                context?.let {
                    showSuccessOperationDialog(it, "add")
                }
            }
        }
    }

    private fun onCancelClicked(view: View) {
        Navigation.findNavController(view).popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}