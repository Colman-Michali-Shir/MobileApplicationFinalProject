package com.example.foodie_finder.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodie_finder.adapter.Post.PostsAdapter
import com.example.foodie_finder.data.entities.Post
import com.example.foodie_finder.data.model.PostModel
import com.example.foodie_finder.databinding.FragmentPostsListBinding
import com.example.foodie_finder.interfaces.OnItemClickListener

class HomeFragment : Fragment() {
    private var adapter: PostsAdapter? = null
    private var binding: FragmentPostsListBinding? = null

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostsListBinding.inflate(inflater, container, false)

        binding?.postsList?.setHasFixedSize(true)
        binding?.postsList?.layoutManager = LinearLayoutManager(context)

        adapter = PostsAdapter(
            viewModel.posts.value ?: emptyList(),
            viewModel.savedPosts.value ?: emptyList(),
            onSavePost = viewModel::savePost,
            onRemoveSavePost = viewModel::removeSavedPost,
        )

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            adapter?.updateAllPosts(posts)
            adapter?.notifyDataSetChanged()
        }


        viewModel.savedPosts.observe(viewLifecycleOwner) { posts ->
            adapter?.updateSavedPosts(posts)
            adapter?.notifyDataSetChanged()

        }

        PostModel.shared.loadingState.observe(viewLifecycleOwner) { state ->
            val isLoading = state == PostModel.LoadingState.LOADING
            // Show progressBar only when first loading, but not on swipe refresh
            if (binding?.swipeToRefresh?.isRefreshing == true) {
                binding?.progressBar?.visibility = View.GONE
            } else {
                binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

            // Show swipe refresh only if it's a manual refresh
            binding?.swipeToRefresh?.isRefreshing =
                isLoading && binding?.swipeToRefresh?.isRefreshing == true
        }

        binding?.swipeToRefresh?.setOnRefreshListener {
            viewModel.refreshAllPosts()
            viewModel.refreshSavedPosts()
        }

        adapter?.listener = object : OnItemClickListener {
            override fun onEditPost(post: Post?) {
                post?.let { clickedPost ->
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToEditPostFragment(
                            clickedPost
                        )
                    binding?.root?.findNavController()?.navigate(action)
                }
            }
        }

        binding?.postsList?.adapter = adapter

        return binding?.root
    }

    private fun getAllPosts() {
        viewModel.refreshAllPosts()
        viewModel.refreshSavedPosts()
    }

    override fun onResume() {
        super.onResume()
        getAllPosts()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}