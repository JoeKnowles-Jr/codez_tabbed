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
import com.jk.codez.databinding.FragmentRemoteBinding
import com.jk.codez.item.ItemAdapter
import com.jk.codez.item.ItemClickSupport
import kotlin.properties.Delegates

class RemoteFragment : Fragment() {

    private lateinit var binding: FragmentRemoteBinding
    private lateinit var viewModel: CodezViewModel
    private var connected by Delegates.notNull<Boolean>()
    private var mAdapter: ItemAdapter? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentRemoteBinding.inflate(layoutInflater)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            viewModel = ViewModelProvider(requireActivity())[CodezViewModel::class.java]
            viewModel.remoteItems.observe(viewLifecycleOwner) {
                binding.pb.visibility = View.GONE
                if (it != null) {
                    mAdapter = ItemAdapter(it.toMutableList())
                    binding.rvCodes.adapter = mAdapter
                    binding.tvEmpty.visibility = View.GONE
                    binding.rvCodes.visibility = View.VISIBLE
                } else {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.rvCodes.visibility = View.GONE
                }
            }

            viewModel.connected.observe(viewLifecycleOwner) {
                if (!it) {
                    binding.pb.visibility = View.GONE
                    viewModel.showConnectDialog()
                }
            }

            ItemClickSupport.addTo(binding.rvCodes)
                ?.setOnItemClickListener(object: ItemClickSupport.OnItemClickListener {
                    override fun onItemClicked(
                        recyclerView: RecyclerView?,
                        position: Int,
                        view: View?
                    ) {
//                        viewModel.s showItem(mAdapter?.get(position))
                    }

                })
                ?.setOnItemLongClickListener(object: ItemClickSupport.OnItemLongClickListener {
                    override fun onItemLongClicked(
                        recyclerView: RecyclerView?,
                        position: Int,
                        view: View?
                    ): Boolean {
                    viewModel.showItemDialog(mAdapter?.get(position), true)
                        return true
                    }

                })

            viewModel.refreshRemoteItems()

            val div = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)

            ResourcesCompat.getDrawable(resources, R.drawable.divider_blue, requireActivity().theme)
                ?.let { div.setDrawable(it) }

            binding.rvCodes.addItemDecoration(div)
        }
    }