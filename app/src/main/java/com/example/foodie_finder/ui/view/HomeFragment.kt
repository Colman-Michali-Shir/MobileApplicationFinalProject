package com.example.foodie_finder.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodie_finder.adapter.PostsAdapter
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.data.model.PostModel
import com.example.foodie_finder.databinding.FragmentPostsListBinding
import com.example.foodie_finder.interfaces.OnItemClickListener
import com.example.foodie_finder.ui.viewModel.PostsListViewModel

class HomeFragment : Fragment() {
    private var adapter: PostsAdapter? = null
    private var binding: FragmentPostsListBinding? = null

    private val viewModel: PostsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostsListBinding.inflate(inflater, container, false)

        binding?.postsList?.setHasFixedSize(true)
        binding?.postsList?.layoutManager = LinearLayoutManager(context)

        adapter = PostsAdapter(viewModel.posts.value, onSavePost = { postId ->
            viewModel.savePost(postId) { success ->
                if (success) {
                    Toast.makeText(
                        requireContext(),
                        "Post $postId saved successfully",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to save post $postId",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        },
            onRemoveSavePost = { postId ->
                viewModel.removeSavedPost(postId) { success ->
                    if (success) {
                        Toast.makeText(
                            requireContext(),
                            "Post $postId removed successfully",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to remove post $postId",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })

        viewModel.posts.observe(viewLifecycleOwner) { posts ->

            Log.d("TAG", "Observed posts: $posts") // Debug log

            adapter?.update(posts)
            adapter?.notifyDataSetChanged()

            binding?.progressBar?.visibility = View.GONE
        }

        binding?.swipeToRefresh?.setOnRefreshListener {
            viewModel.refreshAllPosts()
        }

        PostModel.shared.loadingState.observe(viewLifecycleOwner) { state ->
            binding?.swipeToRefresh?.isRefreshing = state == PostModel.LoadingState.LOADING
            binding?.progressBar?.visibility =
                if (state == PostModel.LoadingState.LOADING) View.VISIBLE else View.GONE
        }

        adapter?.listener = object : OnItemClickListener {
            override fun onItemClick(post: Post?) {
                Log.d("TAG", "On click post $post")
                post?.let { clickedPost ->
//                    val action =
//                        HomeFragmentDirections.actionStudentsListFragmentToStudentDetailsFragment(
//                            clickedPost.id
//                        )
//                    binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                }

            }
        }

        binding?.postsList?.adapter = adapter

        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        getAllPosts()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun getAllPosts() {
        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.refreshAllPosts()
    }
}