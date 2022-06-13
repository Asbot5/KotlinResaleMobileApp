package com.example.mandatoryassignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mandatoryassignment.databinding.FragmentFirstBinding
import com.example.mandatoryassignment.models.Item
import com.example.mandatoryassignment.models.ItemsViewModel
import com.example.mandatoryassignment.models.MyAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val itemsViewModel: ItemsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemsViewModel.itemsLiveData.observe(viewLifecycleOwner) { items ->
            if (items != null) {
                val adapter = MyAdapter<Item>(items) { position ->
                   /* val pictureUrl: String =
                        if (items[position].pictureUrl == null) "" else items[position].pictureUrl!! */
                    val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(position)
                        items[position].id;
                        items[position].title;
                        items[position].description;
                        items[position].price;
                        items[position].seller;
                        items[position].date;
                       // pictureUrl)
                    findNavController().navigate(action)
                }
                binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                binding.recyclerView.adapter = adapter
            }
        }
        itemsViewModel.errorMessageLiveData.observe(viewLifecycleOwner) { errorMessage ->
            binding.textViewFirst.text = errorMessage
        }

        itemsViewModel.reload()

        binding.swiperefresh.setOnRefreshListener {
            itemsViewModel.reload()
            binding.swiperefresh.isRefreshing = false
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}