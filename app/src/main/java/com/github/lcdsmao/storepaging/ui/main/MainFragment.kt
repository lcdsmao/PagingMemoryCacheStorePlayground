package com.github.lcdsmao.storepaging.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.lcdsmao.storepaging.R
import kotlinx.android.synthetic.main.main_fragment.*
import kotlin.random.Random

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MainAdapter()
        recycler_view.adapter = adapter
        viewModel.dummyData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        button_generate_key.setOnClickListener {
            generateKey()
        }
        generateKey()
    }

    private fun generateKey() {
        val key = Random.nextInt().toString()
        text_key.text = "Key: $key"
        button_get.setOnClickListener {
            viewModel.fetchDummyData(false, key)
        }
        button_fresh.setOnClickListener {
            viewModel.fetchDummyData(true, key)
        }
    }

    class MainAdapter : PagedListAdapter<String, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
        companion object {
            val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
                override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                    return oldItem == newItem
                }

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = TextView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            return object : RecyclerView.ViewHolder(view) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder.itemView as TextView).text = getItem(position)
        }
    }
}
