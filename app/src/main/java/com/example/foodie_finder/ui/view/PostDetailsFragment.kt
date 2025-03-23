package com.example.foodie_finder.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.foodie_finder.R
import com.example.foodie_finder.databinding.FragmentPostDetailsBinding
import com.example.foodie_finder.data.model.Model
import com.example.foodie_finder.data.local.Student
import com.example.foodie_finder.utils.DateTimeUtils

class PostDetailsFragment : Fragment() {

    private var student: Student? = null
    private var currentId: String = ""

    private var binding: FragmentPostDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        currentId = arguments?.let {
            PostDetailsFragmentArgs.fromBundle(it).id
        } ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostDetailsBinding.inflate(inflater, container, false)

        setUp()

        return binding?.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editPostFragment -> {
                val action =
                    PostDetailsFragmentDirections.actionPostDetailsFragmentToEditPostFragment(
                        currentId
                    )
                findNavController().navigate(action)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUp() {
        Model.shared.getStudentById(currentId) {
            it.let {
                binding?.nameTextView?.text = it.name
                binding?.idTextView?.text = it.id
                binding?.phoneTextView?.text = it.phone
                binding?.addressTextView?.text = it.address
                binding?.checkBox?.isChecked = it.isChecked
                binding?.birthDateTextView?.text = it.birthDate?.let { birthDate ->
                    DateTimeUtils.formatDate(birthDate)
                }
                binding?.birthTimeTextView?.text = it.birthTime?.let { birthTime ->
                    DateTimeUtils.formatTime(birthTime)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        student?.let {
            binding?.checkBox?.isChecked = it.isChecked
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}