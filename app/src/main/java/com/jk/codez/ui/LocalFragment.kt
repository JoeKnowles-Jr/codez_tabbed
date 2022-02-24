package com.jk.codez.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.jk.codez.CodezViewModel
import com.jk.codez.R
import com.jk.codez.databinding.FragmentLocalBinding
import com.jk.codez.item.ItemAdapter
import com.jk.codez.item.ItemClickSupport

class LocalFragment : Fragment() {
    private lateinit var binding: FragmentLocalBinding
    private lateinit var viewModel: CodezViewModel
    private var mAdapter: ItemAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocalBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[CodezViewModel::class.java]
        viewModel.localItems.observe(viewLifecycleOwner) {
            if (it != null) {
                mAdapter = ItemAdapter(it.toMutableList())
                binding.localRvCodes.adapter = mAdapter
                binding.pb.visibility = View.GONE
                binding.localTvEmpty.visibility = View.GONE
                binding.localRvCodes.visibility = View.VISIBLE
            } else {
                binding.pb.visibility = View.GONE
                binding.localTvEmpty.visibility = View.VISIBLE
                binding.localRvCodes.visibility = View.GONE
            }
        }

        ItemClickSupport.addTo(binding.localRvCodes)
            ?.setOnItemClickListener(object: ItemClickSupport.OnItemClickListener {
                override fun onItemClicked(
                    recyclerView: RecyclerView?,
                    position: Int,
                    view: View?
                ) {
//                    showItem(mAdapter?.get(position))
                }

            })
            ?.setOnItemLongClickListener(object: ItemClickSupport.OnItemLongClickListener {
                override fun onItemLongClicked(
                    recyclerView: RecyclerView?,
                    position: Int,
                    view: View?
                ): Boolean {
                    viewModel.showItemDialog(mAdapter?.get(position), false)
                    return true
                }

            })

        val div = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)

        ResourcesCompat.getDrawable(resources, R.drawable.divider_blue, requireActivity().theme)
            ?.let { div.setDrawable(it) }

        binding.localRvCodes.addItemDecoration(div)
    }
}