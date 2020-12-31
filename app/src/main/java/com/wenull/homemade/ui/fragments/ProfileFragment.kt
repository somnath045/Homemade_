package com.wenull.homemade.ui.fragments

import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wenull.homemade.R
import com.wenull.homemade.adapter.UserPacksAdapter
import com.wenull.homemade.databinding.FragmentProfileBinding
import com.wenull.homemade.ui.viewmodel.HomemadeViewModel
import com.wenull.homemade.ui.fragments.base.BaseFragment
import com.wenull.homemade.utils.model.FoodPack


class ProfileFragment : BaseFragment<FragmentProfileBinding, HomemadeViewModel>() {

    private lateinit var adapter: UserPacksAdapter

    override fun getLayout(): Int = R.layout.fragment_profile

    override fun getViewModelClass(): Class<HomemadeViewModel> = HomemadeViewModel::class.java

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.setFirebaseSourceCallback()

        setUpRecyclerView()
        binding.profileBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpRecyclerView() {

        adapter = UserPacksAdapter {pack, view -> onOptOutMenuClick(pack, view)}
        binding.recylerViewYourPacks.adapter = adapter
        binding.recylerViewYourPacks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        displayPacks()

    }

    private fun displayPacks() {
        viewModel.fetchPackDetails()
        viewModel.packs.observe(viewLifecycleOwner, Observer { packs ->
            adapter.setList(packs)
        })
    }

    private fun onOptOutMenuClick(pack: FoodPack, view: View) {
        val popup = PopupMenu(requireContext(), view)
        val menuInflater = MenuInflater(requireContext())

        menuInflater.inflate(R.menu.optout_menu, popup.menu)
        popup.show()

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.forever_optout -> {
                    //foreveroptout
                    foreverOptout(pack)
                    return@setOnMenuItemClickListener true
                }

                R.id.selective_optout -> {
                    val action =
                        ProfileFragmentDirections.actionProfileFragmentToOptoutBottomsheetFragment()
                    findNavController().navigate(action)
                    return@setOnMenuItemClickListener true
                }

                else -> false //nothing
            }
        }

    }

    private fun foreverOptout(foodPack: FoodPack) {}

}
