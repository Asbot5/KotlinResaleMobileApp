package com.example.mandatoryassignment

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mandatoryassignment.databinding.FragmentSecondBinding
import com.example.mandatoryassignment.models.Item
import com.example.mandatoryassignment.models.ItemsViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val itemsViewModel: ItemsViewModel by activityViewModels()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = requireArguments()
        val secondFragArgs: SecondFragmentArgs = SecondFragmentArgs.fromBundle(bundle)

        val position = secondFragArgs.position
        val item = itemsViewModel[position]
        //val args = SecondFragmentArgs.fromBundle(requireArguments())
        //args.id
        if (item != null) {
            binding.title.text=item.title
            binding.description.text=item.description
            binding.price.text=item.price.toString()
            binding.seller.text=item.seller
            binding.date.text=item.date.toString()

            val format = SimpleDateFormat.getDateTimeInstance()
            val str = format.format(item.date * 1000L)
            binding.date.text = str.toString()
        }

        binding.buttonDelete.visibility = View.GONE

        if (Firebase.auth.currentUser?.email.toString() == item?.seller)
        {
            binding.buttonDelete.visibility = View.VISIBLE

            binding.buttonDelete.setOnClickListener {
                if (item != null) {
                    itemsViewModel.delete(item.id)
                }
                findNavController().popBackStack()
            }
        }

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}